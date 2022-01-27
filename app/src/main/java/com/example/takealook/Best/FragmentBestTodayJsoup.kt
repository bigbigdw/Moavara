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
import com.example.takealook.DataBase.DataBaseBest
import com.example.takealook.DataBase.DataBest

import com.example.takealook.R
import com.example.takealook.Search.BookListDataBestToday
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.text.SimpleDateFormat
import java.util.*


class FragmentBestTodayJsoup(private val tabType : String) : Fragment() {
    private lateinit var db: DataBaseBest

    private var adapterToday: AdapterBestToday? = null
    private var today :  List<DataBest>? = null
    var recyclerView: RecyclerView? = null

    private val items = ArrayList<BookListDataBestToday?>()

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
        root = inflater.inflate(R.layout.fragment_best_today_tab, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)

        adapterToday = AdapterBestToday(requireContext(), items)

        db = Room.databaseBuilder(
            requireContext(),
            DataBaseBest::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

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


        if(tabType == "Ridi"){
            getBookListBest(recyclerView, db.bestDao().selectWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), "Ridi"))
        }

        if(tabType == "OneStore"){
            getBookListBest(recyclerView, db.bestDao().selectWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), "OneStore"))
        }


        return root
    }

    private fun getBookListBest(recyclerView: RecyclerView?, today :  List<DataBest>?) {

        for (i in today!!.indices) {

                val writerName = today!![i].writer
                val subject = today!![i].title
                val bookImg = today!![i].bookImg
                val intro = ""
                val bookCode = today!![i].bookCode
                val cntChapter = today!![i].cntChapter
                val cntPageRead = today!![i].cntPageRead
                val cntFavorite = ""
                val cntRecom = today!![i].cntRecom

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
                        i + 1,
                        false
                    )
                )

        }

        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterToday
        adapterToday!!.notifyDataSetChanged()

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }
}