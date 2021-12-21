package com.example.takealook.Event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.takealook.Best.AdapterBestToday
import com.example.takealook.Best.BottomDialogBest
import com.example.takealook.DataBase.DataBaseJoara
import com.example.takealook.DataBase.JoaraBest
import com.example.takealook.Joara.JoaraBestListResult
import com.example.takealook.Joara.RetrofitJoara
import com.example.takealook.R
import com.example.takealook.Search.BookListDataBestToday
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentEvent : Fragment() {
    private lateinit var db: DataBaseJoara

    private var adapterToday: AdapterBestToday? = null
    private val items = ArrayList<BookListDataBestToday?>()
    var recyclerViewLeft: RecyclerView? = null
    var recyclerViewRight: RecyclerView? = null

    lateinit var root: View

    var day = 0
    var week = 0
    var date = 0
    var monthly = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_event, container, false)

        recyclerViewLeft = root.findViewById(R.id.rview_Left)
        recyclerViewRight = root.findViewById(R.id.rview_Right)

        adapterToday = AdapterBestToday(requireContext(), items)

        getBookListBest(recyclerViewLeft)

        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        date = Calendar.getInstance().get(Calendar.DATE)
        monthly = Calendar.getInstance().get(Calendar.MONTH)

        Log.d("@@@@", "day = $day")
        Log.d("@@@@", "week = $week")
        Log.d("@@@@", "date = $date")
        Log.d("@@@@", "monthly = $monthly")

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

        val call: Call<JoaraBestListResult?>? = RetrofitJoara.getJoaraBookBest( "today", "", "0")

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

                            if(i < 10){
                                db.bestDao().insert(JoaraBest(writerName, subject, bookImg, intro, bookCode, cntChapter, cntPageRead, cntFavorite, cntRecom, i + 1, day, 1, 10 - i))
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