package com.example.moavara.Event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.EventData
import com.example.moavara.Search.EventDataGroup
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
import com.example.moavara.databinding.FragmentEventTabBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URLEncoder

class FragmentEventTab : Fragment() {

    private lateinit var adapter: AdapterEvent
    private val items = ArrayList<EventDataGroup>()

    private var _binding: FragmentEventTabBinding? = null
    private val binding get() = _binding!!
    private var UserInfo = DataBaseUser()
    private var platform = ""
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventTabBinding.inflate(inflater, container, false)
        val view = binding.root
        adapter = AdapterEvent(items)

        firebaseAnalytics = Firebase.analytics

        with(binding) {
            rview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rview.adapter = adapter

            blank.tviewblank.text = "이벤트를 불러오는 중..."
        }

        UserInfo = (parentFragment as FragmentEvent).UserInfo

        platform = arguments?.getString("platform","") ?: ""

        items.clear()

        when (platform) {
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
            "Kakao_Stage" -> {
                getEventKakaoStage()
            }
            "MrBlue" -> {
                getEventMrBlue()
            }
            "Munpia" -> {
                getEventMunpia()
            }
            "Toksoda" -> {
                getEventToksoda("ALL")
                getEventToksoda("BL")
                getEventToksoda("FANTASY")
                getEventToksoda("ROMANCE")
            }
        }

        adapter.setOnItemClickListener(object : AdapterEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, type: String) {
                val item: EventDataGroup = adapter.getItem(position)

                val eventItem: EventData? = if (type == "Left") {
                    item.left
                } else {
                    item.right
                }

                if (eventItem != null) {
                    if(eventItem.type == "Joara" && !eventItem.link.contains("joaralink://event?event_id=") && !eventItem.link.contains("joaralink://notice?notice_id=")){
                        Toast.makeText(requireContext(), "지원하지 않는 이벤트 형식입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val bundle = Bundle()
                        bundle.putString("EVENT_PLATFORM", eventItem.type)
                        firebaseAnalytics.logEvent("EVENT_BottomSheetDialogEvent", bundle)

                        val mBottomSheetDialogEvent =
                            BottomSheetDialogEvent(requireContext(), eventItem, platform, UserInfo, firebaseAnalytics, false)
                        fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
                    }
                }
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

                    if (banner != null) {

                        for (i in banner.indices) {

                            try {
                                items.add(
                                    EventDataGroup(
                                        EventData(
                                            banner[2 * i].joaralink,
                                            banner[2 * i].imgfile.replace("http://", "https://"),
                                            "",
                                            "",
                                            DBDate.DateMMDD(),
                                            999,
                                            "Joara",
                                            ""
                                        ),
                                        EventData(
                                            banner[2 * i + 1].joaralink,
                                            banner[2 * i + 1].imgfile.replace(
                                                "http://",
                                                "https://"
                                            ),
                                            "",
                                            "",
                                            DBDate.DateMMDD(),
                                            999,
                                            "Joara",
                                            ""
                                        )
                                    )
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                    }

                    if (banner != null) {
                        if (banner.isEmpty()) {
                            binding.rview.visibility = View.GONE
                            binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                            binding.blank.root.visibility = View.VISIBLE
                        } else {
                            binding.blank.root.visibility = View.GONE
                            binding.rview.visibility = View.VISIBLE
                        }
                    }

                    adapter.notifyDataSetChanged()
                }
            })
    }

    private fun getEventMrBlue() {
        Thread {
            val doc: Document =
                Jsoup.connect("https://www.mrblue.com/event/novel?sortby=recent").get()
            val mrBlue: Elements = doc.select(".event-list ul li")

            requireActivity().runOnUiThread {
                for (i in mrBlue.indices) {
                    try {
                        items.add(
                            EventDataGroup(
                                EventData(
                                    mrBlue.select("a")[2 * i].absUrl("href")
                                        .replace("https://www.mrblue.com/event/detail/", ""),
                                    mrBlue.select("img")[2 * i].absUrl("src"),
                                    mrBlue.select("img")[2 * i].absUrl("alt").replace("https://www.mrblue.com/event/", ""),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "MrBlue",
                                    ""
                                ),
                                EventData(
                                    mrBlue.select("a")[2 * i + 1].absUrl("href")
                                        .replace("https://www.mrblue.com/event/detail/", ""),
                                    mrBlue.select("img")[2 * i + 1].absUrl("src"),
                                    mrBlue.select("img")[2 * i + 1].absUrl("alt").replace("https://www.mrblue.com/event/", ""),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "MrBlue",
                                    ""
                                )
                            )
                        )
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
                if (items.size == 0) {
                    binding.rview.visibility = View.GONE
                    binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                    binding.blank.root.visibility = View.VISIBLE
                } else {
                    binding.blank.root.visibility = View.GONE
                    binding.rview.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun getEventRidi() {
        Thread {
            val doc: Document = Jsoup.connect("https://ridibooks.com/event/romance_serial").get()
            val ridiKeyword: Elements = doc.select("ul .event_list")

            requireActivity().runOnUiThread {
                for (i in ridiKeyword.indices) {
                    try {
                        items.add(
                            EventDataGroup(
                                EventData(
                                    doc.select(".event_title a")[2 * i].absUrl("href")
                                        .replace("https://ridibooks.com/event/", ""),
                                    doc.select(".image_link img")[2 * i].absUrl("src"),
                                    doc.select(".event_title a")[2 * i].text(),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "Ridi",
                                    ""
                                ),
                                EventData(
                                    doc.select(".event_title a")[2 * i + 1].absUrl("href")
                                        .replace("https://ridibooks.com/event/", ""),
                                    doc.select(".image_link img")[2 * i + 1].absUrl("src"),
                                    doc.select(".event_title a")[2 * i + 1].text(),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "Ridi",
                                    ""
                                )
                            )
                        )
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
                if (items.size == 0) {
                    binding.rview.visibility = View.GONE
                    binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                    binding.blank.root.visibility = View.VISIBLE
                } else {
                    binding.blank.root.visibility = View.GONE
                    binding.rview.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }


        }.start()
    }


    private fun getEventMunpia() {
        Thread {
            val doc: Document = Jsoup.connect("https://square.munpia.com/event").get()
            val MunpiaWrap: Elements = doc.select(".light .entries tbody tr a img")

            requireActivity().runOnUiThread {

                for (i in MunpiaWrap.indices) {

                    try {
                        items.add(
                            EventDataGroup(
                                EventData(
                                    URLEncoder.encode(
                                        doc.select(".light .entries tbody tr td a")[2 * i].attr(
                                            "href"
                                        ), "utf-8"
                                    ),
                                    "https:${
                                        doc.select(".light .entries tbody tr a img")[2 * i].attr(
                                            "src"
                                        )
                                    }",
                                    doc.select(".light .entries .subject td a")[2 * i].text(),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "Munpia",
                                    ""
                                ),
                                EventData(
                                    URLEncoder.encode(
                                        doc.select(".light .entries tbody tr td a")[2 * i + 1].attr(
                                            "href"
                                        ), "utf-8"
                                    ),
                                    "https:${
                                        doc.select(".light .entries tbody tr a img")[2 * i + 1].attr(
                                            "src"
                                        )
                                    }",
                                    doc.select(".light .entries .subject td a")[2 * i + 1].text(),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "Munpia",
                                    ""
                                )
                            )
                        )
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }

                if (items.size == 0) {
                    binding.rview.visibility = View.GONE
                    binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                    binding.blank.root.visibility = View.VISIBLE
                } else {
                    binding.blank.root.visibility = View.GONE
                    binding.rview.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun getEventOneStore() {
        Thread {
            val doc: Document = Jsoup.connect("https://onestory.co.kr/main/PN83003001").get()
            val onestoreKeyword: Elements = doc.select("div .BannerSwiperItem")

            requireActivity().runOnUiThread {
                for (i in onestoreKeyword.indices) {
                    if(i < onestoreKeyword.size / 3){
                        try {
                            requireActivity().runOnUiThread {

                                items.add(
                                    EventDataGroup(
                                        EventData(
                                            "OneStore_${DBDate.DateMMDDHHMM()}",
                                            doc.select(".BannerSwiperItemPic")[2 * i].absUrl("src"),
                                            doc.select(".BannerSwiperItemPic")[2 * i].absUrl("alt").replace("https://onestory.co.kr/main/",""),
                                            "",
                                            DBDate.DateMMDD(),
                                            999,
                                            "OneStore",
                                            ""
                                        ),
                                        EventData(
                                            "OneStore_${DBDate.DateMMDDHHMM()}",
                                            doc.select(".BannerSwiperItemPic")[2 * i + 1].absUrl("src"),
                                            doc.select(".BannerSwiperItemPic")[2 * i + 1].absUrl("alt").replace("https://onestory.co.kr/main/",""),
                                            "",
                                            DBDate.DateMMDD(),
                                            999,
                                            "OneStore",
                                            ""
                                        )
                                    )
                                )
                            }
                        } catch (e: IndexOutOfBoundsException) {}
                    }
                    adapter.notifyDataSetChanged()
                }
                if (items.size == 0) {
                    binding.rview.visibility = View.GONE
                    binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                    binding.blank.root.visibility = View.VISIBLE
                } else {
                    binding.blank.root.visibility = View.GONE
                    binding.rview.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        }.start()

    }

    private fun getEventKakao() {
        Thread {
            val doc: Document = Jsoup.connect("https://page.kakao.com/main/recommend-events").get()
            val kakao: Elements = doc.select(".eventsBox .cellWrapper")

            requireActivity().runOnUiThread {
                for (i in kakao.indices) {
                    try {
                        items.add(
                            EventDataGroup(
                                EventData(
                                    kakao[2 * i].attr("data-url").replace(
                                        "kakaopage://exec?open_web_with_auth/store/event/v2/",
                                        ""
                                    ),
                                    "https:${
                                        kakao[2 * i].select(".imageWrapper img").attr("data-src")
                                    }",
                                    kakao[2 * i].select(".imageWrapper img").attr("alt"),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "Kakao",
                                    ""
                                ),
                                EventData(
                                    kakao[2 * i + 1].attr("data-url").replace(
                                        "kakaopage://exec?open_web_with_auth/store/event/v2/",
                                        ""
                                    ),
                                    "https:${
                                        kakao[2 * i + 1].select(".imageWrapper img")
                                            .attr("data-src")
                                    }",
                                    kakao[2 * i + 1].select(".imageWrapper img").attr("alt"),
                                    "",
                                    DBDate.DateMMDD(),
                                    999,
                                    "Kakao",
                                    ""
                                )
                            )
                        )
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
                if (items.size == 0) {
                    binding.rview.visibility = View.GONE
                    binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                    binding.blank.root.visibility = View.VISIBLE
                } else {
                    binding.blank.root.visibility = View.GONE
                    binding.rview.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun getEventKakaoStage() {
        val apiKakaoStage = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["page"] = "0"
        param["progress"] = "true"
        param["size"] = "9"

        apiKakaoStage.getKakaoStageEventList(
            param,
            object : RetrofitDataListener<KakaoStageEventList> {
                override fun onSuccess(data: KakaoStageEventList) {
                    for (i in data.content.indices) {
                        try {

                            items.add(
                                EventDataGroup(
                                    EventData(
                                        data.content[2 * i].id,
                                        data.content[2 * i].desktopListImage?.image?.url ?: "",
                                        data.content[2 * i].title,
                                        data.content[2 * i].desktopDetailImage?.image?.url ?: "",
                                        DBDate.DateMMDD(),
                                        999,
                                        "Kakao_Stage",
                                        ""
                                    ),
                                    EventData(
                                        data.content[2 * i + 1].id,
                                        data.content[2 * i + 1].desktopListImage?.image?.url ?: "",
                                        data.content[2 * i + 1].title,
                                        data.content[2 * i + 1].desktopDetailImage?.image?.url
                                            ?: "",
                                        DBDate.DateMMDD(),
                                        999,
                                        "Kakao_Stage",
                                        ""
                                    )
                                )
                            )
                        } catch (e: IndexOutOfBoundsException) {
                        }
                    }
                    if (items.size == 0) {
                        binding.rview.visibility = View.GONE
                        binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                        binding.blank.root.visibility = View.VISIBLE
                    } else {
                        binding.blank.root.visibility = View.GONE
                        binding.rview.visibility = View.VISIBLE
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }

    private fun getEventToksoda(cate: String) {
        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

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

                    if (data.resultList != null) {
                        for (i in data.resultList.indices) {

                            try {
                                items.add(
                                    EventDataGroup(
                                        EventData(
                                            data.resultList[2 * i].linkInfo.replace("https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=",""),
                                            "https:${data.resultList[2 * i].imgPath}",
                                            data.resultList[2 * i].bnnrNm,
                                            "",
                                            DBDate.DateMMDD(),
                                            999,
                                            "Toksoda",
                                            ""
                                        ),
                                        EventData(
                                            data.resultList[2 * i + 1].linkInfo.replace("https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=",""),
                                            "https:${data.resultList[2 * i + 1].imgPath}",
                                            data.resultList[2 * i + 1].bnnrNm,
                                            "",
                                            DBDate.DateMMDD(),
                                            999,
                                            "Toksoda",
                                            ""
                                        )
                                    )
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                        if (items.size == 0) {
                            binding.rview.visibility = View.GONE
                            binding.blank.tviewblank.text = "현재 진행중인 이벤트가 없습니다."
                            binding.blank.root.visibility = View.VISIBLE
                        } else {
                            binding.blank.root.visibility = View.GONE
                            binding.rview.visibility = View.VISIBLE
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            })
    }

}
