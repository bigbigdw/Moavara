package com.example.moavara.Best

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
import com.example.moavara.Search.BookListDataBestWeekend
import com.google.firebase.database.*
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


class FragmentBestWeekendTab(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestWeekend? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend?>()
    var recyclerView: RecyclerView? = null

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_weekend, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)
        adapterWeek = AdapterBestWeekend(requireContext(), itemWeek)

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val mRootRef = FirebaseDatabase.getInstance().reference
        val week = mRootRef.child("best").child(tabType).child("week")

        for(bookNum in 0..9){
            getBestToday(week, bookNum)
        }


        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapterWeek

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

        return root
    }

    private fun getBestToday(bestRef: DatabaseReference, num : Int) {

            bestRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val gson = Gson()
                    val s1 = gson.toJson(dataSnapshot.value)

                    val jsonObject = JSONArray(s1).getJSONObject(num)

                    itemWeek.add(
                        BookListDataBestWeekend(
                            if(jsonObject.has("1")){getBookListDataBestToday(jsonObject.getJSONObject("1"))} else null,
                            if(jsonObject.has("2")){getBookListDataBestToday(jsonObject.getJSONObject("2"))} else null,
                            if(jsonObject.has("3")){getBookListDataBestToday(jsonObject.getJSONObject("3"))} else null,
                            if(jsonObject.has("4")){getBookListDataBestToday(jsonObject.getJSONObject("4"))} else null,
                            if(jsonObject.has("5")){getBookListDataBestToday(jsonObject.getJSONObject("5"))} else null,
                            if(jsonObject.has("6")){getBookListDataBestToday(jsonObject.getJSONObject("6"))} else null,
                            if(jsonObject.has("7")){getBookListDataBestToday(jsonObject.getJSONObject("7"))} else null,
                        )
                    )
                    adapterWeek!!.notifyDataSetChanged()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

    }

    fun getBookListDataBestToday(json : JSONObject?) : BookListDataBestToday? {

        val items: BookListDataBestToday?

        if(json != null){
            items = BookListDataBestToday(
                if(json.has("writer")){json.getString("writer")} else "",
                if(json.has("title")){json.getString("title")} else "",
                if(json.has("bookImg")){json.getString("bookImg")} else "",
                if(json.has("intro")){json.getString("intro")} else "",
                if(json.has("bookCode")){json.getString("bookCode")} else "",
                if(json.has("cntChapter")){json.getString("cntChapter")} else "",
                if(json.has("cntPageRead")){json.getString("cntPageRead")} else "",
                if(json.has("cntFavorite")){json.getString("cntFavorite")} else "",
                if(json.has("cntRecom")){json.getString("cntRecom")} else "",
                json.getInt("number"),
                json.getString("date")
            )
        } else {
            items = null
        }

        return items
    }
}