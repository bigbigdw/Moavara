package com.bigbigdw.moavara.Best.ViewModel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.work.*
import com.bigbigdw.moavara.Best.intent.StateBestDetail
import com.bigbigdw.moavara.DataBase.*
import com.bigbigdw.moavara.Firebase.FirebaseWorkManager
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.Retrofit.JoaraBestChapter
import com.bigbigdw.moavara.Retrofit.JoaraBestDetailResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.RetrofitJoara
import com.bigbigdw.moavara.Util.BestRef
import com.bigbigdw.moavara.Util.DBDate
import com.bigbigdw.moavara.Util.Genre
import com.bigbigdw.moavara.Util.Param
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
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


class ViewModelBestDetail @Inject constructor() : ViewModel() {

    private val events = Channel<EventBestDetail>()

    val state: StateFlow<StateBestDetail> = events.receiveAsFlow()
        .runningFold(StateBestDetail(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateBestDetail())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateBestDetail, event: EventBestDetail): StateBestDetail {
        return when(event){
            EventBestDetail.Loading -> {
                current.copy(loading = true)
            }
            is EventBestDetail.Loaded -> {
                current.copy(loading = false)
            }
            is EventBestDetail.InitBestDetail -> {
                current.copy(
                    bookCode = event.bookCode,
                    type = event.type,
                    position = event.position,
                    fromPick = event.fromPick,
                    genre = event.genre,
                    dataBaseUser = event.dataBaseUser
                )
            }
            is EventBestDetail.isTab -> {
                current.copy(isTabAnalyze = event.isTabAnalyze, isTabComment = event.isTabComment, isTabOther = event.isTabOther)
            }
            is EventBestDetail.isJoaraChapter -> {
                current.copy(joaraChapter = event.joaraChapter)
            }
            is EventBestDetail.bestDetailData -> {
                current.copy(bestItemData = event.bestItemData, bestListAnalyze = event.bestListAnalyze)
            }
            is EventBestDetail.bestDetailKeywords -> {
                current.copy(keywords = event.keywords)
            }
            is EventBestDetail.hasBookData -> {
                current.copy(hasBookData = event.hasBookData)
            }
            is EventBestDetail.bookData -> {
                current.copy(bookData = event.bookData)
            }
            else -> {
                current.copy(loading = true)
            }
        }
    }

    fun fetchBestDetailInit(intent : Intent, activity: ComponentActivity) {

        val userDao = Room.databaseBuilder(
            activity,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        viewModelScope.launch {
            events.send(EventBestDetail.InitBestDetail(
                bookCode = intent.getStringExtra("BookCode") ?: "",
                type = intent.getStringExtra("Type") ?: "",
                position = intent.getIntExtra("POSITION", 0),
                fromPick = intent.getBooleanExtra("FROMPICK", false),
                genre = Genre.getGenre(activity).toString(),
                dataBaseUser = userDao.daoUser().get()
            ))

            events.send(EventBestDetail.hasBookData(
                hasBookData = intent.getBooleanExtra("HASDATA", false),
            ))
        }

        Firebase.crashlytics.setCustomKey("ActivityBestDetail_PLATFORM", intent.getStringExtra("Type") ?: "")
        Firebase.crashlytics.setCustomKey("ActivityBestDetail_GENRE", userDao.daoUser().get().Genre)

        FirebaseDatabase.getInstance().reference.child("User").child(state.value.dataBaseUser.UID).child("Novel").child("book")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (pickedItem in dataSnapshot.children) {

                        if (pickedItem.key.toString() == state.value.bookCode) {

                            viewModelScope.launch {
                                events.send(EventBestDetail.IsFirstPick(
                                    isFirstPick = false,
                                    isPicked = true
                                ))
                            }

                            break
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        if (intent.getBooleanExtra("HASDATA", false)) {
            setLayout(activity)
        } else {
            setLayoutWithoutBookData()
        }
    }

    private fun setLayout(activity: ComponentActivity){

        val bookData = ArrayList<BestListAnalyze>()

        if (state.value.fromPick) {
            mRootRef.child("User").child(state.value.dataBaseUser.UID).child("Novel").child("bookCode").child(state.value.bookCode)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (state.value.type == "Joara" || state.value.type == "Joara_Nobless" || state.value.type == "Joara_Premium") {
                            setLayoutJoara(activity)
                        }
                        //        else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
                        //            setLayoutNaverToday()
                        //        } else if (platform == "Kakao") {
                        //            setLayoutKaKao()
                        //        } else if (platform == "Kakao_Stage") {
                        //            setLayoutKaKaoStage()
                        //        } else if (platform == "Ridi") {
                        //            setLayoutRidi()
                        //        } else if (platform == "OneStore") {
                        //            setLayoutOneStory()
                        //        } else if (platform == "Munpia") {
                        //            setLayoutMunpia()
                        //        } else if (platform == "Toksoda") {
                        //            setLayoutToksoda()
                        //        }

                        if (dataSnapshot.exists()) {
                            for (item in dataSnapshot.children) {
                                val group: BestListAnalyze? =
                                    item.getValue(BestListAnalyze::class.java)

                                if (group != null) {
                                    bookData.add(
                                        BestListAnalyze(
                                            group.info1,
                                            group.info2,
                                            group.info3,
                                            group.info4,
                                            group.number,
                                            group.date,
                                            group.numberDiff,
                                            group.trophyCount,
                                        )
                                    )
                                }
                            }

                            setLayoutWithBookData()

                            viewModelScope.launch {
                                events.send(EventBestDetail.bookData(bookData = bookData))
                            }
                        } else {

                            viewModelScope.launch {
                                events.send(EventBestDetail.hasBookData(hasBookData = false))
                            }

                            setLayoutWithoutBookData()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        } else {
            BestRef.getBookCode(state.value.type, state.value.dataBaseUser.Genre).child(state.value.bookCode)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (item in dataSnapshot.children) {
                                val group: BestListAnalyze? =
                                    item.getValue(BestListAnalyze::class.java)

                                if (group != null) {
                                    bookData.add(
                                        BestListAnalyze(
                                            group.info1,
                                            group.info2,
                                            group.info3,
                                            group.info4,
                                            group.number,
                                            group.date,
                                            group.numberDiff,
                                            group.trophyCount,
                                        )
                                    )
                                }
                            }

                            viewModelScope.launch {
                                events.send(EventBestDetail.bookData(bookData = bookData))
                            }

                            setLayoutWithBookData()
                        } else {

                            viewModelScope.launch {
                                events.send(EventBestDetail.hasBookData(hasBookData = false))
                            }

                            setLayoutWithoutBookData()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }

    fun setLayoutWithBookData() {

        if (state.value.type == "Naver_Today" || state.value.type == "Naver_Challenge" || state.value.type == "Naver") {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = true, isTabComment = false, isTabOther = true))
            }
        } else if (state.value.type == "Kakao" || state.value.type == "Kakao_Stage" || state.value.type == "OneStore" || state.value.type == "Munpia") {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = true, isTabComment = true, isTabOther = false))
            }
        } else if (state.value.type == "Ridi") {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = true, isTabComment = false, isTabOther = true))
            }
        } else {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = true, isTabComment = true, isTabOther = true))
            }
        }
    }

    fun setLayoutWithoutBookData(){
        if (state.value.type == "Naver_Today" || state.value.type == "Naver_Challenge" || state.value.type == "Naver") {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = true, isTabComment = false, isTabOther = true))
            }
        } else if (state.value.type == "Kakao" || state.value.type == "Kakao_Stage" || state.value.type == "OneStore" || state.value.type == "Munpia") {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = false, isTabComment = true, isTabOther = false))
            }
        } else if (state.value.type == "Ridi") {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = false, isTabComment = false, isTabOther = true))
            }
        } else {
            viewModelScope.launch {
                events.send(EventBestDetail.isTab(isTabAnalyze = false, isTabComment = true, isTabOther = true))
            }
        }
    }

    fun setUserPick(activity: ComponentActivity) {

        val Novel = mRootRef.child("User").child(state.value.dataBaseUser.UID).child("Novel")

        if (state.value.isPicked) {

            viewModelScope.launch {
                events.send(EventBestDetail.IsFirstPick(isFirstPick = state.value.isFirstPick, isPicked = false))
            }

            Novel.child("book").child(state.value.bookCode).removeValue()
            Novel.child("bookCode").child(state.value.bookCode).removeValue()

            Novel.child("book").child(state.value.bookCode).removeValue()
            Novel.child("bookCode").child(state.value.bookCode).removeValue()

            viewModelScope.launch {
                _sideEffects.send("[${state.value.bestItemData.title}]이(가) 마이픽에서 제거되었습니다.")
            }

            val bundle = Bundle()
            bundle.putString("PICK_NOVEL_PLATFORM", state.value.type)
            bundle.putString("PICK_NOVEL_STATUS", "DELETE")
            Firebase.analytics.logEvent("BEST_ActivityBestDetail", bundle)

        } else {

            viewModelScope.launch {
                events.send(EventBestDetail.IsFirstPick(isFirstPick = state.value.isFirstPick, isPicked = true))
            }

            if (state.value.isFirstPick) {
                viewModelScope.launch {
                    events.send(EventBestDetail.IsFirstPick(isFirstPick = false, isPicked = state.value.isPicked))
                }

                val inputData = Data.Builder()
                    .putString(FirebaseWorkManager.TYPE, "PICK")
                    .putString(FirebaseWorkManager.UID, state.value.dataBaseUser.UID)
                    .putString(FirebaseWorkManager.USER, state.value.dataBaseUser.Nickname)
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
                FirebaseMessaging.getInstance().subscribeToTopic(state.value.dataBaseUser.UID)

                Novel.child("book").child(state.value.bookCode).setValue(state.value.bestItemData)
                mRootRef.child("User").child(state.value.dataBaseUser.UID).child("Mining").setValue(true)

                if (state.value.bookData.isEmpty()) {
                    Novel.child("bookCode").child(state.value.bookCode).child(DBDate.DateMMDD()).setValue(state.value.bestListAnalyze)
                } else {
                    Novel.child("bookCode").child(state.value.bookCode).setValue(state.value.bookCode)
                }

                val bundle = Bundle()
                bundle.putString("PICK_NOVEL_PLATFORM", state.value.type)
                bundle.putString("PICK_NOVEL_STATUS", "FIRST")
                Firebase.analytics.logEvent("BEST_ActivityBestDetail", bundle)
            } else {
                val bundle = Bundle()
                bundle.putString("PICK_NOVEL_PLATFORM", state.value.type)
                bundle.putString("PICK_NOVEL_STATUS", "ADD")
                Firebase.analytics.logEvent("BEST_ActivityBestDetail", bundle)

                Novel.child("book").child(state.value.bookCode).setValue(state.value.type)

                if (state.value.bookData.isEmpty()) {
                    Novel.child("bookCode").child(state.value.bookCode).child(DBDate.DateMMDD())
                        .setValue(state.value.bestListAnalyze)
                } else {
                    Novel.child("bookCode").child(state.value.bookCode).setValue(state.value.bookCode)
                }

                viewModelScope.launch {
                    _sideEffects.send("[${state.value.bestItemData.title}]이(가) 마이픽에 등록되었습니다.")
                }

            }
        }
    }

    private fun setLayoutJoara(activity: ComponentActivity) {
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(activity)
        JoaraRef["book_code"] = state.value.bookCode
        JoaraRef["category"] = "1"

        val bestItemData = BestDetailItemData()
        var bestListAnalyze = BestListAnalyze()
        var joaraChapter: List<JoaraBestChapter>? = null
        val typeItems = ArrayList<BestType>()

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(data: JoaraBestDetailResult) {

                    if (data.status == "1" && data.book != null) {

                        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        bestItemData.bookImg = data.book.bookImg.replace("http://", "https://")
                        bestItemData.title = data.book.subject
                        bestItemData.bookCode = data.book.bookCode
                        bestItemData.info = "${data.book.writerName} | 총 ${data.book.cntChapter}"
                        bestItemData.info1 = BestRef.decimalToString(data.book.cntPageRead.toInt())
                        bestItemData.info2 = BestRef.decimalToString(data.book.cntFavorite.toInt())
                        bestItemData.info3 = BestRef.decimalToString(data.book.cntRecom.toInt())
                        bestItemData.info4 = BestRef.decimalToString(data.book.cntTotalComment.toInt())
                        bestItemData.intro = data.book.intro
                        bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
                        bestItemData.date = DBDate.DateMMDD()
                        bestItemData.type = state.value.type

                        Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

                        joaraChapter = data.book.chapter

                        bestListAnalyze = BestListAnalyze(
                            data.book.cntPageRead,
                            data.book.cntFavorite,
                            data.book.cntRecom,
                            data.book.cntTotalComment,
                            DBDate.DateMMDDHHMMSS().toInt(),
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        for (item in data.book.keyword) {
                            typeItems.add(BestType("#${item}", ""))
                        }
                    }

                    viewModelScope.launch {
                        if(joaraChapter != null){
                            events.send(EventBestDetail.isJoaraChapter(joaraChapter = joaraChapter!!))
                        }

                        events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
                        events.send(EventBestDetail.bestDetailKeywords(keywords = typeItems))
                    }

                    if (state.value.hasBookData) {
                        val bundle = Bundle()
                        bundle.putString("BEST_DETAIL_TAB", "FragmentBestDetailAnalyze")
                        Firebase.analytics.logEvent("BEST_ActivityBestDetail", bundle)

                        viewModelScope.launch {
                            events.send(EventBestDetail.isTab(isTabAnalyze = true, isTabComment = false, isTabOther = false))
                        }
                    } else {
                        viewModelScope.launch {
                            events.send(EventBestDetail.isTab(isTabAnalyze = false, isTabComment = true, isTabOther = false))
                        }
                    }
                }
            })
    }

    fun goToViewDetail(activity: ComponentActivity){
        val bundle = Bundle()
        bundle.putString("BEST_GO_DETAIL", state.value.type)
        Firebase.analytics.logEvent("BEST_ActivityBestDetail", bundle)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl()))
        activity.startActivity(intent)
    }

    private fun getUrl(): String {

        return if (state.value.type == "MrBlue") {
            "https://www.mrblue.com/novel/${state.value.bookCode}"
        } else if (state.value.type == "Naver_Today" || state.value.type == "Naver_Challenge" || state.value.type == "Naver") {
            "https://novel.naver.com/webnovel/list?novelId=${state.value.bookCode}"
        } else if (state.value.type == "Ridi") {
            "https://ridibooks.com/books/${state.value.bookCode}"
        } else if (state.value.type == "Kakao_Stage") {
            "https://pagestage.kakao.com/novels/${state.value.bookCode}"
        } else if (state.value.type == "Kakao") {
            "https://page.kakao.com/home?seriesId=${state.value.bookCode}"
        } else if (state.value.type == "OneStore") {
            "https://onestory.co.kr/detail/${state.value.bookCode}"
        } else if (state.value.type == "Joara" || state.value.type == "Joara_Premium" || state.value.type == "Joara_Nobless") {
            "https://www.joara.com/book/${state.value.bookCode}"
        } else if (state.value.type == "Munpia") {
            "https://novel.munpia.com/${state.value.bookCode}"
        } else if (state.value.type == "Toksoda") {
            "https://www.tocsoda.co.kr/product/productView?brcd=${state.value.bookCode}"
        } else ""
    }
}
