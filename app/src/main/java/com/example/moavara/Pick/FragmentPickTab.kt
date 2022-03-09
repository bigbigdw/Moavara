package com.example.moavara.Pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.DataBase.DataPickEvent
import com.example.moavara.Event.BottomSheetDialogEvent
import com.example.moavara.Search.EventData
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentPickTabBinding


class FragmentPickTab(val pickType : String? = null) : Fragment() {

    private lateinit var adapter: AdapterPickEvent
    private var cate = ""
    private val items = ArrayList<EventData>()
    private lateinit var dbPickEvent: DataPickEvent

    private var _binding: FragmentPickTabBinding? = null
    private val binding get() = _binding!!

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickTabBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()

        dbPickEvent =
            Room.databaseBuilder(requireContext(), DataPickEvent::class.java, pickType!!)
                .allowMainThreadQueries().build()

        adapter = AdapterPickEvent(items)

        getEventTab()

        return view
    }

    private fun getEventTab() {

        binding.rviewPick.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewPick.adapter = adapter

        val eventlist = dbPickEvent.eventDao().getAll()

        for(i in eventlist.indices){

            items.add(
                EventData(
                    eventlist[i].link,
                    eventlist[i].imgfile,
                    eventlist[i].title,
                    eventlist[i].genre,
                    eventlist[i].type,
                    eventlist[i].memo
                )
            )
            adapter.notifyDataSetChanged()
        }

        adapter.setOnItemClickListener(object : AdapterPickEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, type : String) {
                val item: EventData = adapter.getItem(position)

                if(type == "Img"){

                    if(pickType == "pick-novel"){
//                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.item!!, item.type, cate)
//                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                    } else {
                        if(item.type == "Joara" && !item.link.contains("joaralink://event?event_id=")){
                            Toast.makeText(requireContext(), "이벤트 페이지가 아닙니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            val mBottomSheetDialogEvent =
                                BottomSheetDialogEvent(requireContext(), item, item.type)
                            fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
                        }
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

                    dbPickEvent.eventDao().updateItem(adapter.getMemoEdit(), item.link)

                    Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT).show()
                }  else if(type == "Delete"){
                    dbPickEvent.eventDao().deleteItem(item.link)
                    items.remove(item)
                    adapter.notifyItemRemoved(position)

                    Toast.makeText(requireContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}