package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.R

import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBest
import com.example.moavara.Search.BookListDataBestWeekend
import java.util.*


class FragmentBestWeekendTab(private val tabType : String) : Fragment() {


    private lateinit var db: DataBaseBest

    private var adapterWeek: AdapterBestWeekend? = null

    private val itemWeek = ArrayList<BookListDataBestWeekend?>()

    var recyclerView: RecyclerView? = null


    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best_weekend, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)

        adapterWeek = AdapterBestWeekend(requireContext(), itemWeek)

        db = Room.databaseBuilder(
            requireContext(),
            DataBaseBest::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val mon = db.bestDao().getMon(tabType)
        val tue = db.bestDao().getTue(tabType)
        val wed = db.bestDao().getWed(tabType)
        val thur = db.bestDao().getThu(tabType)
        val fri = db.bestDao().getFri(tabType)
        val sat = db.bestDao().getSat(tabType)
        val sun = db.bestDao().getSun(tabType)

        val today = db.bestDao().selectWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK), "Joara")

        for (i in today.indices){

            itemWeek.add(
                BookListDataBestWeekend(
                    if(sun.isNotEmpty() && sun[i].number!! >= 0){
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
                            i + 1
                        )
                    } else null,
                    if(mon.isNotEmpty() && mon[i].number!! >= 0){
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
                            i + 1
                        )
                    } else null,
                    if(tue.isNotEmpty() && tue[i].number!! > 0){
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
                            i + 1
                        )
                    } else null,
                    if(wed.isNotEmpty() && wed[i].number!! > 0){
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
                            i + 1
                        )
                    } else null,
                    if(thur.isNotEmpty() && thur[i].number!! > 0){
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
                            i + 1
                        )
                    } else null,
                    if(fri.isNotEmpty() && fri[i].number!! > 0){
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
                            i + 1
                        )
                    } else null,
                    if(sat.isNotEmpty() && sat[i].number!! > 0){
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
                            i + 1
                        )
                    } else null,
                )
            )
        }

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapterWeek
        adapterWeek!!.notifyDataSetChanged()

        adapterWeek!!.setOnItemClickListener(object : AdapterBestWeekend.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int, value: String?) {
                val item: BookListDataBestWeekend? = adapterWeek!!.getItem(position)

                 if (item!!.sun != null && value.equals("sun")){

                     if(adapterWeek!!.getSelectedBook() == item.sun!!.bookCode.toString()){
                         val mBottomDialogBest = BottomDialogBest(requireContext(), item.sun)
                         fragmentManager?.let { mBottomDialogBest.show(it, null) }
                         adapterWeek!!.setSelectedBook("")
                     } else {
                         adapterWeek!!.setSelectedBook(item.sun!!.bookCode.toString())
                     }
                } else if(item.mon != null && value.equals("mon")){

                     if(adapterWeek!!.getSelectedBook() == item.mon!!.bookCode.toString()){
                         val mBottomDialogBest = BottomDialogBest(requireContext(), item.mon)
                         fragmentManager?.let { mBottomDialogBest.show(it, null) }
                         adapterWeek!!.setSelectedBook("")
                     } else {
                         adapterWeek!!.setSelectedBook(item.mon!!.bookCode.toString())
                     }

                } else if (item.tue != null && value.equals("tue")){

                     if(adapterWeek!!.getSelectedBook() == item.tue!!.bookCode.toString()){
                         val mBottomDialogBest = BottomDialogBest(requireContext(), item.tue)
                         fragmentManager?.let { mBottomDialogBest.show(it, null) }
                         adapterWeek!!.setSelectedBook("")
                     } else {
                         adapterWeek!!.setSelectedBook(item.tue!!.bookCode.toString())
                     }

                }else if (item.wed != null && value.equals("wed")){

                    if(adapterWeek!!.getSelectedBook() == item.wed!!.bookCode.toString()){
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.wed)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                    } else {
                        adapterWeek!!.setSelectedBook(item.wed!!.bookCode.toString())
                    }

                }else if (item.thur != null && value.equals("thur")){

                    if(adapterWeek!!.getSelectedBook() == item.thur!!.bookCode.toString()){
                        val mBottomDialogBest = BottomDialogBest(requireContext(), item.thur)
                        fragmentManager?.let { mBottomDialogBest.show(it, null) }
                        adapterWeek!!.setSelectedBook("")
                    } else {
                        adapterWeek!!.setSelectedBook(item.thur!!.bookCode.toString())
                    }

                }else if (item.fri != null && value.equals("fri")){

                     if(adapterWeek!!.getSelectedBook() == item.fri!!.bookCode.toString()){
                         val mBottomDialogBest = BottomDialogBest(requireContext(), item.fri)
                         fragmentManager?.let { mBottomDialogBest.show(it, null) }
                     } else {
                         adapterWeek!!.setSelectedBook(item.fri!!.bookCode.toString())
                     }

                }else if (item.sat != null && value.equals("sat")){

                     if(adapterWeek!!.getSelectedBook() == item.fri!!.bookCode.toString()){
                         val mBottomDialogBest = BottomDialogBest(requireContext(), item.fri)
                         fragmentManager?.let { mBottomDialogBest.show(it, null) }
                     } else {
                         adapterWeek!!.setSelectedBook(item.fri!!.bookCode.toString())
                     }

                }

                adapterWeek!!.notifyDataSetChanged()

            }
        })

        return root
    }


}