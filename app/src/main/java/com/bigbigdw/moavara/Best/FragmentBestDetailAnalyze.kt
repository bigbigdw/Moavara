package com.bigbigdw.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigbigdw.moavara.R
//import com.example.moavara.Search.BestLineChart
import com.bigbigdw.moavara.Search.BookListDataBestAnalyze
import com.bigbigdw.moavara.databinding.FragmentBestDetailAnalyzeBinding
//import com.github.mikephil.charting.data.Entry

class FragmentBestDetailAnalyze(
    private val platform: String,
    private val BookItem: ArrayList<BookListDataBestAnalyze>,
    private var hasBookData: Boolean,
) : Fragment() {

    private var _binding: FragmentBestDetailAnalyzeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mInnerFragmentBestDetailRank: InnerFragmentBestDetailRank
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

        if(hasBookData){
            var isOnlyPick = false

            for(i in BookItem){
                if(i.number > 100){
                    isOnlyPick = true
                    break
                }
            }

            if(!isOnlyPick && BookItem.size < 1){
                mInnerFragmentBestDetailRank = InnerFragmentBestDetailRank(BookItem)
                childFragmentManager.commit {
                    replace(R.id.InnerFragmentBestDetailRank, mInnerFragmentBestDetailRank)
                }
            }
        }

        mInnerFragmentBestDetailLine = InnerFragmentBestDetailLine(platform, BookItem)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailLine, mInnerFragmentBestDetailLine)
        }

        return view
    }

    private fun getAnalyzeJoara() {
//        val adapterChartJoara: AdapterLine?
//        val itemsJoara = ArrayList<BestLineChart>()
//
//        adapterChartJoara = AdapterLine(requireContext(), itemsJoara)
        binding.rViewChartJoara.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.rViewChartJoara.adapter = adapterChartJoara

//        val chapter = (context as ActivityBestDetail).chapter
//        val dateList = mutableListOf<String>()
//        val entryList2 = mutableListOf<Entry>()
//        val entryList3 = mutableListOf<Entry>()
//        val entryList: ArrayList<Entry> = ArrayList()
//        var num = 0
//
//        if (chapter != null) {
//            for (i in chapter.indices) {
//                dateList.add(chapter[i].sortno + "화")
//                entryList.add(Entry(num.toFloat(), chapter[i].cnt_comment.toFloat()))
//                entryList2.add(Entry(num.toFloat(), chapter[i].cnt_page_read.toFloat()))
//                entryList3.add(Entry(num.toFloat(), chapter[i].cnt_recom.toFloat()))
//                num += 1
//            }
//
//            itemsJoara.add(BestLineChart(dateList, entryList, "편당 댓글 수", "#621CEF"))
//            itemsJoara.add(BestLineChart(dateList, entryList2, "편당 조회 수", "#621CEF"))
//            itemsJoara.add(BestLineChart(dateList, entryList3, "편당 추천 수", "#621CEF"))
//            adapterChartJoara.notifyDataSetChanged()
//        }
    }
}