package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.*
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.google.firebase.database.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class FragmentBestTabWeekend(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestWeekend? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend?>()
    var recyclerView: RecyclerView? = null
    private lateinit var dbYesterday: DataBaseBestDay
    private lateinit var dbWeek: DataBaseBestDay

    private var adapterToday: AdapterBestToday? = null
    var recyclerViewToday: RecyclerView? = null
    private val items = ArrayList<BookListDataBestToday?>()
    var status = ""

    lateinit var root: View
    var cate = ""
    var week = mRootRef.child("best").child(tabType).child(cate).child("week")
    var weekList = mRootRef.child("best").child(tabType).child(cate).child("week list")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_weekend, container, false)

        cate = Genre.getGenre(requireContext()).toString()
        week = mRootRef.child("best").child(tabType).child(cate).child("week")
        weekList = mRootRef.child("best").child(tabType).child(cate).child("week list")

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterWeek = AdapterBestWeekend(requireContext(), itemWeek)
        getBestToday(week)

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.adapter = adapterWeek

        dbYesterday =
            Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-yesterday")
                .allowMainThreadQueries().build()

        dbWeek = Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-week")
            .allowMainThreadQueries().build()

        recyclerViewToday = root.findViewById(R.id.rview_BestToday)
        adapterToday = AdapterBestToday(items)
        getBestWeekList()

        recyclerViewToday!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewToday!!.adapter = adapterToday

        return root
    }

    private fun getBestToday(bestRef: DatabaseReference) {

        bestRef.get().addOnSuccessListener {

            for (i in it.children) {
                val group: BookListDataBestWeekend? =
                    i.getValue(BookListDataBestWeekend::class.java)
                itemWeek.add(
                    BookListDataBestWeekend(
                        group!!.sun,
                        group.mon,
                        group.tue,
                        group.wed,
                        group.thur,
                        group.fri,
                        group.sat,
                    )
                )

                adapterWeek!!.notifyDataSetChanged()
            }

        }.addOnFailureListener {}

        adapterWeek!!.setOnItemClickListener(object : AdapterBestWeekend.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {
                val item: BookListDataBestWeekend? = adapterWeek!!.getItem(position)

                when {
                    value.equals("sun") -> {
                        findBook(item!!.sun)
                    }
                    value.equals("mon") -> {
                        findBook(item!!.mon)
                    }
                    value.equals("tue") -> {
                        findBook(item!!.tue)
                    }
                    value.equals("wed") -> {
                        findBook(item!!.wed)
                    }
                    value.equals("thur") -> {
                        findBook(item!!.thur)
                    }
                    value.equals("fri") -> {
                        findBook(item!!.fri)
                    }
                    value.equals("sat") -> {
                        findBook(item!!.sat)
                    }
                }

                adapterWeek!!.notifyDataSetChanged()

            }
        })

    }

    private fun getBestWeekList() {

        val week = dbWeek.bestDao().getAll(tabType)
        val ref: MutableMap<String?, Any> = HashMap()

        for (i in week.indices) {

            ref["writerName"] = week[i].writer
            ref["subject"] = week[i].title
            ref["bookImg"] = week[i].bookImg
            ref["bookCode"] = week[i].bookCode
            ref["info1"] = week[i].info1
            ref["info2"] = week[i].info2
            ref["info3"] = week[i].info3
            ref["info4"] = week[i].info4
            ref["info5"] = week[i].info5
            ref["number"] = week[i].number
            ref["numberDiff"] = 0
            ref["date"] = week[i].date
            ref["status"] = week[i].status

            items.add(BestRef.setBookListDataBestToday(ref))
            val cmpAsc: Comparator<BookListDataBestToday?> =
                Comparator { o1, o2 -> o1!!.number.compareTo(o2!!.number) }
            Collections.sort(items, cmpAsc)
            adapterToday!!.notifyDataSetChanged()


        }

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item!!, tabType, cate, position)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }

    private fun findBook(item: BookListDataBestToday?) {
        if (item != null) {
            if (adapterWeek!!.getSelectedBook() == item.title.toString()) {
                val mBottomDialogBest = BottomDialogBest(requireContext(), item, tabType, cate, item.number)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
                adapterWeek!!.setSelectedBook("")
            } else {
                adapterWeek!!.setSelectedBook(item.title.toString())
            }
        }
    }

}