package com.example.moavara.Event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Joara.JoaraEventResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.R
import com.example.moavara.Search.EventData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentEvent : Fragment() {

    private var adapterLeft: AdapterEvent? = null
    private var adapterRight: AdapterEvent? = null
    private val itemsLeft = ArrayList<EventData?>()
    private val itemsRight = ArrayList<EventData?>()
    var recyclerViewLeft: RecyclerView? = null
    var recyclerViewRight: RecyclerView? = null

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_event, container, false)

        recyclerViewLeft = root.findViewById(R.id.rview_Left)
        recyclerViewRight = root.findViewById(R.id.rview_Right)

        adapterLeft = AdapterEvent(itemsLeft)
        adapterRight = AdapterEvent(itemsRight)

        getEvent(recyclerViewLeft, recyclerViewRight)

        return root
    }

    private fun getEvent(recyclerViewLeft: RecyclerView?, recyclerViewRight: RecyclerView?) {
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val linearLayoutManager2 =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val call: Call<JoaraEventResult?>? = RetrofitJoara.getJoaraEvent( "0", "app_home_top_banner", "0")

        call!!.enqueue(object : Callback<JoaraEventResult?> {
            override fun onResponse(
                call: Call<JoaraEventResult?>,
                response: Response<JoaraEventResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val banner = it.banner

                        for (i in banner!!.indices) {

                            Log.d("@@@@","!!!!")

                            val idx = banner[i].idx
                            val imgfile = banner[i].imgfile
                            val is_banner_cnt = banner[i].is_banner_cnt
                            val joaralink = banner[i].joaralink

                            if(i%2 != 1){
                                itemsLeft!!.add(
                                    EventData(
                                        idx,
                                        imgfile,
                                        is_banner_cnt,
                                        joaralink
                                    )
                                )
                            } else {
                                itemsRight!!.add(
                                    EventData(
                                        idx,
                                        imgfile,
                                        is_banner_cnt,
                                        joaralink
                                    )
                                )
                            }
                        }
                    }
                    recyclerViewLeft!!.layoutManager = linearLayoutManager
                    recyclerViewLeft.adapter = adapterLeft
                    adapterLeft!!.notifyDataSetChanged()

                    recyclerViewRight!!.layoutManager = linearLayoutManager2
                    recyclerViewRight.adapter = adapterRight
                    adapterRight!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<JoaraEventResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        adapterLeft!!.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: EventData? = adapterLeft!!.getItem(position)

//                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
//                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })

        adapterRight!!.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: EventData? = adapterRight!!.getItem(position)

//                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
//                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }
}