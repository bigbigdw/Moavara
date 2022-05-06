package com.example.moavara.Best

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.WeekendDate
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.BottomDialogBestBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialogBest(
    private val mContext: Context,
    private val item: BookListDataBestToday,
    private val tabType: String,
    private val cate: String,
    private val pos : Int
) :
    BottomSheetDialogFragment() {

    private lateinit var dbEvent: DataBaseBestDay

    private var _binding: BottomDialogBestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomDialogBestBinding.inflate(inflater, container, false)
        val view = binding.root

        dbEvent = Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "pick-novel")
            .allowMainThreadQueries().build()

        with(binding) {

            tviewTitle.text = item.title
            tviewWriter.text = item.writer

            if (tabType == "MrBlue") {
                tviewInfo1.visibility = View.GONE
                tviewInfo2.visibility = View.GONE
                tviewInfo3.visibility = View.GONE
                tviewInfo4.visibility = View.GONE
                tviewInfo5.visibility = View.GONE
            } else if (tabType == "Kakao Stage" || tabType == "Naver Today" || tabType == "Naver Challenge" || tabType == "Naver") {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.GONE

                tviewInfo1.text = item.info1
                tviewInfo2.text = item.info2
                tviewInfo3.text = item.info3
                tviewInfo4.text = item.info4
            } else if (tabType == "Ridi" || tabType == "OneStore") {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.GONE
                tviewInfo5.visibility = View.GONE

                tviewInfo1.text = item.info1
                tviewInfo2.text = item.info2
                tviewInfo3.text = item.info3
            } else if (tabType == "Kakao" || tabType == "Joara" || tabType == "Joara Premium" || tabType == "Joara Nobless") {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.VISIBLE

                tviewInfo1.text = item.info1
                tviewInfo2.text = item.info2
                tviewInfo3.text = item.info3
                tviewInfo4.text = item.info4
                tviewInfo5.text = item.info5
            }

            Glide.with(mContext)
                .load(item.bookImg)
                .into(iviewBookImg)

            when (item.number) {
                0 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_1)
                }
                1 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_2)
                }
                2 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_3)
                }
                3 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_4)
                }
                4 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_5)
                }
                5 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_6)
                }
                6 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_7)
                }
                7 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_8)
                }
                8 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_9)
                }
                else -> {
                    Log.d("bestRankImage", "NO_IMAGE")
                }
            }

            ranklist()

            llayoutBtnLeft.setOnClickListener {
                dbEvent.bestDao().insert(
                    DataBestDay(
                        item.writer,
                        item.title,
                        item.bookImg,
                        item.bookCode,
                        item.info1,
                        item.info2,
                        item.info3,
                        item.info4,
                        item.info5,
                        item.number,
                        item.numberDiff,
                        item.date,
                        tabType,
                        ""
                    )
                )
                Toast.makeText(requireContext(), "Pick 성공!", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            lviewDetail.setOnClickListener {
                val bookDetailIntent = Intent(mContext, ActivityBestDetail::class.java)
                bookDetailIntent.putExtra("BookCode", String.format("%s", item.bookCode))
                bookDetailIntent.putExtra("Type", String.format("%s", tabType))
                startActivity(bookDetailIntent)
                dismiss()
            }

            llayoutBtnRight.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl()))
                startActivity(intent)
            }
        }

        return view
    }

    private fun getUrl(): String {

        return if (tabType == "MrBlue") {
            "https://www.mrblue.com/novel/" + item.bookCode
        } else if (tabType == "Naver Today") {
            item.bookCode
        } else if (tabType == "Naver Challenge") {
            item.bookCode
        } else if (tabType == "Naver") {
            item.bookCode
        } else if (tabType == "Kakao Stage") {
            "https://pagestage.kakao.com/novels/" + item.bookCode
        } else if (tabType == "Kakao") {
            "https://page.kakao.com/home?seriesId=" + item.bookCode
        } else if (tabType == "OneStore") {
            "https://onestory.co.kr/detail/" + item.bookCode
        } else if (tabType == "Ridi") {
            item.bookCode
        } else if (tabType == "Joara" || tabType == "Joara Premium" || tabType == "Joara Nobless") {
            "https://www.joara.com/book/" + item.bookCode
        } else ""

    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    private fun ranklist() {

        val rank = ArrayList<String>()

        BestRef.setBestRefWeekList(tabType, cate).get().addOnSuccessListener {
            for (i in it.children) {

                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)

                if (group!!.title == item.title) {

                    rank.add(group.number.toString())

                    mRootRef.child("Week").get().addOnSuccessListener {

                        val week: WeekendDate? = it.getValue(WeekendDate::class.java)

                        with(binding.includeRank) {
                            when {
                                week!!.sun == group.date -> {
                                    tviewRank1.visibility = View.VISIBLE
                                    iviewRank1.setImageResource(R.drawable.ic_best_vt_24px)
                                    tviewRank1.text = (group.number + 1).toString()
                                }
                                week.mon == group.date -> {
                                    tviewRank2.visibility = View.VISIBLE
                                    iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                                    tviewRank2.text = (group.number + 1).toString()
                                }
                                week.tue == group.date -> {
                                    tviewRank3.visibility = View.VISIBLE
                                    iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                                    tviewRank3.text = (group.number + 1).toString()

                                }
                                week.wed == group.date -> {
                                    tviewRank4.visibility = View.VISIBLE
                                    iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                                    tviewRank4.text = (group.number + 1).toString()
                                }
                                week.thur == group.date -> {
                                    tviewRank5.visibility = View.VISIBLE
                                    iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                                    tviewRank5.text = (group.number + 1).toString()

                                }
                                week.fri == group.date -> {
                                    tviewRank6.visibility = View.VISIBLE
                                    iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                                    tviewRank6.text = (group.number + 1).toString()

                                }
                                week.sat == group.date -> {
                                    tviewRank7.visibility = View.VISIBLE
                                    iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                                    tviewRank7.text = (group.number + 1).toString()
                                }
                            }

                            when {
                                week!!.sun == DBDate.DateMMDD() -> {
                                    tviewRank1.visibility = View.VISIBLE
                                    iviewRank1.setImageResource(R.drawable.ic_best_gn_24px)
                                    tviewRank1.text = (pos + 1).toString()
                                }
                                week.mon == DBDate.DateMMDD() -> {
                                    tviewRank2.visibility = View.VISIBLE
                                    iviewRank2.setImageResource(R.drawable.ic_best_gn_24px)
                                    tviewRank2.text = (pos + 1).toString()
                                }
                                week.tue == DBDate.DateMMDD() -> {
                                    tviewRank3.visibility = View.VISIBLE
                                    iviewRank3.setImageResource(R.drawable.ic_best_gn_24px)
                                    tviewRank3.text = (pos + 1).toString()
                                }
                                week.wed == DBDate.DateMMDD() -> {
                                    tviewRank4.visibility = View.VISIBLE
                                    iviewRank4.setImageResource(R.drawable.ic_best_gn_24px)
                                    tviewRank4.text = (pos + 1).toString()
                                }
                                week.thur == DBDate.DateMMDD() -> {
                                    tviewRank5.visibility = View.VISIBLE
                                    iviewRank5.setImageResource(R.drawable.ic_best_gn_24px)
                                    tviewRank5.text = (pos + 1).toString()
                                }
                                week.fri == DBDate.DateMMDD() -> {
                                    tviewRank6.visibility = View.VISIBLE
                                    iviewRank6.setImageResource(R.drawable.ic_best_gn_24px)
                                    tviewRank6.text = (pos + 1).toString()
                                }
                                week.sat == DBDate.DateMMDD() -> {
                                    tviewRank7.visibility = View.VISIBLE
                                    iviewRank7.setImageResource(R.drawable.ic_best_gn_24px)
                                    tviewRank7.text = (pos + 1).toString()
                                }
                            }
                        }
                    }.addOnFailureListener{}

                }
            }

        }.addOnFailureListener {}
    }
}