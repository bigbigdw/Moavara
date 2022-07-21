package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestMonthBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FragmentBestTabMonth(private val tabType: String) : Fragment() {

    private lateinit var adapterMonth: AdapterBestMonth
    private val itemMonth = ArrayList<BookListDataBestWeekend>()
    private val ItemMonthDay = ArrayList<BookListDataBest>()
    private var adapterMonthDay: AdapterBestToday? = null

    private var _binding: FragmentBestMonthBinding? = null
    private val binding get() = _binding!!
    var genre = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestMonthBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()
        adapterMonth = AdapterBestMonth(itemMonth)
        adapterMonthDay = AdapterBestToday(ItemMonthDay)

        itemMonth.clear()
        getBestMonth()

        with(binding) {
            rviewBestMonth.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rviewBestMonth.adapter = adapterMonth

            adapterMonth.setOnItemClickListener(object : AdapterBestMonth.OnItemClickListener {

                override fun onItemClick(v: View?, position: Int, value: String?) {
                    ItemMonthDay.clear()

                    if (value != null) {
                        BestRef.getBestDataMonth(tabType, genre).child((position + 1).toString())
                            .child(value).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    rviewBestMonthDay.layoutManager =
                                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                                    rviewBestMonthDay.adapter = adapterMonthDay

                                    if (dataSnapshot.childrenCount > 0) {

                                        if (llayoutMonthDetail.visibility == View.GONE) {
                                            llayoutMonthDetail.visibility = View.VISIBLE
                                        }
                                    } else {
                                        llayoutMonthDetail.visibility = View.GONE
                                    }

                                    for (postSnapshot in dataSnapshot.children) {
                                        val group: BookListDataBest? =
                                            postSnapshot.getValue(BookListDataBest::class.java)

                                        if (group != null) {
                                            ItemMonthDay.add(
                                                BookListDataBest(
                                                    group.writer,
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
                                                    group.type,
                                                    group.status,
                                                    group.memo
                                                )
                                            )
                                        }

                                    }
                                    adapterMonthDay?.notifyDataSetChanged()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                }

            })
        }

        adapterMonthDay?.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterMonthDay?.getItem(position)

                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item,
                    tabType,
                    position
                )
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })

        return view
    }

    private fun getBestMonth() {

        for (week in 1..5) {
            BestRef.getBestDataMonth(tabType, genre).child(week.toString())
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val weekItem = BookListDataBestWeekend()

                        for (day in 1..7) {
                            val item: BookListDataBest? =
                                dataSnapshot.child(day.toString()).child("0")
                                    .getValue(BookListDataBest::class.java)

                            when (day) {
                                1 -> weekItem.sun = item
                                2 -> weekItem.mon = item
                                3 -> weekItem.tue = item
                                4 -> weekItem.wed = item
                                5 -> weekItem.thur = item
                                6 -> weekItem.fri = item
                                7 -> weekItem.sat = item
                            }
                        }

                        itemMonth.add(weekItem)
                        adapterMonth.notifyDataSetChanged()

                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }

    }
}