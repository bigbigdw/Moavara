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
import com.example.moavara.databinding.FragmentBestWeekendBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FragmentBestTabWeekend(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestWeekend? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend>()

    lateinit var root: View
    var genre = ""

    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        adapterWeek = AdapterBestWeekend(itemWeek)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterWeek

        getBestWeekList()

        return view
    }

    private fun getBestWeekList() {

        for (num in 0..9) {
            BestRef.getBestDataWeek(tabType, genre).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val weekItem = BookListDataBestWeekend()

                    for (day in 1..7) {
                        val item: BookListDataBest? =
                            dataSnapshot.child(day.toString()).child(num.toString())
                                .getValue(BookListDataBest::class.java)

                        if (day == 1) {
                            weekItem.sun = item
                        } else if (day == 2) {
                            weekItem.mon = item
                        } else if (day == 3) {
                            weekItem.tue = item
                        } else if (day == 4) {
                            weekItem.wed = item
                        } else if (day == 5) {
                            weekItem.thur = item
                        } else if (day == 6) {
                            weekItem.fri = item
                        } else if (day == 7) {
                            weekItem.sat = item
                        }
                    }

                    itemWeek.add(weekItem)
                    adapterWeek?.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

        adapterWeek?.setOnItemClickListener(object : AdapterBestWeekend.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {
                val item: BookListDataBestWeekend? = adapterWeek?.getItem(position)

                when {
                    value.equals("sun") -> {
                        findBook(item?.sun)
                    }
                    value.equals("mon") -> {
                        findBook(item?.mon)
                    }
                    value.equals("tue") -> {
                        findBook(item?.tue)
                    }
                    value.equals("wed") -> {
                        findBook(item?.wed)
                    }
                    value.equals("thur") -> {
                        findBook(item?.thur)
                    }
                    value.equals("fri") -> {
                        findBook(item?.fri)
                    }
                    value.equals("sat") -> {
                        findBook(item?.sat)
                    }
                }

                adapterWeek?.notifyDataSetChanged()

            }
        })

    }
    private fun findBook(item: BookListDataBest?) {
        if (item != null) {
            if (adapterWeek?.getSelectedBook() == item.title) {
                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item,
                    tabType,
                    item.number
                )
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
                adapterWeek!!.setSelectedBook("")
            } else {
                adapterWeek!!.setSelectedBook(item.title)
            }
        }
    }

}