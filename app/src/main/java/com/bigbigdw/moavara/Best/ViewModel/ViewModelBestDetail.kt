package com.bigbigdw.moavara.Best.ViewModel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.work.*
import com.bigbigdw.moavara.Best.intent.StateBestDetail
import com.bigbigdw.moavara.DataBase.*
import com.bigbigdw.moavara.Firebase.FirebaseWorkManager
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.Retrofit.*
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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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
                        } else if (state.value.type == "Naver_Today" || state.value.type == "Naver_Challenge" || state.value.type == "Naver") {
                            setLayoutNaverToday()
                        } else if (state.value.type == "Kakao") {
                            setLayoutKaKao()
                        } else if (state.value.type == "Kakao_Stage") {
                            setLayoutKaKaoStage()
                        } else if (state.value.type == "Ridi") {
                            setLayoutRidi()
                        } else if (state.value.type == "OneStore") {
                            setLayoutOneStory()
                        } else if (state.value.type == "Munpia") {
                            setLayoutMunpia()
                        } else if (state.value.type == "Toksoda") {
                            setLayoutToksoda()
                        }

                        if (dataSnapshot.exists()) {
                            for (item in dataSnapshot.children) {
                                val group: BestListAnalyze? =
                                    item.getValue(BestListAnalyze::class.java)

                                if (group != null) {
                                    bookData.add(group)
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
                                    bookData.add(group)
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
                events.send(EventBestDetail.isTab(isTabAnalyze = false, isTabComment = false, isTabOther = true))
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
        val typeItems = ArrayList<BestType>()
        var joaraChapter: List<JoaraBestChapter>? = null

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(data: JoaraBestDetailResult) {

                    if (data.status == "1" && data.book != null) {

                        viewModelScope.launch {
                            events.send(EventBestDetail.Loaded)
                        }

                        bestItemData.bookImg = data.book.bookImg.replace("http://", "https://")
                        bestItemData.title = data.book.subject
                        bestItemData.bookCode = state.value.bookCode
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
                }
            })
    }

    private fun setLayoutNaverToday() {

        val bestItemData = BestDetailItemData()
        var bestListAnalyze: BestListAnalyze

        Thread {

            val doc: Document =
                Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${state.value.bookCode}").post()

            viewModelScope.launch {
                events.send(EventBestDetail.Loaded)
            }

            bestItemData.bookImg = doc.select(".section_area_info .pic img").attr("src")
            bestItemData.title = doc.select(".book_title").text()
            bestItemData.bookCode = state.value.bookCode
            bestItemData.info = "${doc.select(".writer").text()} | 장르 : ${doc.select(".info_book .genre").text()}"
            bestItemData.info1 = doc.select(".info_book .like").text().replace("관심", "").replace("명", "")
            bestItemData.info2 = doc.select(".grade_area em").text()
            bestItemData.intro = doc.select(".section_area_info .dsc").text()
            bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
            bestItemData.date = DBDate.DateMMDD()
            bestItemData.type = state.value.type

            Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

            if (state.value.type != "Naver") {
                bestItemData.info3 = doc.select(".info_book .download").text().replace("다운로드", "")
            }

            bestListAnalyze = BestListAnalyze(
                bestItemData.info1,
                bestItemData.info2,
                bestItemData.info3,
                bestItemData.info4,
                DBDate.DateMMDDHHMMSS().toInt(),
                DBDate.DateMMDD(),
                0,
                0,
            )

            viewModelScope.launch {
                events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
            }
        }.start()
    }

    private fun setLayoutKaKao() {

        val bestItemData = BestDetailItemData()
        var bestListAnalyze = BestListAnalyze()
        val typeItems = ArrayList<BestType>()

        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["seriesid"] = state.value.bookCode

        apiKakao.postKakaoBookDetail(
            param,
            object : RetrofitDataListener<BestKakaoBookDetail> {
                override fun onSuccess(data: BestKakaoBookDetail) {

                    viewModelScope.launch {
                        events.send(EventBestDetail.Loaded)
                    }

                    data.home?.let { it ->

                        bestItemData.bookImg = "https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}"
                        bestItemData.title = it.title
                        bestItemData.bookCode = state.value.bookCode
                        bestItemData.info = "${it.author_name} | 총 ${it.open_counts}화"
                        bestItemData.info1 = BestRef.decimalToString(it.page_rating_count.toInt())
                        bestItemData.info2 = it.page_rating_summary
                        bestItemData.info3 = BestRef.decimalToString(it.read_count.toInt())
                        bestItemData.info4 = BestRef.decimalToString(it.page_comment_count.toInt())
                        bestItemData.intro = it.description
                        bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
                        bestItemData.date = DBDate.DateMMDD()
                        bestItemData.type = state.value.type

                        Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

                        bestListAnalyze = BestListAnalyze(
                            bestItemData.info1,
                            bestItemData.info2,
                            bestItemData.info3,
                            bestItemData.info4,
                            DBDate.DateMMDDHHMMSS().toInt(),
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )
                    }

                    val keyword = data.related_keytalk_list

                    for (item in keyword) {
                        typeItems.add(BestType("#${item.item_name}", ""))
                    }

                    viewModelScope.launch {
                        events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
                        events.send(EventBestDetail.bestDetailKeywords(keywords = typeItems))
                    }
                }
            })
    }

    private fun setLayoutKaKaoStage() {

        val bestItemData = BestDetailItemData()
        var bestListAnalyze = BestListAnalyze()

        val apiKakaoStage = RetrofitKaKao()

        apiKakaoStage.getBestKakaoStageDetail(
            state.value.bookCode,
            object : RetrofitDataListener<KakaoStageBestBookResult> {
                override fun onSuccess(data: KakaoStageBestBookResult) {

                    viewModelScope.launch {
                        events.send(EventBestDetail.Loaded)
                    }

                    data.let {

                        bestItemData.bookImg = data.thumbnail.url
                        bestItemData.title = it.title
                        bestItemData.bookCode = state.value.bookCode
                        bestItemData.info = "${it.nickname.name} | 총 ${it.publishedEpisodeCount}화"
                        bestItemData.info1 = BestRef.decimalToString(it.favoriteCount.toInt())
                        bestItemData.info2 = BestRef.decimalToString(it.visitorCount.toInt())
                        bestItemData.info3 = BestRef.decimalToString(it.viewCount.toInt())
                        bestItemData.info4 = BestRef.decimalToString(it.episodeLikeCount.toInt())
                        bestItemData.intro = it.synopsis
                        bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
                        bestItemData.date = DBDate.DateMMDD()
                        bestItemData.type = state.value.type

                        Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

                        bestListAnalyze = BestListAnalyze(
                            bestItemData.info1,
                            bestItemData.info2,
                            bestItemData.info3,
                            bestItemData.info4,
                            DBDate.DateMMDDHHMMSS().toInt(),
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        viewModelScope.launch {
                            events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
                        }
                    }
                }
            })
    }

    private fun setLayoutRidi() {

        val bestItemData = BestDetailItemData()
        var bestListAnalyze = BestListAnalyze()

        Thread {
            val doc: Document = Jsoup.connect("https://ridibooks.com/books/${state.value.bookCode}").get()

            var bookWriter = "https://ridibooks.com${
                doc.select(".metadata_writer .author_detail_link").attr("href")
            }"

            viewModelScope.launch {
                events.send(EventBestDetail.Loaded)
            }

            bestItemData.bookImg = "https:${doc.select(".thumbnail_image img").attr("src")}"
            bestItemData.title = doc.select(".header_info_wrap .info_title_wrap h3").text()
            bestItemData.bookCode = state.value.bookCode
            bestItemData.info = "${doc.select(".metadata_writer .author_detail_link").text()} | ${doc.select(".header_info_wrap .info_category_wrap").text()}"
            bestItemData.info1 = doc.select(".header_info_wrap .StarRate_Score").text()
            bestItemData.info2 = doc.select(".header_info_wrap .StarRate_ParticipantCount").text().replace("명", "")
            bestItemData.info3 = doc.select(".metadata_info_series_complete_wrap .metadata_item").text()
            bestItemData.info4 = ((doc.select(".header_info_wrap .StarRate_Score").text().replace("점", "")).toFloat() * 10).toString().replace(".0", "")
            bestItemData.intro = doc.select(".introduce_book .introduce_paragraph").text()
            bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
            bestItemData.date = DBDate.DateMMDD()
            bestItemData.type = state.value.type

            Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

            bestListAnalyze = BestListAnalyze(
                bestItemData.info1,
                bestItemData.info2,
                bestItemData.info3,
                bestItemData.info4,
                DBDate.DateMMDDHHMMSS().toInt(),
                DBDate.DateMMDD(),
                0,
                0,
            )

            viewModelScope.launch {
                events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
            }

        }.start()
    }

    private fun setLayoutOneStory() {
        val bestItemData = BestDetailItemData()
        var bestListAnalyze = BestListAnalyze()
        val typeItems = ArrayList<BestType>()

        val apiOnestory = RetrofitOnestore()
        val param: MutableMap<String?, Any> = HashMap()

        param["channelId"] = state.value.bookCode
        param["bookpassYn"] = "N"

        apiOnestory.getOneStoreDetail(
            state.value.bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetail> {
                override fun onSuccess(data: OnestoreBookDetail) {

                    viewModelScope.launch {
                        events.send(EventBestDetail.Loaded)
                    }

                    data.params.let {

                        bestItemData.bookImg = it?.orgFilePos ?: ""
                        bestItemData.title = it?.prodNm ?: ""
                        bestItemData.bookCode = state.value.bookCode
                        bestItemData.info = "${it?.artistNm} | 총 ${it?.menuNm}"
                        bestItemData.info1 = it?.ratingAvgScore?: ""
                        bestItemData.info2 = it?.favoriteCount?: ""
                        bestItemData.info3 = it?.pageViewTotal?: ""
                        bestItemData.info4 = it?.commentCount?: ""
                        bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
                        bestItemData.date = DBDate.DateMMDD()
                        bestItemData.type = state.value.type

                        Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

                        bestListAnalyze = BestListAnalyze(
                            bestItemData.info1,
                            bestItemData.info2,
                            bestItemData.info3,
                            bestItemData.info4,
                            DBDate.DateMMDDHHMMSS().toInt(),
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        if (it != null) {
                            val keyword = it.tagList

                            if (keyword != null) {

                                for (item in keyword) {
                                    typeItems.add(BestType("#${item.tagNm}", ""))
                                }

                                viewModelScope.launch {
                                    events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
                                    events.send(EventBestDetail.bestDetailKeywords(keywords = typeItems))
                                }
                            }
                        }
                    }
                }
            })
    }

    private fun setLayoutMunpia() {

        val bestItemData = BestDetailItemData()
        var bestListAnalyze = BestListAnalyze()

        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${state.value.bookCode}").get()

            viewModelScope.launch {
                events.send(EventBestDetail.Loaded)
            }

            bestItemData.bookImg = "https:${doc.select(".cover-box img").attr("src")}"
            bestItemData.title = doc.select(".detail-box h2 a").text().replace(doc.select(".detail-box h2 a span").text() + " ", "")
            bestItemData.bookCode = state.value.bookCode
            bestItemData.info = "${doc.select(".member-trigger strong").text()} | ${doc.select(".meta-path strong").text()}"
            bestItemData.info1 = doc.select(".header_info_wrap .StarRate_Score").text()
            bestItemData.info2 = doc.select(".header_info_wrap .StarRate_ParticipantCount").text().replace("명", "")
            bestItemData.info3 = doc.select(".meta-etc dd").next().next()[1]?.text() ?: ""
            bestItemData.info4 = doc.select(".meta-etc dd").next().next()[2]?.text() ?: ""
            bestItemData.intro = doc.select(".story").text()
            bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
            bestItemData.date = DBDate.DateMMDD()
            bestItemData.type = state.value.type

            Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

            try {
                bestItemData.info1 = doc.select(".meta-etc dd").next().next()[1]?.text() ?: ""
                bestItemData.info2 = doc.select(".meta-etc dd").next().next()[2]?.text() ?: ""
            } catch (e: IndexOutOfBoundsException) { }


            bestListAnalyze = BestListAnalyze(
                bestItemData.info1,
                bestItemData.info2,
                bestItemData.info3,
                bestItemData.info4,
                DBDate.DateMMDDHHMMSS().toInt(),
                DBDate.DateMMDD(),
                0,
                0,
            )

            viewModelScope.launch {
                events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
            }

        }.start()
    }

    private fun setLayoutToksoda() {

        val bestItemData = BestDetailItemData()
        var bestListAnalyze = BestListAnalyze()
        val typeItems = ArrayList<BestType>()

        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["brcd"] = state.value.bookCode
        param["_"] = "1657265744728"

        apiToksoda.getBestDetail(
            param,
            object : RetrofitDataListener<BestToksodaDetailResult> {
                override fun onSuccess(data: BestToksodaDetailResult) {

                    data.result?.let {

                        bestItemData.bookImg = "https:${it.imgPath}"
                        bestItemData.title = it.wrknm
                        bestItemData.bookCode = state.value.bookCode
                        bestItemData.info = "${it.athrnm} | 장르 :  ${it.lgctgrNm}"
                        bestItemData.info1 = it.inqrCnt
                        bestItemData.info2 = it.goodCnt
                        bestItemData.info3 = it.intrstCnt

                        bestItemData.intro = it.lnIntro
                        bestItemData.number = DBDate.DateMMDDHHMMSS().toInt()
                        bestItemData.date = DBDate.DateMMDD()
                        bestItemData.type = state.value.type

                        Firebase.crashlytics.setCustomKey("ActivityBestDetail_TITLE", bestItemData.title)

                        bestListAnalyze = BestListAnalyze(
                            bestItemData.info1,
                            bestItemData.info2,
                            bestItemData.info3,
                            bestItemData.info4,
                            DBDate.DateMMDDHHMMSS().toInt(),
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        val keyword = it.hashTagList

                        if (keyword != null) {

                            for (item in keyword) {
                                typeItems.add(BestType("#${item.hashtagNm}", ""))
                            }

                            viewModelScope.launch {
                                events.send(EventBestDetail.bestDetailData(bestItemData = bestItemData, bestListAnalyze = bestListAnalyze))
                                events.send(EventBestDetail.bestDetailKeywords(keywords = typeItems))
                            }
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
