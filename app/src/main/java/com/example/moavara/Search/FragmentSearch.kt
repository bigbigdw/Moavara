package com.example.moavara.Search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Retrofit.*
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentSearchBinding


class FragmentSearch : Fragment() {
    private var adapter : AdapterBookSearch? = null
    private val searchItems = ArrayList<BookListData>()
    var page = 1
    var linearLayoutManager: LinearLayoutManager? = null
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val test = ArrayList<String?>()
    var text = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


        adapter = AdapterBookSearch(requireContext())

        with(binding){
            rviewSearch.addOnScrollListener(recyclerViewScroll)

            text = etextSearch.text.toString()

            btnSearch.setOnClickListener {
                Log.d("####", "1")
                test.add("1")
                searchJoara(page, text)
                Log.d("####", "3")
                test.add("3")
                searchKakao(page - 1, text)
                Log.d("####", "5")
                test.add("5")
                Log.d("#######", test.toString())
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

                    adapter?.setItems(searchItems)
                    adapter!!.notifyDataSetChanged()
                    with(binding){
                        if (page == 1) {
                            rviewSearch.layoutManager = linearLayoutManager
                            rviewSearch.adapter = adapter
                        }
                    }


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
                        val items = results[2].items

                        Log.d("!!!!",results.toString())

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

                    adapter!!.notifyDataSetChanged()
                    with(binding){
                        if (page == 0) {
                            rviewSearch.layoutManager = linearLayoutManager
                            rviewSearch.adapter = adapter
                        }
                    }
                }
            })
    }

    private var recyclerViewScroll: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(!recyclerView.canScrollVertically(1)) {
                page++

                searchJoara(page, text)
                searchKakao(page-1, text)
            }
        }
    }
}