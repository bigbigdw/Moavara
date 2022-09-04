package com.example.moavara.Best

import android.content.Intent
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
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DBBest
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.DataBase.RoomBookListDataBest
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
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
    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!

    private var adapter: AdapterBestWeekend? = null
    var arrayCarousel = ArrayList<BookListDataBest>()
    private val itemWeek = ArrayList<ArrayList<BookListDataBest>?>()
    private var originalMonth = 0
    private var originalWeek = 0
    private var weekCount = 0
    val today = DBDate.getDateData(DBDate.DateMMDD())
    var userDao: DBUser? = null
    var bestDao: DBBest? = null
    var UserInfo : DataBaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        userDao = Room.databaseBuilder(
            requireContext(),
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
        }

        bestDao = Room.databaseBuilder(
            requireContext(),
            DBBest::class.java,
            "Week_${platform}_${UserInfo?.Genre}"
        ).allowMainThreadQueries().build()

        with(binding) {

            adapter = AdapterBestWeekend(requireContext(), itemWeek, platform)

            binding.rviewBest.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rviewBest.adapter = adapter

            val currentDate = DBDate.getDateData(DBDate.DateMMDD())

            originalMonth = DBDate.Month().toInt()
            originalWeek = (currentDate?.week ?: 0).toInt()

            if(bestDao?.bestDao()?.getAll()?.size == 0){
                getBestWeekList()
            } else {
                setRoomData()
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

        with(binding) {
            tviewWeek.text = "${originalMonth + 1}월 ${originalWeek - weekCount}주차"
            llayoutAfter.visibility = View.INVISIBLE

            if (originalWeek - weekCount == 1) {
                llayoutBefore.visibility = View.INVISIBLE
            }

            llayoutBefore.setOnClickListener {
                binding.blank.root.visibility = View.VISIBLE
                binding.llayoutWrap.visibility = View.GONE

                if(originalMonth == DBDate.Month().toInt() -2 && originalWeek - weekCount == 2){
                    binding.llayoutBefore.visibility = View.INVISIBLE
                }

                weekCount += 1

                if (originalWeek - weekCount == 0) {
                    originalMonth -= 1
                    weekCount = -2
                }

                llayoutAfter.visibility = View.VISIBLE
                getBestWeekListBefore(originalMonth, originalWeek, weekCount)
            }

            llayoutAfter.setOnClickListener {
                binding.blank.root.visibility = View.VISIBLE
                binding.llayoutWrap.visibility = View.GONE

                weekCount -= 1

                if (weekCount == -1) {
                    originalMonth += 1
                    originalWeek = 2
                    weekCount = 1
                }

                getBestWeekListBefore(originalMonth, originalWeek, weekCount)
            }
        }

        return view
    }

    private fun getBestWeekListBefore(month: Int, week: Int , count: Int) {

        binding.rviewBest.removeAllViews()
        itemWeek.clear()
        arrayCarousel.clear()

        var dataWeek = 0

        try {
            BestRef.getBestDataWeekBefore(platform, UserInfo?.Genre ?: "").child(month.toString())
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (weekCount == -2) {
                            dataWeek = dataSnapshot.childrenCount.toInt()
                            originalWeek = dataSnapshot.childrenCount.toInt()
                            weekCount = 1
                        } else {
                            dataWeek = week - count
                        }

                        binding.tviewWeek.text = "${month + 1}월 ${dataWeek}주차"

                        for (day in 1..7) {
                            val itemResult =
                                dataSnapshot.child(dataWeek.toString()).child(day.toString())
                            val itemList = ArrayList<BookListDataBest>()

                            if (itemResult.value == null) {
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

                            if (month == DBDate.Month().toInt()
                                && dataSnapshot.child(dataWeek.toString()).childrenCount.toString() == day.toString()
                                && DBDate.Week().toInt() == dataWeek
                            ) {
                                binding.llayoutAfter.visibility = View.INVISIBLE

                                val itemListCarousel = ArrayList<BookListDataBest>()

                                for (numCarousel in 0..8) {

                                    val item: BookListDataBest? =
                                        dataSnapshot.child(dataWeek.toString()).child(day.toString())
                                            .child(numCarousel.toString())
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

                        if (arrayCarousel.size > 0) {
                            with(binding) {
                                carousel.pageCount = arrayCarousel.size
                                carousel.slideInterval = 4000
                                binding.llayoutCarousel.visibility = View.VISIBLE
                            }
                        } else {
                            binding.llayoutCarousel.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getBestWeekList() {

        bestDao?.bestDao()?.initAll()

        binding.rviewBest.removeAllViews()
        itemWeek.clear()

        try {
            BestRef.getBestDataWeek(platform, UserInfo?.Genre ?: "").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (day in 1..7) {
                        val itemResult = dataSnapshot.child(day.toString())
                        val itemList = ArrayList<BookListDataBest>()

                        if (itemResult.value == null) {
                            for (num in 0..19) {
                                itemList.add(BookListDataBest())
                                bestDao?.bestDao()?.insert(RoomBookListDataBest())
                            }
                            itemWeek.add(itemList)

                        } else {
                            for (num in 0..19) {
                                val item: BookListDataBest? =
                                    itemResult.child(num.toString())
                                        .getValue(BookListDataBest::class.java)

                                if (item != null) {
                                    itemList.add(item)
                                    bestDao?.bestDao()?.insert(
                                        RoomBookListDataBest(
                                            item.writer,
                                            item.title,
                                            item.bookImg,
                                            item.bookCode,
                                            item.info1,
                                            item.info2,
                                            item.info3,
                                            item.info4,
                                            item.info5,
                                            item.info6,
                                            item.number,
                                            item.date,
                                            item.type,
                                            item.memo,
                                            DBDate.getDayInt(item.date) ?: 0
                                        )
                                    )
                                }
                            }
                            itemWeek.add(itemList)
                        }

                        val itemListCarousel = ArrayList<BookListDataBest>()

                        for (numCarousel in 0..8) {

                            val item: BookListDataBest? =
                                dataSnapshot.child(today?.date.toString())
                                    .child(numCarousel.toString())
                                    .getValue(BookListDataBest::class.java)

                            if (item != null) {
                                itemListCarousel.add(item)
                            }
                        }

                        arrayCarousel.addAll(itemListCarousel)
                    }

                    binding.blank.root.visibility = View.GONE
                    binding.llayoutWrap.visibility = View.VISIBLE
                    adapter?.notifyDataSetChanged()

                    if (arrayCarousel.size > 0) {
                        with(binding) {
                            carousel.pageCount = arrayCarousel.size
                            carousel.slideInterval = 4000
                            binding.llayoutCarousel.visibility = View.VISIBLE
                        }
                    } else {
                        binding.llayoutCarousel.visibility = View.GONE
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        } catch (e: JSONException) {
            e.printStackTrace()
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
                    Log.d("bestRankImage", "NO_IMAGE")
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
            } else if (platform == "Toksoda") {
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

                val info3 =
                    SpannableStringBuilder(arrayCarousel[position].info3.replace("별점", "별점 : "))
                info3.applyingTextColor(
                    "별점 : ",
                    "#6E7686"
                )

                val info4 =
                    SpannableStringBuilder(arrayCarousel[position].info4.replace("조회", "조회 수 : "))
                info4.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info5 =
                    SpannableStringBuilder(arrayCarousel[position].info5.replace("관심", "관심 : "))
                info5.applyingTextColor(
                    "관심 : ",
                    "#6E7686"
                )

                tviewInfo2.text = info3
                tviewInfo3.text = info4
                tviewInfo4.text = info5
            } else if (platform == "Kakao_Stage") {
                tviewInfo1.text = arrayCarousel[position].info2 ?: ""

                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.GONE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.VISIBLE

                val info3 =
                    SpannableStringBuilder(arrayCarousel[position].info3.replace("별점", "별점 : "))
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info4 =
                    SpannableStringBuilder(arrayCarousel[position].info4.replace("조회", "조회 수 : "))
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

                val info3 =
                    SpannableStringBuilder(arrayCarousel[position].info3.replace("별점", "별점 : "))
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info4 =
                    SpannableStringBuilder(arrayCarousel[position].info4.replace("조회", "조회 수 : "))
                info4.applyingTextColor(
                    "평점 : ",
                    "#6E7686"
                )

                val info5 =
                    SpannableStringBuilder(arrayCarousel[position].info5.replace("관심", "관심 : "))
                info5.applyingTextColor(
                    "댓글 수 : ",
                    "#6E7686"
                )

                tviewInfo2.text = info3
                tviewInfo3.text = info4
                tviewInfo4.text = info5
            } else if (platform == "Kakao" || platform == "Munpia" || platform == "Toksoda" || platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless" || platform == "Munpia") {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.VISIBLE

                if (platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless") {
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
                } else if (platform == "Kakao") {
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
                } else if (platform == "Munpia") {
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

    private fun setRoomData(){

        for (day in 1..7) {
            val itemList = ArrayList<BookListDataBest>()
            val itemResult = bestDao?.bestDao()?.getWeek(day.toString())

            if (itemResult?.size == 0) {
                for (num in 0..19) {
                    itemList.add(BookListDataBest())
                }
                itemWeek.add(itemList)
            } else {
                for (num in 0..19) {
                    if(itemResult != null){
                        itemList.add(BookListDataBest(
                            itemResult[num].writer,
                            itemResult[num].title,
                            itemResult[num].bookImg,
                            itemResult[num].bookCode,
                            itemResult[num].info1,
                            itemResult[num].info2,
                            itemResult[num].info3,
                            itemResult[num].info4,
                            itemResult[num].info5,
                            itemResult[num].info6,
                            itemResult[num].number,
                            itemResult[num].date,
                            itemResult[num].type,
                            itemResult[num].memo
                        ))
                    }
                }

                if (today?.date == day) {

                    val itemListCarousel = ArrayList<BookListDataBest>()
                    val itemResultCarousel = bestDao?.bestDao()?.getWeek(day.toString())

                    for (numCarousel in 0..8) {

                        if(itemResultCarousel != null){
                            itemListCarousel.add(BookListDataBest(
                                itemResultCarousel[numCarousel].writer,
                                itemResultCarousel[numCarousel].title,
                                itemResultCarousel[numCarousel].bookImg,
                                itemResultCarousel[numCarousel].bookCode,
                                itemResultCarousel[numCarousel].info1,
                                itemResultCarousel[numCarousel].info2,
                                itemResultCarousel[numCarousel].info3,
                                itemResultCarousel[numCarousel].info4,
                                itemResultCarousel[numCarousel].info5,
                                itemResultCarousel[numCarousel].info6,
                                itemResultCarousel[numCarousel].number,
                                itemResultCarousel[numCarousel].date,
                                itemResultCarousel[numCarousel].type,
                                itemResultCarousel[numCarousel].memo
                            ))
                        }

                        if (itemResultCarousel != null) {
                            binding.llayoutCarousel.visibility = View.VISIBLE
                        }
                    }

                    arrayCarousel.addAll(itemListCarousel)
                }

                itemWeek.add(itemList)
            }
        }

        binding.blank.root.visibility = View.GONE
        binding.rviewBest.visibility = View.VISIBLE
        adapter?.notifyDataSetChanged()

        if (arrayCarousel.size > 0) {
            with(binding) {
                carousel.pageCount = arrayCarousel.size
                carousel.slideInterval = 4000
                binding.llayoutCarousel.visibility = View.VISIBLE
            }
        } else {
            binding.llayoutCarousel.visibility = View.GONE
        }

        binding.blank.root.visibility = View.GONE
        binding.llayoutWrap.visibility = View.VISIBLE
        adapter?.notifyDataSetChanged()
    }
}