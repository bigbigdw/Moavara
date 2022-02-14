package com.example.moavara.Pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.Best.AdapterBestToday
import com.example.moavara.Best.BottomDialogBest
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataPickEvent
import com.example.moavara.Event.BottomSheetDialogEvent
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.EventData
import com.example.moavara.Util.BestRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class FragmentPickTabEvent : Fragment() {

    private var adapter: AdapterPickEvent? = null
    var recyclerView: RecyclerView? = null

    private val items = ArrayList<EventData?>()
    private lateinit var dbPickEvent: DataPickEvent

    var status = ""

    lateinit var root: View

    val Genre = "ALL"
    private lateinit var dbWeek: DataBaseBestDay

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_pick_tab, container, false)

        dbPickEvent =
            Room.databaseBuilder(requireContext(), DataPickEvent::class.java, "pick-event")
                .allowMainThreadQueries().build()

        recyclerView = root.findViewById(R.id.rviewPick)
        adapter = AdapterPickEvent(items)

        getEventTab()

        return root
    }

    private fun getEventTab() {

        recyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.adapter = adapter

        val eventlist = dbPickEvent.eventDao().getAll()

        for(i in eventlist.indices){
            items.add(
                EventData(
                    eventlist[i].link,
                    eventlist[i].imgfile,
                    eventlist[i].title,
                    eventlist[i].startDate,
                    eventlist[i].endDate,
                    eventlist[i].type,
                    eventlist[i].memo,
                )
            )
            adapter!!.notifyDataSetChanged()
        }

        adapter!!.setOnItemClickListener(object : AdapterPickEvent.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: EventData? = adapter!!.getItem(position)

                if(item!!.type == "Joara" && !item.link!!.contains("joaralink://event?event_id=")){
                    Toast.makeText(requireContext(), "이벤트 페이지가 아닙니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val mBottomSheetDialogEvent =
                        BottomSheetDialogEvent(requireContext(), item, item.type)
                    fragmentManager?.let { mBottomSheetDialogEvent.show(it, null) }
                }
            }
        })
    }
}