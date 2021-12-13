package com.example.takealook.Best

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takealook.Joara.BookListDataBest
import com.example.takealook.R
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

class FragmentBest : Fragment() {
    var best = "weekly"
    var token = ""
    var footer: LinearLayout? = null
    var index = 0

    private var adapter: AdapterBookListBest? = null
    private val items = ArrayList<BookListDataBest?>()
    var recyclerView: RecyclerView? = null

    lateinit var root: View
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_best, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)

        token = requireContext().getSharedPreferences("LOGIN", AppCompatActivity.MODE_PRIVATE)
            .getString("TOKEN", "").toString()

        setLayout()
        return root
    }

    fun setLayout() {

        adapter = AdapterBookListBest(requireContext(), items)

        getBookListBest("", "today", recyclerView)

    }

    private fun getBookListBest(
        token: String?,
        type: String?,
        recyclerView: RecyclerView?
    ) {
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val call: Call<BookListBestResult?>? = RetrofitBookList.getBookBest(token, best, "", "0")


        call!!.enqueue(object : Callback<BookListBestResult?> {
            override fun onResponse(
                call: Call<BookListBestResult?>,
                response: Response<BookListBestResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val books = it.books

                        for (i in books!!.indices) {

                            val writerName = books[i].writerName
                            val subject = books[i].subject
                            val bookImg = books[i].bookImg
                            val isAdult = books[i].isAdult
                            val isFinish = books[i].isFinish
                            val isPremium = books[i].isPremium
                            val isNobless = books[i].isNobless
                            val intro = books[i].intro
                            val isFavorite = books[i].isFavorite
                            val bookCode = books[i].bookCode
                            val categoryKoName = books[i].categoryKoName
                            val cntChapter = books[i].cntChapter
                            val cntFavorite = books[i].cntFavorite
                            val cntRecom = books[i].cntRecom
                            val cntPageRead = books[i].cntPageRead

                            if (i < 9) {
                                items!!.add(
                                    BookListDataBest(
                                        writerName,
                                        subject,
                                        bookImg,
                                        isAdult,
                                        isFinish,
                                        isPremium,
                                        isNobless,
                                        intro,
                                        isFavorite,
                                        bookCode,
                                        categoryKoName,
                                        cntChapter,
                                        cntFavorite,
                                        cntRecom,
                                        cntPageRead
                                    )
                                )
                            }


                        }
                    }
                    recyclerView!!.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BookListBestResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        adapter!!.setOnItemClickListener(object : AdapterBookListBest.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {
                val item: BookListDataBest? = adapter!!.getItem(position)
//                if (value == "FAV") {
//                    RetrofitBookList.postFav(item!!.bookCode, mContext, item!!.title)
//                } else if (value == "BookDetail") {
//                    val intent = Intent(
//                        mContext.applicationContext,
//                        BookDetailCover::class.java
//                    )
//                    intent.putExtra("BookCode", String.format("%s", item!!.bookCode))
//                    intent.putExtra("token", String.format("%s", token))
//                    mContext.startActivity(intent)
//                }
            }
        })
    }
}