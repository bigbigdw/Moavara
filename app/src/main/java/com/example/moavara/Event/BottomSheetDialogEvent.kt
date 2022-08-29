package com.example.moavara.Event

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Retrofit.JoaraEventDetailResult
import com.example.moavara.Retrofit.JoaraNoticeDetailResult
import com.example.moavara.Retrofit.RetrofitDataListener
import com.example.moavara.Retrofit.RetrofitJoara
import com.example.moavara.Search.EventData
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Param
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.BottomDialogEventBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLDecoder

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
    private var isPicked = false

    @RequiresApi(Build.VERSION_CODES.R)
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

        userInfo.child(UID).child("Event").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(pickedItem in dataSnapshot.children){

                    if(pickedItem.key.toString() == item.link){
                        isPicked = true
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#A7ACB7"))
                            shape = GradientDrawable.RECTANGLE
                        }

                        binding.tviewPick.text = "Pick 완료"
                        break
                    } else {
                        binding.llayoutPick.background = GradientDrawable().apply {
                            setColor(Color.parseColor("#621CEF"))
                            shape = GradientDrawable.RECTANGLE
                        }

                        binding.tviewPick.text = "Pick 하기"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        if (tabType == "OneStore"
            || (tabType == "Ridi" && item.link.contains("https://ridibooks.com/books"))
            || tabType == "Munpia"
            || (tabType == "Kakao")
            || tabType == "Toksoda"
        ) {

            Glide.with(mContext).load(item.imgfile).into(binding.iview)
            binding.llayoutWrapResult.setOnClickListener{
                dismiss()
            }
        } else {
            when (tabType) {
                "Joara" -> {
                    getEventJoara()
                }
                "Ridi" -> {
                    getEventRidi()
                }
                "MrBlue" -> {
                    getEventMrBlue()
                }
                "Kakao_Stage" -> {
                    getEventKakaoStage()
                }
            }
        }

        with(binding){
            llayoutPick.setOnClickListener {

                if(isPicked){
                    userInfo.child(UID).child("Event").child(item.link).removeValue()
                    Toast.makeText(requireContext(), "[${item.title}]이(가) 마이픽에서 제거되었습니다.", Toast.LENGTH_SHORT).show()
                    dismiss()

                } else {

                    val group = EventData(
                        item.link,
                        item.imgfile,
                        item.title,
                        item.data,
                        item.date,
                        item.number,
                        item.type,
                        "",
                    )

                    userInfo.child(UID).child("Event").child(item.link).setValue(group)
                    Toast.makeText(requireContext(), "[${group.title}]이(가) 마이픽에 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    dismiss()

                }
            }

            btnLeft.setOnClickListener {

                if(item.type == "Kakao"){
                    getEventKakao()
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(tabType)))
                    startActivity(intent)
                }
            }
        }



        return view
    }

    private fun getEventMrBlue(){
        Thread {
            val doc: Document = Jsoup.connect("https://www.mrblue.com/event/detail/${item.link}" ).get()

            if(doc.select(".event-html img").size > 1){
                val mrBlue1 = doc.select(".event-html img").first()?.absUrl("src")
                requireActivity().runOnUiThread {
                    if (mrBlue1 != null) {
                        binding.let {
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
            val doc: Document
            val ridi : String

            if (item.link.contains("https://ridibooks.com/books/")) {
                ridi = item.link
            } else {
                doc = Jsoup.connect("https://ridibooks.com/event/${item.link}").get()
                ridi = doc.select(".event_detail_top img").first()?.absUrl("src").toString()
            }

            title = item.title

            requireActivity().runOnUiThread {
                Glide.with(mContext)
                    .load(ridi)
                    .into(binding.iview)
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getEventKakao() {
        val linkUrl = "kakaopage://exec?open_web_with_auth/store/event/v2/${item.link}"

        val intent = Intent.parseUri(linkUrl, Intent.URI_INTENT_SCHEME)
        val existPackage = mContext.packageManager.getLaunchIntentForPackage(
            "com.kakao.page"
        )

        if (existPackage != null) {
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            mContext.startActivity(intent)
        } else {
            val marketIntent = Intent(Intent.ACTION_VIEW)
            marketIntent.data = Uri.parse("market://details?id=com.kakao.page")
            mContext.startActivity(marketIntent)
        }
    }

    private fun getUrl(type: String): String {
        when (type) {
            "Joara" -> {
                return "https://www.joara.com/event/" + item.link
            }
            "Ridi" -> {
                return if (item.link.contains("https://ridibooks.com/books/")) {
                    item.link
                } else {
                    "https://ridibooks.com/event/${item.link}"
                }
            }
            "Kakao" -> {
                return "https://page.kakao.com${item.link}"
            }
            "Kakao_Stage" -> {
                return "https://pagestage.kakao.com/events/${item.link}"
            }
            "Munpia" -> {
                return "https://square.munpia.com/${URLDecoder.decode(item.link, "utf-8")}"
            }
            "MrBlue" -> {
                return "https://www.mrblue.com/event/detail/${item.link}"
            }
            "Toksoda" -> {
                return "https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=${item.link}"
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

            val apiJoara = RetrofitJoara()
            val param = Param.getItemAPI(context)
            param["event_id"] = item.link.replace("joaralink://event?event_id=","")

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

        } else if(item.link.contains("joaralink://notice?notice_id=")) {
            val apiJoara = RetrofitJoara()
            val param = Param.getItemAPI(context)
            param["notice_id"] = item.link.replace(
                "joaralink://notice?notice_id=",
                ""
            )

            apiJoara.getNoticeDetail(
                param,
                object : RetrofitDataListener<JoaraNoticeDetailResult> {
                    override fun onSuccess(it: JoaraNoticeDetailResult) {

                        val content = it.notice?.content

                        title = it.notice?.title ?: ""

                        binding.wView.loadDataWithBaseURL(
                            null,
                            content?.replace("<br />", "") ?: "",
                            "text/html; charset=utf-8",
                            "base64",
                            null
                        )
                    }
                })
        } else {
            Glide.with(mContext).load(item.imgfile).into(binding.iview)
            binding.llayoutWrapResult.setOnClickListener{
                dismiss()
            }
        }

    }

    private fun getEventKakaoStage(){
        Glide.with(mContext).load(item.data).into(binding.iview)
        binding.llayoutWrapResult.setOnClickListener{
            dismiss()
        }
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}