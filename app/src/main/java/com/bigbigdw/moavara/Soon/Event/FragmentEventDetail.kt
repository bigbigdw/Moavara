package com.bigbigdw.moavara.Soon.Event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bigbigdw.moavara.Retrofit.*
//import com.example.moavara.Search.BestChart
//import com.example.moavara.Search.EventDetailData
import com.bigbigdw.moavara.DataBase.EventDetailDataMining
import com.bigbigdw.moavara.Util.Param
import com.bigbigdw.moavara.databinding.FragmentEventDetailBinding
//import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentEventDetail(private val type: String) : Fragment() {

//    private lateinit var adapter: AdapterEventDetail
//    private val items = ArrayList<EventDetailData>()

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    val dateList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = binding.root
//        adapter = AdapterEventDetail(items)

//        with(binding) {
//            rview.layoutManager =
//                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            rview.adapter = adapter
//        }
//
//        items.clear()

        if (type == "") {
            getEventJoara()
        } else {
            getNoticeJoara()
        }


//        adapter.setOnItemClickListener(object : AdapterEventDetail.OnItemClickListener {
//            override fun onItemClick(v: View?, position: Int) {
//                val item: EventDetailData = adapter.getItem(position)
//
//                onClickEvent(item)
//            }
//        })

        return view
    }

    private fun getEventJoara() {
        val EventRef = FirebaseDatabase.getInstance().reference.child("")
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["page"] = "1"
        param["show_type"] = "android"
        param["event_type"] = "normal"
        param["offset"] = "25"
        param["token"] = context?.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("TOKEN", "").toString()
        var num = 0
//        val entryList = mutableListOf<BarEntry>()

        apiJoara.getJoaraEventList(
            param,
            object : RetrofitDataListener<JoaraEventsResult> {
                override fun onSuccess(it: JoaraEventsResult) {

                    val data = it.data

                    if (data != null) {
                        for (item in data) {
//                            items.add(
//                                EventDetailData(
//                                    item.title,
//                                    item.ingimg,
//                                    item.e_date,
//                                    item.s_date,
//                                    item.cnt_read,
//                                    BestChart(dateList, entryList, "조회 수", "#ff7b22")
//                                )
//                            )
                        }
                    }
//                    adapter.notifyDataSetChanged()
                }
            })

        EventRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: EventDetailDataMining? =
                        postSnapshot.getValue(EventDetailDataMining::class.java)
                    if (group != null) {
//                        entryList.add(BarEntry(num.toFloat(), group.cntRead.toFloat()))
                        dateList.add(group.date)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getNoticeJoara() {

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["board"] = ""
        param["page"] = "1"
        param["token"] = context?.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("TOKEN", "").toString()

        apiJoara.getNoticeList(
            param,
            object : RetrofitDataListener<JoaraNoticeResult> {
                override fun onSuccess(it: JoaraNoticeResult) {

                    val data = it.notices

                    if (data != null) {
                        for (item in data) {
//                            items.add(
//                                EventDetailData(
//                                    item.title,
//                                    "",
//                                    item.wdate,
//                                    "",
//                                    item.cnt_read,
//                                )
//                            )
                        }
                    }

//                    adapter.notifyDataSetChanged()
                }
            })
    }


//    private fun onClickEvent(item: EventDetailData) {
//
//    }
}
