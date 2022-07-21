package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestTabTodayBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FragmentBestTabToday(private val tabType: String) :
    Fragment(), BestTodayListener {

    private var adapterToday: AdapterBestToday? = null

    private val items = ArrayList<BookListDataBest>()
    private val bookCodeItems = ArrayList<BookListDataBestAnalyze>()
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

        adapterToday = AdapterBestToday(items, bookCodeItems)

        getBookListToday()

        return view
    }

    private fun getBookListToday() {
        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        BestRef.getBestDataToday(tabType, genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)

                    if (group != null) {
                        items.add(
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

                getBestTodayList(items, true)
                adapterToday?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        adapterToday?.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterToday?.getItem(position)

                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item,
                    tabType,
                    position
                )

                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }

    override fun getBestTodayList(items: ArrayList<BookListDataBest>, status: Boolean) {

        BestRef.getBookCode(tabType, genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (bookCodeList in items) {
                    val items = dataSnapshot.child(bookCodeList.bookCode)

                    if (items.childrenCount > 1) {
                        val bookCodes = ArrayList<BookListDataBestAnalyze>()

                        for(item in items.children){

                            val group: BookListDataBest? = item.getValue(BookListDataBest::class.java)

                            if (group != null) {
                                bookCodes.add(
                                    BookListDataBestAnalyze(
                                        group.info1,
                                        group.info2,
                                        group.info3,
                                        group.number,
                                        group.date,
                                    )
                                )
                            }
                        }

                        val lastItem = bookCodes[bookCodes.size - 1]
                        val moreLastItem = bookCodes[bookCodes.size - 2]

                        bookCodeItems.add(
                            BookListDataBestAnalyze(
                                lastItem.info1,
                                lastItem.info2,
                                lastItem.info3,
                                moreLastItem.number - lastItem.number,
                                lastItem.date,
                                bookCodes.size
                            )
                        )

                    } else if (items.childrenCount.toInt() == 1) {

                        val group: BookListDataBest? =
                            dataSnapshot.child(bookCodeList.bookCode).child(DBDate.DateMMDD())
                                .getValue(BookListDataBest::class.java)

                        if (group != null) {
                            bookCodeItems.add(
                                BookListDataBestAnalyze(
                                    group.info1,
                                    group.info2,
                                    group.info3,
                                    group.number,
                                    group.date,
                                    1
                                )
                            )
                        }
                    }
                }
                adapterToday?.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}