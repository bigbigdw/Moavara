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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.DataEvent
import com.example.moavara.DataBase.DataPickEvent
import com.example.moavara.Joara.JoaraEventDetailResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.R
import com.example.moavara.Search.EventData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
    private var rviewEvent: RecyclerView? = null
    private var wView: WebView? = null

    private var adapterEventDetail: AdapterEventDetail? = null
    private val items = ArrayList<String?>()
    private var title : String? = null
    private var endtime : String? = null
    private var starttime : String? = null

    private lateinit var dbEvent: DataPickEvent

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

        dbEvent = Room.databaseBuilder(requireContext(), DataPickEvent::class.java, "pick-event")
            .allowMainThreadQueries().build()

        rviewEvent!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        if (tabType == "Joara") {
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