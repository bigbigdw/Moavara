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
import com.bigbigdw.moavara.DataBase.BestListAnalyzeWeek
import com.bigbigdw.moavara.DataBase.BestItemData
import com.bigbigdw.moavara.DataBase.BestListAnalyze
import com.bigbigdw.moavara.DataBase.BottomBestItemData
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
                current.copy(todayInit = true)
            }
            is EventBestList.TodayDone -> {
                current.copy(todayInit = false)
            }
            is EventBestList.Month -> {
                current.copy(month = true)
            }
            is EventBestList.Week -> {
                current.copy(week = true)
            }
            is EventBestList.InitBest -> {
                current.copy(initBest = event.initBest)
            }
            is EventBestList.BestToday -> {
                current.copy(
                    bestTodayItem = event.BestTodayItem,
                    bestTodayItemBookCode = event.BestTodayItemBookCode
                )
            }
            is EventBestList.Loading -> {
                current.copy(loading = true)
            }
            is EventBestList.Loaded -> {
                current.copy(loading = false)
            }
            is EventBestList.IsFirstPick -> {
                current.copy(isFirstPick = event.isFirstPick, isPicked = event.isPicked)
            }
            is EventBestList.bestListAnalyzeWeek -> {
                current.copy(bestListAnalyzeWeek = event.bestListAnalyzeWeek)
            }
            is EventBestList.BottomItem -> {
                current.copy(bottomBestItemData = event.bottomBestItemData)
            }
            is EventBestList.ItemData -> {
                current.copy(bestItemData = event.bestItemData)
            }
            is EventBestList.Type -> {
                current.copy(type = event.type)
            }
            is EventBestList.Position -> {
                current.copy(position = event.position)
            }
            else -> {
                current.copy(loading = true)
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
            "Today_${type}_${state.value.initBest.Genre}"
        ).allowMainThreadQueries().build()

        val bestDaoBookCode = Room.databaseBuilder(
            context,
            DBBestBookCode::class.java,
            "Today_${type}_${state.value.initBest.Genre}_BookCode"
        ).allowMainThreadQueries().build()

        viewModelScope.launch {
            events.send(EventBestList.Loading)
            events.send(EventBestList.Type(type = type))
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
        callback: (ArrayList<BestItemData>, Boolean) -> Unit
    ) {

        val items = ArrayList<BestItemData>()

        bestDao.bestDao().initAll()

        BestRef.getBestDataToday(type, state.value.initBest.Genre)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (postSnapshot in dataSnapshot.children) {

                        val group: BestItemData? =
                            postSnapshot.getValue(BestItemData::class.java)

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
        items: ArrayList<BestItemData>,
        bestDaoBookCode: DBBestBookCode,
        type: String
    ) {

        val bookCodeItems = ArrayList<BestListAnalyze>()

        bestDaoBookCode.bestDaoBookCode().initAll()

        BestRef.getBookCode(type, state.value.initBest.Genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (bookCodeList in items) {

                    val item = dataSnapshot.child(bookCodeList.bookCode)

                    if (item.childrenCount > 1) {

                        val bookCodes = ArrayList<BestListAnalyze>()

                        for(childItem in item.children){

                            val group: BestListAnalyze? = childItem.getValue(
                                BestListAnalyze::class.java)

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

                        val group: BestListAnalyze? =
                            dataSnapshot.child(bookCodeList.bookCode).child(DBDate.DateMMDD())
                                .getValue(BestListAnalyze::class.java)

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

        val items = ArrayList<BestItemData>()
        val bookCodeItems = ArrayList<BestListAnalyze>()

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

    fun getBottomBestData(bestItemData: BestItemData, index: Int){

        viewModelScope.launch {
            events.send(EventBestList.ItemData(bestItemData = bestItemData))
            events.send(EventBestList.Position(position = index))
        }

        val item = BottomBestItemData()

        item.title = bestItemData.title
        item.bookImg = bestItemData.bookImg
        item.number = bestItemData.number
        item.type = bestItemData.type

        if(bestItemData.type == "Toksoda"){
            item.info1 = "${bestItemData.writer} | ${bestItemData.info2}"

            item.info2Title = "조회 수 : "
            item.info2 = bestItemData.info3

            item.info3Title = "선호작 수 : "
            item.info3 = bestItemData.info5
        } else if (bestItemData.type == "Naver" || bestItemData.type == "Naver_Today" || bestItemData.type == "Naver_Challenge") {
            item.info1 = bestItemData.writer

            item.info2Title = "별점 수 : "
            item.info2 = bestItemData.info3.replace("별점", "")

            item.info3Title = "조회 수 : "
            item.info3 = bestItemData.info4.replace("조회", "조회 수 : ")

            item.info4Title = "관심 : "
            item.info4 = bestItemData.info5.replace("관심", "관심 : ")
        } else if(bestItemData.type == "Kakao_Stage"){
            item.info1 = "${bestItemData.writer} | ${bestItemData.info2}"

            item.info2Title = "조회 수 : "
            item.info2 = bestItemData.info3.replace("조회", "별점 : ")

            item.info3Title = "조회 수 : "
            item.info3 = bestItemData.info4.replace("별점", "조회 수 : ")

            item.info5 = bestItemData.info1
        } else if(bestItemData.type == "Ridi"){
            item.info1 = "${bestItemData.writer} | ${bestItemData.info2}"

            item.info2Title = "추천 수 : "
            item.info2 = bestItemData.info3

            item.info3Title = "평점 : "
            item.info3 = bestItemData.info4
        } else if(bestItemData.type == "OneStore"){
            item.info1 = bestItemData.writer

            item.info2Title = "조회 수 : "
            item.info2 = bestItemData.info3.replace("별점", "별점 : ")

            item.info3Title = "평점 : "
            item.info3 = bestItemData.info4.replace("조회", "조회 수 : ")

            item.info4Title = "댓글 수 : "
            item.info4 = bestItemData.info5.replace("관심", "관심 : ")
        } else if(bestItemData.type == "Joara" || bestItemData.type == "Joara_Premium" || bestItemData.type == "Joara_Nobless"){
            item.info1 = "${bestItemData.writer} | ${bestItemData.info2}"

            item.info2Title = "조회 수 : "
            item.info2 = bestItemData.info3

            item.info3Title = "선호작 수 : "
            item.info3 = bestItemData.info4

            item.info4Title = "추천 수 : "
            item.info4 = bestItemData.info5

            item.info5 = bestItemData.info1
        } else if(bestItemData.type == "Kakao"){
            item.info1 = "${bestItemData.writer} | ${bestItemData.info2}"

            item.info2Title = "조회 수 : "
            item.info2 = bestItemData.info3

            item.info3Title = "추천 수 : "
            item.info3 = bestItemData.info4

            item.info4Title = "평점 : "
            item.info4 = bestItemData.info5

            item.info5 = bestItemData.info1
        } else if(bestItemData.type == "Munpia"){
            item.info1 = "${bestItemData.writer} | ${bestItemData.info2}"

            item.info2Title = "조회 수 : "
            item.info2 = bestItemData.info3

            item.info3Title = "방문 수 : "
            item.info3 = bestItemData.info4

            item.info4Title = "선호작 수 : "
            item.info4 = bestItemData.info5

            item.info5 = bestItemData.info1
        }

        viewModelScope.launch {
            events.send(EventBestList.BottomItem(bottomBestItemData = item))
        }
    }

    fun bottomDialogBestPick(
        activity: ComponentActivity
    ){

        val Novel = mRootRef.child("User").child(state.value.initBest.UID).child("Novel")

        if (state.value.isPicked) {
            Novel.child("book").child(state.value.bestItemData.bookCode ?: "").removeValue()

            val bundle = Bundle()
            bundle.putString("PICK_NOVEL_PLATFORM", state.value.type)
            bundle.putString("PICK_NOVEL_STATUS", "DELETE")
            Firebase.analytics.logEvent("BEST_BottomDialogBest", bundle)

            viewModelScope.launch {
                events.send(EventBestList.IsFirstPick(isFirstPick = state.value.isFirstPick, isPicked = false))
                _sideEffects.send("[${state.value.bestItemData.title}]이(가) 마이픽에서 제거되었습니다.")
            }

        } else {

            val group = state.value.bestItemData.let { it1 -> state.value.bestItemData }

            if (state.value.isFirstPick) {

                val inputData = Data.Builder()
                    .putString(FirebaseWorkManager.TYPE, "PICK")
                    .putString(FirebaseWorkManager.UID, state.value.initBest.UID)
                    .putString(FirebaseWorkManager.USER, state.value.initBest.Nickname)
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

                val workManager = WorkManager.getInstance(activity.applicationContext)

                workManager.enqueueUniquePeriodicWork(
                    "MoavaraPick",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
                FirebaseMessaging.getInstance().subscribeToTopic(state.value.initBest.UID)

                Novel.child("book").child(state.value.bestItemData.bookCode).setValue(group)
                Novel.child("bookCode").child(state.value.bestItemData.bookCode).setValue(state.value.bestTodayItemBookCode)
                mRootRef.child("User").child(state.value.initBest.UID).child("Mining").setValue(true)

                val bundle = Bundle()
                bundle.putString("PICK_NOVEL_PLATFORM", state.value.bestItemData.type)
                bundle.putString("PICK_NOVEL_STATUS", "FIRST")
                Firebase.analytics.logEvent("BEST_BottomDialogBest", bundle)

                viewModelScope.launch {
                    events.send(EventBestList.IsFirstPick(isFirstPick = false, isPicked = true))
                    _sideEffects.send("[${group.title}]이(가) 마이픽에 등록되었습니다.")
                }

            } else {
                Novel.child("book").child(state.value.bestItemData.bookCode).setValue(group)
                Novel.child("bookCode").child(state.value.bestItemData.bookCode).setValue(state.value.bestTodayItemBookCode)

                val bundle = Bundle()
                bundle.putString("PICK_NOVEL_PLATFORM", state.value.bestItemData.type)
                bundle.putString("PICK_NOVEL_STATUS", "ADD")
                Firebase.analytics.logEvent("BEST_BottomDialogBest", bundle)

                viewModelScope.launch {
                    events.send(EventBestList.IsFirstPick(isFirstPick = true, isPicked = true))
                    _sideEffects.send("[${group.title}]이(가) 마이픽에 등록되었습니다.")
                }
            }
        }
    }

    fun bottomDialogBestGetRank(UserInfo: DataBaseUser, item: BestItemData){

        val bookCodeItems = ArrayList<BestListAnalyze>()
        val ArrayWeekItem  = ArrayList<BestListAnalyzeWeek>()
        ArrayWeekItem.add(BestListAnalyzeWeek(dateString = "일"))
        ArrayWeekItem.add(BestListAnalyzeWeek(dateString = "월"))
        ArrayWeekItem.add(BestListAnalyzeWeek(dateString = "화"))
        ArrayWeekItem.add(BestListAnalyzeWeek(dateString = "수"))
        ArrayWeekItem.add(BestListAnalyzeWeek(dateString = "목"))
        ArrayWeekItem.add(BestListAnalyzeWeek(dateString = "금"))
        ArrayWeekItem.add(BestListAnalyzeWeek(dateString = "토"))

        BestRef.getBookCode(item.type, UserInfo.Genre).child(item.bookCode)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (keyItem in dataSnapshot.children) {
                        val group: BestListAnalyze? =
                            keyItem.getValue(BestListAnalyze::class.java)

                        if (group != null) {
                            bookCodeItems.add(group)
                        }

                        val itemDate = group?.let { DBDate.getDateData(it.date) }

                            if (itemDate != null) {

                                val WeekItem  = BestListAnalyzeWeek(
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
                                            WeekItem.dateString = "일"
                                            ArrayWeekItem[0] = WeekItem
                                        }
                                        itemDate.date == 2 -> {
                                            WeekItem.date = 2
                                            WeekItem.dateString = "월"
                                            ArrayWeekItem[1] = WeekItem
                                        }
                                        itemDate.date == 3 -> {
                                            WeekItem.date = 3
                                            WeekItem.dateString = "화"
                                            ArrayWeekItem[2] = WeekItem
                                        }
                                        itemDate.date == 4 -> {
                                            WeekItem.date = 4
                                            WeekItem.dateString = "수"
                                            ArrayWeekItem[3] = WeekItem
                                        }
                                        itemDate.date == 5 -> {
                                            WeekItem.date = 5
                                            WeekItem.dateString = "목"
                                            ArrayWeekItem[4] = WeekItem
                                        }
                                        itemDate.date == 6 -> {
                                            WeekItem.date = 6
                                            WeekItem.dateString = "금"
                                            ArrayWeekItem[5] = WeekItem
                                        }
                                        itemDate.date == 7 -> {
                                            WeekItem.date = 7
                                            WeekItem.dateString = "토"
                                            ArrayWeekItem[6] = WeekItem
                                        }
                                    }
                                }
                            }
                    }

                    viewModelScope.launch {
                        events.send(EventBestList.bestListAnalyzeWeek(bestListAnalyzeWeek = ArrayWeekItem))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun bottomDialogToBestDetail(activity : ComponentActivity, item: BestItemData, type : String, pos : Int){
        val bundle = Bundle()
        bundle.putString("BEST_FROM", "BEST")
        Firebase.analytics.logEvent("BEST_ActivityBestDetail", bundle)

        val bookDetailIntent = Intent(activity, ActivityBestDetail::class.java)
        bookDetailIntent.putExtra("BookCode",
            item.let { it1 -> String.format("%s", it1.bookCode) })
        bookDetailIntent.putExtra("Type", String.format("%s", type))
        bookDetailIntent.putExtra("POSITION", pos)
        item.let { it1 -> bookDetailIntent.putExtra("COUNT", it1.number) }
        bookDetailIntent.putExtra("HASDATA", true)
        activity.startActivity(bookDetailIntent)
    }
    
    fun getIsFirstPick(){

        var isFirstPick = false
        var isPicked = false

        FirebaseDatabase.getInstance().reference.child("User").child(state.value.initBest.UID).child("Novel").child("book")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (!dataSnapshot.exists()) {
                        isFirstPick = true
                    }

                    for (pickedItem in dataSnapshot.children) {

                        if (pickedItem.key.toString() == state.value.bestItemData.bookCode) {
                            isPicked = true
                            break
                        } else {
                            isPicked = false
                        }
                    }

                    viewModelScope.launch {
                        events.send(EventBestList.IsFirstPick(isFirstPick = isFirstPick, isPicked = isPicked))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
    
}
