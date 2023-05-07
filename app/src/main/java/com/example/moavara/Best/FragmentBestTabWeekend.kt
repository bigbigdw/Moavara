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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DBBest
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.DataBase.RoomBookListDataBest
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.applyingTextColor
import com.example.moavara.databinding.FragmentBestWeekendBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
//import com.synnapps.carouselview.ViewListener
import org.json.JSONException

class FragmentBestTabWeekend(private val platform: String, private val UserInfo: DataBaseUser) : Fragment() {

    lateinit var root: View
    private var _binding: FragmentBestWeekendBinding? = null
    private val binding get() = _binding!!

    private var adapter: AdapterBestWeekend? = null
    var arrayCarousel = ArrayList<BookListDataBest>()
    private val itemWeek = ArrayList<ArrayList<BookListDataBest>?>()
    val today = DBDate.getDateData(DBDate.DateMMDD())
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    var bestDao: DBBest? = null
    var currentWeek = 0
    var currentMonth = 0

    var firstMonthWeek = 0
    var secondMonthWeek = 0
    var weekChildNum = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestWeekendBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAnalytics = Firebase.analytics

        val currentDate = DBDate.getDateData(DBDate.DateMMDD())
        currentMonth = DBDate.Month().toInt()
        currentWeek = (currentDate?.week ?: 0).toInt()
        weekChildNum = (DBDate.getDateData(DBDate.DateMMDD())?.week ?: 0).toInt()

        binding.llayoutAfter.visibility = View.INVISIBLE
        binding.tviewWeek.text = "${currentMonth + 1}월 ${currentWeek}주차"

        BestRef.getBestDataWeekBefore(platform, UserInfo.Genre)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    firstMonthWeek =
                        dataSnapshot.child((currentMonth - 1).toString()).childrenCount.toInt()
                    secondMonthWeek =
                        dataSnapshot.child((currentMonth - 2).toString()).childrenCount.toInt()

                    with(binding) {

                        llayoutBefore.setOnClickListener {
                            binding.blank.root.visibility = View.VISIBLE
                            binding.llayoutWrap.visibility = View.GONE

                            llayoutAfter.visibility = View.VISIBLE


                            getBestWeekListBefore("BEFORE")
                        }

                        llayoutAfter.setOnClickListener {
                            binding.blank.root.visibility = View.VISIBLE
                            binding.llayoutWrap.visibility = View.GONE

                            getBestWeekListBefore("AFTER")
                        }


                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })


        bestDao = Room.databaseBuilder(
            requireContext(),
            DBBest::class.java,
            "Week_${platform}_${UserInfo.Genre}"
        ).allowMainThreadQueries().build()

        with(binding) {

            adapter = AdapterBestWeekend(requireContext(), itemWeek, platform, UserInfo, firebaseAnalytics)

            binding.rviewBest.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rviewBest.adapter = adapter


            if(bestDao?.bestDao()?.getAll()?.size == 0){
                getBestWeekList()
            } else {
//                setRoomData()
            }

//            carousel.setViewListener(viewListenerBest)
//            carousel.setImageClickListener { position ->
//                val bundle = Bundle()
//                bundle.putString("BEST_FROM", "week_carousel")
//                firebaseAnalytics.logEvent("BEST_ActivityBestDetail", bundle)
//
//                val bookDetailIntent = Intent(requireContext(), ActivityBestDetail::class.java)
//                bookDetailIntent.putExtra("BookCode", arrayCarousel[position].bookCode)
//                bookDetailIntent.putExtra("Type", String.format("%s", platform))
//                bookDetailIntent.putExtra("POSITION", position)
//                startActivity(bookDetailIntent)
//            }
        }

        return view
    }

    private fun getBestWeekListBefore(status : String) {

        binding.rviewBest.removeAllViews()
        itemWeek.clear()
        arrayCarousel.clear()

        if (status == "BEFORE") {

            currentWeek -= 1

            if (currentWeek == 0) {
                currentMonth -= 1

                if(currentMonth == DBDate.Month().toInt()){
                    currentWeek = (DBDate.getDateData(DBDate.DateMMDD())?.week ?: 0).toInt()
                } else if(currentMonth == DBDate.Month().toInt() - 1){
                    currentWeek = firstMonthWeek
                } else if(currentMonth == DBDate.Month().toInt() - 2){
                    currentWeek = secondMonthWeek
                }
                weekChildNum = currentWeek
            }
        }

        if (status == "AFTER") {


            currentWeek += 1

            if(currentWeek == weekChildNum){
                if(currentMonth == DBDate.Month().toInt()){
                    weekChildNum = (DBDate.getDateData(DBDate.DateMMDD())?.week ?: 0).toInt()
                } else if(currentMonth == DBDate.Month().toInt() - 1){
                    weekChildNum = firstMonthWeek
                } else if(currentMonth == DBDate.Month().toInt() - 2){
                    weekChildNum = secondMonthWeek
                }
            }

            if(currentWeek > weekChildNum){

                currentMonth += 1
                currentWeek = 1

            }
        }


        try {
            BestRef.getBestDataWeekBefore(platform, UserInfo.Genre).child(currentMonth.toString()).child(currentWeek.toString())
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (day in 1..7) {
                            val itemResult =
                                dataSnapshot.child(day.toString())
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

                            if (currentMonth == DBDate.Month().toInt()
                                && dataSnapshot.childrenCount.toString() == day.toString()
                                && DBDate.Week().toInt() == currentWeek
                            ) {
                                binding.llayoutAfter.visibility = View.INVISIBLE

                                val itemListCarousel = ArrayList<BookListDataBest>()

                                for (numCarousel in 0..8) {

                                    val item: BookListDataBest? =
                                        dataSnapshot.child(day.toString())
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
//                                carousel.pageCount = arrayCarousel.size
//                                carousel.slideInterval = 4000
                                binding.llayoutCarousel.visibility = View.VISIBLE
                            }
                        } else {
                            binding.llayoutCarousel.visibility = View.GONE
                        }

                        if (currentMonth == DBDate.Month().toInt() -2 && currentWeek == 1) {
                            binding.llayoutBefore.visibility = View.INVISIBLE
                        } else {
                            binding.llayoutBefore.visibility = View.VISIBLE
                        }

                        if (currentMonth == DBDate.Month().toInt() && currentWeek == (DBDate.getDateData(DBDate.DateMMDD())?.week ?: 0).toInt()) {
                            binding.llayoutAfter.visibility = View.INVISIBLE
                        } else {
                            binding.llayoutAfter.visibility = View.VISIBLE
                        }


                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

            binding.tviewWeek.text = "${currentMonth + 1}월 ${currentWeek}주차"

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getBestWeekList() {

        bestDao?.bestDao()?.initAll()

        binding.rviewBest.removeAllViews()
        itemWeek.clear()

        try {
            BestRef.getBestDataWeek(platform, UserInfo.Genre).addListenerForSingleValueEvent(object :
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
//                            carousel.pageCount = arrayCarousel.size
//                            carousel.slideInterval = 4000
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

//    private val viewListenerBest =
//        ViewListener { position ->
//            val customView: View = layoutInflater.inflate(R.layout.item_best_weekend_carousel, null)
//
//            val iviewBookBest: ImageView = customView.findViewById(R.id.iviewBookBest)
//            val iviewRank: ImageView = customView.findViewById(R.id.iviewRank)
//            val tviewTitle: TextView = customView.findViewById(R.id.tviewTitle)
//            val tviewWriter: TextView = customView.findViewById(R.id.tviewWriter)
//            val tviewInfo1: TextView = customView.findViewById(R.id.tviewInfo1)
//            val tviewInfo2: TextView = customView.findViewById(R.id.tviewInfo2)
//            val tviewInfo3: TextView = customView.findViewById(R.id.tviewInfo3)
//            val tviewInfo4: TextView = customView.findViewById(R.id.tviewInfo4)
//            val tviewInfo5: TextView = customView.findViewById(R.id.tviewInfo5)
//            val tviewBar : TextView = customView.findViewById(R.id.tviewBar)
//
//            Glide.with(requireContext()).load(arrayCarousel[position].bookImg)
//                .into(iviewBookBest)
//
//            when (arrayCarousel[position].number) {
//                0 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_1)
//                }
//                1 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_2)
//                }
//                2 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_3)
//                }
//                3 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_4)
//                }
//                4 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_5)
//                }
//                5 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_6)
//                }
//                6 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_7)
//                }
//                7 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_8)
//                }
//                8 -> {
//                    iviewRank.setImageResource(R.drawable.icon_best_9)
//                }
//                else -> {
//                    Log.d("bestRankImage", "NO_IMAGE")
//                }
//            }
//
//            tviewTitle.text = arrayCarousel[position].title
//            tviewWriter.text = arrayCarousel[position].writer
//
//            if (platform == "Toksoda") {
//
//                tviewInfo1.text = arrayCarousel[position].info2
//
//                val info3 = SpannableStringBuilder("조회 수 : ${arrayCarousel[position].info3}" )
//                info3.applyingTextColor(
//                    "조회 수 : ",
//                    "#6E7686"
//                )
//
//                val info5 = SpannableStringBuilder("선호작 수 : ${arrayCarousel[position].info5}")
//                info5.applyingTextColor(
//                    "선호작 수 : ",
//                    "#6E7686"
//                )
//
//                tviewInfo3.text = info3
//                tviewInfo4.text = info5
//                tviewInfo5.text = arrayCarousel[position].info1
//            } else if (platform == "Naver" || platform == "Naver_Today" || platform == "Naver_Challenge") {
//                tviewInfo1.text = arrayCarousel[position].info1
//
//                val info3 = SpannableStringBuilder("별점 수 : ${arrayCarousel[position].info3.replace("별점", "별점 : ")}")
//                info3.applyingTextColor(
//                    "별점 수 : ",
//                    "#6E7686"
//                )
//
//                val info4 = SpannableStringBuilder("조회 수 : ${arrayCarousel[position].info4.replace("조회", "조회 수 : ")}" )
//                info4.applyingTextColor(
//                    "조회 수 : ",
//                    "#6E7686"
//                )
//
//                val info5 = SpannableStringBuilder("관심 : ${arrayCarousel[position].info5.replace("관심", "관심 : ")}")
//                info5.applyingTextColor(
//                    "관심 : ",
//                    "#6E7686"
//                )
//
//                tviewInfo2.text = info3
//                tviewInfo3.text = info4
//                tviewInfo4.text = info5
//            } else if (platform == "Kakao_Stage") {
//                tviewInfo1.text = arrayCarousel[position].info2
//
//                val info3 = SpannableStringBuilder("조회 수 : ${arrayCarousel[position].info3.replace("별점", "별점 : ")}" )
//                info3.applyingTextColor(
//                    "조회 수 : ",
//                    "#6E7686"
//                )
//
//                val info4 = SpannableStringBuilder("선호작 수 : ${arrayCarousel[position].info4.replace("조회", "조회 수 : ")}" )
//                info4.applyingTextColor(
//                    "선호작 수 : ",
//                    "#6E7686"
//                )
//
//                tviewInfo3.text = info3
//                tviewInfo4.text = info4
//                tviewInfo5.text = arrayCarousel[position].info1
//            } else if (platform == "Ridi") {
//                tviewInfo1.text = arrayCarousel[position].info1
//
//                val info3 = SpannableStringBuilder("추천 수 : ${arrayCarousel[position].info3}" )
//                info3.applyingTextColor(
//                    "추천 수 : ",
//                    "#6E7686"
//                )
//
//                val info4 = SpannableStringBuilder("평점 : ${arrayCarousel[position].info4}")
//                info4.applyingTextColor(
//                    "평점 : ",
//                    "#6E7686"
//                )
//
//                tviewInfo3.text = info3
//                tviewInfo4.text = info4
//            } else if (platform == "OneStore") {
//
//                val info3 = SpannableStringBuilder("조회 수 : ${arrayCarousel[position].info3.replace("별점", "별점 : ")}" )
//                info3.applyingTextColor(
//                    "조회 수 : ",
//                    "#6E7686"
//                )
//
//                val info4 = SpannableStringBuilder("평점 : ${arrayCarousel[position].info4.replace("조회", "조회 수 : ")}" )
//                info4.applyingTextColor(
//                    "평점 : ",
//                    "#6E7686"
//                )
//
//                val info5 = SpannableStringBuilder("댓글 수 : ${arrayCarousel[position].info5.replace("관심", "관심 : ")}" )
//                info5.applyingTextColor(
//                    "댓글 수 : ",
//                    "#6E7686"
//                )
//
//
//                tviewInfo2.text = info3
//                tviewInfo3.text = info4
//                tviewInfo4.text = info5
//            } else if (platform == "Kakao" || platform == "Munpia" || platform == "Toksoda" || platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless" || platform == "Munpia") {
//
//                if (platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless") {
//                    tviewInfo1.text = arrayCarousel[position].info2
//
//                    val info3 = SpannableStringBuilder("조회 수 : ${arrayCarousel[position].info3}")
//                    info3.applyingTextColor(
//                        "조회 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info4 = SpannableStringBuilder("선호작 수 : ${arrayCarousel[position].info4}")
//                    info4.applyingTextColor(
//                        "선호작 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info5 = SpannableStringBuilder("추천 수 : ${arrayCarousel[position].info5}")
//                    info5.applyingTextColor(
//                        "추천 수 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo2.text = info3
//                    tviewInfo3.text = info4
//                    tviewInfo4.text = info5
//
//                    tviewInfo5.text = arrayCarousel[position].info1
//                } else if (platform == "Kakao") {
//                    tviewInfo1.text = arrayCarousel[position].info2
//
//                    val info3 = SpannableStringBuilder("조회 수 : ${arrayCarousel[position].info3}")
//                    info3.applyingTextColor(
//                        "조회 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info4 = SpannableStringBuilder("추천 수 : ${arrayCarousel[position].info4}")
//                    info4.applyingTextColor(
//                        "추천 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info5 = SpannableStringBuilder("평점 : ${arrayCarousel[position].info5}")
//                    info5.applyingTextColor(
//                        "평점 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo2.text = info3
//                    tviewInfo3.text = info4
//                    tviewInfo4.text = info5
//                    tviewInfo5.text = arrayCarousel[position].info1
//                } else if (platform == "Munpia") {
//                    tviewInfo1.text = arrayCarousel[position].info2
//
//                    val info3 = SpannableStringBuilder("조회 수 : ${arrayCarousel[position].info3}")
//                    info3.applyingTextColor(
//                        "조회 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info4 = SpannableStringBuilder("방문 수 : ${arrayCarousel[position].info4}")
//                    info4.applyingTextColor(
//                        "방문 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info5 = SpannableStringBuilder("선호작 수 : ${arrayCarousel[position].info5}")
//                    info5.applyingTextColor(
//                        "선호작 수 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo2.text = info3
//                    tviewInfo3.text = info4
//                    tviewInfo4.text = info5
//                    tviewInfo5.text = arrayCarousel[position].info1
//                } else {
//                    tviewInfo1.text = arrayCarousel[position].info2
//                    tviewInfo2.text = arrayCarousel[position].info3
//                    tviewInfo3.text = arrayCarousel[position].info4
//                    tviewInfo4.text = arrayCarousel[position].info5
//                    tviewInfo5.text = arrayCarousel[position].info1
//                }
//
//                if (tviewInfo1.text.isNotEmpty()) {
//                    tviewInfo1.visibility = View.VISIBLE
//                    tviewBar.visibility = View.VISIBLE
//                }
//                if (tviewInfo2.text.isNotEmpty()) {
//                    tviewInfo2.visibility = View.VISIBLE
//                }
//                if (tviewInfo3.text.isNotEmpty()) {
//                    tviewInfo3.visibility = View.VISIBLE
//                }
//                if (tviewInfo4.text.isNotEmpty()) {
//                    tviewInfo4.visibility = View.VISIBLE
//                }
//                if (tviewInfo5.text.isNotEmpty()) {
//                    tviewInfo5.visibility = View.VISIBLE
//                }
//
//            }
//
////            binding.carousel.indicatorGravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
//            customView
//        }

//    private fun setRoomData(){
//
//        for (day in 1..7) {
//            val itemList = ArrayList<BookListDataBest>()
//            val itemResult = bestDao?.bestDao()?.getWeek(day.toString())
//
//            if (itemResult?.size == 0) {
//                for (num in 0..19) {
//                    itemList.add(BookListDataBest())
//                }
//                itemWeek.add(itemList)
//            } else {
//                for (num in 0..19) {
//                    if(itemResult != null){
//                        itemList.add(BookListDataBest(
//                            itemResult[num].writer,
//                            itemResult[num].title,
//                            itemResult[num].bookImg,
//                            itemResult[num].bookCode,
//                            itemResult[num].info1,
//                            itemResult[num].info2,
//                            itemResult[num].info3,
//                            itemResult[num].info4,
//                            itemResult[num].info5,
//                            itemResult[num].info6,
//                            itemResult[num].number,
//                            itemResult[num].date,
//                            itemResult[num].type,
//                            itemResult[num].memo
//                        ))
//                    }
//                }
//
//                if (today?.date == day) {
//
//                    val itemListCarousel = ArrayList<BookListDataBest>()
//                    val itemResultCarousel = bestDao?.bestDao()?.getWeek(day.toString())
//
//                    for (numCarousel in 0..8) {
//
//                        if(itemResultCarousel != null){
//                            itemListCarousel.add(BookListDataBest(
//                                itemResultCarousel[numCarousel].writer,
//                                itemResultCarousel[numCarousel].title,
//                                itemResultCarousel[numCarousel].bookImg,
//                                itemResultCarousel[numCarousel].bookCode,
//                                itemResultCarousel[numCarousel].info1,
//                                itemResultCarousel[numCarousel].info2,
//                                itemResultCarousel[numCarousel].info3,
//                                itemResultCarousel[numCarousel].info4,
//                                itemResultCarousel[numCarousel].info5,
//                                itemResultCarousel[numCarousel].info6,
//                                itemResultCarousel[numCarousel].number,
//                                itemResultCarousel[numCarousel].date,
//                                itemResultCarousel[numCarousel].type,
//                                itemResultCarousel[numCarousel].memo
//                            ))
//                        }
//
//                        if (itemResultCarousel != null) {
//                            binding.llayoutCarousel.visibility = View.VISIBLE
//                        }
//                    }
//
//                    arrayCarousel.addAll(itemListCarousel)
//                }
//
//                itemWeek.add(itemList)
//            }
//        }
//
//        binding.blank.root.visibility = View.GONE
//        binding.rviewBest.visibility = View.VISIBLE
//        adapter?.notifyDataSetChanged()
//
//        if (arrayCarousel.size > 0) {
//            with(binding) {
////                carousel.pageCount = arrayCarousel.size
////                carousel.slideInterval = 4000
//                binding.llayoutCarousel.visibility = View.VISIBLE
//            }
//        } else {
//            binding.llayoutCarousel.visibility = View.GONE
//        }
//
//        binding.blank.root.visibility = View.GONE
//        binding.llayoutWrap.visibility = View.VISIBLE
//        adapter?.notifyDataSetChanged()
//    }
}