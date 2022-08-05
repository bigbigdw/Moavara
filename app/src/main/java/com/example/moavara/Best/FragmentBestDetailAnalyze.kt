package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.R
import com.example.moavara.Search.BestChart
import com.example.moavara.Soon.Best.AdapterChart
import com.example.moavara.Soon.Best.InnerFragmentBestDetailBar
import com.example.moavara.databinding.FragmentBestDetailAnalyzeBinding
import com.github.mikephil.charting.data.BarEntry
import java.util.*

class FragmentBestDetailAnalyze(
    private val platform: String,
    private val BookItem: ArrayList<BookListDataBestAnalyze>,
    private var itemCount: Int,
) : Fragment() {

    private var _binding: FragmentBestDetailAnalyzeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mInnerFragmentBestDetailRank: InnerFragmentBestDetailRank
    private lateinit var mInnerFragmentBestDetailRadar: InnerFragmentBestDetailRadar
    private lateinit var mInnerFragmentBestDetailLine: InnerFragmentBestDetailLine

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailAnalyzeBinding.inflate(inflater, container, false)
        val view = binding.root

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            binding.rViewChartJoara.visibility = View.VISIBLE
            getAnalyzeJoara()
        }

        mInnerFragmentBestDetailRank = InnerFragmentBestDetailRank(BookItem)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailRank, mInnerFragmentBestDetailRank)
        }

        mInnerFragmentBestDetailRadar = InnerFragmentBestDetailRadar(BookItem, itemCount, platform)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailRadar, mInnerFragmentBestDetailRadar)
        }

        mInnerFragmentBestDetailLine = InnerFragmentBestDetailLine(platform, BookItem)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailLine, mInnerFragmentBestDetailLine)
        }

        return view
    }

    private fun getAnalyzeJoara() {
        val adapterChartJoara: AdapterChart?
        val itemsJoara = ArrayList<BestChart>()

        adapterChartJoara = AdapterChart(itemsJoara)
        binding.rViewChartJoara.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewChartJoara.adapter = adapterChartJoara

        val chapter = (context as ActivityBestDetail).chapter
        val dateList = mutableListOf<String>()
        val entryList2 = mutableListOf<BarEntry>()
        val entryList3 = mutableListOf<BarEntry>()
        val entryList: ArrayList<BarEntry> = ArrayList()
        var num = 0

        if (chapter != null) {
            for (i in chapter.indices) {
                dateList.add(chapter[i].sortno + "화")
                entryList.add(BarEntry(num.toFloat(), chapter[i].cnt_comment.toFloat()))
                entryList2.add(BarEntry(num.toFloat(), chapter[i].cnt_page_read.toFloat()))
                entryList3.add(BarEntry(num.toFloat(), chapter[i].cnt_recom.toFloat()))
                num += 1
            }

            itemsJoara.add(BestChart(dateList, entryList, "편당 댓글 수", "#20459e"))
            itemsJoara.add(BestChart(dateList, entryList2, "편당 조회 수", "#20459e"))
            itemsJoara.add(BestChart(dateList, entryList3, "편당 추천 수", "#20459e"))
            adapterChartJoara.notifyDataSetChanged()
        }
    }
}