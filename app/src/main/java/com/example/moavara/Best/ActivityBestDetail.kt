package com.example.moavara.Best

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.Main.DialogConfirm
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.BestType
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Search.BookListDataBestAnalyze
import com.example.moavara.Util.*
import com.example.moavara.databinding.ActivityBestDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.concurrent.TimeUnit


class ActivityBestDetail : AppCompatActivity() {

    var bookCode = ""
    var platform = ""
    var bookTitle = ""
    var bookWriter = ""
    var bookLink = ""
    var chapter: List<JoaraBestChapter>? = null
    var pos = 0

    var genre = ""
    private lateinit var binding: ActivityBestDetailBinding
    private lateinit var mFragmentBestDetailAnalyze: FragmentBestDetailAnalyze
    private lateinit var mFragmentBestDetailBooks: FragmentBestDetailBooks
    private lateinit var mFragmentBestDetailComment: FragmentBestDetailComment

    private lateinit var adapterType: AdapterKeyword
    private val typeItems = ArrayList<BestType>()

    val bookData = ArrayList<BookListDataBestAnalyze>()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var UID = ""
    var userInfo = mRootRef.child("User")
    private var isPicked = false
    private var hasBookData = false
    private var fromPick = false
    private var fromSearch = false
    private var isFirstPick = false

    var pickItem = BookListDataBest()
    var pickBookCodeItem = BookListDataBestAnalyze()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics

        UID = getSharedPreferences("pref", MODE_PRIVATE)
            ?.getString("UID", "").toString()

        bookCode = intent.getStringExtra("BookCode") ?: ""
        platform = intent.getStringExtra("Type") ?: ""
        pos = intent.getIntExtra("POSITION", 0)
        hasBookData  = intent.getBooleanExtra("HASDATA", false)
        fromPick  = intent.getBooleanExtra("FROMPICK", false)
        fromSearch  = intent.getBooleanExtra("FROMSEARCH", false)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        genre = Genre.getGenre(this).toString()

        val bundle = Bundle()
        bundle.putString("test_test", "ActivityBestDetail")
        firebaseAnalytics.logEvent("test", bundle)

        binding.llayoutBtnRight.background = GradientDrawable().apply {
            setColor(Color.parseColor("#621CEF"))
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 100f.dpToPx()
        }

        binding.loading.root.visibility = View.VISIBLE
        binding.coorWrap.visibility = View.GONE
        window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        setUserPick()

        if(hasBookData){
            if(fromPick){
                userInfo.child(UID).child("Novel").child("bookCode").child(bookCode)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            if(!dataSnapshot.exists()){
                                isFirstPick = true
                            }

                            for (item in dataSnapshot.children) {
                                val group: BookListDataBestAnalyze? =
                                    item.getValue(BookListDataBestAnalyze::class.java)

                                if (group != null) {
                                    bookData.add(
                                        BookListDataBestAnalyze(
                                            group.info1,
                                            group.info2,
                                            group.info3,
                                            group.info4,
                                            group.number,
                                            group.numInfo1,
                                            group.numInfo2,
                                            group.numInfo3,
                                            group.numInfo4,
                                            group.date,
                                            group.numberDiff,
                                            group.trophyCount,
                                        )
                                    )
                                }
                            }

                            setLayout()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
            } else {
                BestRef.getBookCode(platform, genre).child(bookCode)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            if(dataSnapshot.exists()){
                                for (item in dataSnapshot.children) {
                                    val group: BookListDataBestAnalyze? =
                                        item.getValue(BookListDataBestAnalyze::class.java)

                                    if (group != null) {
                                        bookData.add(
                                            BookListDataBestAnalyze(
                                                group.info1,
                                                group.info2,
                                                group.info3,
                                                group.info4,
                                                group.number,
                                                group.numInfo1,
                                                group.numInfo2,
                                                group.numInfo3,
                                                group.numInfo4,
                                                group.date,
                                                group.numberDiff,
                                                group.trophyCount,
                                            )
                                        )
                                    }
                                }

                                setLayout()
                            } else {
                                hasBookData = false
                                setLayoutWithoutBookData()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
            }
        } else {
            setLayoutWithoutBookData()
        }
    }

    private fun setUserPick() {
        userInfo.child(UID).child("Novel").child("book").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (pickedItem in dataSnapshot.children) {

                    if (pickedItem.key.toString() == bookCode) {
                        isPicked = true
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#A7ACB7"))
                            shape = GradientDrawable.RECTANGLE
                        }

                        binding.tviewPick.text = "Pick 완료"
                        break
                    } else {
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#621CEF"))
                            shape = GradientDrawable.RECTANGLE
                        }

                        binding.tviewPick.text = "Pick 하기"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        binding.llayoutPick.setOnClickListener {

            val Novel = userInfo.child(UID).child("Novel")

            if (isPicked) {
                isPicked = false
                Novel.child("book").child(bookCode).removeValue()
                Novel.child("bookCode").child(bookCode).removeValue()

                binding.llayoutPick.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#621CEF"))
                    shape = GradientDrawable.RECTANGLE
                }

                binding.tviewPick.text = "Pick 하기"

                Novel.child("book").child(bookCode).removeValue()
                Novel.child("bookCode").child(bookCode).removeValue()
                Toast.makeText(this, "[${bookTitle}]이(가) 마이픽에서 제거되었습니다.", Toast.LENGTH_SHORT).show()

            } else {
                isPicked = true

                binding.llayoutPick.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#A7ACB7"))
                    shape = GradientDrawable.RECTANGLE
                }

                binding.tviewPick.text = "Pick 완료"

                if(isFirstPick){
                    isFirstPick = false

                    var dialogLogin: DialogConfirm? = null

                    val USER = getSharedPreferences("pref", MODE_PRIVATE)
                        ?.getString("NICKNAME", "").toString()

                    // 안내 팝업
                    dialogLogin = DialogConfirm(
                        this,
                        "환영합니다!",
                        "모아바라에서는 마이픽으로 선택한 작품을 6시간 주기로 데이터를 최신화 하고 있습니다.\n 이 옵션은 유저페이지 -> 마이픽 최신화 ON/OFF로 설정 가능합니다. 지금부터 주기적으로 마이픽 작품들을 최신화 하시겠습니까?",
                        { v: View? ->
                            Novel.child("book").child(bookCode).setValue(pickItem)

                            if(bookData.isEmpty()){
                                Novel.child("bookCode").child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
                            } else {
                                Novel.child("bookCode").child(bookCode).setValue(bookData)
                            }

                            Toast.makeText(this, "[${bookTitle}]이(가) 마이픽에 등록되었습니다.", Toast.LENGTH_SHORT).show()
                            dialogLogin?.dismiss()
                        },
                        {
                            val inputData = Data.Builder()
                                .putString(FirebaseWorkManager.TYPE, "PICK")
                                .putString(FirebaseWorkManager.UID, UID)
                                .putString(FirebaseWorkManager.USER, USER)
                                .build()

                            /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
                            val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(6, TimeUnit.HOURS)
                                .setBackoffCriteria(
                                    BackoffPolicy.LINEAR,
                                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                                    TimeUnit.MILLISECONDS
                                )
                                .addTag("MoavaraPick")
                                .setInputData(inputData)
                                .build()

                            val workManager = WorkManager.getInstance(applicationContext)

                            workManager.enqueueUniquePeriodicWork(
                                "MoavaraPick",
                                ExistingPeriodicWorkPolicy.KEEP,
                                workRequest
                            )
                            FirebaseMessaging.getInstance().subscribeToTopic(UID)

                            Novel.child("book").child(bookCode).setValue(pickItem)
                            userInfo.child(UID).child("Mining").setValue(true)

                            if(bookData.isEmpty()){
                                Novel.child("bookCode").child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
                            } else {
                                Novel.child("bookCode").child(bookCode).setValue(bookData)
                            }

                            Toast.makeText(this, "[${bookTitle}]이(가) 마이픽에 등록되었습니다.", Toast.LENGTH_SHORT).show()
                            dialogLogin?.dismiss()

                        },
                        "비활성화",
                        "활성화"
                    )

                    dialogLogin.window?.setBackgroundDrawable(
                        ColorDrawable(
                            Color.TRANSPARENT)
                    )
                    dialogLogin.show()
                } else {
                    Novel.child("book").child(bookCode).setValue(pickItem)

                    if(bookData.isEmpty()){
                        Novel.child("bookCode").child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
                    } else {
                        Novel.child("bookCode").child(bookCode).setValue(bookData)
                    }

                    Toast.makeText(this, "[${bookTitle}]이(가) 마이픽에 등록되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.lviewDetail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(bookLink)))
            startActivity(intent)
        }

    }


    fun setLayout() {

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            setLayoutJoara()
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            setLayoutNaverToday()
        } else if (platform == "Kakao") {
            setLayoutKaKao()
        } else if (platform == "Kakao_Stage") {
            setLayoutKaKaoStage()
        } else if (platform == "Ridi") {
            setLayoutRidi()
        } else if (platform == "OneStore") {
            setLayoutOneStory()
        } else if (platform == "Munpia") {
            setLayoutMunpia()
        } else if (platform == "Toksoda") {
            setLayoutToksoda()
        }

        if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
        } else if (platform == "Kakao" || platform == "Kakao_Stage" || platform == "OneStore" || platform == "Munpia") {
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
        } else if (platform == "Ridi") {
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
        } else {
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        mFragmentBestDetailAnalyze =
                            FragmentBestDetailAnalyze(platform, bookData, hasBookData)
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                        }
                    }
                    1 -> {
                        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium" || platform == "Kakao" || platform == "Kakao_Stage" || platform == "OneStore" || platform == "Munpia" || platform == "Toksoda") {
                            mFragmentBestDetailComment =
                                FragmentBestDetailComment(platform, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                            }
                        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver" || platform == "Ridi") {
                            mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                            }
                        }
                    }
                    2 -> {
                        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium" || platform == "Toksoda") {
                            mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                            }
                        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver" || platform == "Kakao" || platform == "Kakao_Stage" || platform == "Ridi" || platform == "OneStore" || platform == "Munpia") {
                            mFragmentBestDetailAnalyze =
                                FragmentBestDetailAnalyze(platform, bookData, hasBookData)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }



    private fun setLayoutJoara() {
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(this)
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "1"

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(data: JoaraBestDetailResult) {

                    with(binding) {
                        if (data.status == "1" && data.book != null) {

                            loading.root.visibility = View.GONE
                            coorWrap.visibility = View.VISIBLE
                            llayoutBtn.visibility = View.VISIBLE
                            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            Glide.with(this@ActivityBestDetail)
                                .load(data.book.bookImg.replace("http://", "https://"))
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = data.book.subject
                            chapter = data.book.chapter
                            bookLink = data.book.bookCode

                            tviewToolbar.text = data.book.subject

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = data.book.writerName
                            inclueBestDetail.tviewInfo.text = "총 ${data.book.cntChapter}화"

                            inclueBestDetail.tviewInfo1.text =
                                BestRef.decimalToString(data.book.cntPageRead.toInt())
                            inclueBestDetail.tviewInfo2.text =
                                BestRef.decimalToString(data.book.cntFavorite.toInt())
                            inclueBestDetail.tviewInfo3.text =
                                BestRef.decimalToString(data.book.cntRecom.toInt())
                            inclueBestDetail.tviewInfo4.text =
                                BestRef.decimalToString(data.book.cntTotalComment.toInt())

                            tviewIntro.text = data.book.intro

                            pickItem = BookListDataBest(
                                data.book.writerName,
                                data.book.subject,
                                data.book.bookImg.replace("http://", "https://"),
                                data.book.bookCode,
                                data.book.intro,
                                "총 ${data.book.cntChapter}화",
                                data.book.cntPageRead,
                                data.book.cntFavorite,
                                data.book.cntRecom,
                                data.book.cntTotalComment,
                                pos,
                                DBDate.DateMMDD(),
                                platform,
                                "",
                            )

                            pickBookCodeItem = BookListDataBestAnalyze(
                                data.book.cntPageRead,
                                data.book.cntFavorite,
                                data.book.cntRecom,
                                data.book.cntTotalComment,
                                999,
                                0,
                                0,
                                0,
                                0,
                                DBDate.DateMMDD(),
                                0,
                                0,
                            )

                            adapterType = AdapterKeyword(typeItems)

                            with(binding) {
                                rviewType.layoutManager =
                                    LinearLayoutManager(
                                        this@ActivityBestDetail,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                rviewType.adapter = adapterType
                            }

                            for (item in data.book.keyword) {
                                typeItems.add(BestType("#${item}", ""))
                            }
                            adapterType.notifyDataSetChanged()

                        }
                    }

                    if (hasBookData) {
                        mFragmentBestDetailAnalyze = FragmentBestDetailAnalyze(
                            platform,
                            this@ActivityBestDetail.bookData, hasBookData
                        )
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                        }
                    } else {
                        mFragmentBestDetailComment =
                            FragmentBestDetailComment(platform, bookCode)
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                        }
                    }
                }
            })
    }

    private fun setLayoutNaverToday() {
        Thread {

            val doc: Document =
                Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

            runOnUiThread {
                with(binding) {
                    binding.loading.root.visibility = View.GONE
                    binding.coorWrap.visibility = View.VISIBLE
                    llayoutBtn.visibility = View.VISIBLE
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    Glide.with(this@ActivityBestDetail)
                        .load(doc.select(".section_area_info .pic img").attr("src"))
                        .into(inclueBestDetail.iviewBookCover)

                    bookTitle = doc.select(".book_title").text()
                    tviewToolbar.text = bookTitle
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text = doc.select(".writer").text()

                    inclueBestDetail.tviewInfo.text =
                        "장르 : ${doc.select(".info_book .genre").text()}"

                    inclueBestDetail.tviewInfo1.text =
                        doc.select(".info_book .like").text().replace("관심", "").replace("명", "")
                    inclueBestDetail.tviewInfo2.text = doc.select(".grade_area em").text()

                    if(platform != "Naver"){
                        inclueBestDetail.llayoutTab3.visibility = View.VISIBLE
                        inclueBestDetail.iviewInfo3.setImageResource(R.drawable.ic_launcher_gray)
                        inclueBestDetail.tviewInfo3.text =
                            doc.select(".info_book .download").text().replace("다운로드", "")
                    } else {
                        inclueBestDetail.llayoutTab3.visibility = View.GONE
                    }

                    inclueBestDetail.llayoutTab4.visibility = View.GONE
                    inclueBestDetail.viewTab4.visibility = View.GONE

                    tviewIntro.text = doc.select(".section_area_info .dsc").text()

                    pickItem = BookListDataBest(
                        doc.select(".writer").text(),
                        bookTitle,
                        doc.select(".section_area_info .pic img").attr("src"),
                        bookCode,
                        "장르 : ${doc.select(".info_book .genre").text()}",
                        doc.select(".section_area_info .dsc").text(),
                        doc.select(".info_book .like").text().replace("관심", "").replace("명", ""),
                        doc.select(".grade_area em").text(),
                        doc.select(".info_book .download").text().replace("다운로드", ""),
                        "",
                        999,
                        DBDate.DateMMDD(),
                        platform,
                        "",
                    )

                    pickBookCodeItem = BookListDataBestAnalyze(
                        doc.select(".info_book .like").text().replace("관심", "").replace("명", ""),
                        doc.select(".grade_area em").text(),
                        doc.select(".info_book .download").text().replace("다운로드", ""),
                        "",
                        999,
                        0,
                        0,
                        0,
                        0,
                        DBDate.DateMMDD(),
                        0,
                        0,
                    )
                }



                if (hasBookData) {
                    mFragmentBestDetailAnalyze =
                        FragmentBestDetailAnalyze(platform, bookData, hasBookData)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                    }
                } else {
                    mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                    }
                }
            }
        }.start()
    }

    private fun setLayoutKaKao() {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["seriesid"] = bookCode

        apiKakao.postKakaoBookDetail(
            param,
            object : RetrofitDataListener<BestKakaoBookDetail> {
                override fun onSuccess(data: BestKakaoBookDetail) {

                    with(binding) {
                        loading.root.visibility = View.GONE
                        coorWrap.visibility = View.VISIBLE
                        llayoutBtn.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.home?.let { it ->
                            Glide.with(this@ActivityBestDetail)
                                .load("https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}")
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.title
                            tviewToolbar.text = bookTitle
                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it.author_name

                            inclueBestDetail.tviewInfo.text = "총 ${it.open_counts}화"

                            inclueBestDetail.tviewInfo1.text =
                                BestRef.decimalToString(it.page_rating_count.toInt())
                            inclueBestDetail.tviewInfo2.text = it.page_rating_summary
                            inclueBestDetail.tviewInfo3.text =
                                BestRef.decimalToString(it.read_count.toInt())
                            inclueBestDetail.tviewInfo4.text =
                                BestRef.decimalToString(it.page_comment_count.toInt())

                            tviewIntro.text = it.description

                            pickItem = BookListDataBest(
                                it.author_name,
                                bookTitle,
                                "https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}",
                                bookCode,
                                it.description,
                                "총 ${it.open_counts}화",
                                it.read_count,
                                it.page_rating_count,
                                it.page_rating_summary.replace(".0", ""),
                                it.page_comment_count,
                                999,
                                DBDate.DateMMDD(),
                                platform,
                                "",
                            )

                            pickBookCodeItem = BookListDataBestAnalyze(
                                it.read_count,
                                it.page_rating_count,
                                it.page_rating_summary.replace(".0", ""),
                                it.page_comment_count,
                                999,
                                0,
                                0,
                                0,
                                0,
                                DBDate.DateMMDD(),
                                0,
                                0,
                            )
                        }

                        val keyword = data.related_keytalk_list

                        adapterType = AdapterKeyword(typeItems)

                        with(binding) {
                            rviewType.layoutManager =
                                LinearLayoutManager(
                                    this@ActivityBestDetail,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            rviewType.adapter = adapterType
                        }

                        for (item in keyword) {
                            typeItems.add(BestType("#${item.item_name}", ""))
                        }
                        adapterType.notifyDataSetChanged()
                    }
                }
            })

        if (hasBookData) {
            mFragmentBestDetailAnalyze =
                FragmentBestDetailAnalyze(platform, bookData, hasBookData)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
            }
        } else {
            mFragmentBestDetailComment =
                FragmentBestDetailComment(platform, bookCode)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
            }
        }
    }

    private fun setLayoutKaKaoStage() {
        val apiKakaoStage = RetrofitKaKao()

        apiKakaoStage.getBestKakaoStageDetail(
            bookCode,
            object : RetrofitDataListener<KakaoStageBestBookResult> {
                override fun onSuccess(data: KakaoStageBestBookResult) {

                    with(binding) {
                        loading.root.visibility = View.GONE
                        coorWrap.visibility = View.VISIBLE
                        llayoutBtn.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.let {
                            Glide.with(this@ActivityBestDetail)
                                .load(data.thumbnail.url)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.title
                            tviewToolbar.text = bookTitle

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it.nickname.name

                            inclueBestDetail.tviewInfo.text = "총 ${it.publishedEpisodeCount}화"
                            inclueBestDetail.tviewInfo1.text =
                                BestRef.decimalToString(it.favoriteCount.toInt())
                            inclueBestDetail.tviewInfo2.text =
                                BestRef.decimalToString(it.visitorCount.toInt())
                            inclueBestDetail.tviewInfo3.text =
                                BestRef.decimalToString(it.viewCount.toInt())
                            inclueBestDetail.tviewInfo4.text =
                                BestRef.decimalToString(it.episodeLikeCount.toInt())

                            tviewIntro.text = it.synopsis

                            pickItem = BookListDataBest(
                                it.nickname.name,
                                bookTitle,
                                data.thumbnail.url,
                                bookCode,
                                it.synopsis,
                                "총 ${it.publishedEpisodeCount}화",
                                it.viewCount,
                                it.visitorCount,
                                it.favoriteCount,
                                it.episodeLikeCount,
                                999,
                                DBDate.DateMMDD(),
                                platform,
                                "",
                            )

                            pickBookCodeItem = BookListDataBestAnalyze(
                                it.viewCount,
                                it.visitorCount,
                                it.favoriteCount,
                                it.episodeLikeCount,
                                999,
                                0,
                                0,
                                0,
                                0,
                                DBDate.DateMMDD(),
                                0,
                                0,
                            )
                        }
                    }
                }
            })

        if (hasBookData) {
            mFragmentBestDetailAnalyze =
                FragmentBestDetailAnalyze(platform, bookData, hasBookData)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
            }
        } else {
            mFragmentBestDetailComment =
                FragmentBestDetailComment(platform, bookCode)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
            }
        }
    }

    private fun setLayoutRidi() {
        Thread {
            val doc: Document = Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

            bookCode = "https://ridibooks.com${
                doc.select(".metadata_writer .author_detail_link").attr("href")
            }"

            runOnUiThread {
                binding.tviewIntro.visibility = View.GONE
                binding.loading.root.visibility = View.GONE
                binding.coorWrap.visibility = View.VISIBLE
                binding.llayoutBtn.visibility = View.VISIBLE
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                with(binding) {
                    inclueBestDetail.llayoutTab3.visibility = View.GONE
                    inclueBestDetail.llayoutTab4.visibility = View.GONE
                    inclueBestDetail.viewTab4.visibility = View.GONE
                    inclueBestDetail.viewTab3.visibility = View.GONE

                    Glide.with(this@ActivityBestDetail)
                        .load("https:${doc.select(".thumbnail_image img").attr("src")}")
                        .into(inclueBestDetail.iviewBookCover)

                    bookTitle = doc.select(".header_info_wrap .info_title_wrap h3").text()
                    tviewToolbar.text = bookTitle
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text =
                        doc.select(".metadata_writer .author_detail_link").text()
                    inclueBestDetail.tviewInfo.text =
                        doc.select(".header_info_wrap .info_category_wrap").text()

                    inclueBestDetail.tviewInfo1.text =
                        doc.select(".header_info_wrap .StarRate_Score").text()
                    inclueBestDetail.tviewInfo2.text =
                        doc.select(".header_info_wrap .StarRate_ParticipantCount").text()

                    inclueBestDetail.tviewInfo3.text =
                        doc.select(".metadata_info_series_complete_wrap .metadata_item").text()

                    tviewIntro.text = doc.select(".introduce_book .introduce_paragraph").text()

                    pickItem = BookListDataBest(
                        doc.select(".metadata_writer .author_detail_link").text(),
                        bookTitle,
                        "https:${doc.select(".thumbnail_image img").attr("src")}",
                        bookCode,
                        "",
                        doc.select(".header_info_wrap .info_category_wrap").text(),
                        doc.select(".metadata_writer .author_detail_link").text(),
                        doc.select(".header_info_wrap .StarRate_ParticipantCount").text(),
                        "",
                        "",
                        999,
                        DBDate.DateMMDD(),
                        platform,
                        "",
                    )

                    pickBookCodeItem = BookListDataBestAnalyze(
                        doc.select(".metadata_writer .author_detail_link").text(),
                        doc.select(".header_info_wrap .StarRate_ParticipantCount").text(),
                        "",
                        "",
                        999,
                        0,
                        0,
                        0,
                        0,
                        DBDate.DateMMDD(),
                        0,
                        0,
                    )
                }

                if (hasBookData) {
                    mFragmentBestDetailAnalyze =
                        FragmentBestDetailAnalyze(platform, bookData, hasBookData)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                    }
                } else {
                    mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                    }
                }
            }
        }.start()
    }

    private fun setLayoutOneStory() {
        val apiOnestory = RetrofitOnestore()
        val param: MutableMap<String?, Any> = HashMap()

        param["channelId"] = bookCode
        param["bookpassYn"] = "N"

        apiOnestory.getOneStoreDetail(
            bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetail> {
                override fun onSuccess(data: OnestoreBookDetail) {

                    with(binding) {
                        binding.loading.root.visibility = View.GONE
                        binding.coorWrap.visibility = View.VISIBLE
                        binding.llayoutBtn.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.params.let {
                            Glide.with(this@ActivityBestDetail)
                                .load(it?.orgFilePos)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it?.prodNm ?: ""
                            tviewToolbar.text = bookTitle
                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it?.artistNm
                            inclueBestDetail.tviewInfo.text = it?.menuNm

                            inclueBestDetail.tviewInfo1.text = it?.ratingAvgScore
                            inclueBestDetail.tviewInfo2.text = it?.favoriteCount
                            inclueBestDetail.tviewInfo3.text = it?.pageViewTotal
                            inclueBestDetail.tviewInfo4.text = it?.commentCount
                            tviewIntro.visibility = View.GONE

                            pickItem = BookListDataBest(
                                it?.artistNm ?: "",
                                bookTitle,
                                it?.orgFilePos ?: "",
                                bookCode,
                                "",
                                it?.menuNm ?: "",
                                it?.pageViewTotal ?: "",
                                it?.ratingAvgScore ?: "",
                                it?.commentCount ?: "",
                                it?.favoriteCount ?: "",
                                999,
                                DBDate.DateMMDD(),
                                platform,
                                "",
                            )

                            pickBookCodeItem = BookListDataBestAnalyze(
                                it?.pageViewTotal ?: "",
                                it?.ratingAvgScore ?: "",
                                it?.commentCount ?: "",
                                it?.favoriteCount ?: "",
                                999,
                                0,
                                0,
                                0,
                                0,
                                DBDate.DateMMDD(),
                                0,
                                0,
                            )

                            if (it != null) {
                                val keyword = it.tagList

                                if (keyword != null) {

                                    adapterType = AdapterKeyword(typeItems)

                                    with(binding) {
                                        rviewType.layoutManager =
                                            LinearLayoutManager(
                                                this@ActivityBestDetail,
                                                LinearLayoutManager.HORIZONTAL,
                                                false
                                            )
                                        rviewType.adapter = adapterType
                                    }

                                    for (item in keyword) {
                                        typeItems.add(BestType("#${item.tagNm}", ""))
                                    }
                                    adapterType.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            })

        if (hasBookData) {
            mFragmentBestDetailAnalyze =
                FragmentBestDetailAnalyze(platform, bookData, hasBookData)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
            }
        } else {
            mFragmentBestDetailComment =
                FragmentBestDetailComment(platform, bookCode)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
            }
        }
    }

    private fun setLayoutMunpia() {
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

            runOnUiThread {
                with(binding) {
                    binding.loading.root.visibility = View.GONE
                    binding.coorWrap.visibility = View.VISIBLE
                    binding.llayoutBtn.visibility = View.VISIBLE
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    Glide.with(this@ActivityBestDetail)
                        .load("https:${doc.select(".cover-box img").attr("src")}")
                        .into(inclueBestDetail.iviewBookCover)

                    bookTitle = doc.select(".detail-box h2 a").text()
                        .replace(doc.select(".detail-box h2 a span").text() + " ", "")
                    tviewToolbar.text = bookTitle
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text = doc.select(".member-trigger strong").text()

                    inclueBestDetail.tviewInfo.text = doc.select(".meta-path strong").text()

                    try {
                        inclueBestDetail.tviewInfo1.text =
                            doc.select(".meta-etc dd").next().next()[1]?.text() ?: ""
                        inclueBestDetail.tviewInfo2.text =
                            doc.select(".meta-etc dd").next().next()[2]?.text() ?: ""
                    } catch (e: IndexOutOfBoundsException) {
                        inclueBestDetail.tviewInfo1.visibility = View.GONE
                        inclueBestDetail.tviewInfo2.visibility = View.GONE
                    }

                    inclueBestDetail.llayoutTab3.visibility = View.GONE
                    inclueBestDetail.llayoutTab4.visibility = View.GONE
                    inclueBestDetail.viewTab4.visibility = View.GONE
                    inclueBestDetail.viewTab3.visibility = View.GONE

                    tviewIntro.text = doc.select(".story").text()

                    pickItem = BookListDataBest(
                        doc.select(".member-trigger strong").text(),
                        bookTitle,
                        "https:${doc.select(".cover-box img").attr("src")}",
                        bookCode,
                        doc.select(".story").text(),
                        doc.select(".meta-path strong").text(),
                        doc.select(".meta-etc dd").next().next()[1]?.text() ?: "",
                        doc.select(".meta-etc dd").next().next()[2]?.text() ?: "",
                        "",
                        "",
                        999,
                        DBDate.DateMMDD(),
                        platform,
                        "",
                    )

                    pickBookCodeItem = BookListDataBestAnalyze(
                        doc.select(".meta-etc dd").next().next()[1]?.text() ?: "",
                        doc.select(".meta-etc dd").next().next()[2]?.text() ?: "",
                        "",
                        "",
                        999,
                        0,
                        0,
                        0,
                        0,
                        DBDate.DateMMDD(),
                        0,
                        0,
                    )
                }

                if (hasBookData) {
                    mFragmentBestDetailAnalyze =
                        FragmentBestDetailAnalyze(platform, bookData, hasBookData)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                    }
                } else {
                    mFragmentBestDetailComment =
                        FragmentBestDetailComment(platform, bookCode)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                    }
                }
            }
        }.start()
    }

    private fun setLayoutToksoda() {
        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["brcd"] = bookCode
        param["_"] = "1657265744728"

        apiToksoda.getBestDetail(
            param,
            object : RetrofitDataListener<BestToksodaDetailResult> {
                override fun onSuccess(data: BestToksodaDetailResult) {

                    with(binding) {
                        binding.loading.root.visibility = View.GONE
                        binding.coorWrap.visibility = View.VISIBLE
                        binding.llayoutBtn.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.result?.let {
                            Glide.with(this@ActivityBestDetail)
                                .load("https:${it.imgPath}")
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.wrknm
                            bookWriter = it.athrnm

                            inclueBestDetail.tviewTitle.text = bookTitle
                            tviewToolbar.text = bookTitle
                            inclueBestDetail.tviewWriter.text = bookWriter

                            inclueBestDetail.tviewInfo.text = "장르 :  ${it.lgctgrNm}"

                            inclueBestDetail.tviewInfo1.text = it.inqrCnt
                            inclueBestDetail.tviewInfo2.text = it.goodCnt
                            inclueBestDetail.tviewInfo3.text = it.intrstCnt

                            inclueBestDetail.llayoutTab4.visibility = View.GONE
                            inclueBestDetail.viewTab4.visibility = View.GONE

                            tviewIntro.text = it.lnIntro

                            pickItem = BookListDataBest(
                                it.athrnm,
                                it.wrknm,
                                "https:${it.imgPath}",
                                bookCode,
                                it.lnIntro,
                                "장르 :  ${it.lgctgrNm}",
                                it.inqrCnt,
                                it.goodCnt,
                                it.intrstCnt,
                                "",
                                999,
                                DBDate.DateMMDD(),
                                platform,
                                "",
                            )

                            pickBookCodeItem = BookListDataBestAnalyze(
                                it.inqrCnt,
                                it.goodCnt,
                                it.intrstCnt,
                                "",
                                999,
                                0,
                                0,
                                0,
                                0,
                                DBDate.DateMMDD(),
                                0,
                                0,
                            )

                            val keyword = it.hashTagList

                            if (keyword != null) {
                                adapterType = AdapterKeyword(typeItems)

                                with(binding) {
                                    rviewType.layoutManager =
                                        LinearLayoutManager(
                                            this@ActivityBestDetail,
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                    rviewType.adapter = adapterType
                                }

                                for (item in keyword) {
                                    typeItems.add(BestType("#${item.hashtagNm}", ""))
                                }
                                adapterType.notifyDataSetChanged()
                            }
                        }
                    }
                }
            })

        if (hasBookData) {
            mFragmentBestDetailAnalyze =
                FragmentBestDetailAnalyze(platform, bookData, hasBookData)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
            }
        } else {
            mFragmentBestDetailComment =
                FragmentBestDetailComment(platform, bookCode)
            supportFragmentManager.commit {
                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
            }
        }
    }

    fun setLayoutWithoutBookData() {

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            setLayoutJoara()
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            setLayoutNaverToday()
        } else if (platform == "Kakao") {
            setLayoutKaKao()
        } else if (platform == "Kakao_Stage") {
            setLayoutKaKaoStage()
        } else if (platform == "Ridi") {
            setLayoutRidi()
        } else if (platform == "OneStore") {
            setLayoutOneStory()
        } else if (platform == "Munpia") {
            setLayoutMunpia()
        } else if (platform == "Toksoda") {
            setLayoutToksoda()
        }

        if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
        } else if (platform == "Kakao" || platform == "Kakao_Stage" || platform == "OneStore" || platform == "Munpia") {
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
        } else if (platform == "Ridi") {
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
        } else {
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium" || platform == "Kakao" || platform == "Kakao_Stage" || platform == "OneStore" || platform == "Munpia" || platform == "Toksoda") {
                            mFragmentBestDetailComment =
                                FragmentBestDetailComment(platform, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                            }
                        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver" || platform == "Ridi") {
                            mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                            }
                        }
                    }
                    1 -> {
                        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium" || platform == "Toksoda") {
                            mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                            }
                        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver" || platform == "Kakao" || platform == "Kakao_Stage" || platform == "Ridi" || platform == "OneStore" || platform == "Munpia") {
                            mFragmentBestDetailAnalyze =
                                FragmentBestDetailAnalyze(platform, bookData, hasBookData)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun getUrl(bookCode: String): String {

        return if (platform == "MrBlue") {
            "https://www.mrblue.com/novel/${bookCode}"
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver" || platform == "Ridi") {
            bookCode
        } else if (platform == "Kakao_Stage") {
            "https://pagestage.kakao.com/novels/${bookCode}"
        } else if (platform == "Kakao") {
            "https://page.kakao.com/home?seriesId=${bookCode}"
        } else if (platform == "OneStore") {
            "https://onestory.co.kr/detail/${bookCode}"
        } else if (platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless") {
            "https://www.joara.com/book/${bookCode}"
        } else if (platform == "Munpia") {
            "https://novel.munpia.com/${bookCode}"
        } else if (platform == "Toksoda") {
            "https://www.tocsoda.co.kr/product/productView?brcd=${bookCode}"
        } else ""
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}