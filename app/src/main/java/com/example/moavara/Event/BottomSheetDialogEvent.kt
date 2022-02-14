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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.DataEvent
import com.example.moavara.DataBase.DataPickEvent
import com.example.moavara.Joara.JoaraEventDetailResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.R
import com.example.moavara.Search.EventData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class BottomSheetDialogEvent(
    private val mContext: Context,
    private val item: EventData?,
    private val tabType: String?
) :
    BottomSheetDialogFragment() {

    private var btnRight: Button? = null
    private var btnLeft: Button? = null
    private var wView: WebView? = null

    private var title : String? = null
    private var endtime : String? = null
    private var starttime : String? = null
    private var iView : ImageView? = null

    private lateinit var dbEvent: DataPickEvent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.bottom_dialog_event, container, false)

        btnRight = v.findViewById(R.id.btnRight)
        btnLeft = v.findViewById(R.id.btnLeft)
        wView = v.findViewById(R.id.wView)
        iView = v.findViewById(R.id.iVIew)

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

        btnLeft!!.setOnClickListener {
            dbEvent.eventDao().insert(DataEvent(
                item!!.link,
                item.imgfile,
                title,
                starttime,
                endtime,
                tabType,
                ""
            ))
            Toast.makeText(requireContext(), "Pick 성공!", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnRight!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(tabType!!)))
            startActivity(intent)
        }

        return v
    }

    fun getEventMrBlue(){
        val doc: Document = Jsoup.connect(item!!.link).get()

        Log.d("!!!!", doc.select(".event-html img").size.toString())

        if(doc.select(".event-html img").size > 1){
            val mrBlue1 = doc.select(".event-html img").first()!!.absUrl("src")

            requireActivity().runOnUiThread {
                Glide.with(mContext)
                    .load(mrBlue1.replace("http", "https"))
                    .into(iView!!)
            }
        } else if(doc.select(".event-html img").size < 1) {

            val mrBlue2 = doc.select(".event-visual img").first()!!.absUrl("src")

            requireActivity().runOnUiThread {
                Glide.with(mContext)
                    .load(mrBlue2)
                    .into(iView!!)
            }
        }

        title = item.title


    }

    fun getEventRidi(){
        val doc: Document = Jsoup.connect(item!!.link).get()
        val ridi = doc.select(".event_detail_top img").first()!!.absUrl("src")

        title = item.title
        starttime = item.startDate

        requireActivity().runOnUiThread {
            Glide.with(mContext)
                .load(ridi)
                .into(iView!!)
        }
    }

    fun getEventKakao() {
        val doc: Document = Jsoup.connect("https://page.kakao.com${item!!.link}").get()
        val kakao = doc.select(".themeBox img").first()!!.absUrl("src")

        Log.d("####", kakao.toString())

        title = item.title

        requireActivity().runOnUiThread {
            Glide.with(mContext)
                .load(kakao)
                .into(iView!!)
        }
    }

    private fun getUrl(type: String): String {
        if (type == "Joara") {
            return "https://www.joara.com/event/" + item!!.link!!.replace(
                "joaralink://event?event_id=",
                ""
            )
        }
        return ""
    }

    private fun getEventJoara() {

        wView!!.visibility = View.VISIBLE

        val mws = wView!!.settings

        mws.loadWithOverviewMode = true
        mws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        wView!!.overScrollMode = WebView.OVER_SCROLL_IF_CONTENT_SCROLLS
        WebView.setWebContentsDebuggingEnabled(true)
        mws.setSupportZoom(false) // 화면 줌 허용 여부
        wView!!.webChromeClient = WebChromeClient()
        wView!!.getSettings().setJavaScriptEnabled(true)

        val call: Call<JoaraEventDetailResult?>? =
            RetrofitJoara.getJoaraEventDetail(
                item!!.link!!.replace(
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

                        title = it.event.title!!
                        endtime = it.event.starttime!!
                        starttime = it.event.endtime!!


                        wView!!.loadDataWithBaseURL(
                            null,
                            content!!.replace("http", "https"),
                            "text/html; charset=utf-8",
                            "base64",
                            null
                        );
                    }
                }
            }

            override fun onFailure(call: Call<JoaraEventDetailResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}