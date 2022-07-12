package com.example.moavara.Search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Retrofit.JoaraSearchResult
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Retrofit.RetrofitKaKao
import com.example.moavara.Retrofit.SearchResultKakao
import com.example.moavara.R
import com.example.moavara.Util.TabViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList



class FragmentSearch : Fragment() {
    private var adapter : AdapterBookSearch? = null
    private val searchItems = ArrayList<BookListData>()
    var linearLayoutManager: LinearLayoutManager? = null
    var recyclerView: RecyclerView? = null
    var btnSearchDtail: AppCompatButton? = null
    var page = 1

    var wrap: LinearLayout? = null
    var cover: LinearLayout? = null
    var blank: LinearLayout? = null

    private val test = ArrayList<String?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_searchtab, container, false)
        recyclerView = root.findViewById(R.id.rview_Search)
        btnSearchDtail = root.findViewById(R.id.Btn_SearchDetail)

        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        wrap = root.findViewById(R.id.TabWrap)
        cover = root.findViewById(R.id.LoadingLayout)
        blank = root.findViewById(R.id.BlankLayout)

        Log.d("####", "1")
        test.add("1")
        searchJoara(page)
        Log.d("####", "3")
        test.add("3")
        searchKakao(page - 1)
        Log.d("####", "5")
        test.add("5")
        Log.d("#######", test.toString())
        adapter = AdapterBookSearch(requireContext())

        recyclerView!!.addOnScrollListener(recyclerViewScroll)

        btnSearchDtail!!.setOnClickListener{
            val searchBottomPicker = BottomSheetDialogProcess(requireContext())
            fragmentManager?.let { searchBottomPicker.show(it, null) }
        }

        return root
    }

    fun searchJoara(page: Int?) {

        RetrofitJoara.getSearchJoara(
            page,"사랑", "", "", "", "", "", "", "", "", "", "", "30|25|25|15|15|15|0.5",
            "1",
        )!!.enqueue(object : Callback<JoaraSearchResult?> {
            override fun onResponse(
                call: Call<JoaraSearchResult?>,
                response: Response<JoaraSearchResult?>
            ) {
                Log.d("####", "2")
                test.add("2")
                Log.d("#######-2", test.toString())
                if (response.isSuccessful) {
                    cover!!.visibility = View.GONE
                    blank!!.visibility = View.GONE

                    response.body()?.let { it ->

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
                    }
                    adapter?.setItems(searchItems)
                    adapter!!.notifyDataSetChanged()
                    if (page == 1) {
                        recyclerView!!.layoutManager = linearLayoutManager
                        recyclerView!!.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<JoaraSearchResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })
    }

    fun searchKakao(page: Int?) {
        RetrofitKaKao().postSearchKakao(
            page,"사랑",11)!!.enqueue(object : Callback<SearchResultKakao?> {
            override fun onResponse(
                call: Call<SearchResultKakao?>,
                response: Response<SearchResultKakao?>
            ) {
                Log.d("####", "4")
                test.add("4")
                Log.d("#######-3", test.toString())
                if (response.isSuccessful) {
                    cover!!.visibility = View.GONE
                    blank!!.visibility = View.GONE

                    response.body()?.let { it ->

                        val results = it.results

                        if (results != null) {
                            for (i in results.indices) {
                                val items = results[i].items

                                    for (j in items!!.indices) {
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
                    }
                    adapter!!.notifyDataSetChanged()
                    if (page == 0) {
                        recyclerView!!.layoutManager = linearLayoutManager
                        recyclerView!!.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<SearchResultKakao?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })
    }

    private var recyclerViewScroll: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(!recyclerView.canScrollVertically(1)) {
                cover!!.visibility = View.VISIBLE
                page++

                searchJoara(page)
                searchKakao(page-1)
            }
        }
    }
}