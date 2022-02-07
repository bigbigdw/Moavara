package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.DBDate
import com.example.moavara.DataBase.DataBaseBest
import com.example.moavara.DataBase.DataBest
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import com.google.firebase.database.*


class FragmentBestWeekendTab(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestWeekend? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend?>()
    var recyclerView: RecyclerView? = null
    private lateinit var db: DataBaseBest

    private var adapterToday: AdapterBestToday? = null
    var recyclerViewToday: RecyclerView? = null
    private val items = java.util.ArrayList<BookListDataBestToday?>()

    lateinit var root: View
    var exception = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_weekend, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterWeek = AdapterBestWeekend(requireContext(), itemWeek)

        db = Room.databaseBuilder(
            requireContext(),
            DataBaseBest::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val mRootRef = FirebaseDatabase.getInstance().reference
        val week = mRootRef.child("best").child(tabType).child("week")
        val weeklist = mRootRef.child("best").child(tabType).child("week list")

        itemWeek.clear()
//        getBestToday(week)

        recyclerViewToday = root.findViewById(R.id.rview_BestToday)
        adapterToday = AdapterBestToday(items)

        getBestWeekList(db.bestDao().getAll(), recyclerViewToday)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapterWeek

        recyclerViewToday!!.adapter = adapterToday



        return root
    }

    private fun getBestToday(bestRef: DatabaseReference) {

        bestRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val group: BookListDataBestWeekend? = dataSnapshot.getValue(BookListDataBestWeekend::class.java)

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

        adapterWeek!!.setOnItemClickListener(object : AdapterBestWeekend.OnItemClickListener {
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

    }

    private fun getBestWeekList(bestRef: List<DataBest>?, recyclerView: RecyclerView?) {

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterToday


        for(i in bestRef!!.indices){

            if(i < 1){
                items.add(
                    BookListDataBestToday(
                        bestRef[i].writer,
                        bestRef[i].title,
                        bestRef[i].bookImg,
                        bestRef[i].intro,
                        bestRef[i].bookCode,
                        bestRef[i].cntChapter,
                        bestRef[i].cntPageRead,
                        bestRef[i].cntFavorite,
                        bestRef[i].cntRecom,
                        bestRef[i].number,
                    )
                )
                adapterToday!!.notifyDataSetChanged()
            } else {

                if(bestRef[i - 1].title != bestRef[i].title){
                    items.add(
                        BookListDataBestToday(
                            bestRef[i].writer,
                            bestRef[i].title,
                            bestRef[i].bookImg,
                            bestRef[i].intro,
                            bestRef[i].bookCode,
                            bestRef[i].cntChapter,
                            bestRef[i].cntPageRead,
                            bestRef[i].cntFavorite,
                            bestRef[i].cntRecom,
                            bestRef[i - 1].number?.plus(bestRef[i].number!!),
                        )
                    )
                    adapterToday!!.notifyDataSetChanged()
                }
            }
        }

        val cmpAsc: Comparator<String?> = object : Comparator<String?> {
            override fun compare(o1: String?, o2: String?): Int {
                return o1!!.compareTo(o2!!)
            }

        }

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }

    private fun test() {

    }

    private fun test2() {
        val test = mRootRef.child("best").child(tabType).child("today").child("7")
        val week = mRootRef.child("best").child(tabType).child("week list")
        var num = 0


        test.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    week.child((((7 - 1) * 20 ) + num).toString()).setValue(
                        BookListDataBestToday(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.intro,
                            group.bookCode,
                            group.cntChapter,
                            group.cntPageRead,
                            group.cntFavorite,
                            group.cntRecom,
                            group.number,
                            DBDate.Date()
                        )
                    )
                    num++
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }

}