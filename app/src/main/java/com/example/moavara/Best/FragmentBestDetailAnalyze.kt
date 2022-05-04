package com.example.moavara.Best

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.BestChart
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestDetailAnalyzeBinding
import com.example.moavara.databinding.ItemBestDetailAnalysisBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*


class FragmentBestDetailAnalyze(private val platfrom: String, private val bookCode: String) :
    Fragment() {

    private var adapterChart: AdapterChart? = null
    private val items = ArrayList<BestChart>()

    private var _binding: FragmentBestDetailAnalyzeBinding? = null
    private val binding get() = _binding!!

    var status = ""
    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailAnalyzeBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()

        adapterChart = AdapterChart(items)
        binding.rViewChart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewChart.adapter = adapterChart

        if(platfrom == "Joara" || platfrom == "Joara Nobless" || platfrom == "Joara Premium"){
            getAnalyze()
            getAnalyzeJoara()
        }

        return view
    }

    fun getAnalyzeJoara(){
        var adapterChartJoara: AdapterChart?
        val itemsJoara = ArrayList<BestChart>()

        adapterChartJoara = AdapterChart(itemsJoara)
        binding.rViewChart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewChartJoara.adapter = adapterChartJoara

        var chapter = (context as ActivityBestDetail).chapter
        val dateList = mutableListOf<String>()
        val entryList = mutableListOf<BarEntry>()
        val entryList2 = mutableListOf<BarEntry>()
        val entryList3 = mutableListOf<BarEntry>()
        var num = 0

        Log.d("@@@@", chapter.toString())

        if (chapter != null) {
            for(i in chapter.indices){
                dateList.add(chapter[i].sortno)
                entryList.add(BarEntry(num.toFloat(), chapter[i].cnt_comment.toFloat()))
                entryList2.add(BarEntry(num.toFloat(), chapter[i].cnt_page_read.toFloat()))
                entryList3.add(BarEntry(num.toFloat(), chapter[i].cnt_recom.toFloat()))
            }
            itemsJoara.add(BestChart(dateList, entryList, "조회 수", "#6e2b93"))
            itemsJoara.add(BestChart(dateList, entryList2, "선호작 수", "#6e2b93"))
            itemsJoara.add(BestChart(dateList, entryList3, "추천 수", "#6e2b93"))
            adapterChartJoara!!.notifyDataSetChanged()
        }
    }

    private fun getAnalyze() {
        BestRef.setBestRefWeekList(platfrom, cate).get().addOnSuccessListener {
            val dateList = mutableListOf<String>()
            //BarEntry를 담는 리스트
            val entryList = mutableListOf<BarEntry>()
            val entryList2 = mutableListOf<BarEntry>()
            val entryList3 = mutableListOf<BarEntry>()
            val entryList4 = mutableListOf<Entry>()

            val sun = requireContext().getSharedPreferences("WEEK", AppCompatActivity.MODE_PRIVATE).getString("SUN", "")
            val mon = requireContext().getSharedPreferences("WEEK", AppCompatActivity.MODE_PRIVATE).getString("MON", "")
            val tue = requireContext().getSharedPreferences("WEEK", AppCompatActivity.MODE_PRIVATE).getString("TUE", "")
            val wed = requireContext().getSharedPreferences("WEEK", AppCompatActivity.MODE_PRIVATE).getString("WED", "")
            val thur = requireContext().getSharedPreferences("WEEK", AppCompatActivity.MODE_PRIVATE).getString("THUR", "")
            val fri = requireContext().getSharedPreferences("WEEK", AppCompatActivity.MODE_PRIVATE).getString("FRI", "")
            val sat = requireContext().getSharedPreferences("WEEK", AppCompatActivity.MODE_PRIVATE).getString("SAT", "")

            var num = 0

            for (i in it.children) {

                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)

                if (group!!.title == (context as ActivityBestDetail).bookTitle) {
                    dateList.add(group.date)

                    //BarEntry로 값 추가 후 리스트에 담는다
                    entryList.add(BarEntry(num.toFloat(),group.info3.replace("조회 수 : ", "").toFloat()))
                    entryList2.add(BarEntry(num.toFloat(),group.info4.replace("선호작 수 : ", "").toFloat()))
                    entryList3.add(BarEntry(num.toFloat(),group.info5.replace("추천 수 : ", "").toFloat()))
                    entryList4.add(Entry(num.toFloat(), group.number.toFloat()))


                    with(binding.includeRank) {
                        when {
                            sun == group.date -> {
                                tviewRank1.visibility = View.VISIBLE
                                iviewRank1.setImageResource(R.drawable.ic_best_vt_24px)
                                tviewRank1.text = (group.number + 1).toString()
                            }
                            mon == group.date -> {
                                tviewRank2.visibility = View.VISIBLE
                                iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                                tviewRank2.text = (group.number + 1).toString()
                            }
                            tue == group.date -> {
                                tviewRank3.visibility = View.VISIBLE
                                iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                                tviewRank3.text = (group.number + 1).toString()

                            }
                            wed == group.date -> {
                                tviewRank4.visibility = View.VISIBLE
                                iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                                tviewRank4.text = (group.number + 1).toString()
                            }
                            thur == group.date -> {
                                tviewRank5.visibility = View.VISIBLE
                                iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                                tviewRank5.text = (group.number + 1).toString()

                            }
                            fri == group.date -> {
                                tviewRank6.visibility = View.VISIBLE
                                iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                                tviewRank6.text = (group.number + 1).toString()
                            }
                            sat == group.date -> {
                                tviewRank7.visibility = View.VISIBLE
                                iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                                tviewRank7.text = (group.number + 1).toString()
                            }
                        }

                        when {
                            sun == DBDate.DateMMDD() -> {
                                tviewRank1.visibility = View.VISIBLE
                                iviewRank1.setImageResource(R.drawable.ic_best_gn_24px)
                                tviewRank1.text = (group.number + 1).toString()
                            }
                            mon == DBDate.DateMMDD() -> {
                                tviewRank2.visibility = View.VISIBLE
                                iviewRank2.setImageResource(R.drawable.ic_best_gn_24px)
                                tviewRank2.text = (group.number + 1).toString()
                            }
                            tue == DBDate.DateMMDD() -> {
                                tviewRank3.visibility = View.VISIBLE
                                iviewRank3.setImageResource(R.drawable.ic_best_gn_24px)
                                tviewRank3.text = (group.number + 1).toString()
                            }
                            wed == DBDate.DateMMDD() -> {
                                tviewRank4.visibility = View.VISIBLE
                                iviewRank4.setImageResource(R.drawable.ic_best_gn_24px)
                                tviewRank4.text = (group.number + 1).toString()
                            }
                            thur == DBDate.DateMMDD() -> {
                                tviewRank5.visibility = View.VISIBLE
                                iviewRank5.setImageResource(R.drawable.ic_best_gn_24px)
                                tviewRank5.text = (group.number + 1).toString()
                            }
                            fri == DBDate.DateMMDD() -> {
                                tviewRank6.visibility = View.VISIBLE
                                iviewRank6.setImageResource(R.drawable.ic_best_gn_24px)
                                tviewRank6.text = (group.number + 1).toString()
                            }
                            sat == DBDate.DateMMDD() -> {
                                tviewRank7.visibility = View.VISIBLE
                                iviewRank7.setImageResource(R.drawable.ic_best_gn_24px)
                                tviewRank7.text = (group.number + 1).toString()
                            }
                        }
                    }
                    num += 1
                }
            }

            Log.d("####", entryList3.toString())
            Log.d("####", entryList2.toString())

            items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
            items.add(BestChart(dateList, entryList2, "선호작 수", "#4971EF"))
            items.add(BestChart(dateList, entryList3, "추천 수", "#00d180"))
            adapterChart!!.notifyDataSetChanged()
        }.addOnFailureListener {}
    }
}



class AdapterChart(items: List<BestChart?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BestChart?>? = items as ArrayList<BestChart?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            ItemBestDetailAnalysisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.holder!![position]

            if (item != null) {

                holder.binding.barChart.xAxis.valueFormatter = object: ValueFormatter() {
                    override fun getFormattedValue(value: Float): String? {
                        return item.dateList?.get(value.toInt())
                    }
                }

                val barDataSet = BarDataSet(item.entryList, item.title)

                barDataSet.color = ColorTemplate.rgb(item.color)
                barDataSet.valueTextColor = Color.parseColor("#ffffff")
                barDataSet.valueTextSize = 10F

                val barData = BarData(barDataSet)
                barData.barWidth = 0.5f

                val barChart = holder.binding.barChart
                barChart.data = barData

                barChart.apply {
                    setScaleEnabled(false)
                    setPinchZoom(false)

                    animateXY(0,800)

                    description = null

                    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                    axisLeft.axisMinimum = 0f
                    axisRight.axisMinimum = 0f

                    axisRight.setDrawLabels(false)

                    xAxis.setDrawGridLines(false)

                    xAxis.position = XAxis.XAxisPosition.BOTTOM

                    xAxis.labelCount = item.entryList?.size ?: 0

                    xAxis.textColor = Color.parseColor("#ffffff")
                    axisLeft.textColor = Color.parseColor("#ffffff")
                    legend.textColor = Color.parseColor("#ffffff")
                }
                barChart.invalidate()
            }

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHolder internal constructor(val binding: ItemBestDetailAnalysisBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    fun getItem(position: Int): BestChart? {
        return holder!![position]
    }
}