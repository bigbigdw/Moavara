package com.example.moavara.Pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.Best.BottomDialogBest
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.DataBase.DataPickEvent
import com.example.moavara.Event.BottomSheetDialogEvent
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.EventData
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentPickTabBinding

class FragmentPickTabNovel : Fragment() {

    private lateinit var adapter: AdapterPickNovel
    private var cate = ""
    private val items = ArrayList<BookListDataBestToday>()
    private lateinit var dbPickEvent: DataBaseBestDay

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
            Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java,"pick-novel")
                .allowMainThreadQueries().build()

        adapter = AdapterPickNovel(items)

        getEventTab()

        return view
    }

    private fun getEventTab() {

        binding.rviewPick.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewPick.adapter = adapter

        val eventlist = dbPickEvent.bestDao().getAll("ALL")

        for(i in eventlist.indices){

            items.add(
                BookListDataBestToday(
                    eventlist[i].writer,
                    eventlist[i].title,
                    eventlist[i].bookImg,
                    eventlist[i].bookCode,
                    eventlist[i].info1,
                    eventlist[i].info2,
                    eventlist[i].info3,
                    eventlist[i].info4,
                    eventlist[i].info5,
                    eventlist[i].number,
                    eventlist[i].numberDiff,
                    eventlist[i].date,
                    eventlist[i].type,
                    ""
                )
            )
            adapter.notifyDataSetChanged()
        }

        adapter.setOnItemClickListener(object : AdapterPickNovel.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, type : String) {
                val item: BookListDataBestToday = adapter.getItem(position)

                val data = BookListDataBestToday(
                    item.writer,
                    item.title,
                    item.bookImg,
                    item.bookCode,
                    item.info1,
                    item.info2,
                    item.info3,
                    item.info4,
                    item.info5,
                    item.number,
                    item.numberDiff,
                    item.date,
                    item.status,
                    adapter.getMemoEdit()
                )

                if(type == "Img"){
                    val mBottomDialogBest = BottomDialogBest(requireContext(), data, item.status, cate)
                    fragmentManager?.let { mBottomDialogBest.show(it, null) }
                } else if(type == "Confirm"){

                    adapter.editItem(data, position)
                    dbPickEvent.bestDao().updateItem(adapter.getMemoEdit(), item.bookCode)

                    Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT).show()
                }  else if(type == "Delete"){
                    dbPickEvent.bestDao().deleteItem(item.bookCode)
                    items.remove(item)
                    adapter.notifyItemRemoved(position)

                    Toast.makeText(requireContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}