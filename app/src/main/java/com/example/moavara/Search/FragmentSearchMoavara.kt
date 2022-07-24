package com.example.moavara.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Best.BottomDialogBest
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentSearchmoavaraBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FragmentSearchMoavara : Fragment() {

    private var adapterToday: AdapterBestMoavara? = null

    private val items = ArrayList<BookListDataBest>()
    private val bookCodeItems = ArrayList<BookListDataBestAnalyze>()
    var status = ""
    lateinit var root: View
    var genre = ""
    private var _binding: FragmentSearchmoavaraBinding? = null
    private val binding get() = _binding!!
    private var obj = JSONObject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchmoavaraBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        adapterToday = AdapterBestMoavara(items)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        binding.sview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                items.clear()
                adapterToday?.isSearch = true
                if (newText != null) {
                    adapterToday?.search(newText)
                }
                adapterToday?.notifyDataSetChanged()
                return false
            }
        })

        for(type in BestRef.typeList()){
            getBookListToday(type)
        }

        adapterToday?.setOnItemClickListener(object : AdapterBestMoavara.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterToday?.getItem(position)

                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item,
                    item?.type ?: "",
                    position
                )

                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })

        return view
    }

    private fun getBookListToday(tabType : String) {

        val jsonArray = JSONArray()

        BestRef.getBestDataToday(tabType, genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {

                    for (postSnapshot in dataSnapshot.children) {
                        val jsonObject = JSONObject() //배열 내에 들어갈 json

                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)

                        if (group != null) {

                            jsonObject.put("writer", group.writer)
                            jsonObject.put("title", group.title)
                            jsonObject.put("bookImg", group.bookImg)
                            jsonObject.put("bookCode", group.bookCode)
                            jsonObject.put("info1", group.info1)
                            jsonObject.put("info2", group.info2)
                            jsonObject.put("info3", group.info3)
                            jsonObject.put("info4", group.info4)
                            jsonObject.put("info5", group.info5)
                            jsonObject.put("number", group.number)
                            jsonObject.put("date", group.date)
                            jsonObject.put("type", group.type)
                            jsonObject.put("memo", group.memo)

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
                                group.memo
                            )
                            )

                            jsonArray.put(jsonObject)
                        }
                    }
                    obj.putOpt("items", jsonArray)
                    adapterToday?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}