package com.example.moavara.Soon.Search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Best.BottomDialogBest
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Search.AdapterBestMoavara
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentSearchmoavaraBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class FragmentSearchMoavara(private val UserInfo: DataBaseUser) : Fragment() {

    private var adapterToday: AdapterBestMoavara? = null

    private val items = ArrayList<BookListDataBest>()
    private val searchItems = ArrayList<BookListDataBest>()
    var status = ""
    lateinit var root: View
    var genre = ""
    private var _binding: FragmentSearchmoavaraBinding? = null
    private val binding get() = _binding!!
    private var obj = JSONObject()
    val jsonArray = JSONArray()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchmoavaraBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAnalytics = Firebase.analytics

        genre = Genre.getGenre(requireContext()).toString()

        adapterToday = AdapterBestMoavara(items, searchItems)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        binding.sview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapterToday?.search(newText)
                }
                adapterToday?.notifyDataSetChanged()
                return false
            }
        })

        readJsonList()

        adapterToday?.setOnItemClickListener(object : AdapterBestMoavara.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterToday?.getItem(position)

                if (item != null) {
                    if(item.type == "MrBlue"){
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse( "https://www.mrblue.com/novel/${item.bookCode}")
                        )
                        context?.startActivity(intent)
                    } else {
                        val bundle = Bundle()
                        bundle.putString("BEST_PLATFORM", item.type)
                        firebaseAnalytics.logEvent("BEST_BottomDialogBest", bundle)

                        val mBottomDialogBest = BottomDialogBest(
                            requireContext(),
                            item,
                            item.type ?: "",
                            position,
                            UserInfo,
                            firebaseAnalytics
                        )

                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                    }
                }
            }
        })

        return view
    }

    private fun getBookListToday(tabType : String) {

        val file = File(File("/storage/self/primary/MOAVARA"), "Today_Search.json")
        if (file.exists()) {
            file.delete()
        }

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

                            val item = BookListDataBest(
                                group.writer,
                                group.title,
                                group.bookImg,
                                group.bookCode,
                                group.info1,
                                group.info2,
                                group.info3,
                                group.info4,
                                group.info5,
                                group.info6,
                                group.number,
                                group.date,
                                group.type,
                                group.memo
                            )

                            items.add(item)
                            searchItems.add(item)

                            jsonArray.put(jsonObject)
                        }
                    }
                    obj.putOpt("items", jsonArray)
                    if(tabType == "MrBlue"){
                        writeJsonList(obj)
                    }
                    adapterToday?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun writeJsonList(obj: JSONObject) {

        File("/storage/self/primary/MOAVARA").mkdir()

        val file = File(File("/storage/self/primary/MOAVARA"), "Today_Search.json")

        try {

            if (!file.exists()) {
                file.createNewFile()
            }

            val bw = BufferedWriter(FileWriter(file, true))
            bw.write(obj.toString())
            bw.newLine()
            bw.close()
        } catch (e: IOException) {
            Log.i("저장오류", e.message.toString())
        }
    }

    fun readJsonList() {
        val file = File(File("/storage/self/primary/MOAVARA"), "Today_Search.json")
        try {
            val reader = BufferedReader(FileReader(file))

            val buffer = StringBuilder()
            var line = reader.readLine()
            while (line != null) {
                buffer.append(line).append("\n")
                line = reader.readLine()
            }

            val jsonData = buffer.toString()

            val jsonObject = JSONObject(jsonData)
            val itemsFlag = jsonObject.getJSONArray("items")

            for (i in 0 until itemsFlag.length()) {
                val jo = itemsFlag.getJSONObject(i)

                val item = BookListDataBest(
                    jo.optString("writer"),
                    jo.optString("title"),
                    jo.optString("bookImg"),
                    jo.optString("bookCode"),
                    jo.optString("info1"),
                    jo.optString("info2"),
                    jo.optString("info3"),
                    jo.optString("info4"),
                    jo.optString("info5"),
                    jo.optString("info6"),
                    jo.optInt("number"),
                    jo.optString("date"),
                    jo.optString("type"),
                    jo.optString("memo"),
                )

                items.add(item)
                searchItems.add(item)
            }

            reader.close()

            adapterToday?.notifyDataSetChanged()
        } catch (e1: FileNotFoundException) {
            Log.i("파일못찾음", e1.message.toString())
            for(type in BestRef.typeList()){
                getBookListToday(type)
            }
            Toast.makeText(requireContext(), "리스트를 다운받고 있습니다", Toast.LENGTH_SHORT).show()
        } catch (e2: IOException) {
            Log.i("읽기오류", e2.message.toString())
        }
    }
}