package com.example.takealook.Search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takealook.Joara.*
import com.example.takealook.R
import com.example.takealook.Util.TabViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class FragmentSearchTab : Fragment() {
    private var adapter: AdapterBookSearch? = null
    private val items = ArrayList<BookListData?>()
    var linearLayoutManager: LinearLayoutManager? = null
    var recyclerView: RecyclerView? = null
    var page = 1

    var wrap: LinearLayout? = null
    var cover: LinearLayout? = null
    var blank: LinearLayout? = null

    private var tabviewmodel: TabViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabviewmodel = ViewModelProvider(this)[TabViewModel::class.java]
        var index = 1
        if (arguments != null) {
            index = requireArguments().getInt(ARG_SECTION_NUMBER)
        }
        tabviewmodel!!.setIndex(index)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_searchtab, container, false)
        recyclerView = root.findViewById(R.id.rview_Search)
        adapter = AdapterBookSearch(requireContext(), items)
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        wrap = root.findViewById(R.id.TabWrap)
        cover = root.findViewById(R.id.LoadingLayout)
        blank = root.findViewById(R.id.BlankLayout)

        tabviewmodel!!.text.observe(viewLifecycleOwner, { tabNum: String? ->
            when (tabNum) {
                "TAB1" -> {
                    searchJoara(page)

                    adapter!!.setOnItemClickListener(object : AdapterBookSearch.OnItemClickListener {
                        override fun onItemClick(v: View?, position: Int, value: String?) {
                            val item: BookListData? = adapter!!.getItem(position)
                            Log.d("@@@@","HIHI")
                        }
                    })
                }
                "TAB2" -> {
                    Log.d("@@@@-@", tabNum);
                }
                "TAB3" -> {
                    Log.d("@@@@-#", tabNum);
                }
            }
        })

        recyclerView!!.addOnScrollListener(recyclerViewScroll)

        return root
    }

    fun searchJoara(page: Int?) {
        Log.d("@@@@-!", "!!!!");

        RetrofitSearch.getSearchJoara(
            page,"사랑", "", "", "", "", "", "", "", "", "", "", "30|25|25|15|15|15|0.5",
            "1",
        )!!.enqueue(object : Callback<JoaraSearchResult?> {
            override fun onResponse(
                call: Call<JoaraSearchResult?>,
                response: Response<JoaraSearchResult?>
            ) {

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

                                items.add(
                                    BookListData(
                                        writerName,
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
                                        categoryKoName,
                                    )
                                )
                            }
                        }
                    }
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

    private var recyclerViewScroll: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(!recyclerView.canScrollVertically(1)) {
                cover!!.visibility = View.VISIBLE
                page++
                searchJoara(page)
            }
        }
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): FragmentSearchTab {
            val fragment = FragmentSearchTab()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }
}