package com.example.moavara.Search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Best.AdapterType
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Soon.FragmentSearch
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Param
import com.example.moavara.databinding.ActivitySearchBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class ActivitySearch : AppCompatActivity() {

    private lateinit var mFragmentSearch: FragmentSearch
    private lateinit var adapterType: AdapterType
    private val typeItems = ArrayList<BestType>()
    private lateinit var binding: ActivitySearchBinding

    private var adapter: AdapterBookSearch? = null
    private val searchItems = ArrayList<BookListData>()
    var page = 1
    var linearLayoutManager: LinearLayoutManager? = null
    private var joaraOffset = 20
    private var kakaoStageOffset = 25
    var text = ""
    var type = "Keyword"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (type == "Keyword") {
            searchJoara(page, text)
        } else {
            binding.rviewSearch.addOnScrollListener(recyclerViewScroll)
        }

        mFragmentSearch = FragmentSearch()
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentSearch)
        }

        binding.sview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                with(binding) {
                    adapter = AdapterBookSearch(searchItems)
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

                text = query.toString()

                searchMrBlue(text)
                searchToksoda(text)
                searchMunpia(text)
                searchNaver(text, "Naver_Challenge")
                searchNaver(text, "Naver_Today")
                searchNaver(text, "Naver")
                searchKakao(page - 1, text)
                searchKakaoStage(text)
                searchJoara(page, text)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

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
                                BookListData(
                                    "조아라",
                                    books[i].subject,
                                    books[i].writer_name,
                                    books[i].book_img,
                                    books[i].bookCode,
                                    "선호작 수 : ${books[i].cntFavorite}",
                                    "조회 수 : ${books[i].cntPageRead}",
                                    "추천 수 : ${books[i].cntRecom}",
                                )
                            )
                        }

                        Log.d("####-!!!!", type)

                        if(type == "Joara" || type == "Keyword"){
                            val cmpAsc: java.util.Comparator<BookListData> =
                                Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                            Collections.sort(searchItems, cmpAsc)

                            adapter?.notifyDataSetChanged()

                            with(binding) {
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
                        val items: List<KakaoBookItem>?

                        if (page == 0) {
                            items = results[2].items
                        } else {
                            items = results[0].items
                        }

                        if (items != null) {
                            kakaoStageOffset = items.size
                        }

                        if (items != null) {
                            for (j in items.indices) {
                                searchItems.add(
                                    BookListData(
                                        "카카오",
                                        items[j].title,
                                        items[j].author,
                                        "https://dn-img-page.kakao.com/download/resource?kid=" + items[j].image_url,
                                        items[j].id,
                                        "출판사 : ${items[j].publisher_name}",
                                        "장르 : ${items[j].sub_category}",
                                        "조회수 : ${items[j].read_count}",
                                    )
                                )
                            }
                        }
                    }

                    val cmpAsc: java.util.Comparator<BookListData> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)

                    adapter?.notifyDataSetChanged()

                    with(binding) {
                        blank.root.visibility = View.GONE
                        rviewSearch.visibility = View.VISIBLE
                    }
                }
            })
    }

    fun searchKakaoStage(text: String) {
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
                            BookListData(
                                "카카오 스테이지",
                                items.title,
                                items.nickname.name,
                                items.thumbnail.url,
                                items.stageSeriesNumber,
                                "관심 : ${items.favoriteCount}",
                                "조회 : ${items.viewCount}",
                                "총 ${items.publishedEpisodeCount} 화",
                            )
                        )
                    }

                    val cmpAsc: java.util.Comparator<BookListData> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)

                    adapter?.notifyDataSetChanged()

                    with(binding) {
                        blank.root.visibility = View.GONE
                        rviewSearch.visibility = View.VISIBLE
                    }
                }
            })
    }

    fun searchNaver(text: String, platform: String) {
        Thread {
            var doc: Document? = null
            var title = ""

            if (platform == "Naver") {
                doc =
                    Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=webnovel&target=novel")
                        .post()
                title = "네이버 시리즈"
            } else if (platform == "Naver_Today") {
                doc =
                    Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=best&target=novel")
                        .post()
                title = "네이버 베스트"
            } else if (platform == "Naver_Challenge") {
                doc =
                    Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=challenge&target=novel")
                        .post()
                title = "네이버 챌린지"
            }

            val Naver: Elements = (doc?.select(".srch_cont .list_type2 li") ?: "") as Elements

            runOnUiThread {
                with(binding) {
                    blank.root.visibility = View.GONE
                    rviewSearch.visibility = View.VISIBLE
                }

                for (items in Naver) {
                    searchItems.add(
                        BookListData(
                            title,
                            items.select(".ellipsis").text(),
                            items.select(".league").text(),
                            items.select("div img").attr("src"),
                            items.select("a").attr("href"),
                            items.select(".bullet_comp").text(),
                            "",
                            "",
                        )
                    )
                }

                val cmpAsc: java.util.Comparator<BookListData> =
                    Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                Collections.sort(searchItems, cmpAsc)

                adapter?.notifyDataSetChanged()

                with(binding) {
                    blank.root.visibility = View.GONE
                    rviewSearch.visibility = View.VISIBLE
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

                for (items in Munpia) {

                    searchItems.add(
                        BookListData(
                            "문피아",
                            items.select(".detail a").text(),
                            items.select(".author").text(),
                            "https://${items.select(".thumb img").attr("src")}",
                            items.select(".detail a").attr("href"),
                            items.select(".info span").next().get(0).text(),
                            items.select(".info span").next().get(1).text(),
                            items.select(".info span").next().get(2).text(),
                        )
                    )
                }

                val cmpAsc: java.util.Comparator<BookListData> =
                    Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                Collections.sort(searchItems, cmpAsc)

                adapter?.notifyDataSetChanged()

                with(binding) {
                    blank.root.visibility = View.GONE
                    rviewSearch.visibility = View.VISIBLE
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
                                    BookListData(
                                        "톡소다",
                                        it[i].BOOK_NM,
                                        it[i].AUTHOR,
                                        "https:${it[i].IMG_PATH}",
                                        it[i].BARCODE,
                                        "장르 : ${it[i].LGCTGR_NM}",
                                        "유통사 : ${it[i].PUB_NM}",
                                        "키워드 : ${it[i].HASHTAG_NM}",
                                    )
                                )
                            }

                            val cmpAsc: java.util.Comparator<BookListData> =
                                Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                            Collections.sort(searchItems, cmpAsc)

                            adapter?.notifyDataSetChanged()

                            with(binding) {
                                blank.root.visibility = View.GONE
                                rviewSearch.visibility = View.VISIBLE
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
                        BookListData(
                            "미스터 블루",
                            items.select(".tit").text(),
                            items.select(".name").text(),
                            bookImg,
                            items.select("a").attr("href"),
                            items.select(".genre").text(),
                            items.select(".price").text(),
                            items.select(".review").text(),
                        )
                    )
                }

                val cmpAsc: java.util.Comparator<BookListData> =
                    Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                Collections.sort(searchItems, cmpAsc)

                adapter?.notifyDataSetChanged()

                with(binding) {
                    blank.root.visibility = View.GONE
                    rviewSearch.visibility = View.VISIBLE
                }
            }
        }.start()
    }


    private fun getType() {

        adapterType = AdapterType(typeItems)
        typeItems.clear()

        with(binding) {
            rviewType.layoutManager =
                LinearLayoutManager(this@ActivitySearch, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType
        }

        for (i in BestRef.typeListSearch().indices) {
            typeItems.add(
                BestType(
                    BestRef.typeListTitleSearch()[i],
                    BestRef.typeListSearch()[i]
                )
            )
        }
        adapterType.notifyDataSetChanged()

        adapterType.setOnItemClickListener(object : AdapterType.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType? = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                adapterType.notifyDataSetChanged()

                if (item != null) {

                    searchItems.clear()
                    adapter?.notifyDataSetChanged()

                    item.type?.let { Log.d("####", it) }

                    if (item.type == "Keyword") {
                        searchJoara(page, text)
                        searchKakaoStage(text)
                        searchKakao(page - 1, text)
                        searchNaver(text, "Naver")
                        searchNaver(text, "Naver_Today")
                        searchNaver(text, "Naver_Challenge")
                        searchMunpia(text)
                        searchToksoda(text)
                        searchMrBlue(text)
                    } else if (item.type == "Joara") {
                        searchJoara(page, text)
                    } else if (item.type == "Naver_Today") {
                        searchNaver(text, "Naver_Today")
                    } else if (item.type == "Naver_Challenge") {
                        searchNaver(text, "Naver_Challenge")
                    } else if (item.type == "Naver") {
                        searchNaver(text, "Naver")
                    } else if (item.type == "Kakao") {
                        searchKakao(page - 1, text)
                    } else if (item.type == "Kakao_Stage") {
                        searchKakaoStage(text)
                    } else if (item.type == "Munpia") {
                        searchMunpia(text)
                    } else if (item.type == "Toksoda") {
                        searchToksoda(text)
                    } else if (item.type == "MrBlue") {
                        searchMrBlue(text)
                    }
                }
            }
        })
    }

    private var recyclerViewScroll: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    if (joaraOffset == 20 && kakaoStageOffset == 25) {
                        page++

                        if (type == "Joara") {
                            searchJoara(page, text)
                        } else if (type == "Naver_Today") {
                            searchNaver(text, "Naver_Today")
                        } else if (type == "Naver_Challenge") {
                            searchNaver(text, "Naver_Challenge")
                        } else if (type == "Naver") {
                            searchNaver(text, "Naver")
                        } else if (type == "Kakao") {
                            searchKakao(page - 1, text)
                        } else if (type == "Kakao_Stage") {
                            searchKakaoStage(text)
                        } else if (type == "Munpia") {
                            searchMunpia(text)
                        } else if (type == "Toksoda") {
                            searchToksoda(text)
                        } else if (type == "MrBlue") {
                            searchMrBlue(text)
                        }
                    }
                }
            }
        }

}