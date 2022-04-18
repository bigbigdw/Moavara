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
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*


class FragmentBestTabToday(private val tabType: String) :
    Fragment() {

    private var adapterToday: AdapterBestToday? = null
    var recyclerView: RecyclerView? = null

    private val items = ArrayList<BookListDataBestToday?>()
    private lateinit var dbYesterday: DataBaseBestDay

    var status = ""

    lateinit var root: View

    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        cate = Genre.getGenre(requireContext()).toString()
        root = inflater.inflate(R.layout.fragment_best_today_tab, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterToday = AdapterBestToday(items)

        dbYesterday =
            Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-yesterday")
                .allowMainThreadQueries().build()

        getBookListBest()

        return root
    }

    private fun getBookListBest() {

        val yesterdayRef = mRootRef.child("best").child(tabType).child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
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
                            group.number,
                            group.number,
                            group.date,
                            status
                        )
                    )
                }
                adapterToday!!.notifyDataSetChanged()
                getBookListToday(itemsYesterday)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item!!, tabType, cate)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }

    private fun getBookListToday(itemsYesterday : ArrayList<BookListDataBestToday?>){
        recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = adapterToday

        BestRef.getBestRefToday(tabType, cate).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
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
                            group.number,
                            calculateNum(group.number, group.title, itemsYesterday),
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
    }

    private fun calculateNum(num: Int?, title: String?, itemsYesterday : ArrayList<BookListDataBestToday?>): Int {

        for (i in itemsYesterday) {
            if (i != null) {
                if (i.title == title) {
                    when {
                        i.number == 0 -> {
                            status = "NEW"
                            return 0
                        }
                        i.number < num!! -> {
                            status = "DOWN"
                            return num - i.number
                        }
                        i.number > num -> {
                            status = "UP"
                            return num - i.number
                        }
                        i.number == num -> {
                            status = "SAME"
                            0
                        }
                    }
                }
            }
        }
        status = "NEW"
        return 0
    }




}