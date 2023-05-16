package com.bigbigdw.moavara.Soon.Best

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBestWeekend
import com.bigbigdw.moavara.Util.BestRef
import com.bigbigdw.moavara.Util.BestRef.getItem
import com.bigbigdw.moavara.Util.BestRef.putItem
import com.bigbigdw.moavara.Util.DBDate
import com.bigbigdw.moavara.Util.Genre
import com.bigbigdw.moavara.Util.dpToPx
import com.bigbigdw.moavara.databinding.FragmentBestWeekendOldBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class FragmentBestTabWeekendOld(private val tabType: String,private val UserInfo: DataBaseUser) : Fragment() {

    private var adapterWeek: AdapterBestWeekendOld? = null
    private val itemWeek = ArrayList<BookListDataBestWeekend>()

    lateinit var root: View
    var genre = ""
    private var month = 0
    private var week = 0
    private var weekCount = 0

    private var _binding: FragmentBestWeekendOldBinding? = null
    private val binding get() = _binding!!
    private var obj = JSONObject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendOldBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        adapterWeek = AdapterBestWeekendOld(itemWeek)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterWeek

        readJsonList()

        adapterWeek?.setOnItemClickListener(object : AdapterBestWeekendOld.OnItemClickListener {
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

        val currentDate = DBDate.getDateData(DBDate.DateMMDD())

        month = DBDate.Month().toInt() + 1
        week = (currentDate?.week ?: 0).toInt()
        markDays()

        with(binding){
            tviewWeek.text = "${month}월 ${week - weekCount}주차"
            llayoutAfter.visibility = View.INVISIBLE

            llayoutBefore.setOnClickListener {
                if (weekCount > week - 3) {
                    llayoutBefore.visibility = View.INVISIBLE
                } else {
                    llayoutAfter.visibility = View.VISIBLE
                }

                weekCount += 1
                tviewWeek.text = "${month}월 ${week - weekCount}주차"
                getBestWeekListBefore(week - weekCount)
                markDays()
            }

            llayoutAfter.setOnClickListener {
                if(weekCount < 2){
                    llayoutAfter.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "미래로는 갈 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    llayoutBefore.visibility = View.VISIBLE
                }

                weekCount -= 1
                tviewWeek.text = "${month}월 ${week - weekCount}주차"
                getBestWeekListBefore(week - weekCount)

                markDays()
            }
        }
        return view
    }

    fun markDays(){
        with(binding){
            val today = DBDate.getDateData(DBDate.DateMMDD())

            if (weekCount == 0) {
                if (today != null) {
                    if (today.date == 1) {
                        tviewSun.background = todayMark()
                        tviewSun.setTextColor(Color.parseColor("#ffffff"))
                    } else if (today.date == 2) {
                        tviewMon.background = todayMark()
                    } else if (today.date == 3) {
                        tviewTue.background = todayMark()
                    } else if (today.date == 4) {
                        tviewWed.background = todayMark()
                    } else if (today.date == 5) {
                        tviewThur.background = todayMark()
                    } else if (today.date == 6) {
                        tviewFri.background = todayMark()
                    } else if (today.date == 7) {
                        tviewSat.background = todayMark()
                    }
                }
            } else {
                tviewSun.background = null
                tviewMon.background = null
                tviewTue.background = null
                tviewWed.background = null
                tviewThur.background = null
                tviewFri.background = null
                tviewSat.background = null
            }
        }
    }

    fun todayMark() : GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor("#844DF3"))
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 100f.dpToPx()
        }
    }

    private fun getBestWeekList() {

        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${tabType}.json")
        if (file.exists()) {
            file.delete()
        }

        binding.rviewBest.removeAllViews()
        itemWeek.clear()

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

    private fun getBestWeekListBefore(week : Int) {

        binding.rviewBest.removeAllViews()
        itemWeek.clear()

        try {
            BestRef.getBestDataWeekBefore(tabType, genre).child(week.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (num in 0..9) {
                        val weekItem = BookListDataBestWeekend()

                        for (day in 1..7) {

                            val item: BookListDataBest? =
                                dataSnapshot.child(day.toString()).child(num.toString())
                                    .getValue(BookListDataBest::class.java)

                            if (day == 1) {
                                if (item != null) {
                                    weekItem.sun = item
                                }

                            } else if (day == 2) {
                                if (item != null) {
                                    weekItem.mon = item
                                }

                            } else if (day == 3) {
                                if (item != null) {
                                    weekItem.tue = item
                                }

                            } else if (day == 4) {
                                if (item != null) {
                                    weekItem.wed = item
                                }

                            } else if (day == 5) {
                                if (item != null) {
                                    weekItem.thur = item
                                }

                            } else if (day == 6) {
                                if (item != null) {
                                    weekItem.fri = item
                                }
                            } else if (day == 7) {
                                if (item != null) {
                                    weekItem.sat = item
                                }
                            }
                        }

                        itemWeek.add(weekItem)
                    }

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
//                val mBottomDialogBest = BottomDialogBest(
//                    requireContext(),
//                    item,
//                    tabType,
//                    item.number,
//                    UserInfo
//                )
//                fragmentManager?.let { mBottomDialogBest.show(it, null) }
//                adapterWeek?.setSelectedBook("")
            } else {
                adapterWeek?.setSelectedBook(item.title)
            }
        }
    }



    fun writeFile(obj: JSONObject) {

        File("/storage/self/primary/MOAVARA").mkdir()

        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${tabType}.json")

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
        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${tabType}.json")
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