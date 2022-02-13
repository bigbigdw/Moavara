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
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Joara.JoaraEventDetailResult
import com.example.moavara.Joara.JoaraEventResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.EventData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class BottomSheetDialogEvent(private val mContext: Context, private val item: EventData?, private val tabType: String?) :
    BottomSheetDialogFragment() {

    private var btnRight: Button? = null
    private var btnLeft: Button? = null
    private var rviewEvent: RecyclerView? = null
    private var wView: WebView? = null

    private var adapterEventDetail : AdapterEventDetail? = null
    private val items = ArrayList<String?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.bottom_dialog_event, container, false)

        btnRight = v.findViewById(R.id.btnRight)
        btnLeft = v.findViewById(R.id.btnLeft)
        rviewEvent = v.findViewById(R.id.rviewEvent)
        wView = v.findViewById(R.id.wView)

        adapterEventDetail = AdapterEventDetail(items)

        rviewEvent!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        if(tabType == "Joara"){
            getEventJoara()
        }

        Thread {

//            when (tabType) {
//                "Joara" -> {
//                    getEventJoara()
//                }
//                "Ridi" -> {
//                    getEventRidi()
//                }
//                "OneStore" -> {
//                    getEventOneStore()
//                }
//                "Kakao" -> {
//                    getEventKakao()
//                }
//                "Naver" -> {
//                    getEventNaver()
//                }
//                "MrBlue" -> {
//                    getEventMrBlue()
//                }
//            }
        }.start()

        btnRight!!.setOnClickListener {
            val url = "https://www.joara.com/event/" + item!!.link!!.replace("joaralink://event?event_id=","")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        return v
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
            RetrofitJoara.getJoaraEventDetail(item!!.link!!.replace("joaralink://event?event_id=",""))

        call!!.enqueue(object : Callback<JoaraEventDetailResult?> {
            override fun onResponse(
                call: Call<JoaraEventDetailResult?>,
                response: Response<JoaraEventDetailResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val content = it.event!!.content
                        wView!!.loadDataWithBaseURL(null, content!!.replace("http","https"), "text/html; charset=utf-8", "base64",null);
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