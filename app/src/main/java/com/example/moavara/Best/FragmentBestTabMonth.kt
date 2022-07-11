package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestMonthBinding
import com.google.firebase.database.*
import java.util.*


class FragmentBestTabMonth(private val tabType: String) : Fragment() {

    private lateinit var adapterMonth: AdapterBestMonth
    private val itemMonth = ArrayList<BookListDataBestWeekend>()
    private val ItemMonthDay = ArrayList<BookListDataBest>()
    private var adapterMonthDay: AdapterBestToday? = null

    private var _binding: FragmentBestMonthBinding? = null
    private val binding get() = _binding!!
    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestMonthBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()
        adapterMonth = AdapterBestMonth(itemMonth)
        adapterMonthDay = AdapterBestToday(ItemMonthDay)

        val mRootRef = FirebaseDatabase.getInstance().reference
        val month = mRootRef.child("best").child(tabType).child(cate).child("month").child(DBDate.Month())
        val monthList = mRootRef.child("best").child(tabType).child(cate).child("month").child(DBDate.Month())

        itemMonth.clear()
        getBestMonth(month)

        with(binding){
            rviewBestMonth.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rviewBestMonth.adapter = adapterMonth

            adapterMonth.setOnItemClickListener(object : AdapterBestMonth.OnItemClickListener {
                override fun onItemClick(v: View?, position: Int, value: String?) {

                    ItemMonthDay.clear()
                    val item = adapterMonth.getItem(position)

                    monthList.child((position + 1).toString()).child(value!!).child("day").get()
                        .addOnSuccessListener {

                            rviewBestMonthDay.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            rviewBestMonthDay.adapter = adapterMonthDay

                            if(it.childrenCount > 0){

                                if(llayoutMonthDetail.visibility == View.GONE){
                                    llayoutMonthDetail.visibility = View.VISIBLE
                                }
                            } else {
                                llayoutMonthDetail.visibility = View.GONE
                            }

                            for (postSnapshot in it.children) {

                                val group: BookListDataBest? =
                                    postSnapshot.getValue(BookListDataBest::class.java)

                                ItemMonthDay.add(
                                    BookListDataBest(
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
                                        group.numberDiff,
                                        group.date,
                                        group.status,
                                    )
                                )
                                adapterMonthDay!!.notifyDataSetChanged()
                            }

                        }.addOnFailureListener {}

                }

            })
        }

        adapterMonthDay!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterMonthDay!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item!!,
                    tabType,
                    position
                )
                fragmentManager?.let { mBottomDialogBest.show(it, null) }

            }
        })

        return view
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