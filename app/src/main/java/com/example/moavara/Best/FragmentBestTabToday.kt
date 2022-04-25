package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*


class FragmentBestTabToday(private val tabType: String) :
    Fragment() {

    private var adapterToday: AdapterBestToday? = null
    var recyclerView: RecyclerView? = null

    private val items = ArrayList<BookListDataBestToday?>()
    private lateinit var dbYesterday: DataBaseBestDay

    var status = ""

    lateinit var root: View

    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        cate = Genre.getGenre(requireContext()).toString()
        root = inflater.inflate(R.layout.fragment_best_today_tab, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterToday = AdapterBestToday(items)

        dbYesterday =
            Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-yesterday")
                .allowMainThreadQueries().build()

        getBookListToday()

        return root
    }

    private fun getBookListToday() {
        recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = adapterToday

        BestRef.getBestRefToday(tabType, cate).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    items.add(
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
                            group.numberDiff,
                            group.date,
                            group.status
                        )
                    )
                    adapterToday!!.notifyDataSetChanged()
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item!!, tabType, cate, position)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }
}