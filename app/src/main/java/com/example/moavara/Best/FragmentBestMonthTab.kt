package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.DBDate
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import com.google.firebase.database.*
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class FragmentBestMonthTab(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestMonth? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend?>()
    var recyclerView: RecyclerView? = null

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_month, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterWeek = AdapterBestMonth(requireContext(), itemWeek)

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val mRootRef = FirebaseDatabase.getInstance().reference
        val week = mRootRef.child("best").child(tabType).child("month")

        itemWeek.clear()
        getBestToday(week)


        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapterWeek

        adapterWeek!!.setOnItemClickListener(object : AdapterBestMonth.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {
                val item: BookListDataBestWeekend? = adapterWeek!!.getItem(position)

                if (item!!.sun != null && value.equals("sun")) {

                    if (adapterWeek!!.getSelectedBook() == item.sun!!.bookCode.toString()) {
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.sun)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                        adapterWeek!!.setSelectedBook("")
                    } else {
                        adapterWeek!!.setSelectedBook(item.sun!!.bookCode.toString())
                    }
                } else if (item.mon != null && value.equals("mon")) {

                    if (adapterWeek!!.getSelectedBook() == item.mon!!.bookCode.toString()) {
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.mon)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                        adapterWeek!!.setSelectedBook("")
                    } else {
                        adapterWeek!!.setSelectedBook(item.mon!!.bookCode.toString())
                    }

                } else if (item.tue != null && value.equals("tue")) {

                    if (adapterWeek!!.getSelectedBook() == item.tue!!.bookCode.toString()) {
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.tue)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                        adapterWeek!!.setSelectedBook("")
                    } else {
                        adapterWeek!!.setSelectedBook(item.tue!!.bookCode.toString())
                    }

                } else if (item.wed != null && value.equals("wed")) {

                    if (adapterWeek!!.getSelectedBook() == item.wed!!.bookCode.toString()) {
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.wed)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                    } else {
                        adapterWeek!!.setSelectedBook(item.wed!!.bookCode.toString())
                    }

                } else if (item.thur != null && value.equals("thur")) {

                    if (adapterWeek!!.getSelectedBook() == item.thur!!.bookCode.toString()) {
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.thur)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                        adapterWeek!!.setSelectedBook("")
                    } else {
                        adapterWeek!!.setSelectedBook(item.thur!!.bookCode.toString())
                    }

                } else if (item.fri != null && value.equals("fri")) {

                    if (adapterWeek!!.getSelectedBook() == item.fri!!.bookCode.toString()) {
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.fri)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                    } else {
                        adapterWeek!!.setSelectedBook(item.fri!!.bookCode.toString())
                    }

                } else if (item.sat != null && value.equals("sat")) {

                    if (adapterWeek!!.getSelectedBook() == item.fri!!.bookCode.toString()) {
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.fri)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                    } else {
                        adapterWeek!!.setSelectedBook(item.fri!!.bookCode.toString())
                    }

                }

                adapterWeek!!.notifyDataSetChanged()

            }
        })

        return root
    }

    private fun getBestToday(bestRef: DatabaseReference) {

        bestRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val group: BookListDataBestWeekend? =
                    dataSnapshot.getValue(BookListDataBestWeekend::class.java)

//                if (group!!.tue!!.bookCode == "1104753") {
//                    Log.d("!!!!", group!!.tue!!.number.toString())
//                }

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

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }
}