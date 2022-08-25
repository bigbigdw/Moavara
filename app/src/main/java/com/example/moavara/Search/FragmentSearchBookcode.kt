package com.example.moavara.Search

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moavara.Best.*
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.*
import com.example.moavara.databinding.FragmentSearchBookcodeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class FragmentSearchBookcode(private var platform: String = "Joara") : Fragment() {

    private var _binding: FragmentSearchBookcodeBinding? = null
    private val binding get() = _binding!!
    var bookCode = ""
    private lateinit var adapterType: AdapterSearchKeyword
    private val typeItems = ArrayList<BestType>()
    var UID = ""
    var userInfo = mRootRef.child("User")
    private var isPicked = false
    var bookTitle = ""
    var pickItem = BookListDataBest()
    var pickBookCodeItem = BookListDataBestAnalyze()
    val bookData = ArrayList<BookListDataBestAnalyze>()
    var genre = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBookcodeBinding.inflate(inflater, container, false)
        val view = binding.root

        adapterType = AdapterSearchKeyword(typeItems)
        typeItems.clear()
        binding.sview.queryHint = "조아라 검색"

        UID = requireActivity().getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("UID", "").toString()

        with(binding) {
            tviewSearch.text = "https://www.joara.com/book/1452405"
            sview.inputType = InputType.TYPE_CLASS_NUMBER

            rviewType.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType

            genre = Genre.getGenre(requireContext()).toString()

            binding.llayoutPick.background = GradientDrawable().apply {
                setColor(Color.parseColor("#621CEF"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
            }

            binding.llayoutView.background = GradientDrawable().apply {
                setColor(Color.parseColor("#3E424B"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
            }
        }

        for (i in BestRef.typeListTitleBookCode().indices) {
            typeItems.add(
                BestType(
                    BestRef.typeListTitleBookCode()[i],
                    BestRef.typeListBookcode()[i]
                )
            )
        }
        adapterType.notifyDataSetChanged()

        adapterType.setOnItemClickListener(object : AdapterSearchKeyword.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                adapterType.setSelectedBtn(position)
                adapterType.notifyDataSetChanged()

                val item: BestType = adapterType.getItem(position)
                platform = item.type.toString()

                with(binding) {
                    llayoutResult.visibility = View.GONE
                    llayoutSearch.visibility = View.VISIBLE

                    if (platform == "Joara") {
                        tviewSearch.text = "https://www.joara.com/book/1452405"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.queryHint = "조아라 검색"
                    } else if (platform == "Kakao") {
                        tviewSearch.text = "https://page.kakao.com/home?seriesId=56325530"
                        binding.sview.queryHint = "카카오페이지 검색"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                    } else if (platform == "Kakao_Stage") {
                        tviewSearch.text = "https://pagestage.kakao.com/novels/74312919"
                        binding.sview.queryHint = "카카오스테이지 검색"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                    } else if (platform == "Naver") {
                        tviewSearch.text = "https://novel.naver.com/webnovel/list?novelId=252934"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.queryHint = "네이버 시리즈 검색"
                    } else if (platform == "Naver_Challenge") {
                        tviewSearch.text = "https://novel.naver.com/challenge/list?novelId=75595"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.queryHint = "네이버 챌린지리그 검색"
                    } else if (platform == "Naver_Today") {
                        tviewSearch.text = "https://novel.naver.com/best/list?novelId=268129"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.queryHint = "네이버 베스트리그 검색"
                    } else if (platform == "Ridi") {
                        tviewSearch.text = "https://ridibooks.com/books/425295076"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.queryHint = "리디북스 검색"
                    } else if (platform == "OneStore") {
                        tviewSearch.text = "https://onestory.co.kr/detail/H042820022"
                        sview.inputType = InputType.TYPE_CLASS_TEXT
                    } else if (platform == "Munpia") {
                        tviewSearch.text = "https://novel.munpia.com/284801"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.queryHint = "문피아 검색"

                    } else if (platform == "Toksoda") {
                        tviewSearch.text =
                            "https://www.tocsoda.co.kr/product/productView?brcd=76M2207187389"
                        sview.inputType = InputType.TYPE_CLASS_TEXT
                        binding.sview.queryHint = "톡소다 검색"
                    }
                }
            }
        })

        binding.sview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                bookCode = query.toString()

                if (platform == "Joara") {
                    setLayoutJoara()
                } else if (platform == "Kakao") {
                    setLayoutKaKao()
                } else if (platform == "Kakao_Stage") {
                    setLayoutKaKaoStage()
                } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
                    setLayoutNaverToday()
                } else if (platform == "Ridi") {
                    setLayoutRidi()
                } else if (platform == "Munpia") {
                    setLayoutMunpia()
                } else if (platform == "OneStore") {
                    setLayoutOneStore()
                } else if (platform == "Toksoda") {
                    setLayoutToksoda()
                }
                binding.sview.setQuery("", false)

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        userInfo.child(UID).child("Novel").child("book").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (pickedItem in dataSnapshot.children) {

                    if (pickedItem.key.toString() == bookCode) {
                        isPicked = true
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#A7ACB7"))
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = 100f.dpToPx()
                        }

                        binding.tviewPick.text = "Pick 완료"
                        break
                    } else {
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#621CEF"))
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = 100f.dpToPx()
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
                    cornerRadius = 100f.dpToPx()
                }

                binding.tviewPick.text = "Pick 하기"

                Novel.child("book").child(bookCode).removeValue()
                Novel.child("bookCode").child(bookCode).removeValue()
                Toast.makeText(
                    requireContext(),
                    "[${bookTitle}]이(가) 마이픽에서 제거되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                isPicked = true
                binding.llayoutPick.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#A7ACB7"))
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 100f.dpToPx()
                }

                binding.tviewPick.text = "Pick 완료"

                Novel.child("book").child(bookCode).setValue(pickItem)

                if (bookData.isEmpty()) {
                    Novel.child("bookCode").child(bookCode).child(DBDate.DateMMDD())
                        .setValue(pickBookCodeItem)
                } else {
                    Novel.child("bookCode").child(bookCode).setValue(bookData)
                }

                Toast.makeText(
                    requireContext(),
                    "[${bookTitle}]이(가) 마이픽에 등록되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.llayoutView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(bookCode)))
            startActivity(intent)
        }

        return view
    }

    private fun setLayoutJoara() {
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(requireContext())
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "1"

        getBookData()

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(data: JoaraBestDetailResult) {

                    with(binding) {
                        if (data.status == "1" && data.book != null) {
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE
                            llayoutBtn.visibility = View.VISIBLE

                            Glide.with(requireContext())
                                .load(data.book.bookImg.replace("http://", "https://"))
                                .into(searchResult.iviewBookCover)

                            bookTitle = data.book.subject

                            searchResult.tviewTitle.text = bookTitle
                            searchResult.tviewWriter.text = data.book.writerName
                            searchResult.tviewInfo.text = "총 ${data.book.cntChapter}화"

                            searchResult.tviewInfo1.text =
                                BestRef.decimalToString(data.book.cntPageRead.toInt())
                            searchResult.tviewInfo2.text =
                                BestRef.decimalToString(data.book.cntFavorite.toInt())
                            searchResult.tviewInfo3.text =
                                BestRef.decimalToString(data.book.cntRecom.toInt())
                            searchResult.tviewInfo4.text =
                                BestRef.decimalToString(data.book.cntTotalComment.toInt())

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
                                0,
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
                        } else {
                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
                        }
                    }
                }
            })
    }

    fun setLayoutKaKao() {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        param["seriesid"] = bookCode
        getBookData()

        apiKakao.postKakaoBookDetail(
            param,
            object : RetrofitDataListener<BestKakaoBookDetail> {
                override fun onSuccess(data: BestKakaoBookDetail) {

                    with(binding) {

                        if (data.home != null) {
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE
                            llayoutBtn.visibility = View.VISIBLE

                            data.home.let {
                                Glide.with(requireContext())
                                    .load("https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}")
                                    .into(searchResult.iviewBookCover)

                                bookTitle = it.title
                                searchResult.tviewTitle.text = bookTitle
                                searchResult.tviewWriter.text = it.author_name

                                searchResult.tviewInfo.text = "총 ${it.open_counts}화"

                                searchResult.tviewInfo1.text =
                                    BestRef.decimalToString(it.page_rating_count.toInt())
                                searchResult.tviewInfo2.text = it.page_rating_summary
                                searchResult.tviewInfo3.text =
                                    BestRef.decimalToString(it.read_count.toInt())
                                searchResult.tviewInfo4.text =
                                    BestRef.decimalToString(it.page_comment_count.toInt())

                                pickItem = BookListDataBest(
                                    it.author_name,
                                    bookTitle,
                                    "https://dn-img-page.kakao.com/download/resource?kid=${it.land_thumbnail_url}",
                                    bookCode,
                                    it.description,
                                    "총 ${it.open_counts}화",
                                    it.page_rating_count,
                                    it.page_rating_summary.replace(".0", ""),
                                    it.read_count,
                                    it.page_comment_count,
                                    999,
                                    DBDate.DateMMDD(),
                                    platform,
                                    "",
                                )

                                pickBookCodeItem = BookListDataBestAnalyze(
                                    it.page_rating_count,
                                    it.page_rating_summary.replace(".0", ""),
                                    it.read_count,
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
                        } else {
                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
                        }
                    }
                }
            })
    }

    fun setLayoutKaKaoStage() {
        val apiKakaoStage = RetrofitKaKao()
        getBookData()

        apiKakaoStage.getBestKakaoStageDetail(
            bookCode,
            object : RetrofitDataListener<KakaoStageBestBookResult> {
                override fun onSuccess(data: KakaoStageBestBookResult) {

                    with(binding) {

                        if (data != null) {
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE
                            llayoutBtn.visibility = View.VISIBLE

                            data.let {
                                Glide.with(requireContext())
                                    .load(data.thumbnail.url)
                                    .into(searchResult.iviewBookCover)

                                bookTitle = it.title

                                searchResult.tviewTitle.text = bookTitle
                                searchResult.tviewWriter.text = it.nickname.name

                                searchResult.tviewInfo.text = "총 ${it.publishedEpisodeCount}화"
                                searchResult.tviewInfo1.text =
                                    BestRef.decimalToString(it.favoriteCount.toInt())
                                searchResult.tviewInfo2.text =
                                    BestRef.decimalToString(it.viewCount.toInt())
                                searchResult.tviewInfo3.text =
                                    BestRef.decimalToString(it.visitorCount.toInt())
                                searchResult.tviewInfo4.text =
                                    BestRef.decimalToString(it.episodeLikeCount.toInt())

                                pickItem = BookListDataBest(
                                    it.nickname.name,
                                    bookTitle,
                                    data.thumbnail.url,
                                    bookCode,
                                    it.synopsis,
                                    "총 ${it.publishedEpisodeCount}화",
                                    it.favoriteCount,
                                    it.viewCount,
                                    it.visitorCount,
                                    it.episodeLikeCount,
                                    999,
                                    DBDate.DateMMDD(),
                                    platform,
                                    "",
                                )

                                pickBookCodeItem = BookListDataBestAnalyze(
                                    it.favoriteCount,
                                    it.viewCount,
                                    it.visitorCount,
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
                        } else {
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE

                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
                        }
                    }
                }

            })
    }

    fun setLayoutNaverToday() {
        Thread {
            var doc: Document? = null

            try {
                when (platform) {
                    "Naver" -> {
                        doc =
                            Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}")
                                .post()
                    }
                    "Naver_Today" -> {
                        doc = Jsoup.connect("https://novel.naver.com/best/list?novelId=${bookCode}")
                            .post()
                    }
                    "Naver_Challenge" -> {
                        doc =
                            Jsoup.connect("https://novel.naver.com/challenge/list?novelId=${bookCode}")
                                .post()
                    }
                }

                getBookData()

                requireActivity().runOnUiThread {
                    with(binding) {
                        llayoutBtn.visibility = View.VISIBLE
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE

                        if (doc != null) {
                            Glide.with(requireContext())
                                .load(doc.select(".section_area_info .pic img").attr("src"))
                                .into(searchResult.iviewBookCover)

                            bookTitle = doc.select(".book_title").text()
                            searchResult.tviewTitle.text = bookTitle
                            searchResult.tviewWriter.text = doc.select(".writer").text()

                            searchResult.tviewInfo.text =
                                "장르 : ${doc.select(".info_book .genre").text()}"

                            searchResult.tviewInfo1.text =
                                doc.select(".info_book .like").text().replace("관심", "")
                                    .replace("명", "")
                            searchResult.tviewInfo2.text = doc.select(".grade_area em").text()

                            searchResult.iviewInfo3.setImageResource(R.drawable.ic_launcher_gray)
                            searchResult.tviewInfo3.text =
                                doc.select(".info_book .download").text().replace("다운로드", "")

                            searchResult.llayoutTab4.visibility = View.GONE
                            searchResult.viewTab4.visibility = View.GONE

                            pickItem = BookListDataBest(
                                doc.select(".writer").text(),
                                bookTitle,
                                doc.select(".section_area_info .pic img").attr("src"),
                                bookCode,
                                "장르 : ${doc.select(".info_book .genre").text()}",
                                doc.select(".section_area_info .dsc").text(),
                                doc.select(".info_book .like").text().replace("관심", "")
                                    .replace("명", ""),
                                doc.select(".grade_area em").text(),
                                doc.select(".info_book .download").text().replace("다운로드", ""),
                                "",
                                999,
                                DBDate.DateMMDD(),
                                platform,
                                "",
                            )

                            pickBookCodeItem = BookListDataBestAnalyze(
                                doc.select(".info_book .like").text().replace("관심", "")
                                    .replace("명", ""),
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
                    }

                }
            } catch (e: HttpStatusException) {

                requireActivity().runOnUiThread {
                    binding.llayoutSearch.visibility = View.GONE
                    binding.llayoutResult.visibility = View.VISIBLE

                    binding.blank.root.visibility = View.VISIBLE
                    binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                    binding.searchResult.root.visibility = View.GONE
                    binding.llayoutBtn.visibility = View.GONE
                }
            }

        }.start()
    }

    fun setLayoutRidi() {
        Thread {
            try {
                val doc: Document = Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

                requireActivity().runOnUiThread {
                    with(binding) {
                        llayoutBtn.visibility = View.VISIBLE
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE

                        Glide.with(requireContext())
                            .load("https:${doc.select(".thumbnail_image img").attr("src")}")
                            .into(searchResult.iviewBookCover)

                        bookTitle = doc.select(".header_info_wrap .info_title_wrap h3").text()
                        searchResult.tviewTitle.text = bookTitle
                        searchResult.tviewWriter.text =
                            doc.select(".metadata_writer .author_detail_link").text()
                        searchResult.tviewInfo.text =
                            doc.select(".header_info_wrap .info_category_wrap").text()

                        searchResult.tviewInfo1.text =
                            doc.select(".header_info_wrap .StarRate_Score").text()
                        searchResult.tviewInfo2.text =
                            doc.select(".header_info_wrap .StarRate_ParticipantCount").text()

                        searchResult.llayoutTab3.visibility = View.GONE
                        searchResult.llayoutTab4.visibility = View.GONE
                        searchResult.viewTab4.visibility = View.GONE
                        searchResult.viewTab3.visibility = View.GONE

                        searchResult.tviewInfo3.text =
                            doc.select(".metadata_info_series_complete_wrap .metadata_item").text()

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
                }
            } catch (e: HttpStatusException) {

                requireActivity().runOnUiThread {
                    binding.llayoutSearch.visibility = View.GONE
                    binding.llayoutResult.visibility = View.VISIBLE

                    binding.blank.root.visibility = View.VISIBLE
                    binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                    binding.searchResult.root.visibility = View.GONE
                    binding.llayoutBtn.visibility = View.GONE
                }
            }
        }.start()
    }

    fun setLayoutMunpia() {
        Thread {
            try {
                val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

                requireActivity().runOnUiThread {
                    with(binding) {
                        llayoutBtn.visibility = View.VISIBLE
                        llayoutSearch.visibility = View.GONE
                        llayoutResult.visibility = View.VISIBLE

                        Glide.with(requireContext())
                            .load("https:${doc.select(".cover-box img").attr("src")}")
                            .into(searchResult.iviewBookCover)

                        bookTitle = doc.select(".detail-box h2 a").text()
                            .replace(doc.select(".detail-box h2 a span").text() + " ", "")

                        searchResult.tviewTitle.text = bookTitle
                        searchResult.tviewWriter.text = doc.select(".member-trigger strong").text()

                        searchResult.tviewInfo.text = doc.select(".meta-path strong").text()

                        try {
                            searchResult.tviewInfo1.text =
                                doc.select(".meta-etc dd").next().next()[1]?.text() ?: ""
                            searchResult.tviewInfo2.text =
                                doc.select(".meta-etc dd").next().next()[2]?.text() ?: ""
                        } catch (e: IndexOutOfBoundsException) {
                            searchResult.tviewInfo1.visibility = View.GONE
                            searchResult.tviewInfo2.visibility = View.GONE
                        }

                        searchResult.llayoutTab3.visibility = View.GONE
                        searchResult.llayoutTab4.visibility = View.GONE
                        searchResult.viewTab4.visibility = View.GONE
                        searchResult.viewTab3.visibility = View.GONE

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
                }
            } catch (e: HttpStatusException) {

                requireActivity().runOnUiThread {
                    binding.llayoutSearch.visibility = View.GONE
                    binding.llayoutResult.visibility = View.VISIBLE
                    binding.blank.root.visibility = View.VISIBLE
                    binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                    binding.searchResult.root.visibility = View.GONE
                    binding.llayoutBtn.visibility = View.GONE
                }
            }
        }.start()
    }

    fun setLayoutToksoda() {
        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["brcd"] = bookCode
        param["_"] = "1657265744728"

        apiToksoda.getBestDetail(
            param,
            object : RetrofitDataListener<BestToksodaDetailResult> {
                override fun onSuccess(data: BestToksodaDetailResult) {

                    with(binding) {

                        if(data.result != null){
                            llayoutBtn.visibility = View.VISIBLE
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE

                            data.result.let {
                                Glide.with(requireContext())
                                    .load("https:${it.imgPath}")
                                    .into(searchResult.iviewBookCover)

                                bookTitle = it.wrknm

                                searchResult.tviewTitle.text = bookTitle
                                searchResult.tviewWriter.text = it.athrnm

                                searchResult.tviewInfo.text = "장르 :  ${it.lgctgrNm}"

                                searchResult.tviewInfo1.text = it.inqrCnt
                                searchResult.tviewInfo2.text = it.goodCnt
                                searchResult.tviewInfo3.text = it.intrstCnt

                                searchResult.llayoutTab4.visibility = View.GONE
                                searchResult.viewTab4.visibility = View.GONE

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
                            }
                        } else {
                            binding.llayoutSearch.visibility = View.GONE
                            binding.llayoutResult.visibility = View.VISIBLE
                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
                        }
                    }
                }
            })
    }

    fun setLayoutOneStore() {
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
                        if (data.params != null) {

                            llayoutBtn.visibility = View.VISIBLE
                            llayoutSearch.visibility = View.GONE
                            llayoutResult.visibility = View.VISIBLE

                            data.params.let {
                                Glide.with(requireContext())
                                    .load(it?.orgFilePos)
                                    .into(searchResult.iviewBookCover)

                                bookTitle = it?.prodNm ?: ""
                                searchResult.tviewTitle.text = bookTitle
                                searchResult.tviewWriter.text = it?.artistNm
                                searchResult.tviewInfo.text = it?.menuNm

                                searchResult.tviewInfo1.text = it?.ratingAvgScore
                                searchResult.tviewInfo2.text = it?.favoriteCount
                                searchResult.tviewInfo3.text = it?.pageViewTotal
                                searchResult.tviewInfo4.text = it?.commentCount

                                pickItem = BookListDataBest(
                                    it?.artistNm ?: "",
                                    bookTitle,
                                    it?.orgFilePos ?: "",
                                    bookCode,
                                    "",
                                    it?.menuNm ?: "",
                                    it?.ratingAvgScore ?: "",
                                    it?.favoriteCount ?: "",
                                    it?.pageViewTotal ?: "",
                                    it?.commentCount ?: "",
                                    999,
                                    DBDate.DateMMDD(),
                                    platform,
                                    "",
                                )

                                pickBookCodeItem = BookListDataBestAnalyze(
                                    it?.ratingAvgScore ?: "",
                                    it?.favoriteCount ?: "",
                                    it?.pageViewTotal ?: "",
                                    it?.commentCount ?: "",
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

                        } else {
                            binding.llayoutSearch.visibility = View.GONE
                            binding.llayoutResult.visibility = View.VISIBLE
                            binding.blank.root.visibility = View.VISIBLE
                            binding.blank.tviewblank.text = "찾으시는 작품이 없습니다."
                            binding.searchResult.root.visibility = View.GONE
                            binding.llayoutBtn.visibility = View.GONE
                        }
                    }
                }
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

    private fun getBookData() {
        BestRef.getBookCode(platform, genre).child(bookCode)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

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
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
}