package com.example.moavara.Best

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ActivityBestDetailBinding
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class ActivityBestDetail : AppCompatActivity() {

    var bookCode = ""
    var platform = ""
    var bookTitle = ""
    var bookWriter = ""
    var chapter: List<JoaraBestChapter>? = null
    var pos = 0
    var itemCount = 0
    var genre = ""
    private lateinit var binding: ActivityBestDetailBinding
    private lateinit var mFragmentBestDetailAnalyze: FragmentBestDetailAnalyze
    private lateinit var mFragmentBestDetailBooks: FragmentBestDetailBooks
    private lateinit var mFragmentBestDetailComment: FragmentBestDetailComment
    val data = ArrayList<BookListDataBestAnalyze>()
    val lp = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookCode = intent.getStringExtra("BookCode") ?: ""
        platform = intent.getStringExtra("Type") ?: ""
        pos = intent.getIntExtra("POSITION", 0)
        itemCount = intent.getIntExtra("COUNT", 0)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        genre = Genre.getGenre(this).toString()

        binding.llayoutBtnRight.background = GradientDrawable().apply {
            setColor(Color.parseColor("#621CEF"))
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 100f.dpToPx()
        }

        binding.loading.root.visibility = View.VISIBLE
        binding.coorWrap.visibility = View.GONE
        window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        setUserPick()

        BestRef.getBookCode(platform, genre).child(bookCode).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (item in dataSnapshot.children) {
                    val group: BookListDataBestAnalyze? =
                        item.getValue(BookListDataBestAnalyze::class.java)

                    if (group != null) {
                        data.add(
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


    }

    private fun setUserPick() {
        BestRef.getBestRefToday(platform, genre).child(pos.toString())
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val group: BookListDataBestToday? =
                        dataSnapshot.getValue(BookListDataBestToday::class.java)

                    runOnUiThread {

                        with(binding) {

                            llayoutBtnRight.setOnClickListener {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(getUrl(bookCode))
                                )
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
        } else if (platform == "Munpia") {
            "https://novel.munpia.com/${bookCode}"
        } else if (platform == "Toksoda") {
            "https://www.tocsoda.co.kr/product/productView?brcd=${bookCode}"
        } else ""
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
                            FragmentBestDetailAnalyze(platform, data, genre, itemCount)
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
                                FragmentBestDetailAnalyze(platform, data, genre, itemCount)
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
                            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            Glide.with(this@ActivityBestDetail)
                                .load(data.book.bookImg)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = data.book.subject
                            chapter = data.book.chapter

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = data.book.writerName

                            inclueBestDetail.tviewInfo1.text = "총 ${data.book.cntChapter}화"
                            inclueBestDetail.tviewInfo2.text = data.book.cntFavorite
                            inclueBestDetail.tviewInfo3.text = data.book.cntPageRead
                            inclueBestDetail.tviewInfo4.text = data.book.cntRecom

                            tviewIntro.text = data.book.intro



                            for (item in data.book.keyword) {
                                val chip = Chip(this@ActivityBestDetail)
                                chip.text = "#${item}"
                                chip.chipBackgroundColor = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this@ActivityBestDetail,
                                        R.color.chip
                                    )
                                )
                                chip.setTextColor(Color.parseColor("#EDE6FD"))
                                chip.layoutParams = lp
                                chipgroup.addView(chip)
                            }

                        }
                    }

                    mFragmentBestDetailAnalyze = FragmentBestDetailAnalyze(
                        platform,
                        this@ActivityBestDetail.data, genre, itemCount
                    )
                    supportFragmentManager.commit {
                        replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                    }
                }
            })
    }

    fun setLayoutNaverToday() {
        Thread {
            val doc: Document =
                Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

            runOnUiThread {
                with(binding) {
                    binding.loading.root.visibility = View.GONE
                    binding.coorWrap.visibility = View.VISIBLE
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    chipgroup.visibility = View.GONE

                    Glide.with(this@ActivityBestDetail)
                        .load(doc.select(".section_area_info .pic img").attr("src"))
                        .into(inclueBestDetail.iviewBookCover)

                    if (platform == "Naver_Challenge" || platform == "Naver") {
                        inclueBestDetail.llayoutWrap2.visibility = View.GONE
                    } else {
                        inclueBestDetail.llayoutWrap2.visibility = View.VISIBLE
                    }

                    bookTitle = doc.select(".book_title").text()
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text = doc.select(".writer").text()

                    inclueBestDetail.tviewInfo1.text =
                        "장르 : ${doc.select(".info_book .genre").text()}"

                    inclueBestDetail.tviewInfo2.text = doc.select(".info_book .like").text()
                    inclueBestDetail.tviewInfo3.text = doc.select(".info_book .download").text()
                    inclueBestDetail.tviewInfo4.text = doc.select(".info_book .publish").text()

                    tviewIntro.text = doc.select(".section_area_info .dsc").text()
                }

                mFragmentBestDetailAnalyze =
                    FragmentBestDetailAnalyze(platform, data, genre, itemCount)
                supportFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
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
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.home?.let { it ->
                            Glide.with(this@ActivityBestDetail)
                                .load("https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}")
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.title
                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it.author_name

                            inclueBestDetail.tviewInfo1.text = "총 ${it.open_counts}화"
                            inclueBestDetail.tviewInfo2.text = it.sub_category
                            inclueBestDetail.tviewInfo3.text = it.read_count
                            inclueBestDetail.tviewInfo4.text = it.page_comment_count

                            tviewIntro.text = it.description
                        }

                        val keyword = data.related_keytalk_list

                        for (item in keyword) {
                            val chip = Chip(this@ActivityBestDetail)
                            chip.text = "#${item.item_name}"
                            chip.chipBackgroundColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ActivityBestDetail,
                                    R.color.chip
                                )
                            )
                            chip.setTextColor(Color.parseColor("#EDE6FD"))
                            chip.layoutParams = lp
                            chipgroup.addView(chip)
                        }
                    }
                }
            })

        mFragmentBestDetailAnalyze =
            FragmentBestDetailAnalyze(platform, data, genre, itemCount)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
        }
    }

    private fun setLayoutKaKaoStage() {
        val apiKakaoStage = RetrofitKaKao()

        binding.chipgroup.visibility = View.GONE

        apiKakaoStage.getBestKakaoStageDetail(
            bookCode,
            object : RetrofitDataListener<KakaoStageBestBookResult> {
                override fun onSuccess(data: KakaoStageBestBookResult) {

                    with(binding) {
                        loading.root.visibility = View.GONE
                        coorWrap.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.let {
                            Glide.with(this@ActivityBestDetail)
                                .load(data.thumbnail.url)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.title

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it.nickname.name

                            inclueBestDetail.tviewInfo1.text = "총 ${it.publishedEpisodeCount}화"
                            inclueBestDetail.tviewInfo2.text = it.favoriteCount
                            inclueBestDetail.tviewInfo3.text = it.viewCount
                            inclueBestDetail.tviewInfo4.text = it.visitorCount

                            tviewIntro.text = it.synopsis
                        }
                    }
                }
            })

        mFragmentBestDetailAnalyze =
            FragmentBestDetailAnalyze(platform, data, genre, itemCount)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
        }
    }

    private fun setLayoutRidi() {
        Thread {
            val doc: Document = Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

            bookCode = "https://ridibooks.com${
                doc.select(".metadata_writer .author_detail_link").attr("href")
            }"

            runOnUiThread {
                binding.chipgroup.visibility = View.GONE
                binding.llayoutIntro.visibility = View.GONE

                binding.loading.root.visibility = View.GONE
                binding.coorWrap.visibility = View.VISIBLE
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                with(binding) {
                    Glide.with(this@ActivityBestDetail)
                        .load("https:${doc.select(".thumbnail_image img").attr("src")}")
                        .into(inclueBestDetail.iviewBookCover)

                    bookTitle = doc.select(".header_info_wrap .info_title_wrap h3").text()
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text =
                        doc.select(".metadata_writer .author_detail_link").text()

                    inclueBestDetail.tviewInfo1.text =
                        doc.select(".header_info_wrap .info_category_wrap").text()
                    inclueBestDetail.tviewInfo2.text =
                        doc.select(".header_info_wrap .StarRate_Score").text()
                    inclueBestDetail.tviewInfo3.text =
                        doc.select(".header_info_wrap .StarRate_ParticipantCount").text()
                    inclueBestDetail.tviewInfo4.text =
                        doc.select(".metadata_info_series_complete_wrap .metadata_item").text()

                    tviewIntro.text = doc.select(".introduce_book .introduce_paragraph").text()
                }

                mFragmentBestDetailAnalyze =
                    FragmentBestDetailAnalyze(platform, data, genre, itemCount)
                supportFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
                }
            }
        }.start()
    }

    private fun setLayoutOneStory() {
        val apiOnestory = RetrofitOnestore()
        val param: MutableMap<String?, Any> = HashMap()

        param["channelId"] = bookCode
        param["bookpassYn"] = "N"

        apiOnestory.getBestKakaoStageDetail(
            bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetail> {
                override fun onSuccess(data: OnestoreBookDetail) {

                    with(binding) {
                        binding.loading.root.visibility = View.GONE
                        binding.coorWrap.visibility = View.VISIBLE
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.params.let {
                            Glide.with(this@ActivityBestDetail)
                                .load(it?.orgFilePos)
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it?.prodNm ?: ""

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = it?.artistNm

                            inclueBestDetail.tviewInfo1.text = "별점 : ${it?.ratingAvgScore}점"
                            inclueBestDetail.tviewInfo2.text = it?.pageViewTotal
                            inclueBestDetail.tviewInfo3.text = it?.serialCount
                            inclueBestDetail.tviewInfo4.text = it?.favoriteCount

                            binding.llayoutIntro.visibility = View.GONE

                            if (it != null) {
                                val keyword = it.tagList

                                if (keyword != null) {
                                    for (item in keyword) {
                                        val chip = Chip(this@ActivityBestDetail)
                                        chip.text = "#${item.tagNm}"
                                        chip.chipBackgroundColor = ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                this@ActivityBestDetail,
                                                R.color.chip
                                            )
                                        )
                                        chip.setTextColor(Color.parseColor("#EDE6FD"))
                                        chip.layoutParams = lp
                                        chipgroup.addView(chip)
                                    }
                                }
                            } else {
                                binding.chipgroup.visibility = View.GONE
                            }
                        }
                    }
                }
            })

        mFragmentBestDetailAnalyze =
            FragmentBestDetailAnalyze(platform, data, genre, itemCount)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
        }
    }

    fun setLayoutMunpia() {
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

            runOnUiThread {
                with(binding) {
                    binding.chipgroup.visibility = View.GONE
                    binding.loading.root.visibility = View.GONE
                    binding.coorWrap.visibility = View.VISIBLE
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    Glide.with(this@ActivityBestDetail)
                        .load("https:${doc.select(".cover-box img").attr("src")}")
                        .into(inclueBestDetail.iviewBookCover)

                    bookTitle = doc.select(".detail-box h2 a").text()
                        .replace(doc.select(".detail-box h2 a span").text() + " ", "")
                    inclueBestDetail.tviewTitle.text = bookTitle
                    inclueBestDetail.tviewWriter.text = doc.select(".member-trigger strong").text()

                    inclueBestDetail.tviewInfo1.text = doc.select(".meta-path strong").text()
                    inclueBestDetail.tviewInfo2.text =
                        doc.select(".meta-etc dd").next().next()[1].text()
                    inclueBestDetail.tviewInfo3.text =
                        doc.select(".meta-etc dd").next().next()[2].text()
                    inclueBestDetail.tviewInfo4.text =
                        doc.select(".meta-etc dt").next()[2].text()

                    tviewIntro.text = doc.select(".story").text()
                }

                mFragmentBestDetailAnalyze =
                    FragmentBestDetailAnalyze(platform, data, genre, itemCount)
                supportFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
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
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        data.result?.let {
                            Glide.with(this@ActivityBestDetail)
                                .load("https:${it.imgPath}")
                                .into(inclueBestDetail.iviewBookCover)

                            bookTitle = it.wrknm
                            bookWriter = it.athrnm

                            inclueBestDetail.tviewTitle.text = bookTitle
                            inclueBestDetail.tviewWriter.text = bookWriter

                            inclueBestDetail.tviewInfo1.text = "장르 :  ${it?.lgctgrNm}"
                            inclueBestDetail.tviewInfo2.text = it.inqrCnt
                            inclueBestDetail.tviewInfo3.text = it.intrstCnt
                            inclueBestDetail.tviewInfo4.text = it.goodCnt

                            tviewIntro.text = it.lnIntro

                            val keyword = it.hashTagList

                            if (keyword != null) {
                                for (item in keyword) {
                                    val chip = Chip(this@ActivityBestDetail)
                                    chip.text = "#${item.hashtagNm}"
                                    chip.chipBackgroundColor = ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this@ActivityBestDetail,
                                            R.color.chip
                                        )
                                    )
                                    chip.setTextColor(Color.parseColor("#EDE6FD"))
                                    chip.layoutParams = lp
                                    chipgroup.addView(chip)
                                }
                            }
                        }
                    }
                }
            })

        mFragmentBestDetailAnalyze =
            FragmentBestDetailAnalyze(platform, data, genre, itemCount)
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestDetailAnalyze)
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