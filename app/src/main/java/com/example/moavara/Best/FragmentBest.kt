package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class FragmentBest : Fragment() {

    lateinit var root: View

    private var llayout_Search_Fragment: LinearLayout? = null
    private var mFragmentBestToday: FragmentBestToday? = null
    private var mFragmentBestWeekend: FragmentBestWeekend? = null
    private var mFragmentBestMonth: FragmentBestMonth? = null

    private var tviewToday: TextView? = null
    private var tviewWeekend: TextView? = null
    private var tviewBest: TextView? = null

    private lateinit var dbWeek: DataBaseBestDay
    private lateinit var dbYesterday: DataBaseBestDay

    val Genre = "ALL"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_best, container, false)

        dbYesterday = Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-yesterday")
            .allowMainThreadQueries().build()

        dbWeek = Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-week")
            .allowMainThreadQueries().build()

        setRoomBest("Joara")
        setRoomBest("Naver")
        setRoomBest("Kakao")
        setRoomBest("Ridi")
        setRoomBest("OneStore")
        setRoomBest("MrBlue")

        //프래그먼트 관련
        llayout_Search_Fragment = root.findViewById(R.id.llayout_Search_Fragment)

        tviewToday = root.findViewById(R.id.tview_Today)
        tviewWeekend = root.findViewById(R.id.tview_Weekend)
        tviewBest = root.findViewById(R.id.tview_Best)

        tviewToday!!.setTextColor(Color.parseColor("#6D6F76"))

        mFragmentBestToday = FragmentBestToday()
        parentFragmentManager.beginTransaction()
            .replace(R.id.llayout_Search_Fragment, mFragmentBestToday!!)
            .commit()

        tviewToday!!.setOnClickListener {

            tviewToday!!.setTextColor(Color.parseColor("#ffffff"))
            tviewWeekend!!.setTextColor(Color.parseColor("#6D6F76"))
            tviewBest!!.setTextColor(Color.parseColor("#6D6F76"))

            mFragmentBestToday = FragmentBestToday()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayout_Search_Fragment, mFragmentBestToday!!)
                .commit()
        }

        tviewWeekend!!.setOnClickListener {

            tviewWeekend!!.setTextColor(Color.parseColor("#ffffff"))
            tviewToday!!.setTextColor(Color.parseColor("#6D6F76"))
            tviewBest!!.setTextColor(Color.parseColor("#6D6F76"))

            mFragmentBestWeekend = FragmentBestWeekend()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayout_Search_Fragment, mFragmentBestWeekend!!)
                .commit()
        }

        tviewBest!!.setOnClickListener {

            tviewBest!!.setTextColor(Color.parseColor("#ffffff"))
            tviewToday!!.setTextColor(Color.parseColor("#6D6F76"))
            tviewWeekend!!.setTextColor(Color.parseColor("#6D6F76"))

            mFragmentBestMonth = FragmentBestMonth()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llayout_Search_Fragment, mFragmentBestMonth!!)
                .commit()
        }

        return root
    }

    private fun setRoomBest(type: String){

        val week = mRootRef.child("best").child(type).child(Genre).child("today").child(DBDate.Day())
        var num = 1

        week.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    dbWeek.bestDao().insert(
                        DataBestDay(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.bookCode,
                            group.info1,
                            group.info2,
                            group.info3,
                            group.info4,
                            group.info5,
                            group.number,
                            group.date,
                            type
                        )
                    )

                    num += 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val yesterdayRef = mRootRef.child("best").child(type).child(Genre).child("today").child(DBDate.Yesterday())

        yesterdayRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    dbYesterday.bestDao().insert(
                        DataBestDay(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.bookCode,
                            group.info1,
                            group.info2,
                            group.info3,
                            group.info4,
                            group.info5,
                            group.number,
                            group.date,
                            type
                        )
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

}