package com.bigbigdw.moavara.Pick

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Event.BottomSheetDialogEvent
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.Search.EventData
import com.bigbigdw.moavara.Util.SwipeEvent
import com.bigbigdw.moavara.Util.dpToPx
import com.bigbigdw.moavara.databinding.FragmentPickTabBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*


class FragmentPickTabEvent : Fragment() {

    private lateinit var adapter: AdapterPickEvent
    private val items = ArrayList<EventData>()

    private var _binding: FragmentPickTabBinding? = null
    private val binding get() = _binding!!
    private var UserInfo = DataBaseUser()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickTabBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.blank.tviewblank.text = "마이픽을 한 이벤트가 없습니다."
        UserInfo = (parentFragment as FragmentPick).UserInfo

        firebaseAnalytics = Firebase.analytics

        adapter = AdapterPickEvent(requireContext(), items, this@FragmentPickTabEvent, UserInfo, firebaseAnalytics)

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeEvent(adapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(64f.dpToPx())    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rviewPick)

        getEventTab()

        return view
    }

    private fun getEventTab() {

        binding.rviewPick.removeAllViews()
        items.clear()

        binding.rviewPick.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewPick.adapter = adapter

        mRootRef.child("User").child(UserInfo.UID).child("Event")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (postSnapshot in dataSnapshot.children) {
                        val group: EventData? = postSnapshot.getValue(EventData::class.java)

                        if (group != null) {
                            items.add(
                                EventData(
                                    group.link,
                                    group.imgfile,
                                    group.title,
                                    group.data,
                                    group.date,
                                    group.number,
                                    group.type,
                                    group.memo
                                )
                            )
                        }
                    }

                    if (items.isEmpty()) {
                        binding.blank.root.visibility = View.VISIBLE
                    } else {
                        binding.blank.root.visibility = View.GONE
                    }
                    val cmpAsc: Comparator<EventData> =
                        Comparator { o1, o2 -> o2.number.compareTo(o1.number) }
                    Collections.sort(items, cmpAsc)
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })


        adapter.setOnItemClickListener(object : AdapterPickEvent.OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun onItemClick(v: View?, position: Int, type: String) {
                val item: EventData = adapter.getItem(position)

                if (type == "Item") {

                    if (item.type == "Kakao_Stage") {
                        getEventKakao(item.link)
                    } else if (item.type == "OneStore") {
                        Toast.makeText(requireContext(), "원스토리는 지원하지 않습니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else if (getUrl(item.type, item.link).isEmpty()) {
                        Toast.makeText(requireContext(), "지원하지 않는 이벤트 형식입니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val mBottomSheetDialogEvent =
                            BottomSheetDialogEvent(requireContext(), item, item.type, UserInfo, firebaseAnalytics ,true)
                        fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
                    }

                } else if (type == "Confirm") {
                    adapter.editItem(position)
                    Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT).show()

                    val ref: DatabaseReference

                    if(item.type == "Joara"){
                        ref = mRootRef.child("User").child(UserInfo.UID).child("Event").child(URLEncoder.encode(item.link, "utf-8"))
                    } else {
                        ref = mRootRef.child("User").child(UserInfo.UID).child("Event").child(item.link)
                    }

                    ref.setValue(
                        EventData(
                            item.link,
                            item.imgfile,
                            item.title,
                            item.data,
                            item.date,
                            item.number,
                            item.type,
                            adapter.getItem(position).memo
                        )
                    )
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getEventKakao(link: String) {
        val linkUrl = "kakaopage://exec?open_web_with_auth/store/event/v2/${link}"

        val intent = Intent.parseUri(linkUrl, Intent.URI_INTENT_SCHEME)
        val existPackage = requireContext().packageManager.getLaunchIntentForPackage(
            "com.kakao.page"
        )

        if (existPackage != null) {
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            requireContext().startActivity(intent)
        } else {
            val marketIntent = Intent(Intent.ACTION_VIEW)
            marketIntent.data = Uri.parse("market://details?id=com.kakao.page")
            requireContext().startActivity(marketIntent)
        }
    }

    private fun getUrl(type: String, link: String): String {
        when (type) {
            "Joara" -> {
                if (link.contains("joaralink://event?event_id=")) {
                    return "https://www.joara.com/event/" + link.replace(
                        "joaralink://event?event_id=",
                        ""
                    )
                } else if (link.contains("joaralink://notice?notice_id=")) {
                    return "https://www.joara.com/notice/" + link.replace(
                        "joaralink://notice?notice_id=",
                        ""
                    )
                } else {
                    return ""
                }
            }
            "Ridi" -> {
                return "https://ridibooks.com/event/${link}"
            }
            "Kakao_Stage" -> {
                return "https://page.kakao.com${link}"
            }
            "Munpia" -> {
                return "https://square.munpia.com/${URLDecoder.decode(link, "utf-8")}"
            }
            "MrBlue" -> {
                return "https://www.mrblue.com/event/detail/${link}"
            }
            "Toksoda" -> {
                return "https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=${link}"
            }
            else -> return ""
        }
    }

    fun initScreen(itemCount: Int) {
        if (itemCount == 0) {
            binding.blank.root.visibility = View.VISIBLE
        } else {
            binding.blank.root.visibility = View.GONE
        }
    }
}