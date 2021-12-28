package com.example.takealook.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takealook.Search.BookListDataBestToday
import com.example.takealook.R
import java.util.ArrayList

import androidx.room.Room
import com.example.takealook.DataBase.DataBaseJoara


class FragmentBestWeekend : Fragment() {


    private lateinit var db: DataBaseJoara

    private var adapterWeek: AdapterBestWeekend? = null

    private val items = ArrayList<ArrayList<BookListDataBestToday?>?>()

    private val itemsMon = ArrayList<BookListDataBestToday?>()
    private val itemsTue = ArrayList<BookListDataBestToday?>()
    private val itemsWed = ArrayList<BookListDataBestToday?>()
    private val itemsThur = ArrayList<BookListDataBestToday?>()
    private val itemsFri = ArrayList<BookListDataBestToday?>()
    private val itemsSat = ArrayList<BookListDataBestToday?>()
    private val itemsSun = ArrayList<BookListDataBestToday?>()

    var recyclerView: RecyclerView? = null


    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_best_weekend, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)

        adapterWeek = AdapterBestWeekend(requireContext(), items)

        db = Room.databaseBuilder(
            requireContext(),
            DataBaseJoara::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val mon = db.bestDao().getMon()
        val tue = db.bestDao().getTue()
        val wed = db.bestDao().getWed()
        val thur = db.bestDao().getThu()
        val fri = db.bestDao().getFri()
        val sat = db.bestDao().getSat()
        val sun = db.bestDao().getSun()

        for (i in mon.indices) {
            itemsMon.add(
                BookListDataBestToday(
                    mon[i].writer,
                    mon[i].title,
                    mon[i].bookImg,
                    mon[i].intro,
                    mon[i].bookCode,
                    mon[i].cntChapter,
                    mon[i].cntPageRead,
                    mon[i].cntFavorite,
                    mon[i].cntRecom,
                    i + 1,
                    false
                )
            )
        }

        items.add(itemsMon)

        for (i in tue.indices) {
            itemsTue.add(
                BookListDataBestToday(
                    tue[i].writer,
                    tue[i].title,
                    tue[i].bookImg,
                    tue[i].intro,
                    tue[i].bookCode,
                    tue[i].cntChapter,
                    tue[i].cntPageRead,
                    tue[i].cntFavorite,
                    tue[i].cntRecom,
                    i + 1,
                    false
                )
            )
        }

        items.add(itemsTue)

        for (i in wed.indices) {
            itemsWed.add(
                BookListDataBestToday(
                    wed[i].writer,
                    wed[i].title,
                    wed[i].bookImg,
                    wed[i].intro,
                    wed[i].bookCode,
                    wed[i].cntChapter,
                    wed[i].cntPageRead,
                    wed[i].cntFavorite,
                    wed[i].cntRecom,
                    i + 1,
                    false
                )
            )
        }

        items.add(itemsWed)

        for (i in thur.indices) {
            itemsThur.add(
                BookListDataBestToday(
                    thur[i].writer,
                    thur[i].title,
                    thur[i].bookImg,
                    thur[i].intro,
                    thur[i].bookCode,
                    thur[i].cntChapter,
                    thur[i].cntPageRead,
                    thur[i].cntFavorite,
                    thur[i].cntRecom,
                    i + 1,
                    false
                )
            )
        }

        items.add(itemsThur)

        for (i in fri.indices) {
            itemsFri.add(
                BookListDataBestToday(
                    fri[i].writer,
                    fri[i].title,
                    fri[i].bookImg,
                    fri[i].intro,
                    fri[i].bookCode,
                    fri[i].cntChapter,
                    fri[i].cntPageRead,
                    fri[i].cntFavorite,
                    fri[i].cntRecom,
                    i + 1,
                    false
                )
            )
        }

        items.add(itemsFri)

        for (i in sat.indices) {
            itemsSat.add(
                BookListDataBestToday(
                    sat[i].writer,
                    sat[i].title,
                    sat[i].bookImg,
                    sat[i].intro,
                    sat[i].bookCode,
                    sat[i].cntChapter,
                    sat[i].cntPageRead,
                    sat[i].cntFavorite,
                    sat[i].cntRecom,
                    i + 1,
                    false
                )
            )
        }

        items.add(itemsSat)

        for (i in sun.indices) {
            itemsSun.add(
                BookListDataBestToday(
                    sun[i].writer,
                    sun[i].title,
                    sun[i].bookImg,
                    sun[i].intro,
                    sun[i].bookCode,
                    sun[i].cntChapter,
                    sun[i].cntPageRead,
                    sun[i].cntFavorite,
                    sun[i].cntRecom,
                    i + 1,
                    false
                )
            )
        }

        items.add(itemsSun)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapterWeek
        adapterWeek!!.notifyDataSetChanged()

        adapterWeek!!.setOnItemClickListener(object : AdapterBestWeekend.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: ArrayList<BookListDataBestToday?>? = adapterWeek!!.getItem(position)

                if (item != null) {
                    for (i in item) {
                        Log.d("####","HIHI")
                    }
                }
            }
        })

        return root
    }




}