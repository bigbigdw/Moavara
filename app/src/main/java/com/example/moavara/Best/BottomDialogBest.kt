package com.example.moavara.Best

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.BottomDialogBestBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList

class BottomDialogBest(
    private val mContext: Context,
    private val item: BookListDataBestToday?,
    private val tabType: String?,
    private val cate: String?,
) :
    BottomSheetDialogFragment() {

    private lateinit var dbWeekList: DataBaseBestDay

    private var _binding: BottomDialogBestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomDialogBestBinding.inflate(inflater, container, false)
        val view = binding.root

        dbWeekList = Room.databaseBuilder(
            requireContext().applicationContext,
            DataBaseBestDay::class.java,
            "week-list"
        )
            .allowMainThreadQueries().build()

        with(binding) {

            tviewTitle.text = item!!.title
            tviewWriter.text = item.writer
            tviewChapterCnt.text = "총 " + item.info2 + "편"
            tviewOption1.text = item.info3
            tviewOption2.text = item.info4
            tviewOption3.text = item.info5
            tviewIntro.text = item.info1

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

            llayoutBtn.setOnClickListener {
                val url = "https://www.joara.com/book/" + item.bookCode
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        return view
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    private fun ranklist() {

        Log.d("$$$$", DBDate.DayWeek()[0])

        val rank = ArrayList<String>()

        BestRef.setBestRefWeekList(tabType!!, cate!!).get().addOnSuccessListener {

            for (i in it.children) {

                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)

                if (group!!.title == item!!.title) {
                    Log.d(
                        "!!!!",
                        group.title.toString() + " " + group.status.toString() + " " + group.date.toString() + " " + group.number.toString()
                    )

                    rank.add(group.number!!.toString())

                    with(binding) {
                        if (DBDate.DayWeek()[0] == group.date) {
                            tviewRank1.visibility = View.VISIBLE
                            iviewRank1.setImageResource(R.drawable.ic_best_vt_24px)
                            tviewRank1.text = group.number.toString()
                        } else if (DBDate.DayWeek()[1] == group.date) {
                            tviewRank2.visibility = View.VISIBLE
                            iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                            tviewRank2.text = group.number.toString()
                        } else if (DBDate.DayWeek()[2] == group.date) {
                            tviewRank3.visibility = View.VISIBLE
                            iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                            tviewRank3.text = group.number.toString()
                        } else if (DBDate.DayWeek()[3] == group.date) {
                            tviewRank4.visibility = View.VISIBLE
                            iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                            tviewRank4.text = group.number.toString()
                        } else if (DBDate.DayWeek()[4] == group.date) {
                            tviewRank5.visibility = View.VISIBLE
                            iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                            tviewRank5.text = group.number.toString()
                        } else if (DBDate.DayWeek()[5] == group.date) {
                            tviewRank6.visibility = View.VISIBLE
                            iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                            tviewRank6.text = group.number.toString()
                        } else if (DBDate.DayWeek()[6] == group.date) {
                            tviewRank7.visibility = View.VISIBLE
                            iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                            tviewRank7.text = group.number.toString()
                        }
                    }


                }




            }

            Log.d("@@@@", rank.toString())

        }.addOnFailureListener {}

        val weeklist = dbWeekList.bestDao().getRank(item!!.title!!)

        for (i in weeklist.indices) {

        }


    }
}