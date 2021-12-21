package com.example.takealook.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.takealook.DataBase.DataBaseJoara
import com.example.takealook.DataBase.JoaraBest
import com.example.takealook.Joara.JoaraBestListResult
import com.example.takealook.Joara.RetrofitJoara
import com.example.takealook.Search.BookListDataBestToday
import com.example.takealook.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FragmentBestToday : Fragment() {
    private lateinit var db: DataBaseJoara

    private var adapterToday: AdapterBestToday? = null
    private val items = ArrayList<BookListDataBestToday?>()
    var recyclerView: RecyclerView? = null

    lateinit var root: View

    var day = 0
    var week = 0
    var date = 0
    var month = 0
    var mNow: Long = 0
    var mDate: Date? = null
    var mFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_best_today, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)

        adapterToday = AdapterBestToday(requireContext(), items)

        getBookListBest(recyclerView)

        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        date = Calendar.getInstance().get(Calendar.DATE)
        month = Calendar.getInstance().get(Calendar.MONTH)

        Log.d("@@@@", "locale = " + requireContext().resources.configuration.locales.get(0))
        Log.d("@@@@", "time = " + mFormat.format(Date(System.currentTimeMillis())))
        Log.d("@@@@", "day = $day")
        Log.d("@@@@", "week = $week")
        Log.d("@@@@", "date = $date")
        Log.d("@@@@", "monthly = $month")

        db = Room.databaseBuilder(
            requireContext(),
            DataBaseJoara::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

        db.bestDao().deleteWeek(day)

        return root
    }

    private fun getBookListBest(recyclerView: RecyclerView?) {
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val call: Call<JoaraBestListResult?>? = RetrofitJoara.getJoaraBookBest("today", "", "0")

        call!!.enqueue(object : Callback<JoaraBestListResult?> {
            override fun onResponse(
                call: Call<JoaraBestListResult?>,
                response: Response<JoaraBestListResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val books = it.bookLists

                        for (i in books!!.indices) {

                            val writerName = books[i].writerName
                            val subject = books[i].subject
                            val bookImg = books[i].bookImg
                            val intro = books[i].intro
                            val bookCode = books[i].bookCode
                            val cntChapter = books[i].cntChapter
                            val cntPageRead = books[i].cntPageRead
                            val cntFavorite = books[i].cntFavorite
                            val cntRecom = books[i].cntRecom

                            if (i < 10) {
                                db.bestDao().insert(
                                    JoaraBest(
                                        writerName,
                                        subject,
                                        bookImg,
                                        intro,
                                        bookCode,
                                        cntChapter,
                                        cntPageRead,
                                        cntFavorite,
                                        cntRecom,
                                        i + 1,
                                        day,
                                        week,
                                        date,
                                        month,
                                        10 - i
                                    )
                                )
                            }

                            items!!.add(
                                BookListDataBestToday(
                                    writerName,
                                    subject,
                                    bookImg,
                                    intro,
                                    bookCode,
                                    cntChapter,
                                    cntPageRead,
                                    cntFavorite,
                                    cntRecom,
                                    i + 1
                                )
                            )
                        }
                    }
                    recyclerView!!.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapterToday
                    adapterToday!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }
}