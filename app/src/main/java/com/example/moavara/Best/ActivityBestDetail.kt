package com.example.moavara.Best

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
import com.example.moavara.databinding.ActivityBestDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class ActivityBestDetail : AppCompatActivity() {

    var bookCode = ""
    var platform = ""
    var context = this
    var bookTitle = ""
    var bookWriter = ""
    var chapter : List<JoaraBestChapter>? = null
    var pos = 0
    var genre = ""
    var UID = ""
    var userInfo = mRootRef.child("User")
    private lateinit var binding: ActivityBestDetailBinding
    private lateinit var mFragmentBestDetailAnalyze: FragmentBestDetailAnalyze
    private lateinit var mFragmentBestDetailBooks: FragmentBestDetailBooks
    private lateinit var mFragmentBestDetailComment: FragmentBestDetailComment
    private lateinit var mFragmentBestDetailRank: FragmentBestDetailRank


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookCode = intent.getStringExtra("BookCode") ?: ""
        platform = intent.getStringExtra("Type") ?: ""
        pos = intent.getIntExtra("POSITION", 0)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        genre = Genre.getGenre(this).toString()

        UID = context.getSharedPreferences("pref", MODE_PRIVATE)
            ?.getString("UID", "").toString()

        setUserPick()

        BestRef.getBookCode(platform, genre).child(bookCode).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = ArrayList<BookListDataBestAnalyze>()

                for(item in dataSnapshot.children){
                    val group: BookListDataBestAnalyze? = item.getValue(BookListDataBestAnalyze::class.java)

                    if (group != null) {
                        data.add(
                            BookListDataBestAnalyze(
                                group.info1,
                                group.info2,
                                group.info3,
                                group.number,
                                group.date,
                                group.numberDiff,
                                group.trophyCount,
                            )
                        )
                    }
                }

                setLayout(data)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    private fun setUserPick(){
        BestRef.getBestRefToday(platform, genre).child(pos.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val group: BookListDataBestToday? = dataSnapshot.getValue(BookListDataBestToday::class.java)

                runOnUiThread {

                    with(binding){
                        llayoutBtnLeft.setOnClickListener {

                            val group = BookListDataBestToday(
                                group!!.writer,
                                group.title,
                                group.bookImg,
                                group.bookCode,
                                group.info1,
                                group.info2,
                                group.info3,
                                group.info4,
                                group.info5,
                                group.number,
                                group.numberDiff,
                                group.date,
                                platform,
                                group.status,
                                group.trophyCount,
                            )

                            userInfo.child(UID).child("book").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    userInfo.child(UID).child("book").child(dataSnapshot.childrenCount.toString()).setValue(group)
                                }
                                override fun onCancelled(databaseError: DatabaseError) {}
                            })

                            Toast.makeText(this@ActivityBestDetail, "Pick 성공!", Toast.LENGTH_SHORT).show()
                        }

                        llayoutBtnRight.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(group?.bookCode ?: "")))
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
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
        }else if (platform == "Munpia") {
            "https://novel.munpia.com/${bookCode}"
        } else if (platform == "Toksoda") {
            "https://www.tocsoda.co.kr/product/productView?brcd=${bookCode}"
        }else ""
    }

    fun setLayout(data: ArrayList<BookListDataBestAnalyze>?) {

        Log.d("####", data.toString())

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            setLayoutJoara()
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver"){
            setLayoutNaverToday()
        } else if (platform == "Kakao"){
            setLayoutKaKao()
        } else if (platform == "Kakao_Stage"){
            setLayoutKaKaoStage()
        } else if (platform == "Ridi"){
            setLayoutRidi()
        } else if (platform == "OneStore"){
            setLayoutOneStory()
        } else if (platform == "Munpia"){
            setLayoutMunpia()
        } else if (platform == "Toksoda"){
            setLayoutToksoda()
        }

        if(platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver"){
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("랭크"))
        } else if(platform == "Kakao" || platform == "Kakao_Stage" || platform == "OneStore" || platform == "Munpia") {
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("랭크"))
        } else if(platform == "Ridi") {
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("랭크"))
        } else {
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
            binding.tabs.addTab(binding.tabs.newTab().setText("랭크"))
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium" || platform == "Kakao" || platform == "Kakao_Stage" || platform == "OneStore" || platform == "Munpia" || platform == "Toksoda") {
                            mFragmentBestDetailComment = FragmentBestDetailComment(platform, bookCode)
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
                    1->{
                        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium" || platform == "Toksoda") {
                            mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                            }
                        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver" || platform == "Kakao"|| platform == "Kakao_Stage" || platform == "Ridi" || platform == "OneStore" || platform == "Munpia") {
                            mFragmentBestDetailAnalyze = FragmentBestDetailAnalyze(platform, data)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                            }
                        }
                    }
                    2->{
                        mFragmentBestDetailAnalyze = FragmentBestDetailAnalyze(platform, data)
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                        }
                    }
                    3->{
                        mFragmentBestDetailRank = FragmentBestDetailRank(platform, data)
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailRank)
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun setLayoutJoara(){
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(this)
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "1"

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(it: JoaraBestDetailResult) {

                    with(binding){
                        if(it.status == "1" && it.book != null){
                            Glide.with(context)
                                .load(it.book.bookImg)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.book.subject
                            chapter = it.book.chapter

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it.book.writerName

                            inclueBestDetail.tviewInfo1.text = "총 " + it.book.cntChapter + " 화"
                            inclueBestDetail.tviewInfo2.text =  "선호작 수 : " + it.book.cntFavorite
                            inclueBestDetail.tviewInfo3.text =  "조회 수 : " + it.book.cntPageRead
                            inclueBestDetail.tviewInfo4.text =  "추천 수 : " + it.book.cntRecom

                            tviewIntro.text =  it.book.intro
                        }
                    }

                    mFragmentBestDetailComment = FragmentBestDetailComment(platform, bookCode)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                    }
                }
            })
    }

    fun setLayoutNaverToday(){
        Thread {
            val doc: Document = Jsoup.connect(bookCode).post()

            bookCode = "https://novel.naver.com/${doc.select(".writer a").first()!!.attr("href")}"

            runOnUiThread {
                with(binding){
                    Glide.with(context)
                        .load(doc.select(".section_area_info .pic img").attr("src"))
                        .into(inclueBestDetail.iviewBookCover)

                    if(platform == "Naver_Challenge" || platform == "Naver"){
                        inclueBestDetail.llayoutWrap2.visibility = View.GONE
                    } else {
                        inclueBestDetail.llayoutWrap2.visibility = View.VISIBLE
                    }

                    bookTitle = doc.select(".book_title").text()
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text = doc.select(".writer").text()

                    inclueBestDetail.tviewInfo1.text = doc.select(".info_book .like").text()
                    inclueBestDetail.tviewInfo2.text =  doc.select(".info_book .download").text()
                    inclueBestDetail.tviewInfo3.text =  doc.select(".info_book .publish").text()
                    inclueBestDetail.tviewInfo4.text =  "장르 : ${doc.select(".info_book .genre").text()}"

                    tviewIntro.text = doc.select(".section_area_info .dsc").text()
                }

                mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                supportFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                }
            }
        }.start()
    }

    fun setLayoutKaKao(){
        val apiKakao = RetrofitKaKao()
        val param : MutableMap<String?, Any> = HashMap()

        param["seriesid"] = bookCode

        apiKakao.postKakaoBookDetail(
            param,
            object : RetrofitDataListener<BestKakaoBookDetail> {
                override fun onSuccess(data: BestKakaoBookDetail) {

                    with(binding){
                        data.home?.let { it ->
                            Glide.with(context)
                                .load("https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}")
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.title

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it.author_name

                            inclueBestDetail.tviewInfo1.text = "총 ${it.open_counts}화"
                            inclueBestDetail.tviewInfo2.text =  "장르 : ${it.sub_category}"
                            inclueBestDetail.tviewInfo3.text =  "조회 수 : ${it.read_count}"
                            inclueBestDetail.tviewInfo4.text =  "댓글 수 : ${it.page_comment_count}"

                            tviewIntro.text =  it.description
                        }
                    }
                }
            })

        mFragmentBestDetailComment = FragmentBestDetailComment(platform, bookCode)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
        }
    }

    fun setLayoutKaKaoStage(){
        val apiKakaoStage = RetrofitKaKao()

        apiKakaoStage.getBestKakaoStageDetail(
            bookCode,
            object : RetrofitDataListener<KakaoStageBestBookResult> {
                override fun onSuccess(data: KakaoStageBestBookResult) {

                    with(binding){
                        data.let { it ->
                            Glide.with(context)
                                .load(data.thumbnail.url)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.title

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it.nickname.name

                            inclueBestDetail.tviewInfo1.text = "총 ${it.publishedEpisodeCount}화"
                            inclueBestDetail.tviewInfo2.text =  "선호작 수 : ${it.favoriteCount}"
                            inclueBestDetail.tviewInfo3.text =  "조회 수 : ${it.viewCount}"
                            inclueBestDetail.tviewInfo4.text =  "방문 수 : ${it.visitorCount}"

                            tviewIntro.text =  it.synopsis
                        }
                    }
                }
            })

        mFragmentBestDetailComment = FragmentBestDetailComment(platform, bookCode)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
        }
    }

    fun setLayoutRidi(){
        Thread {
            val doc: Document = Jsoup.connect(bookCode).get()

            bookCode = "https://ridibooks.com${doc.select(".metadata_writer .author_detail_link").attr("href")}"

            runOnUiThread {

                binding.llayoutIntro.visibility = View.GONE

                with(binding){
                    Glide.with(context)
                        .load("https:${doc.select(".thumbnail_image img").attr("src")}")
                        .into(inclueBestDetail.iviewBookCover)

                    bookTitle = doc.select(".header_info_wrap .info_title_wrap h3").text()
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text = doc.select(".metadata_writer .author_detail_link").text()

                    inclueBestDetail.tviewInfo1.text = doc.select(".header_info_wrap .info_category_wrap").text()
                    inclueBestDetail.tviewInfo2.text =  "평점 : ${doc.select(".header_info_wrap .StarRate_Score").text()}"
                    inclueBestDetail.tviewInfo3.text =  doc.select(".header_info_wrap .StarRate_ParticipantCount").text()
                    inclueBestDetail.tviewInfo4.text =  doc.select(".metadata_info_series_complete_wrap .metadata_item").text()

                    tviewIntro.text = doc.select(".introduce_book .introduce_paragraph").text()
                }

                mFragmentBestDetailBooks = FragmentBestDetailBooks(platform, bookCode)
                supportFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                }
            }
        }.start()
    }

    fun setLayoutOneStory(){
        val apiOnestory = RetrofitOnestore()
        val param : MutableMap<String?, Any> = HashMap()

        param["channelId"] = bookCode
        param["bookpassYn"] = "N"

        apiOnestory.getBestKakaoStageDetail(
            bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetail> {
                override fun onSuccess(data: OnestoreBookDetail) {

                    with(binding){
                        data.params.let { it ->
                            Glide.with(context)
                                .load(it?.orgFilePos)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it?.prodNm ?: ""

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it?.artistNm

                            inclueBestDetail.tviewInfo1.text = "별점 : ${it?.ratingAvgScore}점"
                            inclueBestDetail.tviewInfo2.text =  "구독 수 : ${it?.pageViewTotal}"
                            inclueBestDetail.tviewInfo3.text =  "총 ${it?.serialCount}화"
                            inclueBestDetail.tviewInfo4.text =  "선호작 수 : ${it?.favoriteCount}"

                            binding.llayoutIntro.visibility = View.GONE
                        }
                    }
                }
            })

        mFragmentBestDetailComment = FragmentBestDetailComment(platform, bookCode)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
        }
    }

    fun setLayoutMunpia(){
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}" ).get()

            runOnUiThread {

                with(binding){
                    Glide.with(context)
                        .load("https:${doc.select(".cover-box img").attr("src")}")
                        .into(inclueBestDetail.iviewBookCover)

                    bookTitle = doc.select(".detail-box h2 a").text().replace(doc.select(".detail-box h2 a span").text() + " ", "")
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text = doc.select(".member-trigger strong").text()

                    inclueBestDetail.tviewInfo1.text = doc.select(".meta-path strong").text()
                    inclueBestDetail.tviewInfo2.text =  "조회 수 : ${doc.select(".meta-etc dd").next().next().get(1).text()}"
                    inclueBestDetail.tviewInfo3.text =  "추천 수 : ${doc.select(".meta-etc dd").next().next().get(2).text()}"
                    inclueBestDetail.tviewInfo4.text =  doc.select(".meta-etc dt").next().get(2).text()

                    tviewIntro.text = doc.select(".story").text()
                }

                mFragmentBestDetailComment = FragmentBestDetailComment(platform, bookCode)
                supportFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                }
            }
        }.start()
    }

    fun setLayoutToksoda(){
        val apiToksoda = RetrofitToksoda()
        val param : MutableMap<String?, Any> = HashMap()

        param["brcd"] = bookCode
        param["_"] = "1657265744728"

        apiToksoda.getBestDetail(
            param,
            object : RetrofitDataListener<BestToksodaDetailResult> {
                override fun onSuccess(data: BestToksodaDetailResult) {

                    with(binding){
                        data.result.let { it ->
                            Glide.with(context)
                                .load("https:${it?.imgPath}")
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it?.wrknm ?: ""
                            bookWriter = it?.athrnm ?: ""

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = bookWriter

                            inclueBestDetail.tviewInfo1.text = "장르 :  ${it?.lgctgrNm}"
                            inclueBestDetail.tviewInfo2.text =  "구독 수 : ${it?.inqrCnt}"
                            inclueBestDetail.tviewInfo3.text =  "관심 : ${it?.intrstCnt}"
                            inclueBestDetail.tviewInfo4.text =  "선호작 수 : ${it?.goodCnt}"

                            tviewIntro.text =  it?.lnIntro
                        }
                    }
                }
            })

        mFragmentBestDetailComment = FragmentBestDetailComment(platform, bookCode)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}