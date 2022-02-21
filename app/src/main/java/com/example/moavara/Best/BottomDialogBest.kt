package com.example.moavara.Best

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialogBest(private val mContext: Context, private val item: BookListDataBestToday?) :
    BottomSheetDialogFragment() {

    private var iviewBookimg: ImageView? = null
    private var iviewRanking: ImageView? = null
    private var tviewTitle: TextView? = null
    private var tviewWriter: TextView? = null
    private var tviewChaptercnt: TextView? = null
    private var tviewOption1: TextView? = null
    private var tviewOption2: TextView? = null
    private var tviewOption3: TextView? = null
    private var tviewIntro: TextView? = null
    private var llayoutBtn: LinearLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.bottom_dialog_best, container, false)

        iviewBookimg = v.findViewById(R.id.iview_BookImg)
        iviewRanking = v.findViewById(R.id.iview_Ranking)
        tviewTitle = v.findViewById(R.id.tview_Title)
        tviewWriter = v.findViewById(R.id.tview_Writer)
        tviewChaptercnt = v.findViewById(R.id.tview_ChapterCnt)
        tviewOption1 = v.findViewById(R.id.tview_Option1)
        tviewOption2 = v.findViewById(R.id.tview_Option2)
        tviewOption3 = v.findViewById(R.id.tview_Option3)
        tviewIntro = v.findViewById(R.id.tview_Intro)
        llayoutBtn = v.findViewById(R.id.llayout_Btn)

        tviewTitle!!.text = item!!.title
        tviewWriter!!.text = item.writer
        tviewChaptercnt!!.text = "총 " + item.info2 + "편"
        tviewOption1!!.text = item.info3
        tviewOption2!!.text = item.info4
        tviewOption3!!.text = item.info5
        tviewIntro!!.text = item.info1

        Glide.with(mContext)
            .load(item.bookImg)
            .into(iviewBookimg!!)

        val position = item.number?.minus(1)

        when (position) {
            0 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_1)
            }
            1 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_2)
            }
            2 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_3)
            }
            3 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_4)
            }
            4 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_5)
            }
            5 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_6)
            }
            6 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_7)
            }
            7 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_8)
            }
            8 -> {
                iviewRanking!!.setImageResource(R.drawable.icon_best_9)
            }
            else -> {
                Log.d("bestRankImage","NO_IMAGE")
            }
        }

        llayoutBtn!!.setOnClickListener {
            val url = "https://www.joara.com/book/" + item.bookCode
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        return v
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}