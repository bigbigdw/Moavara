package com.bigbigdw.moavara.Search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigbigdw.moavara.Best.ActivityBestDetail
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Retrofit.*
import com.bigbigdw.moavara.Util.BestRef
import com.bigbigdw.moavara.Util.DBDate
import com.bigbigdw.moavara.Util.Param
import com.bigbigdw.moavara.databinding.ActivitySearchBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*

class ActivitySearch : AppCompatActivity() {

    private lateinit var adapterType: AdapterSearchKeyword
    private val typeItems = ArrayList<BestType>()
    private lateinit var binding: ActivitySearchBinding

    private var adapter: AdapterBookSearch? = null
    private val searchItems = ArrayList<BestItemData>()
    var page = 1
    var linearLayoutManager: LinearLayoutManager? = null
    private var joaraOffset = 20
    private var kakaoStageOffset = 25
    var text = ""
    var type = "Keyword"
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sview.hint = "통합 검색"
        firebaseAnalytics = Firebase.analytics

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSearch.setOnClickListener {
            text = binding.sview.text.toString()
            searchItems.clear()
            adapter?.notifyDataSetChanged()
            val view = this.currentFocus
            if (view != null) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }


            with(binding) {
                adapter = AdapterBookSearch(searchItems, text)
                rviewSearch.adapter = adapter
                rviewSearch.layoutManager =
                    LinearLayoutManager(
                        this@ActivitySearch,
                        LinearLayoutManager.VERTICAL,
                        false
                    )

                llayoutSearch.visibility = View.GONE
                llayoutResult.visibility = View.VISIBLE
            }

            val bundle = Bundle()
            bundle.putString("SEARCH_PLATFORM", "Keyword")
            firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

            when (type) {
                "Keyword" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Keyword")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchJoara(page, text)
                    searchKakaoStage(page - 1, text)
                    searchKakao(page - 1, text)
                    searchNaver(text, "Naver")
                    searchNaver(text, "Naver_Today")
                    searchNaver(text, "Naver_Challenge")
                    searchMunpia(text)
                    searchToksoda(text)
                    searchMrBlue(text)
                }
                "Joara" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Joara")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchJoara(page, text)
                }
                "Naver_Today" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Naver_Today")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchNaver(text, "Naver_Today")
                }
                "Naver_Challenge" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Naver_Challenge")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchNaver(text, "Naver_Challenge")
                }
                "Naver" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Naver")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchNaver(text, "Naver")
                }
                "Kakao" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Kakao")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchKakao(page - 1, text)
                }
                "Kakao_Stage" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Kakao_Stage")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchKakaoStage(page - 1, text)
                }
                "Munpia" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Munpia")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchMunpia(text)
                }
                "Toksoda" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "Toksoda")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchToksoda(text)
                }
                "MrBlue" -> {
                    val bundle = Bundle()
                    bundle.putString("SEARCH_PLATFORM", "MrBlue")
                    firebaseAnalytics.logEvent("SEARCH_ActivitySearch", bundle)

                    searchMrBlue(text)
                }
            }
            adapter?.notifyDataSetChanged()

            adapter?.setOnItemClickListener(object : AdapterBookSearch.OnItemClickListener {
                override fun onItemClick(v: View?, position: Int) {
                    val item: BestItemData? = adapter?.getItem(position)

                    if(item?.type == "MrBlue"){
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse( "https://www.mrblue.com${item.bookCode}")
                        )
                        startActivity(intent)
                    } else {
                        val bundle = Bundle()
                        bundle.putString("BEST_FROM", "search")
                        firebaseAnalytics.logEvent("BEST_ActivityBestDetail", bundle)

                        val bookDetailIntent =
                            Intent(this@ActivitySearch, ActivityBestDetail::class.java)
                        bookDetailIntent.putExtra("BookCode", item?.bookCode)
                        bookDetailIntent.putExtra("Type", item?.type)
                        bookDetailIntent.putExtra("POSITION", position)
                        bookDetailIntent.putExtra("HASDATA", true)
                        startActivity(bookDetailIntent)
                    }
                }
            })
        }

        binding.blank.tviewblank.text = "검색어를 입력해주세요"

        getType()


    }

    fun searchJoara(page: Int, text: String) {
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(this)
        param["page"] = page
        param["query"] = text
        param["collection"] = ""
        param["search"] = "subject"
        param["kind"] = ""
        param["category"] = ""
        param["min_chapter"] = ""
        param["max_chapter"] = ""
        param["interval"] = ""
        param["orderby"] = ""
        param["except_query"] = ""
        param["except_search"] = ""
        param["expr_point"] = ""
        param["score_point"] = "1"

        apiJoara.getSearchJoara(
            param,
            object : RetrofitDataListener<JoaraSearchResult> {
                override fun onSuccess(data: JoaraSearchResult) {

                    val books = data.books

                    if (books != null) {
                        joaraOffset = books.size
                    }

                    if (books != null) {
                        for (i in books.indices) {
                            searchItems.add(
                                BestItemData(
                                    books[i].writer_name,
                                    books[i].subject,
                                    books[i].book_img.replace("http://","https://"),
                                    books[i].bookCode,
                                    books[i].intro,
                                    books[i].categoryKoName,
                                    books[i].cntPageRead,
                                    books[i].cntFavorite,
                                    books[i].cntRecom,
                                    books[i].cntTotalComment,
                                    999,
                                    DBDate.DateMMDDHHMM(),
                                    "Joara",
                                    "",
                                )
                            )
                        }

                        if (type == "Keyword") {
                            val cmpAsc: Comparator<BestItemData> =
                                Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                            Collections.sort(searchItems, cmpAsc)
                        }
                        adapter?.notifyDataSetChanged()

                        with(binding) {
                            if(searchItems.size == 0){
                                blank.root.visibility = View.VISIBLE
                                rviewSearch.visibility = View.GONE
                                binding.blank.tviewblank.text = "검색 결과가 없습니다."
                            } else {
                                blank.root.visibility = View.GONE
                                rviewSearch.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            })
    }

    fun searchKakao(page: Int, text: String) {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        param["page"] = page
        param["word"] = text
        param["category_uid"] = 11

        apiKakao.postKakaoSearch(
            param,
            object : RetrofitDataListener<SearchResultKakao> {
                override fun onSuccess(data: SearchResultKakao) {

                    val results = data.results

                    if (results != null) {

                        val items: List<KakaoBookItem>? = if (page == 0) {
                            results[2].items
                        } else {
                            results[0].items
                        }

                        if (items != null) {
                            kakaoStageOffset = items.size
                        }

                        if (items != null) {
                            for (j in items.indices) {
                                searchItems.add(
                                    BestItemData(
                                        items[j].author,
                                        items[j].title,
                                        "https://dn-img-page.kakao.com/download/resource?kid=${items[j].image_url}",
                                        items[j].id,
                                        items[j].publisher_name,
                                        items[j].sub_category,
                                        "",
                                        items[j].page,
                                        items[j].read_count,
                                        "",
                                        999,
                                        DBDate.DateMMDDHHMM(),
                                        "Kakao",
                                        "",
                                    )
                                )
                            }
                        }
                    }

                    if (type == "Keyword") {
                        val cmpAsc: Comparator<BestItemData> =
                            Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                        Collections.sort(searchItems, cmpAsc)
                    }

                    adapter?.notifyDataSetChanged()

                    with(binding) {
                        if(searchItems.size == 0){
                            blank.root.visibility = View.VISIBLE
                            rviewSearch.visibility = View.GONE
                            binding.blank.tviewblank.text = "검색 결과가 없습니다."
                        } else {
                            blank.root.visibility = View.GONE
                            rviewSearch.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }

    fun searchKakaoStage(page: Int, text: String) {
        val apiKakaoStage = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        param["genreIds"] = "1,2,3,4,5,6,7"
        param["keyword"] = text
        param["size"] = 20
        param["adult"] = "false"
        param["kakaopageSeries"] = "true"
        param["page"] = page

        apiKakaoStage.getSearchKakaoStage(
            param,
            object : RetrofitDataListener<KakaoStageSearchResult> {
                override fun onSuccess(data: KakaoStageSearchResult) {

                    val results = data.content

                    for (items in results) {
                        searchItems.add(
                            BestItemData(
                                items.nickname.name,
                                items.title,
                                items.thumbnail.url,
                                items.stageSeriesNumber,
                                items.synopsis,
                                items.subGenre.name,
                                items.favoriteCount,
                                items.viewCount,
                                items.publishedEpisodeCount,
                                "",
                                999,
                                DBDate.DateMMDDHHMM(),
                                "Kakao_Stage",
                                "",
                            )
                        )
                    }

                    if (type == "Keyword") {
                        val cmpAsc: Comparator<BestItemData> =
                            Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                        Collections.sort(searchItems, cmpAsc)
                    }
                    adapter?.notifyDataSetChanged()

                    with(binding) {
                        if(searchItems.size == 0){
                            blank.root.visibility = View.VISIBLE
                            rviewSearch.visibility = View.GONE
                            binding.blank.tviewblank.text = "검색 결과가 없습니다."
                        } else {
                            blank.root.visibility = View.GONE
                            rviewSearch.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }

    fun searchNaver(text: String, platform: String) {
        Thread {
            var doc: Document? = null

            when (platform) {
                "Naver" -> {
                    doc =
                        Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=webnovel&target=novel")
                            .post()
                }
                "Naver_Today" -> {
                    doc =
                        Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=best&target=novel")
                            .post()
                }
                "Naver_Challenge" -> {
                    doc =
                        Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=challenge&target=novel")
                            .post()
                }
            }

            val Naver: Elements = (doc?.select(".srch_cont .list_type2 li") ?: "") as Elements

            runOnUiThread {
                with(binding) {
                    blank.root.visibility = View.GONE
                    rviewSearch.visibility = View.VISIBLE
                }

                for (items in Naver) {
                    searchItems.add(
                        BestItemData(
                            items.select(".league").text(),
                            items.select(".ellipsis").text(),
                            items.select("div img").attr("src"),
                            items.select("a").attr("href").replace("/webnovel/list?novelId=","").replace("/best/list?novelId=", "").replace("/challenge/list?novelId=", ""),
                            "",
                            items.select(".bullet_comp").text(),
                            "",
                            "",
                            "",
                            "",
                            999,
                            DBDate.DateMMDDHHMM(),
                            "Naver",
                            "",
                        )
                    )
                }

                if (type == "Keyword") {
                    val cmpAsc: Comparator<BestItemData> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)
                }
                adapter?.notifyDataSetChanged()

                with(binding) {
                    if(searchItems.size == 0){
                        blank.root.visibility = View.VISIBLE
                        rviewSearch.visibility = View.GONE
                        binding.blank.tviewblank.text = "검색 결과가 없습니다."
                    } else {
                        blank.root.visibility = View.GONE
                        rviewSearch.visibility = View.VISIBLE
                    }
                }
            }
        }.start()
    }

    fun searchMunpia(text: String) {
        Thread {
            val doc: Document =
                Jsoup.connect("https://novel.munpia.com/page/hd.platinum/view/search/keyword/${text}/order/search_result")
                    .post()

            val Munpia: Elements = doc.select(".article_wrap .article")

            runOnUiThread {
                with(binding) {
                    blank.root.visibility = View.GONE
                    rviewSearch.visibility = View.VISIBLE
                }

                var info3 = ""
                var info4 = ""
                var info5 = ""

                for (items in Munpia) {

                    try{
                        info3 = items.select(".info span").next().get(0)?.text() ?: ""
                        info4 = items.select(".info span").next().get(1)?.text() ?: ""
                        info5 = items.select(".info span").next().get(2)?.text() ?: ""
                    } catch (e : IndexOutOfBoundsException){}

                    searchItems.add(
                        BestItemData(
                            items.select(".author").text(),
                            items.select(".detail a").text(),
                            "https://${items.select(".thumb img").attr("src")}",
                            items.select(".detail a").attr("href").replace("https://novel.munpia.com/",""),
                            items.select(".synopsis").text(),
                            items.select(".info span").first()?.text().toString(),
                            info3,
                            info4,
                            info5,
                            "",
                            999,
                            DBDate.DateMMDDHHMM(),
                            "Munpia",
                            "",
                        )
                    )
                }

                if (type == "Keyword") {
                    val cmpAsc: Comparator<BestItemData> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)
                }
                adapter?.notifyDataSetChanged()

                with(binding) {
                    if(searchItems.size == 0){
                        blank.root.visibility = View.VISIBLE
                        rviewSearch.visibility = View.GONE
                        binding.blank.tviewblank.text = "검색 결과가 없습니다."
                    } else {
                        blank.root.visibility = View.GONE
                        rviewSearch.visibility = View.VISIBLE
                    }
                }
            }
        }.start()
    }

    fun searchToksoda(text: String) {
        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["srchwrd"] = text
        param["keyword"] = text
        param["pageSize"] = "20"
        param["pageIndex"] = (page * 20)
        param["ageGrade"] = "0"
        param["lgctgrCd"] = ""
        param["mdctgrCd"] = ""
        param["searchType"] = "T"
        param["sortType"] = "W"
        param["prdtType"] = ""
        param["eventYn"] = "N"
        param["realSearchType"] = "N"
        param["_"] = "1657267049443"

        apiToksoda.getSearch(
            param,
            object : RetrofitDataListener<BestToksodaSearchResult> {
                override fun onSuccess(data: BestToksodaSearchResult) {

                    data.resultList.let {
                        if (it != null) {
                            for (i in it.indices) {

                                searchItems.add(
                                    BestItemData(
                                        it[i].AUTHOR,
                                        it[i].BOOK_NM,
                                        "https:${it[i].IMG_PATH}",
                                        it[i].BARCODE,
                                        it[i].HASHTAG_NM,
                                        it[i].PUB_NM,
                                        it[i].LGCTGR_NM,
                                        it[i].INQR_CNT,
                                        it[i].INTRST_CNT,
                                        "",
                                        999,
                                        DBDate.DateMMDDHHMM(),
                                        "Toksoda",
                                        "",
                                    )
                                )
                            }

                            if (type == "Keyword") {
                                val cmpAsc: Comparator<BestItemData> =
                                    Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                                Collections.sort(searchItems, cmpAsc)
                            }
                            adapter?.notifyDataSetChanged()

                            with(binding) {
                                if(searchItems.size == 0){
                                    blank.root.visibility = View.VISIBLE
                                    rviewSearch.visibility = View.GONE
                                    binding.blank.tviewblank.text = "검색 결과가 없습니다."
                                } else {
                                    blank.root.visibility = View.GONE
                                    rviewSearch.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            })
    }

    fun searchMrBlue(text: String) {
        Thread {
            val doc: Document =
                Jsoup.connect("https://www.mrblue.com/search/novel?keyword=${text}&sortby=title")
                    .post()

            val MrBlue: Elements = doc.select(".list-box ul li")

            runOnUiThread {
                with(binding) {
                    blank.root.visibility = View.GONE
                    rviewSearch.visibility = View.VISIBLE
                }

                for (items in MrBlue) {

                    var bookImg = ""

                    if (items.select(".img a img").attr("data-original")
                            .contains("https://img.mrblue.com/")
                    ) {
                        bookImg = items.select(".img a img").attr("data-original")
                    } else {
                        bookImg = "https://www.mrblue.com/${
                            items.select(".img a img").attr("data-original")
                        }"
                    }

                    searchItems.add(
                        BestItemData(
                            items.select(".authorname").text(),
                            items.select(".tit").text(),
                            bookImg,
                            items.select("a").attr("href"),
                            items.select(".price span").get(0).text(),
                            items.select(".genre").text(),
                            items.select(".review").text(),
                            items.select(".info span").get(1)?.text() ?: "",
                            items.select(".info span").get(2)?.text() ?: "",
                            "",
                            999,
                            DBDate.DateMMDDHHMM(),
                            "MrBlue",
                            "",
                        )
                    )
                }

                if (type == "Keyword") {
                    val cmpAsc: Comparator<BestItemData> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)
                }
                adapter?.notifyDataSetChanged()

                with(binding) {
                    if(searchItems.size == 0){
                        blank.root.visibility = View.VISIBLE
                        rviewSearch.visibility = View.GONE
                        binding.blank.tviewblank.text = "검색 결과가 없습니다."
                    } else {
                        blank.root.visibility = View.GONE
                        rviewSearch.visibility = View.VISIBLE
                    }
                }
            }
        }.start()
    }


    private fun getType() {

        adapterType = AdapterSearchKeyword(typeItems)
        typeItems.clear()

        with(binding) {
            rviewType.layoutManager =
                LinearLayoutManager(this@ActivitySearch, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType
        }

        for (i in BestRef.typeListTitleSearch().indices) {
            typeItems.add(
                BestType(
                    BestRef.typeListTitleSearch()[i],
                    BestRef.typeListSearch()[i]
                )
            )
        }
        adapterType.notifyDataSetChanged()

        adapterType.setOnItemClickListener(object : AdapterSearchKeyword.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                adapterType.notifyDataSetChanged()

                page = 1
                type = item.type.toString()

                with(binding){
                    blank.root.visibility = View.VISIBLE
                    rviewSearch.visibility = View.GONE
                    blank.tviewblank.text = "검색어를 입력해주세요."
                    sview.text = SpannableStringBuilder("")
                }

                if (item.type != "Keyword") {
                    binding.rviewSearch.addOnScrollListener(recyclerViewScroll)
                }

                when (type) {
                    "Keyword" -> {
                        binding.sview.hint = "통합 검색"
                    }
                    "Joara" -> {
                        binding.sview.hint = "조아라 검색"
                    }
                    "Naver_Today" -> {
                        binding.sview.hint = "시리즈 검색"
                    }
                    "Naver_Challenge" -> {
                        binding.sview.hint = "챌린지리그 검색"
                    }
                    "Naver" -> {
                        binding.sview.hint = "베스트리그 검색"
                    }
                    "Kakao" -> {
                        binding.sview.hint = "카카오페이지 검색"
                    }
                    "Kakao_Stage" -> {
                        binding.sview.hint = "카카오스테이지 검색"
                    }
                    "Munpia" -> {
                        binding.sview.hint = "문피아 검색"
                    }
                    "Toksoda" -> {
                        binding.sview.hint = "톡소다 검색"
                    }
                    "MrBlue" -> {
                        binding.sview.hint = "미스터블루 검색"
                    }
                }

                searchItems.clear()


            }
        })
    }

    private var recyclerViewScroll: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    page++

                    when (type) {
                        "Joara" -> {
                            searchJoara(page, text)
                        }
                        "Kakao" -> {
                            searchKakao(page - 1, text)
                        }
                        "Kakao_Stage" -> {
                            searchKakaoStage(page - 1, text)
                        }
                        "Toksoda" -> {
                            searchToksoda(text)
                        }
                    }
                }
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