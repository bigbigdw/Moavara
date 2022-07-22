package com.example.moavara.Best

import android.os.Bundle
import android.os.Environment
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

        readFile("test")
//        getBookListToday()

        return view
    }

    private fun getBookListToday() {

        val jsonArr1 = JSONArray()

        BestRef.getBestDataToday(tabType, genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {

                    for (postSnapshot in dataSnapshot.children) {
                        val sObject = JSONObject() //배열 내에 들어갈 json

                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)

                        if (group != null) {

                            sObject.put("writer", group.writer)
                            sObject.put("title", group.title)
                            sObject.put("bookImg", group.bookImg)
                            sObject.put("bookCode", group.bookCode)
                            sObject.put("info1", group.info1)
                            sObject.put("info2", group.info2)
                            sObject.put("info3", group.info3)
                            sObject.put("info4", group.info4)
                            sObject.put("info5", group.info5)
                            sObject.put("number", group.number)
                            sObject.put("date", group.date)
                            sObject.put("type", group.type)
                            sObject.put("memo", group.memo)

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

                            jsonArr1.put(sObject)
                        }
                    }

                    obj.putOpt("items", jsonArr1)
                    writeFile(obj, "test")
                    getBestTodayList(items, true)
                    adapterToday?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
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
                                lastItem.number,
                                lastItem.date,
                                moreLastItem.number - lastItem.number,
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
                                    0,
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

    fun writeFile(obj: JSONObject, fileName : String) {

        //저장소 위치 -> "/storage/self/peimary/test.json"
        val file = File(Environment.getExternalStorageDirectory(), "${fileName}.json")

        // new File( "/storage/self/peimary/",fileTitle);
        //위와 같이 저장소를 직접 입력해도 작동된다.
        try {
            /**
             * 폴더 생성
             * File dir = new File(Environment.getExternalStorageDirectory(), "Data");
             * if (!dir.exists()){
             * dir.mkdir();
             * }
             */


            //파일이 존재하지 않다면 생성
            if (!file.exists()) {
                file.createNewFile()
            }

            // FileWriter을 사용해도 상관 없음
            // 그러나 본인은 이어쓰기에 용이한 BufferedWriter 사용
            val bw = BufferedWriter(FileWriter(file, true))
            bw.write(obj.toString())
            bw.newLine()
            bw.close()
        } catch (e: IOException) {
            Log.i("저장오류", e.message.toString())
        }
    }

    fun readFile(fileName : String) {
        val file = File(Environment.getExternalStorageDirectory(), "${fileName}.json")
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
            val flag = jsonObject.getJSONArray("items")

            for (i in 0 until flag.length()) {
                val jo = flag.getJSONObject(i)
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

            adapterToday?.notifyDataSetChanged()

            reader.close()
        } catch (e1: FileNotFoundException) {
            Log.i("파일못찾음", e1.message.toString())
        } catch (e2: IOException) {
            Log.i("읽기오류", e2.message.toString())
        }
    }


}