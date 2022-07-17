package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestTabTodayBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FragmentBestTabToday(private val tabType: String) :
    Fragment() {

    private var adapterToday: AdapterBestToday? = null

    private val items = ArrayList<BookListDataBest?>()
    var status = ""
    lateinit var root: View
    var genre = ""
    private var _binding: FragmentBestTabTodayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestTabTodayBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()
        root = inflater.inflate(R.layout.fragment_best_tab_today, container, false)

        adapterToday = AdapterBestToday(items)
        getBookListToday()

        return view
    }

    private fun getBookListToday() {
        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        BestRef.getBestRefToday(tabType, genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)

                    items.add(
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
                            group.type,
                            group.status,
                            group.trophyCount,
                            group.data,
                            group.memo
                        )
                    )

                }
                adapterToday!!.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        adapterToday?.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item!!,
                    tabType,
                    position
                )

                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }
}