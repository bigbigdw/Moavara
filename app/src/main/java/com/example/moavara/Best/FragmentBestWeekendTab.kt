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
import com.example.moavara.DataBase.*
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.BestRef
import com.google.firebase.database.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class FragmentBestWeekendTab(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestWeekend? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend?>()
    var recyclerView: RecyclerView? = null
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

        dbWeek = Room.databaseBuilder(
            requireContext(),
            DataBaseBestDay::class.java,
            "best-week"
        ).allowMainThreadQueries()
            .build()

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        itemWeek.clear()
        getBestToday(week)

        recyclerViewToday = root.findViewById(R.id.rview_BestToday)
        adapterToday = AdapterBestToday(items)

        items.clear()
        getBestWeekList(dbWeek.bestDao().getAll(tabType), recyclerViewToday)

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

    private fun getBestWeekList(bestRef: List<DataBestDay>?, recyclerView: RecyclerView?) {

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterToday

        if(dbWeek.bestDao().getAll(tabType).isEmpty()) {

            var num = 1

            weekList.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val tempItems = ArrayList<BookListDataBestToday?>()

                    for (postSnapshot in dataSnapshot.children) {

                        val group: BookListDataBestToday? =
                            postSnapshot.getValue(BookListDataBestToday::class.java)

                        val Ref: MutableMap<String?, Any> = HashMap()

                        Ref["writerName"] = group!!.writer!!
                        Ref["subject"] = group.title!!
                        Ref["bookImg"] = group.bookImg!!
                        Ref["bookCode"] = group.bookCode!!
                        Ref["info1"] = group.info1!!
                        Ref["info2"] = group.info2!!
                        Ref["info3"] = group.info3!!
                        Ref["info4"] = group.info4!!
                        Ref["info5"] = group.info5!!
                        Ref["number"] = group.number!!
                        Ref["date"] = group.date!!
                        Ref["status"] = " "

                        dbWeek.bestDao().insert(BestRef.setDataBestDay(Ref, tabType))
                        tempItems.add(BestRef.setBookListDataBestToday(Ref, num))

                        num += 1
                    }

                    for(i in tempItems.indices){
                        if(i < 1){
                            items.add(tempItems[i])
                            adapterToday!!.notifyDataSetChanged()
                        } else {
                            if(tempItems[i - 1]!!.title != tempItems[i]!!.title){
                                items.add(tempItems[i])
                                adapterToday!!.notifyDataSetChanged()
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        } else {
            for(i in bestRef!!.indices){

                if(i < 1){
                    items.add(
                        BookListDataBestToday(
                            bestRef[i].writer,
                            bestRef[i].title,
                            bestRef[i].bookImg,
                            bestRef[i].intro,
                            bestRef[i].bookCode,
                            bestRef[i].cntChapter,
                            bestRef[i].cntPageRead,
                            bestRef[i].cntFavorite,
                            bestRef[i].cntRecom,
                            bestRef[i].number,
                            bestRef[i].date,
                            ""
                        )
                    )
                    adapterToday!!.notifyDataSetChanged()
                } else {
                    if(bestRef[i - 1].title != bestRef[i].title){
                        items.add(
                            BookListDataBestToday(
                                bestRef[i].writer,
                                bestRef[i].title,
                                bestRef[i].bookImg,
                                bestRef[i].intro,
                                bestRef[i].bookCode,
                                bestRef[i].cntChapter,
                                bestRef[i].cntPageRead,
                                bestRef[i].cntFavorite,
                                bestRef[i].cntRecom,
                                bestRef[i].number,
                                bestRef[i].date,
                                ""
                            )
                        )
                        adapterToday!!.notifyDataSetChanged()
                    }
                }
            }
        }

        val cmpAsc: Comparator<BookListDataBestToday?> =
            Comparator { o1, o2 -> o1!!.number!!.compareTo(o2!!.number!!) }

        Collections.sort(items, cmpAsc)

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
            if (adapterWeek!!.getSelectedBook() == item.bookCode.toString()) {
                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
                adapterWeek!!.setSelectedBook("")
            } else {
                adapterWeek!!.setSelectedBook(item.bookCode.toString())
            }
        }
    }

    fun calculateNum(num : Int?, title : String?) : Int{

        val yesterdayNum = dbWeek.bestDao().findName(tabType, title!!)

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
                    (num - yesterdayNum) * -1
                }
                yesterdayNum == num -> {
                    status = "SAME"
                    num - yesterdayNum
                }
                else -> {
                    status = "SAME"
                    0
                }
            }
        }
    }
}