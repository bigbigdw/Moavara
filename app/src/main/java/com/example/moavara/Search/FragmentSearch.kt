package com.example.moavara.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentSearchBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*


class FragmentSearch : Fragment() {
    private var adapter: AdapterBookSearch? = null
    private val searchItems = ArrayList<BookListData>()
    var page = 1
    var linearLayoutManager: LinearLayoutManager? = null
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var joaraOffset = 20
    private var kakaoStageOffset = 25

    private val test = ArrayList<String?>()
    var text = "사랑"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {

//            rviewSearch.addOnScrollListener(recyclerViewScroll)
            etextSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    text: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("idtext", "beforeTextChanged")
                }

                override fun onTextChanged(
                    text: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    Log.d("idtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    text = s.toString()
                }
            })

            btnSearch.setOnClickListener {
                adapter = AdapterBookSearch(searchItems)
                rviewSearch.adapter = adapter
                rviewSearch.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                llayoutSearch.visibility = View.GONE
                llayoutResult.visibility = View.VISIBLE

                Log.d("####", "1")
                test.add("1")
                searchJoara(page, text)
                Log.d("####", "3")
                test.add("3")
                searchKakaoStage(text)
                searchKakao(page - 1, text)
                Log.d("####", "5")
                test.add("5")
                Log.d("#######", test.toString())
                Log.d("####", "6")
                searchNaver(text, "Naver")
                searchNaver(text, "Naver Challenge")
                searchNaver(text, "Naver Today")
            }
        }



        return view
    }

    fun searchJoara(page: Int, text: String) {
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
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
                override fun onSuccess(it: JoaraSearchResult) {

                    Log.d("####", "2")
                    test.add("2")
                    Log.d("#######-2", test.toString())

                    val books = it.books

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

                        val cmpAsc: Comparator<BookListData> =
                            Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                        Collections.sort(searchItems, cmpAsc)

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
                override fun onSuccess(it: SearchResultKakao) {

                    Log.d("####", "4")
                    test.add("4")
                    Log.d("#######-3", test.toString())

                    val results = it.results

                    if (results != null) {
                        var items: List<KakaoBookItem>? = null

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
        param["page"] = 0

        apiKakaoStage.getSearchKakaoStage(
            param,
            object : RetrofitDataListener<KakaoStageSearchResult> {
                override fun onSuccess(it: KakaoStageSearchResult) {

                    val results = it.content

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
                }
            })
    }

    fun searchNaver(text: String, platform : String){
        Thread {
            var doc: Document? = null
            var title = ""

            if(platform == "Naver"){
                doc = Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=webnovel&target=novel").post()
                title = "네이버 시리즈"
            } else if(platform == "Naver Today") {
                doc = Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=best&target=novel").post()
                title = "네이버 베스트"
            } else if(platform == "Naver Challenge") {
                doc = Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=challenge&target=novel").post()
                title = "네이버 챌린지"
            }

            val Naver: Elements = (doc?.select(".srch_cont .list_type2 li") ?: "") as Elements

            requireActivity().runOnUiThread {
                with(binding){
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
            }
        }.start()
    }

//    private var recyclerViewScroll: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//            if(!recyclerView.canScrollVertically(1)) {
//                if(joaraOffset == 20 && kakaoStageOffset == 25){
//                    page++
//
//                    searchJoara(page, text)
//                    searchKakao(page-1, text)
//                }
//            }
//        }
//    }
}