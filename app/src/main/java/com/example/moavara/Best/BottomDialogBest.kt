package com.example.moavara.Best

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Search.BookListDataBestAnalyze
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.applyingTextColor
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.BottomDialogBestBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit

class BottomDialogBest(
    private val mContext: Context,
    private val item: BookListDataBest?,
    private val platform: String,
    private val pos: Int,
    private val UserInfo: DataBaseUser
) :
    BottomSheetDialogFragment() {

    var bookCodeItems = ArrayList<BookListDataBestAnalyze>()

    private var _binding: BottomDialogBestBinding? = null
    private val binding get() = _binding!!
    private var isPicked = false
    private var isFirstPick = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomDialogBestBinding.inflate(inflater, container, false)
        val view = binding.root

        val Novel = mRootRef.child("User").child(UserInfo.UID).child("Novel")

        mRootRef.child("User").child(UserInfo.UID).child("Novel").child("book").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(!dataSnapshot.exists()){
                    isFirstPick = true
                }

                for(pickedItem in dataSnapshot.children){

                    if(pickedItem.key.toString() == item?.bookCode){
                        isPicked = true
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#A7ACB7"))
                            shape = GradientDrawable.RECTANGLE
                        }

                        binding.tviewPick.text = "Pick 완료"
                        break
                    } else {
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#621CEF"))
                            shape = GradientDrawable.RECTANGLE
                        }

                        binding.tviewPick.text = "Pick"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })



        with(binding) {

            llayoutWrap.background = GradientDrawable().apply {
                setColor(Color.parseColor("#26292E"))
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(
                    20f.dpToPx(),
                    20f.dpToPx(),
                    20f.dpToPx(),
                    20f.dpToPx(),
                    0f,
                    0f,
                    0f,
                    0f
                )
            }

            viewDeco.background = GradientDrawable().apply {
                setColor(Color.parseColor("#404551"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 50f.dpToPx()
            }

            tviewTitle.text = item?.title ?: ""
            tviewWriter.text = item?.writer ?: ""

            if(platform == "MrBlue"){
                llayoutBtnDetail.visibility = View.GONE
            }else if(platform == "Toksoda"){

                tviewInfo1.text = item?.info2 ?: ""

                val info3 = SpannableStringBuilder("조회 수 : ${item?.info3}" )
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info5 = SpannableStringBuilder("선호작 수 : ${item?.info5}")
                info5.applyingTextColor(
                    "선호작 수 : ",
                    "#6E7686"
                )

                tviewInfo3.text = info3
                tviewInfo4.text = info5
                tviewInfo5.text = item?.info1 ?: ""
            } else if (platform == "Naver" || platform == "Naver_Today" || platform == "Naver_Challenge") {
                tviewInfo1.text = item?.info1 ?: ""

                val info3 = SpannableStringBuilder("별점 수 : ${item?.info3?.replace("별점", "")}")
                info3.applyingTextColor(
                    "별점 수 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder("조회 수 : ${item?.info4?.replace("조회", "조회 수 : ")}" )
                info4.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info5 = SpannableStringBuilder("관심 : ${item?.info5?.replace("관심", "관심 : ")}")
                info5.applyingTextColor(
                    "관심 : ",
                    "#6E7686"
                )

                tviewInfo2.text = info3
                tviewInfo3.text = info4
                tviewInfo4.text = info5
            }  else if (platform == "Kakao_Stage") {
                tviewInfo1.text = item?.info2 ?: ""

                val info3 = SpannableStringBuilder("조회 수 : ${item?.info3?.replace("별점", "별점 : ")}" )
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder("선호작 수 : ${item?.info4?.replace("조회", "조회 수 : ")}" )
                info4.applyingTextColor(
                    "선호작 수 : ",
                    "#6E7686"
                )

                tviewInfo3.text = info3
                tviewInfo4.text = info4
                tviewInfo5.text = item?.info1 ?: ""
            } else if (platform == "Ridi") {
                tviewInfo1.text = item?.info1 ?: ""

                val info3 = SpannableStringBuilder("추천 수 : ${item?.info3}" )
                info3.applyingTextColor(
                    "추천 수 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder("평점 : ${item?.info4}")
                info4.applyingTextColor(
                    "평점 : ",
                    "#6E7686"
                )

                tviewInfo3.text = info3
                tviewInfo4.text = info4
            } else if (platform == "OneStore") {

                val info3 = SpannableStringBuilder("조회 수 : ${item?.info3?.replace("별점", "별점 : ")}" )
                info3.applyingTextColor(
                    "조회 수 : ",
                    "#6E7686"
                )

                val info4 = SpannableStringBuilder("평점 : ${item?.info4?.replace("조회", "조회 수 : ")}" )
                info4.applyingTextColor(
                    "평점 : ",
                    "#6E7686"
                )

                val info5 = SpannableStringBuilder("댓글 수 : ${item?.info5?.replace("관심", "관심 : ")}" )
                info5.applyingTextColor(
                    "댓글 수 : ",
                    "#6E7686"
                )

                tviewInfo2.text = info3
                tviewInfo3.text = info4
                tviewInfo4.text = info5
            } else if (platform == "Kakao" || platform == "Munpia" || platform == "Toksoda" || platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless" || platform == "Munpia" ) {

                if(platform == "Joara" || platform == "Joara_Premium" || platform == "Joara_Nobless"){
                    tviewInfo1.text = item?.info2 ?: ""

                    val info3 = SpannableStringBuilder("조회 수 : ${item?.info3}")
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("선호작 수 : ${item?.info4}")
                    info4.applyingTextColor(
                        "선호작 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder("추천 수 : ${item?.info5}")
                    info5.applyingTextColor(
                        "추천 수 : ",
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5

                    tviewInfo5.text = item?.info1 ?: ""
                } else if(platform == "Kakao"){
                    tviewInfo1.text = item?.info2 ?: ""

                    val info3 = SpannableStringBuilder("조회 수 : ${item?.info3}")
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("추천 수 : ${item?.info4}")
                    info4.applyingTextColor(
                        "추천 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder("평점 : ${item?.info5}")
                    info5.applyingTextColor(
                        "평점 : ",
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5
                    tviewInfo5.text = item?.info1 ?: ""
                } else if(platform == "Munpia"){
                    tviewInfo1.text = item?.info2 ?: ""

                    val info3 = SpannableStringBuilder("조회 수 : ${item?.info3}")
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("방문 수 : ${item?.info4}")
                    info4.applyingTextColor(
                        "방문 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder("선호작 수 : ${item?.info5}")
                    info5.applyingTextColor(
                        "선호작 수 : ",
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5
                    tviewInfo5.text = item?.info1 ?: ""
                } else {
                    tviewInfo1.text = item?.info2 ?: ""
                    tviewInfo2.text = item?.info3
                    tviewInfo3.text = item?.info4 ?: ""
                    tviewInfo4.text = item?.info5
                    tviewInfo5.text = item?.info1 ?: ""
                }
            }

            if (tviewInfo1.text.isNotEmpty()) {
                tviewInfo1.visibility = View.VISIBLE
                tviewBar.visibility = View.VISIBLE
            }
            if (tviewInfo2.text.isNotEmpty()) {
                tviewInfo2.visibility = View.VISIBLE
            }
            if (tviewInfo3.text.isNotEmpty()) {
                tviewInfo3.visibility = View.VISIBLE
            }
            if (tviewInfo4.text.isNotEmpty()) {
                tviewInfo4.visibility = View.VISIBLE
            }
            if (tviewInfo5.text.isNotEmpty()) {
                tviewInfo5.visibility = View.VISIBLE
            }

            Glide.with(mContext)
                .load(item?.bookImg)
                .into(iviewBookImg)

            getRankList(item)

            llayoutBtnDetail.setOnClickListener {
                val bookDetailIntent = Intent(mContext, ActivityBestDetail::class.java)
                bookDetailIntent.putExtra("BookCode",
                    item?.let { it1 -> String.format("%s", it1.bookCode) })
                bookDetailIntent.putExtra("Type", String.format("%s", platform))
                bookDetailIntent.putExtra("POSITION", pos)
                item?.let { it1 -> bookDetailIntent.putExtra("COUNT", it1.number) }
                bookDetailIntent.putExtra("HASDATA", true)
                startActivity(bookDetailIntent)
                dismiss()
            }

            binding.llayoutPick.setOnClickListener {

                if(isPicked){
                    Novel.child("book").child(item?.bookCode ?: "").removeValue()

                    binding.llayoutPick.background = GradientDrawable().apply {
                        setColor(Color.parseColor("#621CEF"))
                        shape = GradientDrawable.RECTANGLE
                    }

                    binding.tviewPick.text = "Pick"
                    Toast.makeText(requireContext(), "[${item?.title}]이(가) 마이픽에서 제거되었습니다.", Toast.LENGTH_SHORT).show()
                    dismiss()

                } else {

                    val group = item?.let { it1 ->
                        BookListDataBest(
                            it1.writer,
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
                            platform,
                            ""
                        )
                    }

                    if(isFirstPick){
                        isFirstPick = false

                        val inputData = Data.Builder()
                            .putString(FirebaseWorkManager.TYPE, "PICK")
                            .putString(FirebaseWorkManager.UID, UserInfo.UID)
                            .putString(FirebaseWorkManager.USER, UserInfo.Nickname)
                            .build()

                        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
                        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(6, TimeUnit.HOURS)
                            .setBackoffCriteria(
                                BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                                TimeUnit.MILLISECONDS
                            )
                            .addTag("MoavaraPick")
                            .setInputData(inputData)
                            .build()

                        val workManager = WorkManager.getInstance(requireContext().applicationContext)

                        workManager.enqueueUniquePeriodicWork(
                            "MoavaraPick",
                            ExistingPeriodicWorkPolicy.KEEP,
                            workRequest
                        )
                        FirebaseMessaging.getInstance().subscribeToTopic(UserInfo.UID)

                        Novel.child("book").child(item?.bookCode ?: "").setValue(group)
                        Novel.child("bookCode").child(item?.bookCode ?: "").setValue(bookCodeItems)
                        mRootRef.child("User").child(UserInfo.UID).child("Mining").setValue(true)

                        Toast.makeText(requireContext(), "[${group?.title}]이(가) 마이픽에 등록되었습니다.", Toast.LENGTH_SHORT).show()

                    } else {
                        Novel.child("book").child(item?.bookCode ?: "").setValue(group)
                        Novel.child("bookCode").child(item?.bookCode ?: "").setValue(bookCodeItems)

                        Toast.makeText(requireContext(), "[${group?.title}]이(가) 마이픽에 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            }
        }

        return view
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    private fun getRankList(item: BookListDataBest?) {

        if (item != null) {
            BestRef.getBookCode(item.type, UserInfo.Genre).child(item.bookCode)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (keyItem in dataSnapshot.children) {
                            val group: BookListDataBestAnalyze? =
                                keyItem.getValue(BookListDataBestAnalyze::class.java)

                            if (group != null) {
                                bookCodeItems.add(group)
                            }

                            with(binding.includeRank) {
                                val itemDate = group?.let { DBDate.getDateData(it.date) }

                                if (itemDate != null) {
                                    if (itemDate.week == DBDate.Week().toInt()) {
                                        when {
                                            itemDate.date == 1 -> {
                                                tviewRank1.visibility = View.VISIBLE

                                                if (itemDate.date == DBDate.DayInt()) {
                                                    iviewRank1.setImageResource(R.drawable.ic_best_gn_24px)
                                                } else {
                                                    iviewRank1.setImageResource(R.drawable.ic_best_vt_24px)
                                                }
                                                tviewRank1.text = (group.number + 1).toString()
                                            }
                                            itemDate.date == 2 -> {
                                                tviewRank2.visibility = View.VISIBLE
                                                if (itemDate.date == DBDate.DayInt()) {
                                                    iviewRank2.setImageResource(R.drawable.ic_best_gn_24px)
                                                } else {
                                                    iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                                                }
                                                tviewRank2.text = (group.number + 1).toString()
                                            }
                                            itemDate.date == 3 -> {
                                                tviewRank3.visibility = View.VISIBLE
                                                if (itemDate.date == DBDate.DayInt()) {
                                                    iviewRank3.setImageResource(R.drawable.ic_best_gn_24px)
                                                } else {
                                                    iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                                                }
                                                tviewRank3.text = (group.number + 1).toString()
                                            }
                                            itemDate.date == 4 -> {
                                                tviewRank4.visibility = View.VISIBLE
                                                if (itemDate.date == DBDate.DayInt()) {
                                                    iviewRank4.setImageResource(R.drawable.ic_best_gn_24px)
                                                } else {
                                                    iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                                                }
                                                tviewRank4.text = (group.number + 1).toString()
                                            }
                                            itemDate.date == 5 -> {
                                                tviewRank5.visibility = View.VISIBLE
                                                if (itemDate.date == DBDate.DayInt()) {
                                                    iviewRank5.setImageResource(R.drawable.ic_best_gn_24px)
                                                } else {
                                                    iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                                                }
                                                tviewRank5.text = (group.number + 1).toString()
                                            }
                                            itemDate.date == 6 -> {
                                                tviewRank6.visibility = View.VISIBLE
                                                if (itemDate.date == DBDate.DayInt()) {
                                                    iviewRank6.setImageResource(R.drawable.ic_best_gn_24px)
                                                } else {
                                                    iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                                                }
                                                tviewRank6.text = (group.number + 1).toString()
                                            }
                                            itemDate.date == 7 -> {
                                                tviewRank7.visibility = View.VISIBLE
                                                if (itemDate.date == DBDate.DayInt()) {
                                                    iviewRank7.setImageResource(R.drawable.ic_best_gn_24px)
                                                } else {
                                                    iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                                                }
                                                tviewRank7.text = (group.number + 1).toString()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }
}