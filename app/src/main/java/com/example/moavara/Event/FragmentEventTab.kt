package com.example.moavara.Event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Best.BottomDialogBest
import com.example.moavara.Joara.JoaraEventResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.R
import com.example.moavara.Search.EventData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentEventTab(private val tabType: String) : Fragment() {

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
    ): View {
        root = inflater.inflate(R.layout.fragment_event, container, false)

        recyclerViewLeft = root.findViewById(R.id.rview_Left)
        recyclerViewRight = root.findViewById(R.id.rview_Right)

        adapterLeft = AdapterEvent(itemsLeft)
        adapterRight = AdapterEvent(itemsRight)

        recyclerViewRight!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewRight!!.adapter = adapterRight
        recyclerViewLeft!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewLeft!!.adapter = adapterLeft

        itemsLeft.clear()
        itemsRight.clear()

        Thread {
            when (tabType) {
                "Joara" -> {
                    getEventJoara()
                }
                "Ridi" -> {
                    getEventRidi()
                }
                "OneStore" -> {
                    getEventOneStore()
                }
                "Kakao" -> {
                    getEventKakao()
                }
                "Naver" -> {
                    getEventNaver()
                }
                "MrBlue" -> {
                    getEventMrBlue()
                }
            }
        }.start()

        adapterLeft!!.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: EventData? = adapterLeft!!.getItem(position)

                if(tabType == "Joara" && !item!!.link!!.contains("joaralink://event?event_id=")){
                    Toast.makeText(requireContext(), "이벤트 페이지가 아닙니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val mBottomSheetDialogEvent =
                        BottomSheetDialogEvent(requireContext(), item, tabType)
                    fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
                }
            }
        })

        adapterRight!!.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: EventData? = adapterRight!!.getItem(position)

                if(tabType == "Joara" && !item!!.link!!.contains("joaralink://event?event_id=")){
                    Toast.makeText(requireContext(), "이벤트 페이지가 아닙니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val mBottomSheetDialogEvent =
                        BottomSheetDialogEvent(requireContext(), item, tabType)
                    fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
                }
            }
        })

        return root
    }

    private fun getEventJoara() {

        val call: Call<JoaraEventResult?>? =
            RetrofitJoara.getJoaraEvent("0", "app_home_top_banner", "0")

        call!!.enqueue(object : Callback<JoaraEventResult?> {
            override fun onResponse(
                call: Call<JoaraEventResult?>,
                response: Response<JoaraEventResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val banner = it.banner

                        for (i in banner!!.indices) {

                            val idx = banner[i].joaralink
                            val imgfile = banner[i].imgfile


                            if (i % 2 == 1) {
                                itemsRight.add(
                                    EventData(
                                        idx,
                                        imgfile,
                                        "",
                                        "",
                                        "",
                                        "Joara",
                                    )
                                )
                            } else {
                                itemsLeft.add(
                                    EventData(
                                        idx,
                                        imgfile,
                                        "",
                                        "",
                                        "",
                                        "Joara"
                                    )
                                )
                            }
                        }
                    }

                    adapterLeft!!.notifyDataSetChanged()
                    adapterRight!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<JoaraEventResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })
    }

    private fun getEventNaver() {
        val doc: Document = Jsoup.connect("https://www.mrblue.com/event/novel?sortby=recent").get()
        val mrBlue: Elements = doc.select(".event-list ul li")

        for (i in mrBlue.indices) {

            val imgfile = mrBlue.select("img")[i].absUrl("src")
            val title = mrBlue.select("img")[i].absUrl("alt")
            val link = mrBlue.select("a")[i].absUrl("href")

            requireActivity().runOnUiThread {
                if (i % 2 != 1) {
                    itemsLeft.add(
                        EventData(
                            link,
                            imgfile,
                            title,
                            "",
                            "",
                            "MrBlue"
                        )
                    )
                    adapterLeft!!.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            link,
                            imgfile,
                            title,
                            "",
                            "",
                            "MrBlue"
                        )
                    )
                    adapterRight!!.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getEventMrBlue() {
        val doc: Document = Jsoup.connect("https://www.mrblue.com/event/novel?sortby=recent").get()
        val mrBlue: Elements = doc.select(".event-list ul li")

        for (i in mrBlue.indices) {

            val imgfile = mrBlue.select("img")[i].absUrl("src")
            val title = mrBlue.select("img")[i].absUrl("alt")
            val link = mrBlue.select("a")[i].absUrl("href")

            requireActivity().runOnUiThread {
                if (i % 2 != 1) {
                    itemsLeft.add(
                        EventData(
                            link,
                            imgfile,
                            title,
                            "",
                            "",
                            "MrBlue"
                        )
                    )
                    adapterLeft!!.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            link,
                            imgfile,
                            title,
                            "",
                            "",
                            "MrBlue"
                        )
                    )
                    adapterRight!!.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getEventRidi() {
        val doc: Document = Jsoup.connect("https://ridibooks.com/event/romance_serial").get()
        val ridiKeyword: Elements = doc.select("ul .event_list")

        for (i in ridiKeyword.indices) {

            val imgfile = doc.select(".image_link img")[i].absUrl("src")

            requireActivity().runOnUiThread {
                if (i % 2 != 1) {
                    itemsLeft.add(
                        EventData(
                            "idx",
                            imgfile,
                            "is_banner_cnt",
                            "joaralink"
                        )
                    )
                    adapterLeft!!.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            "idx",
                            imgfile,
                            "is_banner_cnt",
                            "joaralink"
                        )
                    )
                    adapterRight!!.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getEventOneStore() {
        val doc: Document = Jsoup.connect("https://onestory.co.kr/main/PN83003001").get()
        val ridiKeyword: Elements = doc.select("div .BannerSwiperItem")

        for (i in ridiKeyword.indices) {

            val imgfile = doc.select(".BannerSwiperItemPic")[i].absUrl("src")

            requireActivity().runOnUiThread {
                if (i % 2 != 1) {
                    itemsLeft.add(
                        EventData(
                            "idx",
                            imgfile,
                            "is_banner_cnt",
                            "joaralink"
                        )
                    )
                    adapterLeft!!.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            "idx",
                            imgfile,
                            "is_banner_cnt",
                            "joaralink"
                        )
                    )
                    adapterRight!!.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getEventKakao() {
        val doc: Document = Jsoup.connect("https://page.kakao.com/main/recommend-events").get()
        val ridiKeyword: Elements = doc.select(".eventsBox .imageWrapper")

        var num = 0

        for (elem in ridiKeyword) {
            val imgfile = elem.select("img").attr("data-src")

            if (num % 2 != 1) {
                requireActivity().runOnUiThread {
                    itemsLeft.add(
                        EventData(
                            "idx",
                            "https://$imgfile",
                            "is_banner_cnt",
                            "joaralink"
                        )
                    )
                    adapterLeft!!.notifyDataSetChanged()
                }
            } else {
                requireActivity().runOnUiThread {
                    itemsRight.add(
                        EventData(
                            "idx",
                            "https://$imgfile",
                            "is_banner_cnt",
                            "joaralink"
                        )
                    )
                    adapterRight!!.notifyDataSetChanged()
                }
            }

            num += 1
        }
    }
}
