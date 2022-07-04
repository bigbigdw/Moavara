package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Retrofit.JoaraBestChapter
import com.example.moavara.Retrofit.JoaraBestDetailResult
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
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

        Log.d("####", type)

        if (type == "Joara" || type == "Joara Nobless" || type == "Joara Premium") {
            setLayoutJoara()
        } else if (type == "Naver Today" || type == "Naver Challenge" || type == "Naver"){
            setLayoutNaverToday()
        }

        if(type == "Naver Today" || type == "Naver Challenge" || type == "Naver"){
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
                        if (type == "Joara" || type == "Joara Nobless" || type == "Joara Premium") {
                            mFragmentBestDetailComment = FragmentBestDetailComment(type, bookCode)
                            supportFragmentManager.commit {
                                replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                            }
                        } else if (type == "Naver Today" || type == "Naver Challenge" || type == "Naver") {
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
                        } else if (type == "Naver Today" || type == "Naver Challenge" || type == "Naver") {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}