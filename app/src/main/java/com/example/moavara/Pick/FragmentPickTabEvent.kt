package com.example.moavara.Pick

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Main.mRootRef
import com.example.moavara.Search.EventData
import com.example.moavara.Util.Genre
import com.example.moavara.Util.SwipeEvent
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.FragmentPickTabBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.net.URLDecoder
import java.util.*


class FragmentPickTabEvent : Fragment() {

    private lateinit var adapter: AdapterPickEvent
    private var cate = ""
    private val items = ArrayList<EventData>()

    private var _binding: FragmentPickTabBinding? = null
    private val binding get() = _binding!!

    var status = ""
    var UID = ""
    var userInfo = mRootRef.child("User")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickTabBinding.inflate(inflater, container, false)
        val view = binding.root

        UID = context?.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("UID", "").toString()

        binding.blank.tviewblank.text = "마이픽을 한 이벤트가 없습니다."

        cate = Genre.getGenre(requireContext()).toString()

        adapter = AdapterPickEvent(requireContext(), items, this@FragmentPickTabEvent)

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

        binding.rviewPick.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewPick.adapter = adapter

        userInfo.child(UID).child("Event").addListenerForSingleValueEvent(object :
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

                if(items.isEmpty()){
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
            override fun onItemClick(v: View?, position: Int, type : String) {
                val item: EventData = adapter.getItem(position)

                if(type == "Item"){

                    if(item.type == "Kakao_Stage"){
                        getEventKakao(item.link)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(item.type, item.link)))
                        startActivity(intent)
                    }

                } else if(type == "Confirm"){
                    adapter.editItem(position)
                    Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT).show()
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

    private fun getUrl(type: String, link : String): String {
        when (type) {
            "Joara" -> {
                return "https://www.joara.com/event/$link"
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

    override fun onDetach() {
        for(i in items.indices){
            userInfo.child(UID).child("Event").child(items[i].link).child("number").setValue((items.size - i))
        }
        super.onDetach()
    }

    fun initScreen(itemCount : Int){
        if(itemCount == 0){
            binding.blank.root.visibility = View.VISIBLE
        } else {
            binding.blank.root.visibility = View.GONE
        }
    }
}