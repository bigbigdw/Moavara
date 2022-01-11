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

import androidx.room.Room
import com.example.takealook.DataBase.DataBaseJoara
import com.example.takealook.Search.BookListDataBestWeekend
import java.util.*


class FragmentBestWeekend : Fragment() {


    private lateinit var db: DataBaseJoara

    private var adapterWeek: AdapterBestWeekend? = null

    private val itemWeek = ArrayList<BookListDataBestWeekend?>()

    var recyclerView: RecyclerView? = null


    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_best_weekend, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)

        adapterWeek = AdapterBestWeekend(requireContext(), itemWeek)

        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        db = Room.databaseBuilder(
            requireContext(),
            DataBaseJoara::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val mon = db.bestDao().getMon()
        val tue = db.bestDao().getTue()
        val wed = db.bestDao().getWed()
        val thur = db.bestDao().getThu()
        val fri = db.bestDao().getFri()
        val sat = db.bestDao().getSat()
        val sun = db.bestDao().getSun()

        val today = db.bestDao().selectWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))

        Log.d("!!!!", db.bestDao().selectWeek(day).toString())

        for(i in tue.indices){
            Log.d("!!!!", tue[i].title!!)
        }

        for (i in today.indices){

            itemWeek.add(
                BookListDataBestWeekend(
                    if(mon.isNotEmpty()){
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
                    } else null,
                    if(tue.isNotEmpty()){
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
                    } else null,
                    if(wed.isNotEmpty()){
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
                    } else null,
                    if(thur.isNotEmpty()){
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
                    } else null,
                    if(fri.isNotEmpty()){
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
                    } else null,
                    if(sat.isNotEmpty()){
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
                    } else null,
                    if(sun.isNotEmpty()){
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
                    } else null,
                )
            )
        }

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapterWeek
        adapterWeek!!.notifyDataSetChanged()

        adapterWeek!!.setOnItemClickListener(object : AdapterBestWeekend.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestWeekend? = adapterWeek!!.getItem(position)

                Log.d("####", "position = $position")

                if(item!!.mon != null){
                    Log.d("####", "mon" + item.mon!!.bookCode.toString())
                } else if (item.tue != null){
                    Log.d("####", "tue" + item.tue!!.bookCode.toString())
                }else if (item.wed != null){
                    Log.d("####", "wed" + item.wed!!.bookCode.toString())
                }else if (item.thur != null){
                    Log.d("####", "thur" + item.thur!!.bookCode.toString())
                }else if (item.fri != null){
                    Log.d("####", "fri" + item.fri!!.bookCode.toString())
                }else if (item.sat != null){
                    Log.d("####", "sat" + item.sat!!.bookCode.toString())
                }else if (item.sun != null){
                    Log.d("####", "sun" + item.sun!!.bookCode.toString())
                }

                Log.d("@@@@-1", item.toString())
                Log.d("@@@@-2", position.toString())

            }
        })

        return root
    }


}