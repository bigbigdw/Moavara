package com.example.moavara.Best

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class FragmentBestTabWeekend(private val platform: String) : Fragment() {

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
    val today = DBDate.getDateData(DBDate.DateMMDD())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        genre = Genre.getGenre(requireContext()).toString()

        with(binding){

            adapter = AdapterBestWeekend(requireContext(), itemWeek, platform)

            binding.rviewBest.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rviewBest.adapter = adapter

            val currentDate = DBDate.getDateData(DBDate.DateMMDD())

            month = DBDate.Month().toInt() + 1
            week = (currentDate?.week ?: 0).toInt()

            Looper.myLooper()?.let {
                Handler(it).postDelayed(
                    {
                        readJsonList()
                    },
                    300
                )
            }

            carousel.setViewListener(viewListenerBest)
            carousel.setImageClickListener { position ->
                val bookDetailIntent = Intent(requireContext(), ActivityBestDetail::class.java)
                bookDetailIntent.putExtra("BookCode", arrayCarousel[position].bookCode)
                bookDetailIntent.putExtra("Type", String.format("%s", platform))
                bookDetailIntent.putExtra("POSITION", position)
                startActivity(bookDetailIntent)
            }
        }

        with(binding){
            tviewWeek.text = "${month}월 ${week - weekCount}주차"
            llayoutAfter.visibility = View.INVISIBLE

            if(week - weekCount == 1){
                llayoutBefore.visibility = View.INVISIBLE
            }

            llayoutBefore.setOnClickListener {
                binding.blank.root.visibility = View.VISIBLE
                binding.llayoutWrap.visibility = View.GONE
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
                binding.blank.root.visibility = View.VISIBLE
                binding.llayoutWrap.visibility = View.GONE
                if(weekCount < 2){
                    llayoutAfter.visibility = View.INVISIBLE
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

        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${platform}.json")
        if (file.exists()) {
            file.delete()
        }

        binding.rviewBest.removeAllViews()
        itemWeek.clear()

        try {
            BestRef.getBestDataWeekBefore(platform, genre).child(week.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (day in 1..7) {
                        val itemResult = dataSnapshot.child(day.toString())
                        val itemList = ArrayList<BookListDataBest>()

                        if(itemResult.value == null){
                            for (num in 0..19) {
                                itemList.add(BookListDataBest())
                            }
                            itemWeek.add(itemList)
                        } else {
                            for (num in 0..19) {
                                val item: BookListDataBest? =
                                    itemResult.child(num.toString())
                                        .getValue(BookListDataBest::class.java)

                                if (item != null) {
                                    itemList.add(item)
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
                                }
                            }

                            arrayCarousel.addAll(itemListCarousel)
                        }

                    }

                    binding.blank.root.visibility = View.GONE
                    binding.llayoutWrap.visibility = View.VISIBLE
                    adapter?.notifyDataSetChanged()

                    if(arrayCarousel.size > 0){
                        with(binding){
                            carousel.pageCount = arrayCarousel.size
                            carousel.slideInterval = 4000
                            binding.llayoutCarousel.visibility = View.VISIBLE
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

        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${platform}.json")
        if (file.exists()) {
            file.delete()
        }

        binding.rviewBest.removeAllViews()
        itemWeek.clear()

        try {
            BestRef.getBestDataWeek(platform, genre).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                     for (day in 1..7) {
                         val itemResult = dataSnapshot.child(day.toString())
                         val jsonArray = JSONArray()
                         val itemList = ArrayList<BookListDataBest>()

                         if(itemResult.value == null){
                             for (num in 0..19) {
                                 val jsonObject = JSONObject()

                                 itemList.add(BookListDataBest())
                                 jsonArray.put(BestRef.putItem(jsonObject, BookListDataBest()))
                             }
                             itemWeek.add(itemList)
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

                         val itemListCarousel = ArrayList<BookListDataBest>()

                         for (numCarousel in 0..8) {

                             val item: BookListDataBest? =
                                 dataSnapshot.child(today?.date.toString()).child(numCarousel.toString())
                                     .getValue(BookListDataBest::class.java)

                             if (item != null) {
                                 itemListCarousel.add(item)
                             }
                         }

                         arrayCarousel.addAll(itemListCarousel)

                         obj.putOpt(day.toString(), jsonArray)
                    }

                    writeFile(obj)
                    binding.blank.root.visibility = View.GONE
                    binding.llayoutWrap.visibility = View.VISIBLE
                    adapter?.notifyDataSetChanged()

                    if(arrayCarousel.size > 0){
                        with(binding){
                            carousel.pageCount = arrayCarousel.size
                            carousel.slideInterval = 4000
                            binding.llayoutCarousel.visibility = View.VISIBLE
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

        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${platform}.json")

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
        val file = File(File("/storage/self/primary/MOAVARA"), "Week_${platform}.json")
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

            for (day in 1..7) {
                val jsonArray = JSONArray()
                val itemList = ArrayList<BookListDataBest>()
                val itemResult = jsonObject.getJSONArray(day.toString())

                if(itemResult.length() == 0){
                    for (num in 0..19) {
                        itemList.add(BookListDataBest())
                        jsonArray.put(BestRef.putItem(jsonObject, BookListDataBest()))
                    }
                    itemWeek.add(itemList)
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

            if(arrayCarousel.size > 0){
                with(binding){
                    carousel.pageCount = arrayCarousel.size
                    carousel.slideInterval = 4000
                    binding.llayoutCarousel.visibility = View.VISIBLE
                }
            }

            reader.close()
            binding.blank.root.visibility = View.GONE
            binding.llayoutWrap.visibility = View.VISIBLE
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

            if (platform == "MrBlue") {
                tviewInfo1.visibility = View.GONE
                tviewInfo2.visibility = View.GONE
                tviewInfo3.visibility = View.GONE
                tviewInfo4.visibility = View.GONE
                tviewInfo5.visibility = View.GONE
            }   else if(platform == "Toksoda"){
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.GONE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.VISIBLE

                tviewInfo1.text = arrayCarousel[position].info2

                val info3 = SpannableStringBuilder(arrayCarousel[position].info3)
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info5 = SpannableStringBuilder(arrayCarousel[position].info5)
                info5.applyingTextColor(
                    "선호작 수 : ",
                    "#6E7686"
                )

                tviewInfo3.text = info3
                tviewInfo4.text = info5
                tviewInfo5.text = arrayCarousel[position].info1 ?: ""
            } else if (platform == "Naver" || platform == "Naver_Today" || platform == "Naver_Challenge") {
                tviewInfo1.text = arrayCarousel[position].info1 ?: ""
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.GONE

                val info3 = SpannableStringBuilder(arrayCarousel[position].info3.replace("별점", "별점 : "))
                info3.applyingTextColor(
                    "별점 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder(arrayCarousel[position].info4?.replace("조회", "조회 수 : "))
                info4.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info5 = SpannableStringBuilder(arrayCarousel[position].info5?.replace("관심", "관심 : "))
                info5.applyingTextColor(
                    "관심 : ",
                    "#6E7686"
                )

                tviewInfo2.text = info3
                tviewInfo3.text = info4
                tviewInfo4.text = info5
            }  else if (platform == "Kakao_Stage") {
                tviewInfo1.text = arrayCarousel[position].info2 ?: ""

                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.GONE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.VISIBLE

                val info3 = SpannableStringBuilder(arrayCarousel[position].info3?.replace("별점", "별점 : "))
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder(arrayCarousel[position].info4?.replace("조회", "조회 수 : "))
                info4.applyingTextColor(
                    "선호작 수 : ",
                    "#6E7686"
                )

                tviewInfo3.text = info3
                tviewInfo4.text = info4
                tviewInfo5.text = arrayCarousel[position].info1 ?: ""
            } else if (platform == "Ridi") {
                tviewInfo1.text = arrayCarousel[position].info1 ?: ""
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.GONE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.GONE

                val info3 = SpannableStringBuilder(arrayCarousel[position].info3)
                info3.applyingTextColor(
                    "추천 수 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder(arrayCarousel[position].info4)
                info4.applyingTextColor(
                    "평점 : ",
                    "#6E7686"
                )

                tviewInfo3.text = info3
                tviewInfo4.text = info4
            } else if (platform == "OneStore") {
                tviewInfo1.visibility = View.GONE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.GONE

                val info3 = SpannableStringBuilder(arrayCarousel[position].info3.replace("별점", "별점 : "))
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder(arrayCarousel[position].info4.replace("조회", "조회 수 : "))
                info4.applyingTextColor(
                    "평점 : ",
                    "#6E7686"
                )

                val info5 = SpannableStringBuilder(arrayCarousel[position].info5?.replace("관심", "관심 : "))
                info5.applyingTextColor(
                    "댓글 수 : ",
                    "#6E7686"
                )

                tviewInfo2.text = info3
                tviewInfo3.text = info4
                tviewInfo4.text = info5
            } else if (platform == "Kakao" || platform == "Munpia" || platform == "Toksoda" || platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless" || platform == "Munpia" ) {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.VISIBLE

                if(platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless"){
                    tviewInfo1.text = arrayCarousel[position].info2 ?: ""

                    val info3 = SpannableStringBuilder(arrayCarousel[position].info3)
                    info3.applyingTextColor(
                        BestRef.setDetailText(platform, 1),
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder(arrayCarousel[position].info4)
                    info4.applyingTextColor(
                        BestRef.setDetailText(platform, 2),
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder(arrayCarousel[position].info5)
                    info5.applyingTextColor(
                        BestRef.setDetailText(platform, 3),
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5

                    tviewInfo5.text = arrayCarousel[position].info1 ?: ""
                } else if(platform == "Kakao"){
                    tviewInfo1.text = arrayCarousel[position].info2 ?: ""

                    val info3 = SpannableStringBuilder(arrayCarousel[position].info3)
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder(arrayCarousel[position].info4)
                    info4.applyingTextColor(
                        "추천 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder(arrayCarousel[position].info5)
                    info5.applyingTextColor(
                        "평점 : ",
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5
                    tviewInfo5.text = arrayCarousel[position].info1
                } else if(platform == "Munpia"){
                    tviewInfo1.text = arrayCarousel[position].info2

                    val info3 = SpannableStringBuilder(arrayCarousel[position].info3)
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder(arrayCarousel[position].info4)
                    info4.applyingTextColor(
                        "방문 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder(arrayCarousel[position].info5)
                    info5.applyingTextColor(
                        "선호작 수 : ",
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5
                    tviewInfo5.text = arrayCarousel[position].info1
                } else {
                    tviewInfo1.text = arrayCarousel[position].info2
                    tviewInfo2.text = arrayCarousel[position].info3
                    tviewInfo3.text = arrayCarousel[position].info4
                    tviewInfo4.text = arrayCarousel[position].info5
                    tviewInfo5.text = arrayCarousel[position].info1
                }
            }

            binding.carousel.indicatorGravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            customView
        }
}