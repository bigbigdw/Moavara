package com.example.moavara.Best

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestWeekendBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class FragmentBestTabWeekend(private val tabType: String) : Fragment() {

    private var adapterWeek: AdapterBestWeekend? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend>()

    lateinit var root: View
    var genre = ""

    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!
    private var obj = JSONObject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        adapterWeek = AdapterBestWeekend(itemWeek)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterWeek

        readJsonList()
//        getBestWeekList()

        adapterWeek?.setOnItemClickListener(object : AdapterBestWeekend.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {
                val item: BookListDataBestWeekend? = adapterWeek?.getItem(position)

                when {
                    value.equals("sun") -> {
                        findBook(item?.sun)
                    }
                    value.equals("mon") -> {
                        findBook(item?.mon)
                    }
                    value.equals("tue") -> {
                        findBook(item?.tue)
                    }
                    value.equals("wed") -> {
                        findBook(item?.wed)
                    }
                    value.equals("thur") -> {
                        findBook(item?.thur)
                    }
                    value.equals("fri") -> {
                        findBook(item?.fri)
                    }
                    value.equals("sat") -> {
                        findBook(item?.sat)
                    }
                }

                adapterWeek?.notifyDataSetChanged()

            }
        })

        return view
    }

    private fun getBestWeekList() {

        val file = File(Environment.getExternalStorageDirectory(), "Week_${tabType}.json")
        if (file.exists()) {
            file.delete()
        }

        try {
            BestRef.getBestDataWeek(tabType, genre).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (num in 0..9) {
                        val weekItem = BookListDataBestWeekend()
                        val jsonArray = JSONArray()

                        for (day in 1..7) {

                            val jsonObject = JSONObject()

                            val item: BookListDataBest? =
                                dataSnapshot.child(day.toString()).child(num.toString())
                                    .getValue(BookListDataBest::class.java)

                            if (day == 1) {
                                if (item != null) {
                                    weekItem.sun = item
                                    putItem(jsonObject, item)
                                }

                            } else if (day == 2) {
                                if (item != null) {
                                    weekItem.mon = item
                                    putItem(jsonObject, item)
                                }

                            } else if (day == 3) {
                                if (item != null) {
                                    weekItem.tue = item
                                    putItem(jsonObject, item)
                                }

                            } else if (day == 4) {
                                if (item != null) {
                                    weekItem.wed = item
                                    putItem(jsonObject, item)
                                }

                            } else if (day == 5) {
                                if (item != null) {
                                    weekItem.thur = item
                                    putItem(jsonObject, item)
                                }

                            } else if (day == 6) {
                                if (item != null) {
                                    putItem(jsonObject, item)
                                    weekItem.fri = item
                                }
                            } else if (day == 7) {
                                if (item != null) {
                                    putItem(jsonObject, item)
                                    weekItem.sat = item
                                }
                            }
                            jsonArray.put(jsonObject)
                        }

                        obj.putOpt(num.toString(), jsonArray)
                        itemWeek.add(weekItem)
                    }

                    writeFile(obj)
                    adapterWeek?.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun findBook(item: BookListDataBest?) {
        if (item != null) {
            if (adapterWeek?.getSelectedBook() == item.title) {
                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item,
                    tabType,
                    item.number
                )
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
                adapterWeek?.setSelectedBook("")
            } else {
                adapterWeek?.setSelectedBook(item.title)
            }
        }
    }

    fun putItem(jsonObject: JSONObject, item: BookListDataBest): JSONObject {

        jsonObject.put("writer", item.writer)
        jsonObject.put("title", item.title)
        jsonObject.put("bookImg", item.bookImg)
        jsonObject.put("bookCode", item.bookCode)
        jsonObject.put("info1", item.info1)
        jsonObject.put("info2", item.info2)
        jsonObject.put("info3", item.info3)
        jsonObject.put("info4", item.info4)
        jsonObject.put("info5", item.info5)
        jsonObject.put("number", item.number)
        jsonObject.put("date", item.date)
        jsonObject.put("type", item.type)
        jsonObject.put("memo", item.memo)

        return jsonObject
    }

    fun getItem(jsonObject: JSONObject): BookListDataBest {

        return BookListDataBest(
            jsonObject.optString("writer"),
            jsonObject.optString("title"),
            jsonObject.optString("bookImg"),
            jsonObject.optString("bookCode"),
            jsonObject.optString("info1"),
            jsonObject.optString("info2"),
            jsonObject.optString("info3"),
            jsonObject.optString("info4"),
            jsonObject.optString("info5"),
            jsonObject.optInt("number"),
            jsonObject.optString("date"),
            jsonObject.optString("type"),
            jsonObject.optString("memo"),
        )
    }

    fun writeFile(obj: JSONObject) {

        val file = File(Environment.getExternalStorageDirectory(), "Week_${tabType}.json")

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
        val file = File(Environment.getExternalStorageDirectory(), "Week_${tabType}.json")
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

            for (num in 0..9) {
                val weekItem = BookListDataBestWeekend()

                for (day in 0..6) {
                    val item = jsonObject.getJSONArray(num.toString()).getJSONObject(day)

                    if (day == 0) {
                        weekItem.sun = getItem(item)
                    } else if (day == 1) {
                        weekItem.mon = getItem(item)
                    } else if (day == 2) {
                        weekItem.tue = getItem(item)
                    } else if (day == 3) {
                        weekItem.wed = getItem(item)
                    } else if (day == 4) {
                        weekItem.thur = getItem(item)
                    } else if (day == 5) {
                        weekItem.fri = getItem(item)
                    } else if (day == 6) {
                        weekItem.sat = getItem(item)
                    }
                }

                itemWeek.add(weekItem)
            }

            reader.close()

            adapterWeek?.notifyDataSetChanged()
        } catch (e1: FileNotFoundException) {
            Log.i("파일못찾음", e1.message.toString())
            getBestWeekList()
            Toast.makeText(requireContext(), "리스트를 다운받고 있습니다", Toast.LENGTH_SHORT).show()
        } catch (e2: IOException) {
            Log.i("읽기오류", e2.message.toString())
        }
    }
}