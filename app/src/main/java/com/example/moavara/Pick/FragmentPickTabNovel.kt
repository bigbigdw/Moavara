package com.example.moavara.Pick

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.Main.mRootRef
import com.example.moavara.Util.Genre
import com.example.moavara.Util.SwipeHelperCallback
import com.example.moavara.databinding.FragmentPickTabBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FragmentPickTabNovel : Fragment() {

    private lateinit var adapter: AdapterPickNovel
    private var cate = ""
    private val items = ArrayList<BookListDataBest>()

    private var _binding: FragmentPickTabBinding? = null
    private val binding get() = _binding!!
    var UID = ""
    var userInfo = mRootRef.child("User")

    var status = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickTabBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()

        UID = context?.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("UID", "").toString()

        adapter = AdapterPickNovel(requireContext(), items)

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(adapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)    // 1080 / 4 = 270
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

        binding.rviewPick.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewPick.adapter = adapter

        userInfo.child(UID).child("Novel").child("book").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    if (group != null) {
                        items.add(
                            BookListDataBest(
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
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })



        adapter.setOnItemClickListener(object : AdapterPickNovel.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, type: String) {
                val group: BookListDataBest = adapter.getItem(position)

                val data = BookListDataBest(
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

                if (type == "Img") {
//                    val mBottomDialogBest = BottomDialogBest(
//                        requireContext(),
//                        data,
//                        item.status,
//                        item.number
//                    )
//                    fragmentManager?.let { mBottomDialogBest.show(it, null) }
                } else if (type == "Confirm") {

                    adapter.editItem(data, position)
//                    dbPickEvent.bestDao().updateItem(adapter.getMemoEdit(), item.bookCode)

                    Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT).show()
                } else if (type == "Delete") {
//                    dbPickEvent.bestDao().deleteItem(item.bookCode)
//                    items.remove(item)
                    adapter.notifyItemRemoved(position)

                    Toast.makeText(requireContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}