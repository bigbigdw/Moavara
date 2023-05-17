package com.bigbigdw.moavara.Best.ViewModel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.work.*
import com.bigbigdw.moavara.Best.ActivityBestDetail
import com.bigbigdw.moavara.Best.intent.StateBestList
import com.bigbigdw.moavara.DataBase.*
import com.bigbigdw.moavara.Firebase.FirebaseWorkManager
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.BookBestAnalyzeWeek
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBestAnalyze
import com.bigbigdw.moavara.Util.*
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ViewModelBestList @Inject constructor() : ViewModel() {

    private val events = Channel<EventBestList>()

    val state: StateFlow<StateBestList> = events.receiveAsFlow()
        .runningFold(StateBestList(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateBestList())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateBestList, event: EventBestList): StateBestList {
        return when(event){
            EventBestList.Today -> {
            current.copy(TodayInit = true)
            } is EventBestList.TodayDone -> {
                current.copy(TodayInit = false)
            }  is EventBestList.Month -> {
                current.copy(Month = true)
            } is EventBestList.Week -> {
                current.copy(Week = true)
            } is EventBestList.InitBest -> {
                current.copy(InitBest = event.initBest)
            }  is EventBestList.BestToday -> {
                current.copy(BestTodayItem = event.BestTodayItem, BestTodayItemBookCode = event.BestTodayItemBookCode)
            } is EventBestList.Loading -> {
                current.copy(Loading = true)
            } is EventBestList.Loaded -> {
                current.copy(Loading = false)
            }  is EventBestList.isFirstPick -> {
                current.copy(isFirstPick = event.isFirstPick, isPicked = event.isPicked)
            }   is EventBestList.bookBestAnalyzeWeek -> {
                current.copy(BookBestAnalyzeWeek = event.bookBestAnalyzeWeek)
            } else -> {
                current.copy(Loading = true)
            }
        }
    }

    fun fetchBestTodayDone() {
        viewModelScope.launch {
            events.send(EventBestList.TodayDone)
        }
    }

    fun fetchBestList(context : Context) {

        val userDao = Room.databaseBuilder(
            context,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        val UserInfo = userDao.daoUser().get()

        viewModelScope.launch {
            events.send(EventBestList.InitBest(initBest = UserInfo))
        }
    }

    fun fetchBestListToday(type: String, context: Context) {

        val bestDao = Room.databaseBuilder(
            context,
            DBBest::class.java,
            "Today_${type}_${state.value.InitBest.Genre}"
        ).allowMainThreadQueries().build()

        val bestDaoBookCode = Room.databaseBuilder(
            context,
            DBBestBookCode::class.java,
            "Today_${type}_${state.value.InitBest.Genre}_BookCode"
        ).allowMainThreadQueries().build()

        viewModelScope.launch {
            events.send(EventBestList.Loading)
        }

        if(bestDao.bestDao().getAll().isEmpty()){
            getBookListToday(bestDao, type){item, result ->
                if(result){
                    getBestTodayList(item, bestDaoBookCode, type)
                }
            }
        } else {
            setRoomData(bestDao, bestDaoBookCode)
        }

    }

    private fun getBookListToday(
        bestDao: DBBest,
        type: String,
        callback: (ArrayList<BookListDataBest>, Boolean) -> Unit
    ) {

        val items = ArrayList<BookListDataBest>()

        bestDao.bestDao().initAll()

        BestRef.getBestDataToday(type, state.value.InitBest.Genre)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (postSnapshot in dataSnapshot.children) {

                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)

                        if (group != null) {

                            items.add(group)

                            if (dataSnapshot.exists()) {
                                bestDao.bestDao().insert(convertBookListDataBestToRoomBookListDataBest(group))
                            }
                        }
                    }

                    callback.invoke(items, true)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun getBestTodayList(
        items: ArrayList<BookListDataBest>,
        bestDaoBookCode: DBBestBookCode,
        type: String
    ) {

        val bookCodeItems = ArrayList<BookListDataBestAnalyze>()

        bestDaoBookCode.bestDaoBookCode().initAll()

        BestRef.getBookCode(type, state.value.InitBest.Genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (bookCodeList in items) {

                    val item = dataSnapshot.child(bookCodeList.bookCode)

                    if (item.childrenCount > 1) {

                        val bookCodes = ArrayList<BookListDataBestAnalyze>()

                        for(childItem in item.children){

                            val group: BookListDataBestAnalyze? = childItem.getValue(
                                BookListDataBestAnalyze::class.java)

                            if (group != null) {
                                bookCodes.add(group)
                            }
                        }

                        val lastItem = bookCodes[bookCodes.size - 1]
                        val moreLastItem = bookCodes[bookCodes.size - 2]

                        lastItem.numberDiff = moreLastItem.number - lastItem.number
                        lastItem.trophyCount = bookCodes.size

                        bookCodeItems.add(lastItem)

                        bestDaoBookCode.bestDaoBookCode().insert(convertBookListDataBestAnalyzeToRoomBookListDataBestAnalyze(lastItem))

                    } else if (item.childrenCount.toInt() == 1) {

                        val group: BookListDataBestAnalyze? =
                            dataSnapshot.child(bookCodeList.bookCode).child(DBDate.DateMMDD())
                                .getValue(BookListDataBestAnalyze::class.java)

                        group?.numberDiff = 0
                        group?.trophyCount = 1

                        if (group != null) {
                            bookCodeItems.add(group)

                            bestDaoBookCode.bestDaoBookCode().insert(convertBookListDataBestAnalyzeToRoomBookListDataBestAnalyze(group))
                        }
                    }
                }

                viewModelScope.launch {
                    events.send(EventBestList.BestToday(BestTodayItem = items, BestTodayItemBookCode = bookCodeItems))
                    events.send(EventBestList.Loaded)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setRoomData(bestDao: DBBest, bestDaoBookCode: DBBestBookCode) {
        val bookItem = bestDao.bestDao().getAll()
        val bookCodeItem = bestDaoBookCode.bestDaoBookCode().get()

        val items = ArrayList<BookListDataBest>()
        val bookCodeItems = ArrayList<BookListDataBestAnalyze>()

        for(item in bookItem){
            items.add(convertRoomBookListDataBestToBookListDataBest(item))
        }

        for(item in bookCodeItem){
            bookCodeItems.add(convertRoomBookListDataBestAnalyzeToBookListDataBestAnalyze(item))
        }

        viewModelScope.launch {
            events.send(EventBestList.BestToday(BestTodayItem = items, BestTodayItemBookCode = bookCodeItems))
            events.send(EventBestList.Loaded)
        }
    }

    fun getBottomBestData(){

    }

    fun BottomDialogBestPick(isPicked : Boolean, UserInfo: DataBaseUser, item: BookListDataBest?, type : String, context: Context){

        val Novel = mRootRef.child("User").child(UserInfo.UID).child("Novel")

        if (isPicked) {
            Novel.child("book").child(item?.bookCode ?: "").removeValue()

            val bundle = Bundle()
            bundle.putString("PICK_NOVEL_PLATFORM", item?.type)
            bundle.putString("PICK_NOVEL_STATUS", "DELETE")
            Firebase.analytics.logEvent("BEST_BottomDialogBest", bundle)

        } else {

            val group = item?.let { it1 -> item }

            if (state.value.isFirstPick) {
                viewModelScope.launch {
                    events.send(EventBestList.isFirstPick(isFirstPick = false, isPicked = state.value.isPicked))
                }

                val inputData = Data.Builder()
                    .putString(FirebaseWorkManager.TYPE, "PICK")
                    .putString(FirebaseWorkManager.UID, UserInfo.UID)
                    .putString(FirebaseWorkManager.USER, UserInfo.Nickname)
                    .build()

                /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
                val workRequest =
                    PeriodicWorkRequestBuilder<FirebaseWorkManager>(6, TimeUnit.HOURS)
                        .setBackoffCriteria(
                            BackoffPolicy.LINEAR,
                            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                            TimeUnit.MILLISECONDS
                        )
                        .addTag("MoavaraPick")
                        .setInputData(inputData)
                        .build()

                val workManager = WorkManager.getInstance(context.applicationContext)

                workManager.enqueueUniquePeriodicWork(
                    "MoavaraPick",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
                FirebaseMessaging.getInstance().subscribeToTopic(UserInfo.UID)

                Novel.child("book").child(item?.bookCode ?: "").setValue(group)
                Novel.child("bookCode").child(item?.bookCode ?: "").setValue(state.value.BestTodayItemBookCode)
                mRootRef.child("User").child(UserInfo.UID).child("Mining").setValue(true)

                val bundle = Bundle()
                bundle.putString("PICK_NOVEL_PLATFORM", item?.type)
                bundle.putString("PICK_NOVEL_STATUS", "FIRST")
                Firebase.analytics.logEvent("BEST_BottomDialogBest", bundle)

            } else {
                Novel.child("book").child(item?.bookCode ?: "").setValue(group)
                Novel.child("bookCode").child(item?.bookCode ?: "").setValue(state.value.BestTodayItemBookCode)

                val bundle = Bundle()
                bundle.putString("PICK_NOVEL_PLATFORM", item?.type)
                bundle.putString("PICK_NOVEL_STATUS", "ADD")
                Firebase.analytics.logEvent("BEST_BottomDialogBest", bundle)

            }
        }
    }

    fun BottomDialogBestGetRank(UserInfo: DataBaseUser, item: BookListDataBest?,){

        val bookCodeItems = ArrayList<BookListDataBestAnalyze>()
        val ArrayWeekItem  = ArrayList<BookBestAnalyzeWeek>()

        if (item != null) {
            BestRef.getBookCode(item.type, UserInfo.Genre).child(item.bookCode)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (keyItem in dataSnapshot.children) {
                            val group: BookListDataBestAnalyze? =
                                keyItem.getValue(BookListDataBestAnalyze::class.java)

                            if (group != null) {
                                bookCodeItems.add(group)
                            }

                            val itemDate = group?.let { DBDate.getDateData(it.date) }

                                if (itemDate != null) {

                                    val WeekItem  = BookBestAnalyzeWeek(
                                        isVisible = true,
                                        trophyImage = if (itemDate.date == DBDate.DayInt()) {
                                            R.drawable.ic_best_gn_24px
                                        } else {
                                            R.drawable.ic_best_vt_24px
                                        },
                                        number = group.number + 1,
                                        date = 0
                                    )

                                    if (itemDate.week.toString() == DBDate.Week() && (itemDate.month + 0).toString() == DBDate.Month()) {
                                        when {
                                            itemDate.date == 1 -> {
                                                WeekItem.date = 1
                                                ArrayWeekItem.add(WeekItem)
                                            }
                                            itemDate.date == 2 -> {
                                                WeekItem.date = 2
                                                ArrayWeekItem.add(WeekItem)
                                            }
                                            itemDate.date == 3 -> {
                                                WeekItem.date = 3
                                                ArrayWeekItem.add(WeekItem)
                                            }
                                            itemDate.date == 4 -> {
                                                WeekItem.date = 4
                                                ArrayWeekItem.add(WeekItem)
                                            }
                                            itemDate.date == 5 -> {
                                                WeekItem.date = 5
                                                ArrayWeekItem.add(WeekItem)
                                            }
                                            itemDate.date == 6 -> {
                                                WeekItem.date = 6
                                                ArrayWeekItem.add(WeekItem)
                                            }
                                            itemDate.date == 7 -> {
                                                WeekItem.date = 7
                                                ArrayWeekItem.add(WeekItem)
                                            }
                                        }
                                    }
                                }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }

        viewModelScope.launch {
            events.send(EventBestList.bookBestAnalyzeWeek(bookBestAnalyzeWeek = ArrayWeekItem))
        }
    }

    fun BottomDialogToBestDetail(activity : ComponentActivity, item: BookListDataBest, platform : String, pos : Int){
        val bundle = Bundle()
        bundle.putString("BEST_FROM", "BEST")
        Firebase.analytics.logEvent("BEST_ActivityBestDetail", bundle)

        val bookDetailIntent = Intent(activity, ActivityBestDetail::class.java)
        bookDetailIntent.putExtra("BookCode",
            item.let { it1 -> String.format("%s", it1.bookCode) })
        bookDetailIntent.putExtra("Type", String.format("%s", platform))
        bookDetailIntent.putExtra("POSITION", pos)
        item.let { it1 -> bookDetailIntent.putExtra("COUNT", it1.number) }
        bookDetailIntent.putExtra("HASDATA", true)
        activity.startActivity(bookDetailIntent)
    }
    
    fun getIsFirstPick(
        UserInfo: DataBaseUser,
        bookCode : String,
    ){

        var isFirstPick = false
        var isPicked = false

        FirebaseDatabase.getInstance().reference.child("User").child(UserInfo.UID).child("Novel").child("book")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (!dataSnapshot.exists()) {
                        isFirstPick = true
                    }

                    for (pickedItem in dataSnapshot.children) {

                        if (pickedItem.key.toString() == bookCode) {
                            isPicked = true
                            break
                        } else {
                            isPicked = false
                        }
                    }

                    viewModelScope.launch {
                        events.send(EventBestList.isFirstPick(isFirstPick = isFirstPick, isPicked = isPicked))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
    
}
