package com.example.moavara.Event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.EventData
import com.example.moavara.Search.EventDataGroup
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentEventTabBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class FragmentEventTab(private val tabType: String = "Joara") : Fragment() {

    private lateinit var adapter: AdapterEvent
    private val items = ArrayList<EventDataGroup>()

    private var _binding: FragmentEventTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventTabBinding.inflate(inflater, container, false)
        val view = binding.root
        adapter = AdapterEvent(items)

        with(binding){
            rview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rview.adapter = adapter
        }

        items.clear()

        Thread {
            when (tabType) {
                "Joara" -> {
                    getEventJoara()
                }
                "Ridi" -> {
                    getEventRidi()
                }
//                "OneStore" -> {
//                    getEventOneStore()
//                }
                "Kakao" -> {
                    getEventKakao()
                }
                "MrBlue" -> {
                    getEventMrBlue()
                }
                "Munpia" -> {
                    getEventMunpia()
                } "Toksoda" -> {
                    getEventToksoda("ALL")
                    getEventToksoda("BL")
                    getEventToksoda("FANTASY")
                    getEventToksoda("ROMANCE")
                }
            }
        }.start()

        adapter.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, type : String) {
                val item: EventDataGroup = adapter.getItem(position)

                onClickEvent(item, type)
            }
        })

        return view
    }

    private fun getEventJoara() {

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["page"] = "1"
        param["show_type"] = "android"
        param["event_type"] = "normal"
        param["offset"] = "25"

        apiJoara.getJoaraEventList(
            param,
            object : RetrofitDataListener<JoaraEventsResult> {
                override fun onSuccess(it: JoaraEventsResult) {

                    val data = it.data

                    if (data != null) {
                        for (i in data.indices) {

                            try{
                                val left = data[2 * i]
                                val right = data[2 * i + 1]

                                items.add(
                                    EventDataGroup(
                                        EventData(
                                            left.idx,
                                            left.ingimg.replace("http://", "https://"),
                                            left.title,
                                            left.cnt_read,
                                            DBDate.DateMMDD(),
                                            "Joara",
                                            ""
                                        ),
                                        EventData(
                                            right.idx,
                                            right.ingimg.replace("http://", "https://"),
                                            right.title,
                                            right.cnt_read,
                                            DBDate.DateMMDD(),
                                            "Joara",
                                            ""
                                        )
                                    )
                                )
                            } catch (e : IndexOutOfBoundsException){}
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })

    }

    private fun getEventJoaraOld() {

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["page"] = "0"
        param["banner_type"] = "app_home_top_banner"
        param["category"] = "0"

        apiJoara.getJoaraEvent(
            param,
            object : RetrofitDataListener<JoaraEventResult> {
                override fun onSuccess(data: JoaraEventResult) {

                    val banner = data.banner

                    if (banner != null) {
                        for (i in banner.indices) {

                            val idx = banner[i].joaralink
                            val imgfile = banner[i].imgfile.replace("http://", "https://")

                            items.add(
                                EventDataGroup(
                                    EventData(
                                        idx,
                                        imgfile,
                                        "",
                                        "",
                                        "Joara",
                                        ""
                                    ),
                                    EventData(
                                        idx,
                                        imgfile,
                                        "",
                                        "",
                                        "Joara",
                                        ""
                                    )
                                )
                            )
                        }
                    }

                    adapter.notifyDataSetChanged()
                }
            })
    }

    private fun getEventMrBlue() {
        val doc: Document = Jsoup.connect("https://www.mrblue.com/event/novel?sortby=recent").get()
        val mrBlue: Elements = doc.select(".event-list ul li")

        for (i in mrBlue.indices) {

            requireActivity().runOnUiThread {

                try{
                    items.add(
                        EventDataGroup(
                            EventData(
                                mrBlue.select("a")[2 * i].absUrl("href"),
                                mrBlue.select("img")[2 * i].absUrl("src"),
                                mrBlue.select("img")[2 * i].absUrl("alt"),
                                "",
                                DBDate.DateMMDD(),
                                "MrBlue",
                                ""
                            ),
                            EventData(
                                mrBlue.select("a")[2 * i + 1].absUrl("href"),
                                mrBlue.select("img")[2 * i + 1].absUrl("src"),
                                mrBlue.select("img")[2 * i + 1].absUrl("alt"),
                                "",
                                DBDate.DateMMDD(),
                                "MrBlue",
                                ""
                            )
                        )
                    )
                } catch (e : IndexOutOfBoundsException){}
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getEventRidi() {
        val doc: Document = Jsoup.connect("https://ridibooks.com/event/romance_serial").get()
        val ridiKeyword: Elements = doc.select("ul .event_list")

        for (i in ridiKeyword.indices) {
            requireActivity().runOnUiThread {
                try {
                    items.add(
                        EventDataGroup(
                            EventData(
                                doc.select(".event_title a")[2 * i].absUrl("href"),
                                doc.select(".image_link img")[2 * i].absUrl("src"),
                                doc.select(".event_title a")[2 * i].text(),
                                "",
                                DBDate.DateMMDD(),
                                "Ridi",
                                ""
                            ),
                            EventData(
                                doc.select(".event_title a")[2 * i + 1].absUrl("href"),
                                doc.select(".image_link img")[2 * i + 1].absUrl("src"),
                                doc.select(".event_title a")[2 * i + 1].text(),
                                "",
                                DBDate.DateMMDD(),
                                "Ridi",
                                ""
                            )
                        )
                    )
                } catch (e: IndexOutOfBoundsException) { }
                adapter.notifyDataSetChanged()
            }
        }
    }



    private fun getEventMunpia() {

        val doc: Document = Jsoup.connect("https://square.munpia.com/event").get()
        val MunpiaWrap: Elements = doc.select(".light .entries tbody tr a img")

        requireActivity().runOnUiThread {

            for (i in MunpiaWrap.indices) {

                try {
                    items.add(
                        EventDataGroup(
                            EventData(
                                doc.select(".light .entries tbody tr td a")[2 * i].attr("href"),
                                "https://${doc.select(".light .entries tbody tr a img")[2 * i].attr("src")}",
                                doc.select(".light .entries .subject td a")[2 * i].text(),
                                "",
                                DBDate.DateMMDD(),
                                "Munpia",
                                ""
                            ),
                            EventData(
                                doc.select(".light .entries tbody tr td a")[2 * i + 1].attr("href"),
                                "https://${doc.select(".light .entries tbody tr a img")[2 * i + 1].attr("src")}",
                                doc.select(".light .entries .subject td a")[2 * i + 1].text(),
                                "",
                                DBDate.DateMMDD(),
                                "Munpia",
                                ""
                            )
                        )
                    )
                } catch (e: IndexOutOfBoundsException) { }
                adapter.notifyDataSetChanged()
            }
        }

    }

    private fun getEventOneStore() {
        val doc: Document = Jsoup.connect("https://onestory.co.kr/main/PN83003001").get()
        val ridiKeyword: Elements = doc.select("div .BannerSwiperItem")

        for (i in ridiKeyword.indices) {

            val imgfile = doc.select(".BannerSwiperItemPic")[i].absUrl("src")

            requireActivity().runOnUiThread {

                try {
                    items.add(
                        EventDataGroup(
                            EventData(
                                "",
                                imgfile,
                                "",
                                "",
                                DBDate.DateMMDD(),
                                "OneStore",
                                ""
                            ),
                            EventData(
                                "",
                                imgfile,
                                "",
                                "",
                                DBDate.DateMMDD(),
                                "OneStore",
                                ""
                            )
                        )
                    )
                } catch (e: IndexOutOfBoundsException) { }
                adapter.notifyDataSetChanged()
            }

        }
    }

    private fun getEventKakao() {
        val doc: Document = Jsoup.connect("https://page.kakao.com/main/recommend-events").get()
        val kakao: Elements = doc.select(".eventsBox .cellWrapper")

        requireActivity().runOnUiThread {
            for (i in kakao.indices) {
                try {
                    Log.d("####-Left", "https://${kakao[2 * i + 1].select(".imageWrapper img").attr("data-src")}")

                    items.add(
                        EventDataGroup(
                            EventData(
                                kakao[2 * i].attr("data-url"),
                                "https://${kakao[2 * i].select(".imageWrapper img").attr("data-src")}",
                                kakao[2 * i].select(".imageWrapper img").attr("alt"),
                                "",
                                DBDate.DateMMDD(),
                                "Kakao",
                                ""
                            ),
                            EventData(
                                kakao[2 * i + 1].attr("data-url"),
                                "https://${kakao[2 * i + 1].select(".imageWrapper img").attr("data-src")}",
                                kakao[2 * i + 1].select(".imageWrapper img").attr("alt"),
                                "",
                                DBDate.DateMMDD(),
                                "Kakao",
                                ""
                            )
                        )
                    )
                } catch (e: IndexOutOfBoundsException) { }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getEventToksoda(cate : String){
        val apiToksoda = RetrofitToksoda()
        val param : MutableMap<String?, Any> = HashMap()

        param["bnnrPstnCd"] = "00023"
        param["bnnrClsfCd"] = "00320"
        param["expsClsfCd"] = Genre.setToksodaGenre(cate)
        param["fileNo"] = "1"
        param["pageRowCount"] = "10"
        param["_"] = "1657271613642"

        apiToksoda.getEventList(
            param,
            object : RetrofitDataListener<BestBannerListResult> {
                override fun onSuccess(data: BestBannerListResult) {

                    if(data.resultList != null){
                        for (i in data.resultList.indices) {

                            try {
                                items.add(
                                    EventDataGroup(
                                        EventData(
                                            "https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=${data.resultList[2 * i].linkInfo}",
                                            "https:${data.resultList[2 * i].imgPath}",
                                            data.resultList[2 * i].bnnrNm,
                                            "",
                                            DBDate.DateMMDD(),
                                            "Toksoda",
                                            ""
                                        ),
                                        EventData(
                                            "https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=${data.resultList[2 * i + 1].linkInfo}",
                                            "https:${data.resultList[2 * i + 1].imgPath}",
                                            data.resultList[2 * i + 1].bnnrNm,
                                            "",
                                            DBDate.DateMMDD(),
                                            "Toksoda",
                                            ""
                                        )
                                    )
                                )
                            } catch (e: IndexOutOfBoundsException) { }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }

    private fun onClickEvent(item: EventDataGroup, type: String){

        val eventItem: EventData? = if(type == "Left"){
            item.left
        } else {
            item.right
        }

        if (eventItem != null) {
            Log.d("####", eventItem.imgfile)
        }

        if(eventItem != null){
            val mBottomSheetDialogEvent =
                BottomSheetDialogEvent(requireContext(), eventItem, tabType)
            fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
        }
    }
}
