package com.example.moavara.Event

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DataEvent
import com.example.moavara.DataBase.DataPickEvent
import com.example.moavara.Joara.JoaraEventDetailResult
import com.example.moavara.Joara.JoaraNoticeDetailResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.R
import com.example.moavara.Search.EventData
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.BottomDialogEventBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetDialogEvent(
    private val mContext: Context,
    private val item: EventData,
    private val tabType: String = ""
) :
    BottomSheetDialogFragment() {

    private lateinit var dbEvent: DataPickEvent
    var cate = ""

    private var _binding: BottomDialogEventBinding? = null
    private val binding get() = _binding!!
    private var title : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomDialogEventBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()

        dbEvent = Room.databaseBuilder(requireContext(), DataPickEvent::class.java, "pick-event")
            .allowMainThreadQueries().build()

        if (tabType == "Joara") {
            getEventJoara()
        }

        Thread {
            when (tabType) {
                "Ridi" -> {
                    getEventRidi()
                }
                "Kakao" -> {
                    getEventKakao()
                }
                "MrBlue" -> {
                    getEventMrBlue()
                }
            }
        }.start()

        with(binding){
            btnLeft.setOnClickListener {
                dbEvent.eventDao().insert(DataEvent(
                    item.link,
                    item.imgfile,
                    title,
                    cate,
                    tabType,
                    ""
                ))
                Toast.makeText(requireContext(), "Pick 성공!", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            btnRight.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(tabType)))
                startActivity(intent)
            }
        }



        return view
    }

    private fun getEventMrBlue(){
        val doc: Document = Jsoup.connect(item.link).get()

        if(doc.select(".event-html img").size > 1){
            val mrBlue1 = doc.select(".event-html img").first()!!.absUrl("src")

            requireActivity().runOnUiThread {
                Glide.with(mContext)
                    .load(mrBlue1.replace("http", "https"))
                    .into(binding.iview)
            }
        } else if(doc.select(".event-html img").size < 1) {

            val mrBlue2 = doc.select(".event-visual img").first()!!.absUrl("src")

            requireActivity().runOnUiThread {
                Glide.with(mContext)
                    .load(mrBlue2)
                    .into(binding.iview)
            }
        }

        title = item.title


    }

    private fun getEventRidi(){
        val doc: Document = Jsoup.connect(item.link).get()
        val ridi = doc.select(".event_detail_top img").first()!!.absUrl("src")

        title = item.title

        requireActivity().runOnUiThread {
            Glide.with(mContext)
                .load(ridi)
                .into(binding.iview)
        }
    }

    private fun getEventKakao() {
        val doc: Document = Jsoup.connect("https://page.kakao.com${item.link}").get()
        val kakao = doc.select(".themeBox img").first()!!.absUrl("src")

        title = item.title

        requireActivity().runOnUiThread {
            Glide.with(mContext)
                .load(kakao)
                .into(binding.iview)
        }
    }

    private fun getUrl(type: String): String {
        when (type) {
            "Joara" -> {
                return if(item.link.contains("joaralink://event?event_id=")){
                    "https://www.joara.com/event/" + item.link.replace(
                        "joaralink://event?event_id=",
                        ""
                    )
                } else {
                    "https://www.joara.com/notice/" + item.link.replace(
                        "joaralink://notice?notice_id=",
                        ""
                    )
                }
            }
            "Ridi" -> {
                return item.link
            }
            "Kakao" -> {
                return "https://page.kakao.com${item.link}"
            }
            "MrBlue" -> {
                return item.link
            }
            else -> return ""
        }
    }

    private fun getEventJoara() {

        binding.wView.visibility = View.VISIBLE

        val mws = binding.wView.settings

        mws.loadWithOverviewMode = true
        mws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.wView.overScrollMode = WebView.OVER_SCROLL_IF_CONTENT_SCROLLS
        WebView.setWebContentsDebuggingEnabled(true)
        mws.setSupportZoom(false) // 화면 줌 허용 여부
        binding.wView.webChromeClient = WebChromeClient()
        binding.wView.getSettings().setJavaScriptEnabled(true)

        if(item.link.contains("joaralink://event?event_id=")){
            val call: Call<JoaraEventDetailResult?>? =
                RetrofitJoara.getJoaraEventDetail(
                    item.link.replace(
                        "joaralink://event?event_id=",
                        ""
                    )
                )

            call!!.enqueue(object : Callback<JoaraEventDetailResult?> {
                override fun onResponse(
                    call: Call<JoaraEventDetailResult?>,
                    response: Response<JoaraEventDetailResult?>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let { it ->
                            val content = it.event!!.content

                            title = it.event.title


                            binding.wView.loadDataWithBaseURL(
                                null,
                                content.replace("http", "https"),
                                "text/html; charset=utf-8",
                                "base64",
                                null
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JoaraEventDetailResult?>, t: Throwable) {
                    Log.d("onFailure", "실패")
                }
            })
        } else {
            val call: Call<JoaraNoticeDetailResult?>? =
                RetrofitJoara.getJoaraNoticeDetail(
                    item.link.replace(
                        "joaralink://notice?notice_id=",
                        ""
                    )
                )

            call!!.enqueue(object : Callback<JoaraNoticeDetailResult?> {
                override fun onResponse(
                    call: Call<JoaraNoticeDetailResult?>,
                    response: Response<JoaraNoticeDetailResult?>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let { it ->
                            val content = it.notice!!.content

                            title = it.notice.title

                            binding.wView.loadDataWithBaseURL(
                                null,
                                content.replace("<br />", ""),
                                "text/html; charset=utf-8",
                                "base64",
                                null
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JoaraNoticeDetailResult?>, t: Throwable) {
                    Log.d("onFailure", "실패")
                }
            })
        }


    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}