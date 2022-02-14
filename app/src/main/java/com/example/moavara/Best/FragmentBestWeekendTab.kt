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
import com.example.moavara.Util.DBDate
import com.google.firebase.database.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class FragmentBestWeekendTab(private val tabType: String) : Fragment() {

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
    val Genre = "ALL"
    val week = mRootRef.child("best").child(tabType).child(Genre).child("week")
    val weekList = mRootRef.child("best").child(tabType).child(Genre).child("week list")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_weekend, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterWeek = AdapterBestWeekend(requireContext(), itemWeek)

        dbYesterday = Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-yesterday")
            .allowMainThreadQueries().build()

        dbWeek = Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-week")
            .allowMainThreadQueries().build()

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        itemWeek.clear()
        getBestToday(week)

        recyclerViewToday = root.findViewById(R.id.rview_BestToday)
        adapterToday = AdapterBestToday(items)

        items.clear()
        getBestWeekList(recyclerViewToday)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapterWeek

        recyclerViewToday!!.adapter = adapterToday

        return root
    }

    private fun getBestToday(bestRef: DatabaseReference) {

        bestRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val group: BookListDataBestWeekend? = dataSnapshot.getValue(BookListDataBestWeekend::class.java)

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

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })

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

    private fun getBestWeekList(recyclerView: RecyclerView?) {

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterToday

        val week = dbWeek.bestDao().getAll(tabType)
        val ref: MutableMap<String?, Any> = HashMap()

        for(i in week.indices){

            if (calculateNum(week[i].number, week[i].title) != 0) {
                ref["writerName"] = week[i].writer!!
                ref["subject"] = week[i].title!!
                ref["bookImg"] = week[i].bookImg!!
                ref["bookCode"] = week[i].bookCode!!
                ref["info1"] = week[i].intro!!
                ref["info2"] = week[i].cntChapter!!
                ref["info3"] = week[i].cntPageRead!!
                ref["info4"] = week[i].cntFavorite!!
                ref["info5"] = week[i].cntRecom!!
                ref["number"] = calculateNum(week[i].number, week[i].title)
                ref["date"] = week[i].date!!
                ref["status"] = status

                items.add(BestRef.setBookListDataBestToday(ref))
                BestRef.setBestRefWeekList(tabType, i, Genre).setValue(BestRef.setBookListDataBestToday(ref))
                val cmpAsc: Comparator<BookListDataBestToday?> =
                    Comparator { o1, o2 -> o1!!.number!!.compareTo(o2!!.number!!) }
                Collections.sort(items, cmpAsc)
                adapterToday!!.notifyDataSetChanged()
            }

        }

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }

    private fun findBook(item: BookListDataBestToday?) {
        if (item != null) {
            if (adapterWeek!!.getSelectedBook() == item.title.toString()) {
                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
                adapterWeek!!.setSelectedBook("")
            } else {
                adapterWeek!!.setSelectedBook(item.title.toString())
            }
        }
    }

    fun calculateNum(num : Int?, title : String?) : Int{

        val yesterdayNum = dbYesterday.bestDao().findName(tabType, title!!)

        if (yesterdayNum == 0) {
            status = "NEW"
            return 0
        } else {
            return when {
                yesterdayNum < num!! -> {
                    status = "DOWN"
                    num - yesterdayNum
                }
                yesterdayNum > num -> {
                    status = "UP"
                    num - yesterdayNum
                }
                yesterdayNum == num -> {
                    status = "SAME"
                    num - yesterdayNum
                }
                else -> {
                    status = "SAME"
                    -1
                }
            }
        }
    }
}