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
import com.example.moavara.databinding.ActivityGenreBinding
import com.example.moavara.databinding.FragmentBestTabTodayBinding
import com.example.moavara.databinding.FragmentBestWeekendBinding
import com.google.firebase.database.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class FragmentBestTabWeekend(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestWeekend? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend?>()

    lateinit var root: View
    var cate = ""
    var week = mRootRef.child("best").child(tabType).child(cate).child("week")

    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()
        week = FirebaseDatabase.getInstance().reference.child("best").child(tabType).child(cate).child("week")

        adapterWeek = AdapterBestWeekend(requireContext(), itemWeek)
        getBestWeekList(week)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterWeek

        return view
    }

    private fun getBestWeekList(bestRef: DatabaseReference) {

//        bestRef.child("4")

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
    private fun findBook(item: BookListDataBestToday?) {
        if (item != null) {
            if (adapterWeek!!.getSelectedBook() == item.title) {
                val mBottomDialogBest = BottomDialogBest(requireContext(), item, tabType, cate, item.number)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
                adapterWeek!!.setSelectedBook("")
            } else {
                adapterWeek!!.setSelectedBook(item.title)
            }
        }
    }

}