package com.example.moavara.Event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.EventData
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentEventTabBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class FragmentEventTab(private val tabType: String = "Joara") : Fragment() {

    private lateinit var adapterLeft: AdapterEvent
    private lateinit var adapterRight: AdapterEvent
    private val itemsLeft = ArrayList<EventData>()
    private val itemsRight = ArrayList<EventData>()

    private var _binding: FragmentEventTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventTabBinding.inflate(inflater, container, false)
        val view = binding.root
        adapterLeft = AdapterEvent(itemsLeft)
        adapterRight = AdapterEvent(itemsRight)

        with(binding){
            rviewRight.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rviewRight.adapter = adapterRight
            rviewLeft.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rviewLeft.adapter = adapterLeft
        }

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

        adapterLeft.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: EventData = adapterLeft.getItem(position)

                onClickEvent(item)
            }
        })

        adapterRight.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: EventData = adapterRight.getItem(position)

                onClickEvent(item)
            }
        })

        return view
    }

    private fun getEventJoara() {

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
                                    "Joara"
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

                    adapterLeft.notifyDataSetChanged()
                    adapterRight.notifyDataSetChanged()
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
                    adapterLeft.notifyDataSetChanged()
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
                    adapterRight.notifyDataSetChanged()
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
                            title.replace("https://www.mrblue.com/event/",""),
                            "",
                            "",
                            "MrBlue"
                        )
                    )
                    adapterLeft.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            link,
                            imgfile,
                            title.replace("https://www.mrblue.com/event/",""),
                            "",
                            "",
                            "MrBlue"
                        )
                    )
                    adapterRight.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getEventRidi() {
        val doc: Document = Jsoup.connect("https://ridibooks.com/event/romance_serial").get()
        val ridiKeyword: Elements = doc.select("ul .event_list")

        for (i in ridiKeyword.indices) {

            val imgfile = doc.select(".image_link img")[i].absUrl("src")
            val link = doc.select(".event_title a")[i].absUrl("href")
            val title = doc.select(".event_title a")[i].text()

            requireActivity().runOnUiThread {
                if (i % 2 != 1) {
                    itemsLeft.add(
                        EventData(
                            link,
                            imgfile,
                            title,
                            "",
                            "",
                            "Ridi"
                        )
                    )
                    adapterLeft.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            link,
                            imgfile,
                            title,
                            "",
                            "",
                            "Ridi"
                        )
                    )
                    adapterRight.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getEventMunpia() {

        val doc: Document = Jsoup.connect("https://square.munpia.com/event").get()
        val MunpiaWrap: Elements = doc.select(".light .entries tbody tr a img")

        for (i in MunpiaWrap.indices) {

            val link = doc.select(".light .entries tbody tr td a")[i].attr("href")
            val imgfile = doc.select(".light .entries tbody tr a img")[i].attr("src")
            val title = doc.select(".light .entries .subject td a")[i].text()

            requireActivity().runOnUiThread {
                if (i % 2 != 1) {
                    itemsLeft.add(
                        EventData(
                            "https://square.munpia.com${link}",
                            "https:${imgfile}",
                            title,
                            "",
                            "",
                            "Munpia"
                        )
                    )

                    adapterLeft.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            "https://www.munpia.com${link}",
                            "https:${imgfile}",
                            title,
                            "",
                            "",
                            "Munpia"
                        )
                    )
                    adapterRight.notifyDataSetChanged()
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
                            "",
                            imgfile,
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                    adapterLeft.notifyDataSetChanged()
                } else {
                    itemsRight.add(
                        EventData(
                            "",
                            imgfile,
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                    adapterRight.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getEventKakao() {
        val doc: Document = Jsoup.connect("https://page.kakao.com/main/recommend-events").get()
        val kakao: Elements = doc.select(".eventsBox .cellWrapper")
        var num = 0


        for (elem in kakao) {
            val imgfile = elem.select(".imageWrapper img").attr("data-src")
            val link = elem.attr("data-url")
            val title = elem.select(".imageWrapper img").attr("alt")

            if (num % 2 != 1) {
                requireActivity().runOnUiThread {
                    itemsLeft.add(
                        EventData(
                            link,
                            "https://$imgfile",
                            title,
                            "",
                            "",
                            "Kakao"
                        )
                    )
                    adapterLeft.notifyDataSetChanged()
                }
            } else {
                requireActivity().runOnUiThread {
                    itemsRight.add(
                        EventData(
                            link,
                            "https://$imgfile",
                            title,
                            "",
                            "",
                            "Kakao"
                        )
                    )
                    adapterRight.notifyDataSetChanged()
                }
            }

            num += 1
        }
    }

    fun getEventToksoda(cate : String){
        val apiToksoda = RetrofitToksoda()
        val param : MutableMap<String?, Any> = HashMap()
        var num = 0

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

                    for (item in data.resultList!!) {
                        val imgfile = "https:${item.imgPath}"
                        val link = "https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=${item.linkInfo}"
                        val title = item.bnnrNm

                        if (num % 2 != 1) {
                            requireActivity().runOnUiThread {
                                itemsLeft.add(
                                    EventData(
                                        link,
                                        imgfile,
                                        title,
                                        "",
                                        "",
                                        "Toksoda"
                                    )
                                )
                                adapterLeft.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                itemsRight.add(
                                    EventData(
                                        link,
                                        imgfile,
                                        title,
                                        "",
                                        "",
                                        "Toksoda"
                                    )
                                )
                                adapterRight.notifyDataSetChanged()
                            }
                        }

                        num += 1
                    }
                }
            })
    }

    private fun onClickEvent(item: EventData){
        if (tabType == "Joara" && !item.link.contains("joaralink://event?event_id=") && !item.link.contains("joaralink://notice?notice_id=")) {
            Toast.makeText(requireContext(), "이벤트 페이지가 아닙니다.", Toast.LENGTH_SHORT).show()
        } else if (tabType == "OneStore") {
            Toast.makeText(requireContext(), "원스토어는 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
        }else if (tabType == "Munpia") {
            Toast.makeText(requireContext(), "문피아는 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
        } else if (tabType == "Kakao" && item.link.contains("kakaopage://exec?open_web_with_auth/store/event")) {
            Toast.makeText(requireContext(), "이벤트 페이지가 아닙니다.", Toast.LENGTH_SHORT).show()
        } else if (tabType == "Toksoda") {
            Toast.makeText(requireContext(), "톡소다는 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            startActivity(intent)
        } else {
            val mBottomSheetDialogEvent =
                BottomSheetDialogEvent(requireContext(), item, tabType)
            fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
        }
    }
}
