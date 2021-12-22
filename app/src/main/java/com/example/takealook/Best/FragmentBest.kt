package com.example.takealook.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.takealook.R


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
    ): View? {
        root = inflater.inflate(R.layout.fragment_best, container, false)

        //프래그먼트 관련
        llayout_Search_Fragment = root.findViewById(R.id.llayout_Search_Fragment)

        tviewToday = root.findViewById(R.id.tview_Today)
        tviewWeekend = root.findViewById(R.id.tview_Weekend)
        tviewBest = root.findViewById(R.id.tview_Best)

        tviewToday!!.visibility = View.GONE

        mFragmentBestToday = FragmentBestToday()
        parentFragmentManager.beginTransaction()
            .replace(R.id.llayout_Search_Fragment, mFragmentBestToday!!)
            .commit()

        tviewToday!!.setOnClickListener {

            tviewToday!!.visibility = View.GONE
            tviewWeekend!!.visibility = View.VISIBLE
            tviewBest!!.visibility = View.VISIBLE

            mFragmentBestToday = FragmentBestToday()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayout_Search_Fragment, mFragmentBestToday!!)
                .commit()
        }

        tviewWeekend!!.setOnClickListener {

            tviewWeekend!!.visibility = View.GONE
            tviewToday!!.visibility = View.VISIBLE
            tviewBest!!.visibility = View.VISIBLE

            mFragmentBestWeekend = FragmentBestWeekend()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayout_Search_Fragment, mFragmentBestWeekend!!)
                .commit()
        }

        tviewBest!!.setOnClickListener {

            tviewBest!!.visibility = View.GONE
            tviewToday!!.visibility = View.VISIBLE
            tviewWeekend!!.visibility = View.VISIBLE

            mFragmentBestMonth = FragmentBestMonth()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayout_Search_Fragment, mFragmentBestMonth!!)
                .commit()
        }

        return root
    }

}