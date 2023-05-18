package com.bigbigdw.moavara.Pick

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.bigbigdw.moavara.Best.ActivityBestDetail
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.DataBase.BestItemData
import com.bigbigdw.moavara.Util.SwipeHelperCallback
import com.bigbigdw.moavara.Util.dpToPx
import com.bigbigdw.moavara.databinding.FragmentPickTabBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.*

class FragmentPickTabNovel : Fragment() {

    private lateinit var adapter: AdapterPickNovel
    private val items = ArrayList<BestItemData>()

    private var _binding: FragmentPickTabBinding? = null
    private val binding get() = _binding!!
    private var UserInfo = DataBaseUser()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickTabBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAnalytics = Firebase.analytics

        UserInfo = (parentFragment as FragmentPick).UserInfo
        adapter = AdapterPickNovel(requireContext(), items, this@FragmentPickTabNovel, UserInfo, firebaseAnalytics)

        binding.blank.tviewblank.text = "마이픽을 한 작품이 없습니다."

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(adapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(64f.dpToPx())    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rviewPick)

        // 다른 곳 터치 시 기존 선택했던 뷰 닫기
        binding.rviewPick.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding.rviewPick)
            false
        }

        getEventTab()

        return view
    }

    private fun getEventTab() {

        binding.rviewPick.removeAllViews()
        items.clear()

        binding.rviewPick.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewPick.adapter = adapter

        mRootRef.child("User").child(UserInfo.UID).child("Novel").child("book").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (postSnapshot in dataSnapshot.children) {
                    val group: BestItemData? =
                        postSnapshot.getValue(BestItemData::class.java)
                    if (group != null) {
                        items.add(
                            BestItemData(
                                group.writer,
                                group.title,
                                group.bookImg,
                                group.bookCode,
                                group.info1,
                                group.info2,
                                group.info3,
                                group.info4,
                                group.info5,
                                group.info6,
                                group.number,
                                group.date,
                                group.type,
                                group.memo
                            )
                        )
                    }

                    if(items.isEmpty()){
                        binding.blank.root.visibility = View.VISIBLE
                    } else {
                        binding.blank.root.visibility = View.GONE
                    }

                    val cmpAsc: Comparator<BestItemData> =
                        Comparator { o1, o2 -> o2.number.compareTo(o1.number) }
                    Collections.sort(items, cmpAsc)
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        adapter.setOnItemClickListener(object : AdapterPickNovel.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, type: String) {
                val group: BestItemData = adapter.getItem(position)

                when (type) {
                    "Confirm" -> {
                        adapter.editItem(position)
                        Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT).show()
                        mRootRef.child("User").child(UserInfo.UID).child("Novel").child("book").child(group.bookCode).setValue(
                            BestItemData(
                                group.writer,
                                group.title,
                                group.bookImg,
                                group.bookCode,
                                group.info1,
                                group.info2,
                                group.info3,
                                group.info4,
                                group.info5,
                                group.info6,
                                group.number,
                                group.date,
                                group.type,
                                adapter.getItem(position).memo
                            )
                        )
                    }
                    "Item" -> {
                        if(group.type == "MrBlue"){
                            Toast.makeText(requireContext(), "미스터 블루는 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            val bundle = Bundle()
                            bundle.putString("BEST_FROM", "pick_novel")
                            firebaseAnalytics.logEvent("BEST_ActivityBestDetail", bundle)

                            val bookDetailIntent =
                                Intent(requireContext(), ActivityBestDetail::class.java)
                            bookDetailIntent.putExtra("BookCode", group.bookCode)
                            bookDetailIntent.putExtra("Type", String.format("%s", group.type))
                            bookDetailIntent.putExtra("POSITION", position)
                            bookDetailIntent.putExtra("HASDATA", true)
                            bookDetailIntent.putExtra("FROMPICK", true)
                            startActivity(bookDetailIntent)
                        }
                    }
                }
            }
        })
    }

    fun initScreen(itemCount : Int){
        if(itemCount == 0){
            val workManager = WorkManager.getInstance(requireContext().applicationContext)
            workManager.cancelAllWorkByTag("MoavaraPick")
            Toast.makeText(requireContext().applicationContext, "선호작 최신화 해제됨", Toast.LENGTH_SHORT).show()
            binding.blank.root.visibility = View.VISIBLE
        } else {
            binding.blank.root.visibility = View.GONE
        }
    }
}