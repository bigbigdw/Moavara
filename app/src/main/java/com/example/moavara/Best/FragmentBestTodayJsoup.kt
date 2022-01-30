package com.example.moavara.Best

import android.content.Context
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
import com.google.firebase.database.*
import java.util.*


class FragmentBestTodayJsoup(private val tabType: String, private var bestRef: DatabaseReference) :
    Fragment() {

    private var adapterToday: AdapterBestToday? = null
    var recyclerView: RecyclerView? = null

    private val items = ArrayList<BookListDataBestToday?>()

    lateinit var root: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_today_tab, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterToday = AdapterBestToday(items)

        getBookListBest(recyclerView)

        return root
    }

    private fun getBookListBest(recyclerView: RecyclerView?) {
        bestRef =
            bestRef.child(tabType).child(DBDate.Week().toString()).child(DBDate.Day().toString())

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterToday

        bestRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    val writerName = group!!.writer
                    val subject = group.title
                    val bookImg = group.bookImg
                    val intro = group.cntFavorite
                    val bookCode = group.bookCode
                    val cntChapter = group.cntChapter
                    val cntPageRead = group.cntPageRead
                    val cntFavorite = group.cntFavorite
                    val cntRecom = group.cntRecom
                    val number = group.number

                    items.add(
                        BookListDataBestToday(
                            writerName,
                            subject,
                            bookImg,
                            intro,
                            bookCode,
                            cntChapter,
                            cntPageRead,
                            cntFavorite,
                            cntRecom,
                            number,
                        )
                    )
                    adapterToday!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        })



        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }


}