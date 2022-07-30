package com.example.moavara.Best

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.TrophyInfo
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.Util.applyingTextColor
import com.example.moavara.databinding.FragmentBestWeekendBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.synnapps.carouselview.ViewListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class FragmentBestTabWeekend(private val tabType: String) : Fragment() {

    lateinit var root: View
    var genre = ""

    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!

    private var adapter: AdapterBestWeekend? = null
    var arrayCarousel = ArrayList<BookListDataBest>()
    private val itemWeek = ArrayList<ArrayList<BookListDataBest>?>()
    private var obj = JSONObject()
    private var month = 0
    private var week = 0
    private var weekCount = 0
    private val currentDate = TrophyInfo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        with(binding){

            adapter = AdapterBestWeekend(requireContext(), itemWeek)

            binding.rviewBest.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rviewBest.adapter = adapter

            val currentDate = DBDate.getDateData(DBDate.DateMMDD())

            month = DBDate.Month().toInt() + 1
            week = (currentDate?.week ?: 0).toInt()

            readJsonList()

            carousel.setViewListener(viewListenerBest)

            carousel.setImageClickListener { position ->
                Log.d("####", "HIHI")
            }
        }

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
                binding.llayoutCarousel.visibility = View.GONE
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
                binding.llayoutCarousel.visibility = View.GONE
            }
        }

        return view
    }

    private fun getBestWeekListBefore(week : Int) {

        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${tabType}.json")
        if (file.exists()) {
            file.delete()
        }

        binding.rviewBest.removeAllViews()
        itemWeek.clear()

        try {
            BestRef.getBestDataWeekBefore(tabType, genre).child(week.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (day in 1..7) {
                        val itemResult = dataSnapshot.child(day.toString())
                        val jsonArray = JSONArray()
                        val itemList = ArrayList<BookListDataBest>()

                        if(itemResult.value == null){
                            itemWeek.add(null)
                        } else {
                            for (num in 0..19) {
                                val jsonObject = JSONObject()

                                val item: BookListDataBest? =
                                    itemResult.child(num.toString())
                                        .getValue(BookListDataBest::class.java)

                                if (item != null) {
                                    itemList.add(item)
                                    jsonArray.put(BestRef.putItem(jsonObject, item))
                                }
                            }
                            itemWeek.add(itemList)
                        }

                        if(dataSnapshot.childrenCount.toString() == day.toString() && DBDate.Week().toInt() == week)  {

                            val itemListCarousel = ArrayList<BookListDataBest>()

                            for (numCarousel in 0..8) {

                                val item: BookListDataBest? =
                                    dataSnapshot.child(day.toString()).child(numCarousel.toString())
                                        .getValue(BookListDataBest::class.java)

                                if (item != null) {
                                    itemListCarousel.add(item)
                                    binding.llayoutCarousel.visibility = View.VISIBLE
                                }
                            }

                            arrayCarousel.addAll(itemListCarousel)
                        }

                        obj.putOpt(day.toString(), jsonArray)
                    }

                    writeFile(obj)

                    adapter?.notifyDataSetChanged()

                    if(arrayCarousel.size > 0){
                        with(binding){
                            carousel.pageCount = arrayCarousel.size
                            carousel.slideInterval = 4000
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        } catch (e: JSONException) {
            e.printStackTrace()
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

                     for (day in 1..7) {
                         val itemResult = dataSnapshot.child(day.toString())
                         val jsonArray = JSONArray()
                         val itemList = ArrayList<BookListDataBest>()

                         if(itemResult.value == null){
                             itemWeek.add(null)
                         } else {
                             for (num in 0..19) {
                                 val jsonObject = JSONObject()

                                 val item: BookListDataBest? =
                                     itemResult.child(num.toString())
                                         .getValue(BookListDataBest::class.java)

                                 if (item != null) {
                                     itemList.add(item)
                                     jsonArray.put(BestRef.putItem(jsonObject, item))
                                 }
                             }
                             itemWeek.add(itemList)
                         }

                         if(dataSnapshot.childrenCount.toString() == day.toString()){

                             val itemListCarousel = ArrayList<BookListDataBest>()

                             for (numCarousel in 0..8) {

                                 val item: BookListDataBest? =
                                     dataSnapshot.child(day.toString()).child(numCarousel.toString())
                                         .getValue(BookListDataBest::class.java)

                                 if (item != null) {
                                     itemListCarousel.add(item)
                                     binding.llayoutCarousel.visibility = View.VISIBLE
                                 }
                             }

                             arrayCarousel.addAll(itemListCarousel)
                         }

                         obj.putOpt(day.toString(), jsonArray)
                    }

                    writeFile(obj)

                    adapter?.notifyDataSetChanged()

                    if(arrayCarousel.size > 0){
                        with(binding){
                            carousel.pageCount = arrayCarousel.size
                            carousel.slideInterval = 4000
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        } catch (e: JSONException) {
            e.printStackTrace()
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
            val today = DBDate.getDateData(DBDate.DateMMDD())
            val reader = BufferedReader(FileReader(file))

            val buffer = StringBuilder()
            var line = reader.readLine()
            while (line != null) {
                buffer.append(line).append("\n")
                line = reader.readLine()
            }

            val jsonData = buffer.toString()
            val jsonObject = JSONObject(jsonData)

            for (day in 1..7) {
                val jsonArray = JSONArray()
                val itemList = ArrayList<BookListDataBest>()
                val itemResult = jsonObject.getJSONArray(day.toString())

                if(itemResult.length() == 0){
                    itemWeek.add(null)
                } else {
                    for (num in 0..19) {
                        val item = itemResult.getJSONObject(num)

                        if (item != null) {
                            itemList.add(BestRef.getItem(item))
                            jsonArray.put(BestRef.putItem(jsonObject, BestRef.getItem(item)))
                        }
                    }

                    if(today?.date == day){

                        val itemListCarousel = ArrayList<BookListDataBest>()

                        for (numCarousel in 0..8) {
                            val item = itemResult.getJSONObject(numCarousel)

                            if (item != null) {
                                itemListCarousel.add(BestRef.getItem(item))
                                binding.llayoutCarousel.visibility = View.VISIBLE
                            }
                        }

                        arrayCarousel.addAll(itemListCarousel)
                    }

                    itemWeek.add(itemList)
                }

                obj.putOpt(day.toString(), jsonArray)
            }

            writeFile(obj)
            adapter?.notifyDataSetChanged()

            with(binding){
                carousel.pageCount = arrayCarousel.size
                carousel.slideInterval = 4000
            }

            reader.close()

            adapter?.notifyDataSetChanged()

        } catch (e1: FileNotFoundException) {
            Log.i("파일못찾음", e1.message.toString())
            getBestWeekList()
            Toast.makeText(requireContext(), "리스트를 다운받고 있습니다", Toast.LENGTH_SHORT).show()
        } catch (e2: IOException) {
            Log.i("읽기오류", e2.message.toString())
        }
    }

    private val viewListenerBest =
        ViewListener { position ->
            val customView: View = layoutInflater.inflate(R.layout.item_best_weekend_carousel, null)

            val iviewBookBest: ImageView = customView.findViewById(R.id.iviewBookBest)
            val iviewRank: ImageView = customView.findViewById(R.id.iviewRank)
            val tviewTitle: TextView = customView.findViewById(R.id.tviewTitle)
            val tviewWriter: TextView = customView.findViewById(R.id.tviewWriter)
            val tviewInfo1: TextView = customView.findViewById(R.id.tviewInfo1)
            val tviewInfo2: TextView = customView.findViewById(R.id.tviewInfo2)
            val tviewInfo3: TextView = customView.findViewById(R.id.tviewInfo3)
            val tviewInfo4: TextView = customView.findViewById(R.id.tviewInfo4)
            val tviewInfo5: TextView = customView.findViewById(R.id.tviewInfo5)

            Glide.with(requireContext()).load(arrayCarousel[position].bookImg)
                .into(iviewBookBest)

            when (position) {
                0 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_1)
                }
                1 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_2)
                }
                2 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_3)
                }
                3 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_4)
                }
                4 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_5)
                }
                5 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_6)
                }
                6 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_7)
                }
                7 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_8)
                }
                8 -> {
                    iviewRank.setImageResource(R.drawable.icon_best_9)
                }
                else -> {
                    Log.d("bestRankImage","NO_IMAGE")
                }
            }

            tviewTitle.text = arrayCarousel[position].title
            tviewWriter.text = arrayCarousel[position].writer
            tviewInfo1.text = arrayCarousel[position].info2

            val info3 = SpannableStringBuilder(arrayCarousel[position].info3)
            info3.applyingTextColor(
                "조회 수 : ",
                "#6E7686"
            )

            val info4 = SpannableStringBuilder(arrayCarousel[position].info4)
            info4.applyingTextColor(
                "선호작 수 : ",
                "#6E7686"
            )

            val info5 = SpannableStringBuilder(arrayCarousel[position].info5)
            info5.applyingTextColor(
                "추천 수 : ",
                "#6E7686"
            )

            tviewInfo2.text = info3
            tviewInfo3.text = info4
            tviewInfo4.text = info5
            tviewInfo5.text = arrayCarousel[position].info1

            binding.carousel.indicatorGravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            customView
        }
}