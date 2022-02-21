package com.example.moavara.Best.Before

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.moavara.R


class FragmentBest : Fragment() {

    lateinit var root: View

    private var llayout_Search_Fragment: LinearLayout? = null
    private var mFragmentBestToday: FragmentBestToday? = null
    private var mFragmentBestWeekend: FragmentBestWeekend? = null
    private var mFragmentBestMonth: FragmentBestMonth? = null

    private var tviewToday: TextView? = null
    private var tviewWeekend: TextView? = null
    private var tviewBest: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best, container, false)

        //프래그먼트 관련
        llayout_Search_Fragment = root.findViewById(R.id.llayoutWrap)

        tviewToday = root.findViewById(R.id.tview_Today)
        tviewWeekend = root.findViewById(R.id.tview_Weekend)
        tviewBest = root.findViewById(R.id.tview_Best)

        tviewToday!!.setTextColor(Color.parseColor("#ffffff"))

        mFragmentBestToday = FragmentBestToday()
        parentFragmentManager.beginTransaction()
            .replace(R.id.llayoutWrap, mFragmentBestToday!!)
            .commit()

        tviewToday!!.setOnClickListener {

            tviewToday!!.setTextColor(Color.parseColor("#ffffff"))
            tviewWeekend!!.setTextColor(Color.parseColor("#6D6F76"))
            tviewBest!!.setTextColor(Color.parseColor("#6D6F76"))

            mFragmentBestToday = FragmentBestToday()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayoutWrap, mFragmentBestToday!!)
                .commit()
        }

        tviewWeekend!!.setOnClickListener {

            tviewWeekend!!.setTextColor(Color.parseColor("#ffffff"))
            tviewToday!!.setTextColor(Color.parseColor("#6D6F76"))
            tviewBest!!.setTextColor(Color.parseColor("#6D6F76"))

            mFragmentBestWeekend = FragmentBestWeekend()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayoutWrap, mFragmentBestWeekend!!)
                .commit()
        }

        tviewBest!!.setOnClickListener {

            tviewBest!!.setTextColor(Color.parseColor("#ffffff"))
            tviewToday!!.setTextColor(Color.parseColor("#6D6F76"))
            tviewWeekend!!.setTextColor(Color.parseColor("#6D6F76"))

            mFragmentBestMonth = FragmentBestMonth()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayoutWrap, mFragmentBestMonth!!)
                .commit()
        }

        return root
    }



}