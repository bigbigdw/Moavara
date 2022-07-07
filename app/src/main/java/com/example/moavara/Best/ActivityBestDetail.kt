package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.*
import com.example.moavara.databinding.ActivityBestDetailBinding
import com.google.android.material.tabs.TabLayout
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ActivityBestDetail : AppCompatActivity() {

    var bookCode = ""
    var type = ""
    var context = this
    var bookTitle = ""
    var chapter : List<JoaraBestChapter>? = null
    private lateinit var binding: ActivityBestDetailBinding
    private lateinit var mFragmentBestDetailAnalyze: FragmentBestDetailAnalyze
    private lateinit var mFragmentBestDetailBooks: FragmentBestDetailBooks
    private lateinit var mFragmentBestDetailComment: FragmentBestDetailComment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookCode = intent.getStringExtra("BookCode") ?: ""
        type = intent.getStringExtra("Type") ?: ""

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        if (type == "Joara" || type == "Joara Nobless" || type == "Joara Premium") {
            setLayoutJoara()
        } else if (type == "Naver Today" || type == "Naver Challenge" || type == "Naver"){
            setLayoutNaverToday()
        } else if (type == "Kakao"){
            setLayoutKaKao()
        } else if (type == "Kakao Stage"){
            setLayoutKaKaoStage()
        } else if (type == "Ridi"){
            setLayoutRidi()
        } else if (type == "OneStore"){
            setLayoutOneStory()
        } else if (type == "Munpia"){
            setLayoutMunpia()
        }

        if(type == "Naver Today" || type == "Naver Challenge" || type == "Naver"){
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
        } else if(type == "Kakao" || type == "Kakao Stage" || type == "OneStore" || type == "Munpia") {
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
        } else if(type == "Ridi") {
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
        } else {
            binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
            binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
            binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        if (type == "Joara" || type == "Joara Nobless" || type == "Joara Premium" || type == "Kakao" || type == "Kakao Stage" || type == "OneStore" || type == "Munpia") {
                            mFragmentBestDetailComment = FragmentBestDetailComment(type, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                            }
                        } else if (type == "Naver Today" || type == "Naver Challenge" || type == "Naver" || type == "Ridi") {
                            mFragmentBestDetailBooks = FragmentBestDetailBooks(type, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                            }
                        }
                    }
                    1->{
                        if (type == "Joara" || type == "Joara Nobless" || type == "Joara Premium") {
                            mFragmentBestDetailBooks = FragmentBestDetailBooks(type, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                            }
                        } else if (type == "Naver Today" || type == "Naver Challenge" || type == "Naver" || type == "Kakao"|| type == "Kakao Stage" || type == "Ridi" || type == "OneStore" || type == "Munpia") {
                            mFragmentBestDetailAnalyze = FragmentBestDetailAnalyze(type, intent.getIntExtra("POSITION", 0))
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                            }
                        }
                    }
                    2->{
                        mFragmentBestDetailAnalyze = FragmentBestDetailAnalyze(type, intent.getIntExtra("POSITION", 0))
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun setLayoutJoara(){
        val JoaraRef = Param.getItemAPI(this)
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "1"

        val call: Call<JoaraBestDetailResult> = RetrofitJoara.getBookDetailJoa(JoaraRef)

        call.enqueue(object : Callback<JoaraBestDetailResult?> {
            override fun onResponse(
                call: Call<JoaraBestDetailResult?>,
                response: Response<JoaraBestDetailResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->

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
                    }

                    mFragmentBestDetailComment = FragmentBestDetailComment(type, bookCode)
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestDetailResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
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

                    if(type == "Naver Challenge" || type == "Naver"){
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

                mFragmentBestDetailBooks = FragmentBestDetailBooks(type, bookCode)
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

        mFragmentBestDetailComment = FragmentBestDetailComment(type, bookCode)
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

        mFragmentBestDetailComment = FragmentBestDetailComment(type, bookCode)
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

                mFragmentBestDetailBooks = FragmentBestDetailBooks(type, bookCode)
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

        mFragmentBestDetailComment = FragmentBestDetailComment(type, bookCode)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
        }
    }

    fun setLayoutMunpia(){
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}" ).get()

            Log.d("####-1", doc.select(".detail-box h2 a").text())
            Log.d("####-2", doc.select(".detail-box h2 a span").text() + " ")
            Log.d("####-3", doc.select(".detail-box h2 a").text().replace(doc.select(".detail-box h2 a span").text() + " ", ""))

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

                mFragmentBestDetailComment = FragmentBestDetailComment(type, bookCode)
                supportFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                }
            }
        }.start()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}