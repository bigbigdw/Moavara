package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Retrofit.JoaraBestDetailResult
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Util.Param
import com.example.moavara.databinding.ActivityBestDetailBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ActivityBestDetail : AppCompatActivity() {

    var bookCode = ""
    var type = ""
    var context = this
    private lateinit var binding: ActivityBestDetailBinding
    private lateinit var mFragmentBestTabMonth: FragmentBestTabMonth
    private lateinit var mFragmentBestDetailBooks: FragmentBestDetailBooks
    private lateinit var mFragmentBestDetailComment: FragmentBestDetailComment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookCode = intent.getStringExtra("BookCode") ?: ""
        type = intent.getStringExtra("Type") ?: ""

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        mFragmentBestDetailComment = FragmentBestDetailComment("Joara", bookCode)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
        }

        binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
        binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.Best_Tab3))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        mFragmentBestDetailComment = FragmentBestDetailComment("Joara", bookCode)
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailComment)
                        }
                    }
                    1->{
                        mFragmentBestDetailBooks = FragmentBestDetailBooks("Joara", bookCode)
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestDetailBooks)
                        }
                    }
                    2->{
                        mFragmentBestTabMonth = FragmentBestTabMonth("Joara")
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabMonth)
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        if(type == "Joara"){
            setLayoutJoara()
        }

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

                                inclueBestDetail.tviewTitle.text = it.book.subject
                                inclueBestDetail.tviewWriter.text = it.book.writerName

                                inclueBestDetail.tviewInfo1.text = "총 " + it.book.cntChapter + " 화"
                                inclueBestDetail.tviewInfo2.text =  "조회 수 : " + it.book.cntFavorite
                                inclueBestDetail.tviewInfo3.text =  "선호작 수 : " + it.book.cntPageRead
                                inclueBestDetail.tviewInfo4.text =  "추천 수 : " + it.book.cntRecom

                                tviewIntro.text =  it.book.intro
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestDetailResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}