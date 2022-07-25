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
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.BestRef.putItem
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestMonthBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.util.*


class FragmentBestTabMonth(private val tabType: String) : Fragment(), BestTodayListener {

    private lateinit var adapterMonth: AdapterBestMonth
    private val itemMonth = ArrayList<BookListDataBestWeekend>()
    private val ItemMonthDay = ArrayList<BookListDataBest>()
    private var adapterMonthDay: AdapterBestToday? = null
    private val bookCodeItems = ArrayList<BookListDataBestAnalyze>()

    private var _binding: FragmentBestMonthBinding? = null
    private val binding get() = _binding!!
    var genre = ""
    private var obj = JSONObject()
    private var year = 0
    private var month = 0
    private var monthCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestMonthBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()
        adapterMonth = AdapterBestMonth(itemMonth)
        adapterMonthDay = AdapterBestToday(ItemMonthDay, bookCodeItems)

        itemMonth.clear()
        readJsonList()

        with(binding) {
            rviewBestMonth.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rviewBestMonth.adapter = adapterMonth

            adapterMonth.setOnItemClickListener(object : AdapterBestMonth.OnItemClickListener {

                override fun onItemClick(v: View?, position: Int, value: String?) {
                    ItemMonthDay.clear()

                    if (value != null) {
                        BestRef.getBestDataMonth(tabType, genre).child((position + 1).toString())
                            .child(value).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    rviewBestMonthDay.layoutManager =
                                        LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    rviewBestMonthDay.adapter = adapterMonthDay

                                    if (dataSnapshot.childrenCount > 0) {

                                        if (llayoutMonthDetail.visibility == View.GONE) {
                                            llayoutMonthDetail.visibility = View.VISIBLE
                                        }
                                    } else {
                                        llayoutMonthDetail.visibility = View.GONE
                                    }

                                    for (postSnapshot in dataSnapshot.children) {
                                        val group: BookListDataBest? =
                                            postSnapshot.getValue(BookListDataBest::class.java)

                                        if (group != null) {
                                            ItemMonthDay.add(
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
                                        }

                                    }
                                    getBestTodayList(ItemMonthDay, true)
                                    adapterMonthDay?.notifyDataSetChanged()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                }

            })

            val currentDate = DBDate.getDateData(DBDate.DateMMDD())

            year = DBDate.Year().substring(2,4).toInt()
            month = (currentDate?.month ?: 0).toInt() + 1

            tviewMonth.text = "${year}년 ${month - monthCount}월"

            llayoutBefore.setOnClickListener {
                monthCount += 1

                Log.d("@@@@-!!", monthCount.toString())

                if(monthCount == 3){
                    Toast.makeText(requireContext(), "과거로는 갈 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    tviewMonth.text = "${year}년 ${month - monthCount}월"
                    adapterMonth.setMonthDate(monthCount)
                    getMonthBefore(month - monthCount)
                }
            }

            llayoutAfter.setOnClickListener {
                monthCount -= 1

                if(monthCount == -1){
                    Toast.makeText(requireContext(), "미래로는 갈 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    tviewMonth.text = "${year}년 ${month - monthCount}월"
                    adapterMonth.setMonthDate(monthCount)
                    getMonthBefore(month - monthCount)
                }
            }
        }

        adapterMonthDay?.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterMonthDay?.getItem(position)

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

    private fun getBestMonth() {

        binding.rviewBestMonth.removeAllViews()
        itemMonth.clear()

        val file = File(File("/storage/self/primary/MOAVARA"), "Month_${tabType}.json")
        if (file.exists()) {
            file.delete()
        }

        try {

            BestRef.getBestDataMonth(tabType, genre)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (week in 1..6) {
                            val jsonArray = JSONArray()
                            val weekItem = BookListDataBestWeekend()

                            for (day in 1..7) {
                                val item: BookListDataBest? =
                                    dataSnapshot.child(week.toString()).child(day.toString())
                                        .child("0")
                                        .getValue(BookListDataBest::class.java)

                                val jsonObject = JSONObject()

                                when (day) {
                                    1 -> {
                                        if (item != null) {
                                            weekItem.sun = item
                                            putItem(jsonObject, item)
                                        }
                                    }
                                    2 -> {
                                        if (item != null) {
                                            weekItem.mon = item
                                            putItem(jsonObject, item)
                                        }
                                    }
                                    3 -> {
                                        if (item != null) {
                                            weekItem.tue = item
                                            putItem(jsonObject, item)
                                        }
                                    }
                                    4 -> {
                                        if (item != null) {
                                            weekItem.wed = item
                                            putItem(jsonObject, item)
                                        }
                                    }
                                    5 -> {
                                        if (item != null) {
                                            weekItem.thur = item
                                            putItem(jsonObject, item)
                                        }
                                    }
                                    6 -> {
                                        if (item != null) {
                                            putItem(jsonObject, item)
                                            weekItem.fri = item
                                        }
                                    }
                                    7 -> {
                                        if (item != null) {
                                            putItem(jsonObject, item)
                                            weekItem.sat = item
                                        }
                                    }
                                }
                                jsonArray.put(jsonObject)
                            }

                            itemMonth.add(weekItem)
                            obj.putOpt(week.toString(), jsonArray)

                            adapterMonth.notifyDataSetChanged()
                        }
                        writeFile(obj)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })


        } catch (e: IOException) {
        }

    }

    fun getMonthBefore(month : Int){

        Log.d("@@@@-@@", month.toString())

        binding.rviewBestMonth.removeAllViews()
        itemMonth.clear()

        BestRef.getBestDataMonthBefore(tabType, genre).child((month - 1).toString())
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (week in 1..6) {
                        val weekItem = BookListDataBestWeekend()

                        for (day in 1..7) {
                            val item: BookListDataBest? =
                                dataSnapshot.child(week.toString()).child(day.toString())
                                    .child("0")
                                    .getValue(BookListDataBest::class.java)

                            when (day) {
                                1 -> {
                                    if (item != null) {
                                        weekItem.sun = item
                                    }
                                }
                                2 -> {
                                    if (item != null) {
                                        weekItem.mon = item
                                    }
                                }
                                3 -> {
                                    if (item != null) {
                                        weekItem.tue = item
                                    }
                                }
                                4 -> {
                                    if (item != null) {
                                        weekItem.wed = item
                                    }
                                }
                                5 -> {
                                    if (item != null) {
                                        weekItem.thur = item
                                    }
                                }
                                6 -> {
                                    if (item != null) {
                                        weekItem.fri = item
                                    }
                                }
                                7 -> {
                                    if (item != null) {
                                        weekItem.sat = item
                                    }
                                }
                            }
                        }

                        itemMonth.add(weekItem)
                        adapterMonth.notifyDataSetChanged()
                    }
                    writeFile(obj)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
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

                        for (item in items.children) {

                            val group: BookListDataBest? =
                                item.getValue(BookListDataBest::class.java)

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
                adapterMonthDay?.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun writeFile(obj: JSONObject) {

        File("/storage/self/primary/MOAVARA").mkdir()

        val file = File(File("/storage/self/primary/MOAVARA"), "Month_${tabType}.json")

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
        val file = File(File("/storage/self/primary/MOAVARA"), "Month_${tabType}.json")
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

            for (num in 1..6) {
                val weekItem = BookListDataBestWeekend()

                for (day in 0..6) {
                    val item = jsonObject.getJSONArray(num.toString()).getJSONObject(day)

                    if (day == 0) {
                        weekItem.sun = BestRef.getItem(item)
                    } else if (day == 1) {
                        weekItem.mon = BestRef.getItem(item)
                    } else if (day == 2) {
                        weekItem.tue = BestRef.getItem(item)
                    } else if (day == 3) {
                        weekItem.wed = BestRef.getItem(item)
                    } else if (day == 4) {
                        weekItem.thur = BestRef.getItem(item)
                    } else if (day == 5) {
                        weekItem.fri = BestRef.getItem(item)
                    } else if (day == 6) {
                        weekItem.sat = BestRef.getItem(item)
                    }
                }

                itemMonth.add(weekItem)
            }

            reader.close()

            adapterMonth.notifyDataSetChanged()
        } catch (e1: FileNotFoundException) {
            Log.i("파일못찾음", e1.message.toString())
            getBestMonth()
            Toast.makeText(requireContext(), "리스트를 다운받고 있습니다", Toast.LENGTH_SHORT).show()
        } catch (e2: IOException) {
            Log.i("읽기오류", e2.message.toString())
        }
    }
}