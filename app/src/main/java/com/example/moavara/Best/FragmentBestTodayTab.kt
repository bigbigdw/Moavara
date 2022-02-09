package com.example.moavara.Best

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.DBDate
import com.example.moavara.DataBase.DataBaseBestWeek
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.google.firebase.database.*
import java.util.*


class FragmentBestTodayTab(private val tabType: String, private var bestRef: DatabaseReference) :
    Fragment() {

    private var adapterToday: AdapterBestToday? = null
    var recyclerView: RecyclerView? = null

    private val items = ArrayList<BookListDataBestToday?>()
    private val itemsYesterday = ArrayList<BookListDataBestToday?>()
    private lateinit var dbYesterday: DataBaseBestWeek

    var status = ""

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_today_tab, container, false)

        dbYesterday = Room.databaseBuilder(requireContext(), DataBaseBestWeek::class.java, "best-yesterday")
            .allowMainThreadQueries().build()

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterToday = AdapterBestToday(items)

        getBookListBest(recyclerView)

        return root
    }

    fun getBookListBest(recyclerView: RecyclerView?) {
        bestRef = bestRef.child(tabType).child("today").child(DBDate.Day())

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterToday

        bestRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    items.add(
                        BookListDataBestToday(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.intro,
                            group.bookCode,
                            group.cntChapter,
                            group.cntPageRead,
                            group.cntFavorite,
                            group.cntRecom,
                            calculateNum(group.number, group.title),
                            group.date,
                            status
                        )
                    )
                    adapterToday!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
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



    fun calculateNum(num : Int?, title : String?) : Int{

        val yesterdayNum = dbYesterday.bestDao().findName(tabType, title!!)

        if (yesterdayNum == 0) {
            return num!!
        } else {
            return if (yesterdayNum < num!!) {
                status = "UP"
                (yesterdayNum - num) * -1
            } else if (yesterdayNum < num) {
                status = "DOWN"
                (yesterdayNum - num) * -1
            } else if (yesterdayNum == num) {
                status = "SAME"
                (yesterdayNum - num) * -1
            } else {
                (yesterdayNum - num) * -1
            }
        }
    }


}