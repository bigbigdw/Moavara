package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*


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
    private var obj = JSONObject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestTabTodayBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        adapterToday = AdapterBestToday(items, bookCodeItems)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        binding.blank.root.visibility = View.VISIBLE
        binding.blank.tviewblank.text = "작품을 불러오는 중..."
        binding.rviewBest.visibility = View.GONE

        readJsonList()

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

        return view
    }

    private fun getBookListToday() {

        val file = File(File("/storage/self/primary/MOAVARA"), "Today_${tabType}.json")
        if (file.exists()) {
            file.delete()
        }

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

                            items.add(BookListDataBest(
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
                            ))

                            jsonArray.put(jsonObject)
                        }
                    }
                    obj.putOpt("items", jsonArray)

                    getBestTodayList(items, true)

                    binding.blank.root.visibility = View.GONE
                    binding.rviewBest.visibility = View.VISIBLE
                    adapterToday?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    override fun getBestTodayList(items: ArrayList<BookListDataBest>, status: Boolean) {

        BestRef.getBookCode(tabType, genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val jsonArray = JSONArray()

                try {
                    for (bookCodeList in items) {

                        val items = dataSnapshot.child(bookCodeList.bookCode)

                        if (items.childrenCount > 1) {

                            val jsonObject = JSONObject()

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
                                    lastItem.number,
                                    lastItem.date,
                                    moreLastItem.number - lastItem.number,
                                    bookCodes.size
                                )
                            )

                            jsonObject.put("info1", lastItem.info1)
                            jsonObject.put("info2", lastItem.info2)
                            jsonObject.put("info3", lastItem.info3)
                            jsonObject.put("number", lastItem.number)
                            jsonObject.put("date", lastItem.date)
                            jsonObject.put("numberDiff", moreLastItem.number - lastItem.number)
                            jsonObject.put("trophyCount", bookCodes.size)

                            jsonArray.put(jsonObject)

                        } else if (items.childrenCount.toInt() == 1) {

                            val jsonObject = JSONObject()

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
                                        0,
                                        1
                                    )
                                )

                                jsonObject.put("info1", group.info1)
                                jsonObject.put("info2", group.info2)
                                jsonObject.put("info3", group.info3)
                                jsonObject.put("number", group.number)
                                jsonObject.put("date", group.date)
                                jsonObject.put("numberDiff", 0)
                                jsonObject.put("trophyCount", 1)

                                jsonArray.put(jsonObject)
                            }
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                writeJsonList(obj.putOpt("itemStatus", jsonArray))

                binding.blank.root.visibility = View.GONE
                binding.rviewBest.visibility = View.VISIBLE

                adapterToday?.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun writeJsonList(obj: JSONObject) {

        File("/storage/self/primary/MOAVARA").mkdir()

        val file = File(File("/storage/self/primary/MOAVARA"), "Today_${tabType}.json")

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
        val file = File(File("/storage/self/primary/MOAVARA"), "Today_${tabType}.json")
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
            val itemStatusFlag = jsonObject.getJSONArray("itemStatus")

            for (i in 0 until itemsFlag.length()) {
                val jo = itemsFlag.getJSONObject(i)
                items.add(BookListDataBest(
                    jo.optString("writer"),
                    jo.optString("title"),
                    jo.optString("bookImg"),
                    jo.optString("bookCode"),
                    jo.optString("info1"),
                    jo.optString("info2"),
                    jo.optString("info3"),
                    jo.optString("info4"),
                    jo.optString("info5"),
                    jo.optInt("number"),
                    jo.optString("date"),
                    jo.optString("type"),
                    jo.optString("memo"),
                ))
            }

            for (i in 0 until itemStatusFlag.length()) {
                val jo = itemStatusFlag.getJSONObject(i)
                bookCodeItems.add(BookListDataBestAnalyze(
                    jo.optString("info1"),
                    jo.optString("info2"),
                    jo.optString("info3"),
                    jo.optInt("number"),
                    jo.optString("date"),
                    jo.optInt("numberDiff"),
                    jo.optInt("trophyCount"),
                ))
            }

            reader.close()
            binding.blank.root.visibility = View.GONE
            binding.rviewBest.visibility = View.VISIBLE
            adapterToday?.notifyDataSetChanged()
        } catch (e1: FileNotFoundException) {
            Log.i("파일못찾음", e1.message.toString())
            getBookListToday()
            Toast.makeText(requireContext(), "리스트를 다운받고 있습니다", Toast.LENGTH_SHORT).show()
        } catch (e2: IOException) {
            Log.i("읽기오류", e2.message.toString())
        }
    }
}