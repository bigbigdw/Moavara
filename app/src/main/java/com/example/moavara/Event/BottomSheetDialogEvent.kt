package com.example.moavara.Event

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Retrofit.JoaraEventDetailResult
import com.example.moavara.Retrofit.RetrofitDataListener
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Search.EventData
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.BottomDialogEventBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class BottomSheetDialogEvent(
    private val mContext: Context,
    private val item: EventData,
    private val tabType: String = ""
) :
    BottomSheetDialogFragment() {

    var cate = ""

    private var _binding: BottomDialogEventBinding? = null
    private val binding get() = _binding!!
    private var title : String = ""
    var UID = ""
    var userInfo = mRootRef.child("User")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomDialogEventBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()

        UID = context?.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("UID", "").toString()

        binding.iview.background = GradientDrawable().apply {
            setColor(Color.TRANSPARENT)
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(
                20f.dpToPx(),
                20f.dpToPx(),
                20f.dpToPx(),
                20f.dpToPx(),
                0f,
                0f,
                0f,
                0f
            )
        }

        if (tabType == "OneStore"
            || tabType == "Munpia"
            || (tabType == "Kakao" && item.link.contains("kakaopage://exec?open_web_with_auth/store/event"))
            || tabType == "Toksoda"
        ) {
            Glide.with(mContext).load(item.imgfile).into(binding.iview)
        } else {
            when (tabType) {
                "Joara" -> {
                    getEventJoara()
                }
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
        }

        with(binding){
            btnRight.setOnClickListener {
                val group = EventData(
                    item.link,
                    item.imgfile,
                    item.title,
                    item.data,
                    item.date,
                    item.type,
                    "",
                )

                userInfo.child(UID).child("Event").child(item.link).setValue(group)

                Toast.makeText(requireContext(), "Pick 성공!", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            btnLeft.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(tabType)))
                startActivity(intent)
            }
        }



        return view
    }

    private fun getEventMrBlue(){
        Thread {
            val doc: Document = Jsoup.connect(item.link).get()

            if(doc.select(".event-html img").size > 1){
                val mrBlue1 = doc.select(".event-html img").first()?.absUrl("src")
                requireActivity().runOnUiThread {
                    if (mrBlue1 != null) {
                        binding?.let {
                            Glide.with(mContext)
                                .load(mrBlue1.replace("http", "https"))
                                .into(it.iview)
                        }
                    }
                }
            } else if(doc.select(".event-html img").size < 1) {

                val mrBlue2 = doc.select(".event-visual img").first()?.absUrl("src")
                title = item.title

                requireActivity().runOnUiThread {
                    Glide.with(mContext)
                        .load(mrBlue2)
                        .into(binding.iview)
                }
            }
        }.start()
    }

    private fun getEventRidi(){

        Thread {
            val doc: Document = Jsoup.connect(item.link).get()
            val ridi = doc.select(".event_detail_top img").first()?.absUrl("src")

            title = item.title

            requireActivity().runOnUiThread {
                Glide.with(mContext)
                    .load(ridi)
                    .into(binding.iview)
            }
        }.start()
    }

    private fun getEventKakao() {
        Thread {
            val doc: Document = Jsoup.connect("https://page.kakao.com${item.link}").get()
            val kakao = doc.select(".themeBox img").first()?.absUrl("src")

            title = item.title

            requireActivity().runOnUiThread {
                Glide.with(mContext)
                    .load(kakao)
                    .into(binding.iview)
            }
        }.start()
    }

    private fun getUrl(type: String): String {
        when (type) {
            "Joara" -> {
                return "https://www.joara.com/event/" + item.link
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

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["event_id"] = item.link

        apiJoara.getJoaraEventDetail(
            param,
            object : RetrofitDataListener<JoaraEventDetailResult> {
                override fun onSuccess(data: JoaraEventDetailResult) {
                    val content = data.event?.content

                    title = data.event?.title ?: ""

                    binding.wView.loadDataWithBaseURL(
                        null,
                        content?.replace("http://", "https://") ?: "",
                        "text/html; charset=utf-8",
                        "utf-8",
                        null
                    )
                }
            })

//        if(item.link.contains("joaralink://event?event_id=")){
//
//            val apiJoara = RetrofitJoara()
//            val param = Param.getItemAPI(context)
//            param["event_id"] = item.link
//
//
//            apiJoara.getJoaraEventDetail(
//                param,
//                object : RetrofitDataListener<JoaraEventDetailResult> {
//                    override fun onSuccess(it: JoaraEventDetailResult) {
//                        val content = it.event?.content
//
//                        title = it.event?.title ?: ""
//
//                        binding.wView.loadDataWithBaseURL(
//                            null,
//                            content?.replace("http://", "https://") ?: "",
//                            "text/html; charset=utf-8",
//                            "utf-8",
//                            null
//                        )
//                    }
//                })
//
//        } else {
//            val apiJoara = RetrofitJoara()
//            val param = Param.getItemAPI(context)
//            param["notice_id"] = item.link.replace(
//                "joaralink://notice?notice_id=",
//                ""
//            )
//
//            apiJoara.getNoticeDetail(
//                param,
//                object : RetrofitDataListener<JoaraNoticeDetailResult> {
//                    override fun onSuccess(it: JoaraNoticeDetailResult) {
//
//                        val content = it.notice?.content
//
//                        title = it.notice.title
//
//                        binding.wView.loadDataWithBaseURL(
//                            null,
//                            content.replace("<br />", ""),
//                            "text/html; charset=utf-8",
//                            "base64",
//                            null
//                        )
//                    }
//                })
//        }


    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}