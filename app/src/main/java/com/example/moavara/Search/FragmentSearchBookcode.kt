package com.example.moavara.Search

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.moavara.Best.ActivityBestDetail
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.*
import com.example.moavara.databinding.FragmentSearchBookcodeBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.concurrent.TimeUnit

class FragmentSearchBookcode(private var platform: String = "Joara") : Fragment() {

    private var _binding: FragmentSearchBookcodeBinding? = null
    private val binding get() = _binding!!
    var bookCode = ""
    private lateinit var adapterType: AdapterSearchKeyword
    private val typeItems = ArrayList<BestType>()
    private var isPicked = false
    var bookTitle = ""
    var pickItem = BookListDataBest()
    var pickBookCodeItem = BookListDataBestAnalyze()
    val bookData = ArrayList<BookListDataBestAnalyze>()

    var userDao: DBUser? = null
    var UserInfo = DataBaseUser()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var isFirstPick = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBookcodeBinding.inflate(inflater, container, false)
        val view = binding.root

        adapterType = AdapterSearchKeyword(typeItems)
        typeItems.clear()
        binding.sview.hint = "조아라 검색"

        firebaseAnalytics = Firebase.analytics

        userDao = Room.databaseBuilder(
            requireContext(),
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if (userDao?.daoUser()?.get() != null) {
            UserInfo = userDao?.daoUser()?.get() ?: DataBaseUser()
        }

        with(binding) {
            tviewSearch.text = "1452405"
            sview.inputType = InputType.TYPE_CLASS_NUMBER

            rviewType.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType

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

            val tviewSearch1Text = SpannableStringBuilder("각 플랫폼 사이트 작품상세페이지\n주소에 북코드가 숨어있어요~")
            tviewSearch1Text.applyingTextColor(
                "북코드가 숨어있어요~",
                "#A780F6"
            )

            tviewSearch1.text = tviewSearch1Text

            val tviewSearch2Text = SpannableStringBuilder("북코드로 검색하면 더 많은\n정보를 볼 수 있어요!")
            tviewSearch2Text.applyingTextColor(
                "북코드로 검색",
                "#A780F6"
            )

            tviewSearch2.text = tviewSearch2Text
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
                        tviewSearch.text = "1452405"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.hint = "조아라 검색"
                        iviewSearch.setImageResource(R.drawable.quick_search_joara_img)
                    } else if (platform == "Kakao") {
                        tviewSearch.text = "56325530"
                        binding.sview.hint = "카카오페이지 검색"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        iviewSearch.setImageResource(R.drawable.quick_search_kakao_img)
                    } else if (platform == "Kakao_Stage") {
                        tviewSearch.text = "74312919"
                        binding.sview.hint = "카카오스테이지 검색"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        iviewSearch.setImageResource(R.drawable.quick_search_kakao2_img)
                    } else if (platform == "Naver") {
                        tviewSearch.text = "252934"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.hint = "시리즈 검색"
                        iviewSearch.setImageResource(R.drawable.quick_search_naver3_img)
                    } else if (platform == "Naver_Challenge") {
                        tviewSearch.text = "75595"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.hint = "챌린지리그 검색"
                        iviewSearch.setImageResource(R.drawable.quick_search_naver2_img)
                    } else if (platform == "Naver_Today") {
                        tviewSearch.text = "268129"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.hint = "베스트리그 검색"
                        iviewSearch.setImageResource(R.drawable.quick_search_naver_img)
                    } else if (platform == "Ridi") {
                        tviewSearch.text = "425295076"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.hint = "리디북스 검색"
                        iviewSearch.setImageResource(R.drawable.quick_search_ridi_img)
                    } else if (platform == "OneStore") {
                        tviewSearch.text = "H042820022"
                        sview.inputType = InputType.TYPE_CLASS_TEXT
                        iviewSearch.setImageResource(R.drawable.quick_search_onestory_img)
                    } else if (platform == "Munpia") {
                        tviewSearch.text = "284801"
                        sview.inputType = InputType.TYPE_CLASS_NUMBER
                        binding.sview.hint = "문피아 검색"
                        iviewSearch.setImageResource(R.drawable.quick_search_munpia_img)
                    } else if (platform == "Toksoda") {
                        tviewSearch.text =
                            "76M2207187389"
                        sview.inputType = InputType.TYPE_CLASS_TEXT
                        binding.sview.hint = "톡소다 검색"
                        iviewSearch.setImageResource(R.drawable.quick_search_tocsoda_img)
                    }
                }
            }
        })

        binding.btnSearch.setOnClickListener {
            bookCode = binding.sview.text.toString()

            if (platform == "Joara") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "Joara")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutJoara()
            } else if (platform == "Kakao") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "Kakao")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutKaKao()
            } else if (platform == "Kakao_Stage") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "Kakao_Stage")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutKaKaoStage()
            } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "Naver_Today")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutNaverToday()
            } else if (platform == "Ridi") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "Ridi")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutRidi()
            } else if (platform == "Munpia") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "Munpia")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutMunpia()
            } else if (platform == "OneStore") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "OneStore")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutOneStore()
            } else if (platform == "Toksoda") {
                val bundle = Bundle()
                bundle.putString("SEARCH_BOOKCODE_PLATFORM", "Toksoda")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                setLayoutToksoda()
            }
            binding.sview.text = SpannableStringBuilder("")

        }

        mRootRef.child("User").child(UserInfo.UID).child("Novel").child("book")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (!dataSnapshot.exists()) {
                        isFirstPick = true
                    }

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

                            binding.tviewPick.text = "Pick"
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        binding.llayoutPick.setOnClickListener {

            val Novel = mRootRef.child("User").child(UserInfo.UID).child("Novel")

            if (isPicked) {
                isPicked = false
                Novel.child("book").child(bookCode).removeValue()
                Novel.child("bookCode").child(bookCode).removeValue()

                binding.llayoutPick.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#621CEF"))
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 100f.dpToPx()
                }

                binding.tviewPick.text = "Pick"

                val bundle = Bundle()
                bundle.putString("PICK_NOVEL_PLATFORM", platform)
                bundle.putString("PICK_NOVEL_STATUS", "DELETE")
                firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)

                Novel.child("book").child(bookCode).removeValue()
                Novel.child("bookCode").child(bookCode).removeValue()
                Toast.makeText(
                    requireContext(),
                    "[${bookTitle}]이(가) 마이픽에서 제거되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                isPicked = true

                if (bookTitle.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "비정상적인 작품은 Pick을 할 수 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    if (isFirstPick) {
                        isFirstPick = false

                        val inputData = Data.Builder()
                            .putString(FirebaseWorkManager.TYPE, "PICK")
                            .putString(FirebaseWorkManager.UID, UserInfo.UID)
                            .putString(FirebaseWorkManager.USER, UserInfo.Nickname)
                            .build()

                        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
                        val workRequest =
                            PeriodicWorkRequestBuilder<FirebaseWorkManager>(6, TimeUnit.HOURS)
                                .setBackoffCriteria(
                                    BackoffPolicy.LINEAR,
                                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                                    TimeUnit.MILLISECONDS
                                )
                                .addTag("MoavaraPick")
                                .setInputData(inputData)
                                .build()

                        val workManager =
                            WorkManager.getInstance(requireContext().applicationContext)

                        workManager.enqueueUniquePeriodicWork(
                            "MoavaraPick",
                            ExistingPeriodicWorkPolicy.KEEP,
                            workRequest
                        )
                        FirebaseMessaging.getInstance().subscribeToTopic(UserInfo.UID)

                        Novel.child("book").child(bookCode).setValue(pickItem)

                        if (bookData.isEmpty()) {
                            Novel.child("bookCode").child(bookCode).child(DBDate.DateMMDD())
                                .setValue(pickBookCodeItem)
                        } else {
                            Novel.child("bookCode").child(bookCode).setValue(bookData)
                        }
                        mRootRef.child("User").child(UserInfo.UID).child("Mining").setValue(true)

                        val bundle = Bundle()
                        bundle.putString("PICK_NOVEL_PLATFORM", platform)
                        bundle.putString("PICK_NOVEL_STATUS", "FIRST")
                        firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)

                    } else {
                        Novel.child("book").child(bookCode).setValue(pickItem)

                        if (bookData.isEmpty()) {
                            Novel.child("bookCode").child(bookCode).child(DBDate.DateMMDD())
                                .setValue(pickBookCodeItem)
                        } else {
                            Novel.child("bookCode").child(bookCode).setValue(bookData)
                        }

                        val bundle = Bundle()
                        bundle.putString("PICK_NOVEL_PLATFORM", platform)
                        bundle.putString("PICK_NOVEL_STATUS", "ADD")
                        firebaseAnalytics.logEvent("SEARCH_FragmentSearchBookcode", bundle)
                    }

                    binding.llayoutPick.background = GradientDrawable().apply {
                        setColor(Color.parseColor("#A7ACB7"))
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 100f.dpToPx()
                    }

                    binding.tviewPick.text = "Pick 완료"

                    Toast.makeText(
                        requireContext(),
                        "[${bookTitle}]이(가) 마이픽에 등록되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.llayoutView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("BEST_FROM", "search")
            firebaseAnalytics.logEvent("BEST_ActivityBestDetail", bundle)

            val bookDetailIntent =
                Intent(requireContext(), ActivityBestDetail::class.java)
            bookDetailIntent.putExtra("BookCode", bookCode)
            bookDetailIntent.putExtra("Type", platform)
            bookDetailIntent.putExtra("POSITION", 0)
            bookDetailIntent.putExtra("HASDATA", true)
            startActivity(bookDetailIntent)
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

    private fun setLayoutKaKao() {
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

    private fun setLayoutKaKaoStage() {
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

    private fun setLayoutRidi() {
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

    private fun setLayoutMunpia() {
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

                        if (data.result != null) {
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

    private fun getBookData() {
        BestRef.getBookCode(platform, UserInfo.Genre).child(bookCode)
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