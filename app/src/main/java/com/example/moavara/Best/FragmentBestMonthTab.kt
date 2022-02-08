package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestMonth
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import com.google.firebase.database.*
import java.util.*


class FragmentBestMonthTab(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestMonth? = null
    private var adapterMonthList: AdapterBestToday? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend?>()

    private val itemMonthList = ArrayList<BookListDataBestToday?>()
    var recyclerView: RecyclerView? = null
    private lateinit var dbMonth: DataBaseBestMonth
    var recyclerViewMonth: RecyclerView? = null

    private val itemMonthListDetail = ArrayList<BookListDataBestToday?>()
    var recyclerViewMonthDetail: RecyclerView? = null
    var llayoutMonthDetail: LinearLayout? = null
    private var adapterMonthListDetail: AdapterBestToday? = null

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_month, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterWeek = AdapterBestMonth(requireContext(), itemWeek)

        recyclerViewMonth = root.findViewById(R.id.rview_BestMonth)
        adapterMonthList = AdapterBestToday(itemMonthList)

        llayoutMonthDetail = root.findViewById(R.id.llayoutMonthDetail)
        recyclerViewMonthDetail = root.findViewById(R.id.rview_BestMonthDetail)
        adapterMonthListDetail = AdapterBestToday(itemMonthListDetail)

        dbMonth = Room.databaseBuilder(
            requireContext(),
            DataBaseBestMonth::class.java,
            "user-databaseM"
        ).allowMainThreadQueries()
            .build()

        val mRootRef = FirebaseDatabase.getInstance().reference
        val week = mRootRef.child("best").child(tabType).child("month")
        val monthList = mRootRef.child("best").child(tabType).child("month")

        itemWeek.clear()
        getBestToday(week)
        getMonthList()

        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.adapter = adapterWeek

        adapterWeek!!.setOnItemClickListener(object : AdapterBestMonth.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {

                itemMonthListDetail.clear()

                val item: BookListDataBestWeekend? = adapterWeek!!.getItem(position)

                recyclerViewMonthDetail!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewMonthDetail!!.adapter = adapterMonthListDetail

                if(llayoutMonthDetail!!.visibility == View.GONE){
                    llayoutMonthDetail!!.visibility = View.VISIBLE
                }
                    monthList.child((position + 1).toString()).child(value!!).child("day").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (postSnapshot in dataSnapshot.children) {

                                val group: BookListDataBestToday? =
                                    postSnapshot.getValue(BookListDataBestToday::class.java)

                                Log.d("!!!!", group!!.title.toString())

                                itemMonthListDetail.add(
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
                                        group.number,
                                    )
                                )
                                adapterMonthListDetail!!.notifyDataSetChanged()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                        }
                    })

                Log.d("@@@@", value!! + position)
            }
        })

        adapterMonthList!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterMonthList!!.getItem(position)

            }})

        return root
    }

    private fun getMonthList(){

        val monthList = dbMonth.bestDaoMonth().getAllTypes(tabType)

        recyclerViewMonth!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewMonth!!.adapter = adapterMonthList

        for(i in monthList.indices){
            itemMonthList.add(
                BookListDataBestToday(
                    monthList[i].writer,
                    monthList[i].title,
                    monthList[i].bookImg,
                    monthList[i].intro,
                    monthList[i].bookCode,
                    monthList[i].cntChapter,
                    monthList[i].cntPageRead,
                    monthList[i].cntFavorite,
                    monthList[i].cntRecom,
                    monthList[i].number,
                )
            )
            adapterMonthList!!.notifyDataSetChanged()
        }
    }

    private fun getBestToday(bestRef: DatabaseReference) {

        bestRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val group: BookListDataBestWeekend? =
                    dataSnapshot.getValue(BookListDataBestWeekend::class.java)

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

    }
}