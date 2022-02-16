package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.google.firebase.database.*
import java.util.*


class FragmentBestTodayTab(private val tabType: String) :
    Fragment() {

    private var adapterToday: AdapterBestToday? = null
    var recyclerView: RecyclerView? = null

    private val items = ArrayList<BookListDataBestToday?>()
    private lateinit var dbYesterday: DataBaseBestDay

    var status = ""

    lateinit var root: View

    var cate = ""
    private lateinit var dbWeek: DataBaseBestDay

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        cate = Genre.getGenre(requireContext()).toString()

        root = inflater.inflate(R.layout.fragment_best_today_tab, container, false)

        dbYesterday =
            Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-yesterday")
                .allowMainThreadQueries().build()

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterToday = AdapterBestToday(items)

        dbWeek = Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-week")
            .allowMainThreadQueries().build()

        getBookListBest(recyclerView)

        return root
    }

    private fun getBookListBest(recyclerView: RecyclerView?) {

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterToday

        BestRef.getBestRefToday(tabType, cate).get().addOnSuccessListener {

            for (i in it.children) {
                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)
                items.add(
                    BookListDataBestToday(
                        group!!.writer,
                        group.title,
                        group.bookImg,
                        group.bookCode,
                        group.info1,
                        group.info2,
                        group.info3,
                        group.info4,
                        group.info5,
                        calculateNum(group.number, group.title),
                        group.date,
                        status
                    )
                )
                adapterToday!!.notifyDataSetChanged()
            }

        }.addOnFailureListener {}



        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }


    fun calculateNum(num: Int?, title: String?): Int {

        val yesterdayNum = dbYesterday.bestDao().findName(tabType, title!!)

        if (yesterdayNum == 0) {
            status = "NEW"
            return 0
        } else {
            return if (yesterdayNum < num!!) {
                status = "DOWN"
                num - yesterdayNum
            } else if (yesterdayNum > num) {
                status = "UP"
                num - yesterdayNum
            } else if (yesterdayNum == num) {
                status = "SAME"
                0
            } else {
                status = "SAME"
                0
            }
        }
    }


}