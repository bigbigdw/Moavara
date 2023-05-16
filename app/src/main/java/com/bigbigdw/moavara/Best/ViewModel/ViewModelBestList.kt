package com.bigbigdw.moavara.Best.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bigbigdw.moavara.Best.intent.StateBestList
import com.bigbigdw.moavara.DataBase.*
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBestAnalyze
import com.bigbigdw.moavara.Util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
            current.copy(Today = true)
            } is EventBestList.Month -> {
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
            } else -> {
                current.copy(Loading = true)
            }
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

        if(bestDao.bestDao().getAll().isNullOrEmpty()){
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
    
}
