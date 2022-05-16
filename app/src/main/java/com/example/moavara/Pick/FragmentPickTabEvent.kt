package com.example.moavara.Pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.DataBase.DataPickEvent
import com.example.moavara.Event.BottomSheetDialogEvent
import com.example.moavara.Main.mRootRef
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.EventData
import com.example.moavara.Search.UserPickBook
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentPickTabBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FragmentPickTabEvent() : Fragment() {

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

        cate = Genre.getGenre(requireContext()).toString()



        adapter = AdapterPickEvent(items)

        getEventTab()

        return view
    }

    private fun getEventTab() {

        binding.rviewPick.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewPick.adapter = adapter

        userInfo.child(UID).child("event").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: EventData? = postSnapshot.getValue(EventData::class.java)
                    items.add(
                        EventData(
                            group!!.link,
                            group.imgfile,
                            group.title,
                            group.genre,
                            group.type,
                            group.memo
                        )
                    )
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })


        adapter.setOnItemClickListener(object : AdapterPickEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, type : String) {
                val item: EventData = adapter.getItem(position)

                if(type == "Img"){

                    if(item.type == "Joara" && !item.link.contains("joaralink://event?event_id=")){
                        Toast.makeText(requireContext(), "이벤트 페이지가 아닙니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val mBottomSheetDialogEvent =
                            BottomSheetDialogEvent(requireContext(), item, item.type)
                        fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
                    }


                } else if(type == "Confirm"){

                    val data = EventData(
                        item.link,
                        item.imgfile,
                        item.title,
                        item.genre,
                        item.type,
                        adapter.getMemoEdit()
                    )

                    adapter.editItem(data, position)

//                    dbPickEvent.eventDao().updateItem(adapter.getMemoEdit(), item.link)

                    Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT).show()
                }  else if(type == "Delete"){
//                    dbPickEvent.eventDao().deleteItem(item.link)
                    items.remove(item)
                    adapter.notifyItemRemoved(position)

                    Toast.makeText(requireContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}