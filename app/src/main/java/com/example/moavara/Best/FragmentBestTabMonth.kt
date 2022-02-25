package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.*
import java.util.*


class FragmentBestTabMonth(private val tabType: String) : Fragment() {

    private var adapterMonth: AdapterBestMonth? = null
    private val itemMonth = ArrayList<BookListDataBestWeekend?>()

    var recyclerViewMonth: RecyclerView? = null

    private val ItemMonthDay = ArrayList<BookListDataBestToday?>()
    var recyclerviewMonthDay: RecyclerView? = null
    var llayoutMonthDetail: LinearLayout? = null
    private var adapterMonthDay: AdapterBestToday? = null

    lateinit var root: View
    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_month, container, false)

        cate = Genre.getGenre(requireContext()).toString()

        recyclerViewMonth = root.findViewById(R.id.rview_BestMonth)
        adapterMonth = AdapterBestMonth(itemMonth)

        llayoutMonthDetail = root.findViewById(R.id.llayoutMonthDetail)
        recyclerviewMonthDay = root.findViewById(R.id.rview_BestMonthDay)
        adapterMonthDay = AdapterBestToday(ItemMonthDay)


        val mRootRef = FirebaseDatabase.getInstance().reference
        val month =
            mRootRef.child("best").child(tabType).child(cate).child("month").child(DBDate.Month())
        val monthList =
            mRootRef.child("best").child(tabType).child(cate).child("month").child(DBDate.Month())

        itemMonth.clear()
        getBestMonth(month)

        recyclerViewMonth!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewMonth!!.adapter = adapterMonth

        adapterMonth!!.setOnItemClickListener(object : AdapterBestMonth.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {

                ItemMonthDay.clear()
                val item = adapterMonth!!.getItem(position)

                monthList.child((position + 1).toString()).child(value!!).child("day").get()
                    .addOnSuccessListener {

                        recyclerviewMonthDay!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        recyclerviewMonthDay!!.adapter = adapterMonthDay

                        if(it.childrenCount > 0){

                            if(llayoutMonthDetail!!.visibility == View.GONE){
                                llayoutMonthDetail!!.visibility = View.VISIBLE
                            }
                        } else {
                            llayoutMonthDetail!!.visibility = View.GONE
                        }

                        for (postSnapshot in it.children) {

                            val group: BookListDataBestToday? =
                                postSnapshot.getValue(BookListDataBestToday::class.java)

                            ItemMonthDay.add(
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
                                    group.date,
                                    "",
                                )
                            )
                            adapterMonthDay!!.notifyDataSetChanged()
                        }

                    }.addOnFailureListener {}

            }

        })

        adapterMonthDay!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterMonthDay!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }

            }
        })

        return root
    }

    private fun getBestMonth(bestRef: DatabaseReference) {

        bestRef.get().addOnSuccessListener {

            for (i in 1..5) {
                if (it.child(i.toString()).value != null) {
                    val group: BookListDataBestWeekend? =
                        it.child(i.toString()).getValue(BookListDataBestWeekend::class.java)
                    itemMonth.add(
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
                } else {
                    itemMonth.add(
                        BookListDataBestWeekend(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                        )
                    )
                }
                adapterMonth!!.notifyDataSetChanged()
            }

        }.addOnFailureListener {}

    }
}