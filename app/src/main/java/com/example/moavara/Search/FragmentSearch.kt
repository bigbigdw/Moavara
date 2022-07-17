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
import java.util.*


class FragmentSearch : Fragment() {
    private var adapter : AdapterBookSearch? = null
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

        with(binding){

//            rviewSearch.addOnScrollListener(recyclerViewScroll)
            etextSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                    Log.d("idtext", "beforeTextChanged")
                }

                override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                    Log.d("idtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    text = s.toString()
                }
            })

            btnSearch.setOnClickListener {
                adapter = AdapterBookSearch(searchItems)
                rviewSearch.adapter = adapter
                rviewSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                llayoutSearch.visibility = View.GONE
                llayoutResult.visibility = View.VISIBLE

                Log.d("####", "1")
                test.add("1")
                searchJoara(page, text)
                Log.d("####", "3")
                test.add("3")
                searchKakao(page - 1, text)
                Log.d("####", "5")
                test.add("5")
                Log.d("#######", test.toString())
                Log.d("####", "6")

            }
        }



        return view
    }

    fun searchJoara(page: Int, text : String) {
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
                    val status = it.status
                    val all = it.joaraSearchTotalCnt?.keyword_cntJoara?.all
                    val intro = it.joaraSearchTotalCnt?.keyword_cntJoara?.intro
                    val keyword = it.joaraSearchTotalCnt?.keyword_cntJoara?.keyword
                    val subject = it.joaraSearchTotalCnt?.keyword_cntJoara?.subject
                    val writerNickname = it.joaraSearchTotalCnt?.keyword_cntJoara?.subject

                    if (books != null) {
                        joaraOffset = books.size
                    }

                    if (books != null) {
                        for (i in books.indices) {

                            val writerName = books[i].writer_name
                            val subject = books[i].subject
                            val bookImg = books[i].book_img
                            val isAdult = books[i].isAdult
                            val isFinish = books[i].isFinish
                            val isPremium = books[i].isPremium
                            val isNobless = books[i].isNobless
                            val intro = books[i].intro
                            val isFavorite = books[i].isFavorite
                            val cntPageRead = books[i].cntPageRead
                            val cntRecom = books[i].cntRecom
                            val cntFavorite = books[i].cntFavorite
                            val bookCode = books[i].bookCode
                            val categoryKoName = books[i].categoryKoName

                            searchItems.add(
                                BookListData(
                                    "조아라",
                                    subject,
                                    bookImg,
                                    isAdult,
                                    isFinish,
                                    isPremium,
                                    isNobless,
                                    intro,
                                    isFavorite,
                                    cntPageRead,
                                    cntRecom,
                                    cntFavorite,
                                    bookCode,
                                    categoryKoName
                                )
                            )
                        }
                    }

                    val cmpAsc: Comparator<BookListData> =
                        Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
                    Collections.sort(searchItems, cmpAsc)

                    Log.d("@@@@", searchItems.size.toString())


                    adapter?.notifyDataSetChanged()
                }
            })
    }

    fun searchKakao(page: Int, text : String)  {
        val apiKakao = RetrofitKaKao()
        val param : MutableMap<String?, Any> = HashMap()
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

                        if(page == 0){
                            items = results[2].items
                        } else {
                            items = results[0].items
                        }

                        if (items != null) {
                            kakaoStageOffset = items.size
                        }


                        if (items != null) {
                            for (j in items.indices) {
                                val author = items[j].author
                                val image_url = "https://dn-img-page.kakao.com/download/resource?kid=" + items[j].image_url
                                val publisher_name = items[j].publisher_name
                                val sub_category = items[j].sub_category
                                val title = items[j].title

                                searchItems.add(
                                    BookListData(
                                        "카카오",
                                        title,
                                        image_url,
                                        "isAdult",
                                        "isFinish",
                                        "isPremium",
                                        "isNobless",
                                        publisher_name,
                                        "isFavorite",
                                        "cntPageRead",
                                        "cntRecom",
                                        "cntFavorite",
                                        "bookCode",
                                        sub_category,
                                    )
                                )
                            }
                        }
                    }

                    with(binding){
                        blank.root.visibility = View.GONE
                        rviewSearch.visibility = View.VISIBLE
                    }
                }
            })
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