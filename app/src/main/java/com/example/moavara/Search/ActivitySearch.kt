package com.example.moavara.Search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Best.AdapterType
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.R
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Param
import com.example.moavara.databinding.ActivitySearchBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*

class ActivitySearch : AppCompatActivity() {

    private lateinit var mFragmentSearch: FragmentSearch
    private lateinit var adapterType: AdapterType
    private val typeItems = ArrayList<BestType>()
    private lateinit var binding: ActivitySearchBinding

    private var adapter: AdapterBookSearch? = null
    private val searchItems = ArrayList<BookListDataBest>()
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
                                BookListDataBest(
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
                            val cmpAsc: Comparator<BookListDataBest> =
                                Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                            Collections.sort(searchItems, cmpAsc)
                        }
                        adapter?.notifyDataSetChanged()

                        with(binding) {
                            blank.root.visibility = View.GONE
                            rviewSearch.visibility = View.VISIBLE
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

                        items = if (page == 0) {
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
                                    BookListDataBest(
                                        items[j].author,
                                        items[j].title,
                                        "https://dn-img-page.kakao.com/download/resource?kid=${items[j].image_url}",
                                        items[j].id,
                                        items[j].publisher_name,
                                        items[j].sub_category,
                                        items[j].read_count,
                                        "총 ${items[j].page}화",
                                        "",
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
                        val cmpAsc: Comparator<BookListDataBest> =
                            Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                        Collections.sort(searchItems, cmpAsc)
                    }

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
                            BookListDataBest(
                                items.nickname.name,
                                items.title,
                                items.thumbnail.url,
                                items.stageSeriesNumber,
                                items.synopsis,
                                items.subGenre.name,
                                items.favoriteCount,
                                items.viewCount,
                                "총 ${items.publishedEpisodeCount}화",
                                "",
                                999,
                                DBDate.DateMMDDHHMM(),
                                "Kakao_Stage",
                                "",
                            )
                        )
                    }

                    if (type == "Keyword") {
                        val cmpAsc: Comparator<BookListDataBest> =
                            Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                        Collections.sort(searchItems, cmpAsc)
                    }
                    adapter?.notifyDataSetChanged()

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
                        BookListDataBest(
                            items.select(".ellipsis").text(),
                            title,
                            items.select("div img").attr("src"),
                            items.select("a").attr("href"),
                            "",
                            "",
                            items.select(".bullet_comp").text(),
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
                    val cmpAsc: Comparator<BookListDataBest> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)
                }
                adapter?.notifyDataSetChanged()

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
                        BookListDataBest(
                            items.select(".author").text(),
                            items.select(".detail a").text(),
                            "https://${items.select(".thumb img").attr("src")}",
                            items.select(".detail a").attr("href"),
                            "",
                            "",
                            "",
                            items.select(".info span").next().get(0).text(),
                            items.select(".info span").next().get(1).text(),
                            items.select(".info span").next().get(2).text(),
                            999,
                            DBDate.DateMMDDHHMM(),
                            "Munpia",
                            "",
                        )
                    )
                }

                if (type == "Keyword") {
                    val cmpAsc: Comparator<BookListDataBest> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)
                }
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
                                    BookListDataBest(
                                        it[i].AUTHOR,
                                        it[i].BOOK_NM,
                                        "https:${it[i].IMG_PATH}",
                                        it[i].BARCODE,
                                        it[i].LGCTGR_NM,
                                        it[i].PUB_NM,
                                        it[i].HASHTAG_NM,
                                        "",
                                        "",
                                        "",
                                        999,
                                        DBDate.DateMMDDHHMM(),
                                        "Toksoda",
                                        "",
                                    )
                                )
                            }

                            if (type == "Keyword") {
                                val cmpAsc: Comparator<BookListDataBest> =
                                    Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                                Collections.sort(searchItems, cmpAsc)
                            }
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
                        BookListDataBest(
                            items.select(".name").text(),
                            items.select(".tit").text(),
                            bookImg,
                            items.select("a").attr("href"),
                            items.select(".genre").text(),
                            items.select(".price").text(),
                            items.select(".review").text(),
                            "",
                            "",
                            "",
                            999,
                            DBDate.DateMMDDHHMM(),
                            "MrBlue",
                            "",
                        )
                    )
                }

                if (type == "Keyword") {
                    val cmpAsc: Comparator<BookListDataBest> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)
                }
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

        for (i in BestRef.typeListTitleSearch().indices) {
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
                val item: BestType = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                adapterType.notifyDataSetChanged()

                searchItems.clear()
                adapter?.notifyDataSetChanged()
                page = 1
                type = item.type.toString()

                if (item.type != "Keyword") {
                    binding.rviewSearch.addOnScrollListener(recyclerViewScroll)
                }

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
        })
    }

    private var recyclerViewScroll: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    page++

                    if (type == "Joara") {
                        searchJoara(page, text)
                    } else if (type == "Kakao") {
                        searchKakao(page - 1, text)
                    } else if (type == "Kakao_Stage") {
                        searchKakaoStage(text)
                    } else if (type == "Toksoda") {
                        searchToksoda(text)
                    }
                }
            }
        }

}