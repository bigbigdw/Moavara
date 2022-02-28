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
import com.example.moavara.databinding.BottomDialogBestBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class BottomDialogBest(private val mContext: Context, private val item: BookListDataBestToday?) :
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

        dbWeekList = Room.databaseBuilder(requireContext().applicationContext, DataBaseBestDay::class.java, "week-list")
            .allowMainThreadQueries().build()

        val today = Date()

        val sdf = SimpleDateFormat("EEEE")
        val dayOfTheWeek = sdf.format(today)

        Log.d("@@@@-@", dayOfTheWeek)


        with(binding){

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
                    Log.d("bestRankImage","NO_IMAGE")
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

    private fun ranklist(){

        val weeklist = dbWeekList.bestDao().getRank(item!!.title!!)

        for(i in weeklist.indices){
            Log.d("!!!!",  weeklist[i].title.toString() + "@" + weeklist[i].status.toString() + "@@" + weeklist[i].date.toString() + "@@@" + weeklist[i].number.toString())
        }

        with(binding){

        }
    }
}