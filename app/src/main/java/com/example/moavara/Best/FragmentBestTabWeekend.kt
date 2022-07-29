package com.example.moavara.Best

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.R
import com.example.moavara.Util.BestRef
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
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FragmentBestTabWeekend(private val tabType: String) : Fragment() {

    lateinit var root: View
    var genre = ""

    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!

    private var adapter: AdapterBestWeekend? = null
    var arrayCarousel = ArrayList<BookListDataBest>()
    private val itemWeek = ArrayList<ArrayList<BookListDataBest>>()
    private var obj = JSONObject()

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

            getBestWeekList()

            carousel.setViewListener(viewListenerBest)

            carousel.setImageClickListener { position ->
                Log.d("####", "HIHI")
            }
        }

        return view
    }

    private fun getBestWeekList() {

        binding.tviewBestTop.text = "오늘의 베스트"

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

                    val jsonArray = JSONArray()

                     for (day in 1..7) {

                         val itemList = ArrayList<BookListDataBest>()
                         val jsonObject = JSONObject()

                         for (num in 0..19) {

                            val item: BookListDataBest? =
                                dataSnapshot.child(day.toString()).child(num.toString())
                                    .getValue(BookListDataBest::class.java)

                             if (item != null) {
                                 itemList.add(item)
                                 BestRef.putItem(jsonObject, item)
                             }
                        }
                         jsonArray.put(jsonObject)
                         obj.putOpt(day.toString(), jsonArray)

                         if(dataSnapshot.key.toString() == day.toString()){

                             val itemListCarousel = ArrayList<BookListDataBest>()

                             for (num in 0..9) {

                                 val item: BookListDataBest? =
                                     dataSnapshot.child(day.toString()).child(num.toString())
                                         .getValue(BookListDataBest::class.java)

                                 if (item != null) {
                                     itemListCarousel.add(item)
                                 }
                             }

                             arrayCarousel.addAll(itemList)
                         }

                         itemWeek.add(itemList)

                    }

                    writeFile(obj)
                    adapter?.notifyDataSetChanged()

                    with(binding){
                        carousel.pageCount = arrayCarousel.size
                        carousel.slideInterval = 4000
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

    fun initCarousel(){
        with(binding){
            carousel.removeAllViews()
            arrayCarousel = ArrayList()
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