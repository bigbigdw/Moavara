package com.bigbigdw.moavara.Best

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bigbigdw.moavara.DataBase.DBBest
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.DataBase.RoomBookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBestAnalyze
import com.bigbigdw.moavara.Search.BookListDataBestWeekend
import com.bigbigdw.moavara.Util.BestRef
import com.bigbigdw.moavara.Util.DBDate
import com.bigbigdw.moavara.databinding.FragmentBestMonthBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.io.IOException


class FragmentBestTabMonth(private val platform: String, private val UserInfo: DataBaseUser) : Fragment(), BestTodayListener {

    private lateinit var adapterMonth: AdapterBestMonth
    private val itemMonth = ArrayList<BookListDataBestWeekend>()
    private val ItemMonthDay = ArrayList<BookListDataBest>()
    private var adapterMonthDay: AdapterBestToday? = null
    private val bookCodeItems = ArrayList<BookListDataBestAnalyze>()

    private var _binding: FragmentBestMonthBinding? = null
    private val binding get() = _binding!!

    private var year = 0
    private var month = 0
    private var monthCount = 0
    var bestDao: DBBest? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestMonthBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAnalytics = Firebase.analytics

        bestDao = Room.databaseBuilder(
            requireContext(),
            DBBest::class.java,
            "Month_${platform}_${UserInfo.Genre}"
        ).allowMainThreadQueries().build()

        val currentDate = DBDate.getDateData(DBDate.DateMMDD())

        year = DBDate.Year().substring(2,4).toInt()
        month = (currentDate?.month ?: 0).toInt() + 1

        adapterMonth = AdapterBestMonth(itemMonth)
        adapterMonthDay = AdapterBestToday(ItemMonthDay, bookCodeItems)

        binding.blank.root.visibility = View.VISIBLE
        binding.blank.tviewblank.text = "작품을 불러오는 중..."
        binding.rviewBestMonth.visibility = View.GONE

        itemMonth.clear()

        if(bestDao?.bestDao()?.getAll()?.size == 0){
            getBestMonth()
        } else {
            setRoomData()
        }

        with(binding) {
            rviewBestMonth.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rviewBestMonth.adapter = adapterMonth

            adapterMonth.setOnItemClickListener(object : AdapterBestMonth.OnItemClickListener {
                override fun onItemClick(v: View?, position: Int, value: String?) {

                    ItemMonthDay.clear()

                    binding.loading.root.visibility = View.VISIBLE
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.rviewBestMonthDay.visibility = View.GONE

                    if (value != null) {
                        BestRef.getBestDataMonth(platform, UserInfo.Genre).child((month - monthCount -1).toString()).child((position + 1).toString())
                            .child(value).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    rviewBestMonthDay.layoutManager =
                                        LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    rviewBestMonthDay.adapter = adapterMonthDay

                                    if (dataSnapshot.childrenCount > 0) {

                                        if (llayoutMonthDetail.visibility == View.GONE) {
                                            llayoutMonthDetail.visibility = View.VISIBLE
                                        }
                                    } else {
                                        llayoutMonthDetail.visibility = View.GONE
                                    }

                                    for (postSnapshot in dataSnapshot.children) {
                                        val group: BookListDataBest? =
                                            postSnapshot.getValue(BookListDataBest::class.java)

                                        if (group != null) {
                                            ItemMonthDay.add(
                                                BookListDataBest(
                                                    group.writer,
                                                    group.title,
                                                    group.bookImg.replace("http://","https://"),
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

                                    }
                                    getBestTodayList(ItemMonthDay, true)
                                    adapterMonthDay?.notifyDataSetChanged()

                                    try{
                                        val adapterDate = ItemMonthDay[value.toInt() - 1].date
                                        binding.tviewDay.text = "${adapterDate.substring(4,6)}월 ${adapterDate.substring(6,8)}일 베스트"

                                        val delay: Int = if(binding.llayoutMonthDetail.visibility == View.VISIBLE){
                                            1500
                                        } else {
                                            1000
                                        }

                                        Looper.myLooper()?.let {
                                            Handler(it).postDelayed(
                                                {
                                                    binding.sView.smoothScrollTo(0, binding.rviewBestMonth.height)
                                                },
                                                delay.toLong()
                                            )
                                        }
                                    } catch (e : IndexOutOfBoundsException){
                                        Toast.makeText(requireContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                }

            })

            llayoutAfter.visibility = View.INVISIBLE

            tviewMonth.text = "${year}년 ${month - monthCount}월"

            llayoutBefore.setOnClickListener {
                monthCount += 1

                if(monthCount == 2){
                    llayoutBefore.visibility = View.INVISIBLE
                }  else {
                    llayoutAfter.visibility = View.VISIBLE
                }

                tviewMonth.text = "${year}년 ${month - monthCount}월"
                adapterMonth.setMonthDate(monthCount)
                getMonthBefore(month - monthCount)
            }

            llayoutAfter.setOnClickListener {
                monthCount -= 1

                if(monthCount == 0){
                    llayoutAfter.visibility = View.INVISIBLE
                } else {
                    llayoutBefore.visibility = View.VISIBLE
                }

                tviewMonth.text = "${year}년 ${month - monthCount}월"
                adapterMonth.setMonthDate(monthCount)
                getMonthBefore(month - monthCount)
            }
        }

        adapterMonthDay?.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterMonthDay?.getItem(position)

                if(platform == "MrBlue"){
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.mrblue.com/novel/${item?.bookCode}")
                    )
                    requireContext().startActivity(intent)
                } else {
                    val bundle = Bundle()
                    bundle.putString("BEST_PLATFORM", item?.type)
                    bundle.putString("BEST_BOTTOM_DIALOG_FROM", "Month")
                    firebaseAnalytics.logEvent("BEST_BottomDialogBest", bundle)

                    val mBottomDialogBest = BottomDialogBest(
                        requireContext(),
                        item,
                        platform,
                        position,
                        UserInfo,
                        firebaseAnalytics
                    )
                    childFragmentManager.let { mBottomDialogBest.show(it, null) }
                }
            }
        })

        return view
    }

    private fun getBestMonth() {

        binding.rviewBestMonth.removeAllViews()
        itemMonth.clear()
        bestDao?.bestDao()?.initAll()

        try {

            BestRef.getBestDataMonth(platform, UserInfo.Genre)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var num = 5

                        if(dataSnapshot.child((month - 1).toString()).childrenCount.toInt() >= 5){
                            num = dataSnapshot.child((month - 1).toString()).childrenCount.toInt()
                        }

                        for (week in 1..num) {
                            val weekItem = BookListDataBestWeekend()

                            for (day in 1..7) {
                                val item: BookListDataBest? =
                                    dataSnapshot.child((month - monthCount -1).toString()).child(week.toString()).child(day.toString())
                                        .child("1")
                                        .getValue(BookListDataBest::class.java)

                                when (day) {
                                    1 -> {
                                        if (item != null) {
                                            weekItem.sun = item
                                            bestDao?.bestDao()?.insert(
                                                RoomBookListDataBest(
                                                    item.writer,
                                                    item.title,
                                                    item.bookImg,
                                                    item.bookCode,
                                                    item.info1,
                                                    item.info2,
                                                    item.info3,
                                                    item.info4,
                                                    item.info5,
                                                    item.info6,
                                                    item.number,
                                                    item.date,
                                                    item.type,
                                                    item.memo,
                                                    day,
                                                    week
                                                )
                                            )
                                        }
                                    }
                                    2 -> {
                                        if (item != null) {
                                            weekItem.mon = item
                                            bestDao?.bestDao()?.insert(
                                                RoomBookListDataBest(
                                                    item.writer,
                                                    item.title,
                                                    item.bookImg,
                                                    item.bookCode,
                                                    item.info1,
                                                    item.info2,
                                                    item.info3,
                                                    item.info4,
                                                    item.info5,
                                                    item.info6,
                                                    item.number,
                                                    item.date,
                                                    item.type,
                                                    item.memo,
                                                    day,
                                                    week
                                                )
                                            )
                                        }
                                    }
                                    3 -> {
                                        if (item != null) {
                                            weekItem.tue = item
                                            bestDao?.bestDao()?.insert(
                                                RoomBookListDataBest(
                                                    item.writer,
                                                    item.title,
                                                    item.bookImg,
                                                    item.bookCode,
                                                    item.info1,
                                                    item.info2,
                                                    item.info3,
                                                    item.info4,
                                                    item.info5,
                                                    item.info6,
                                                    item.number,
                                                    item.date,
                                                    item.type,
                                                    item.memo,
                                                    day,
                                                    week
                                                )
                                            )
                                        }
                                    }
                                    4 -> {
                                        if (item != null) {
                                            weekItem.wed = item
                                            bestDao?.bestDao()?.insert(
                                                RoomBookListDataBest(
                                                    item.writer,
                                                    item.title,
                                                    item.bookImg,
                                                    item.bookCode,
                                                    item.info1,
                                                    item.info2,
                                                    item.info3,
                                                    item.info4,
                                                    item.info5,
                                                    item.info6,
                                                    item.number,
                                                    item.date,
                                                    item.type,
                                                    item.memo,
                                                    day,
                                                    week
                                                )
                                            )
                                        }
                                    }
                                    5 -> {
                                        if (item != null) {
                                            weekItem.thur = item
                                            bestDao?.bestDao()?.insert(
                                                RoomBookListDataBest(
                                                    item.writer,
                                                    item.title,
                                                    item.bookImg,
                                                    item.bookCode,
                                                    item.info1,
                                                    item.info2,
                                                    item.info3,
                                                    item.info4,
                                                    item.info5,
                                                    item.info6,
                                                    item.number,
                                                    item.date,
                                                    item.type,
                                                    item.memo,
                                                    day,
                                                    week
                                                )
                                            )
                                        }
                                    }
                                    6 -> {
                                        if (item != null) {
                                            weekItem.fri = item
                                            bestDao?.bestDao()?.insert(
                                                RoomBookListDataBest(
                                                    item.writer,
                                                    item.title,
                                                    item.bookImg,
                                                    item.bookCode,
                                                    item.info1,
                                                    item.info2,
                                                    item.info3,
                                                    item.info4,
                                                    item.info5,
                                                    item.info6,
                                                    item.number,
                                                    item.date,
                                                    item.type,
                                                    item.memo,
                                                    day,
                                                    week
                                                )
                                            )
                                        }
                                    }
                                    7 -> {
                                        if (item != null) {
                                            weekItem.sat = item
                                            bestDao?.bestDao()?.insert(
                                                RoomBookListDataBest(
                                                    item.writer,
                                                    item.title,
                                                    item.bookImg,
                                                    item.bookCode,
                                                    item.info1,
                                                    item.info2,
                                                    item.info3,
                                                    item.info4,
                                                    item.info5,
                                                    item.info6,
                                                    item.number,
                                                    item.date,
                                                    item.type,
                                                    item.memo,
                                                    day,
                                                    week
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            itemMonth.add(weekItem)

                            binding.blank.root.visibility = View.GONE
                            binding.rviewBestMonth.visibility = View.VISIBLE
                            adapterMonth.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })


        } catch (e: IOException) {
        }

    }

    private fun getMonthBefore(month : Int){

        binding.blank.root.visibility = View.VISIBLE
        binding.rviewBestMonth.visibility = View.GONE
        binding.rviewBestMonth.removeAllViews()
        itemMonth.clear()

        BestRef.getBestDataMonthBefore(platform, UserInfo.Genre)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var num = 5

                    if(dataSnapshot.child((month - 1).toString()).childrenCount.toInt() >= 5){
                        num = dataSnapshot.child((month - 1).toString()).childrenCount.toInt()
                    }

                    for (week in 1..num) {
                        val weekItem = BookListDataBestWeekend()

                        for (day in 1..7) {
                            val item: BookListDataBest? =
                                dataSnapshot.child((month - 1).toString()).child(week.toString()).child(day.toString())
                                    .child("0")
                                    .getValue(BookListDataBest::class.java)

                            when (day) {
                                1 -> {
                                    if (item != null) {
                                        weekItem.sun = item
                                    }
                                }
                                2 -> {
                                    if (item != null) {
                                        weekItem.mon = item
                                    }
                                }
                                3 -> {
                                    if (item != null) {
                                        weekItem.tue = item
                                    }
                                }
                                4 -> {
                                    if (item != null) {
                                        weekItem.wed = item
                                    }
                                }
                                5 -> {
                                    if (item != null) {
                                        weekItem.thur = item
                                    }
                                }
                                6 -> {
                                    if (item != null) {
                                        weekItem.fri = item
                                    }
                                }
                                7 -> {
                                    if (item != null) {
                                        weekItem.sat = item
                                    }
                                }
                            }
                        }

                        itemMonth.add(weekItem)
                        binding.blank.root.visibility = View.GONE
                        binding.rviewBestMonth.visibility = View.VISIBLE
                        adapterMonth.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun getBestTodayList(items: ArrayList<BookListDataBest>, status: Boolean) {
        BestRef.getBookCode(platform, UserInfo.Genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (bookCodeList in items) {
                    val bookCodeitems = dataSnapshot.child(bookCodeList.bookCode)

                    if (bookCodeitems.childrenCount > 1) {
                        val bookCodes = ArrayList<BookListDataBestAnalyze>()

                        for (item in bookCodeitems.children) {

                            val group: BookListDataBestAnalyze? =
                                item.getValue(BookListDataBestAnalyze::class.java)

                            if (group != null) {
                                bookCodes.add(
                                    BookListDataBestAnalyze(
                                        group.info1,
                                        group.info2,
                                        group.info3,
                                        group.info4,
                                        group.number,
                                        group.date,
                                    )
                                )
                            }
                        }

                        val lastItem = bookCodes[bookCodes.size - 1]
                        val moreLastItem = bookCodes[bookCodes.size - 2]

                        bookCodeItems.add(
                            BookListDataBestAnalyze(
                                lastItem.info1,
                                lastItem.info2,
                                lastItem.info3,
                                lastItem.info4,
                                lastItem.number,
                                lastItem.date,
                                moreLastItem.number - lastItem.number,
                                bookCodes.size
                            )
                        )

                    } else if (bookCodeitems.childrenCount.toInt() == 1) {

                        val group: BookListDataBestAnalyze? =
                            dataSnapshot.child(bookCodeList.bookCode).child(DBDate.DateMMDD())
                                .getValue(BookListDataBestAnalyze::class.java)

                        if (group != null) {
                            bookCodeItems.add(
                                BookListDataBestAnalyze(
                                    group.info1,
                                    group.info2,
                                    group.info3,
                                    group.info4,
                                    group.number,
                                    group.date,
                                    0,
                                    1
                                )
                            )
                        }
                    }
                }

                binding.loading.root.visibility = View.GONE
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.rviewBestMonthDay.visibility = View.VISIBLE
                adapterMonthDay?.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setRoomData(){

        for (num in 1..5) {
            val weekItem = BookListDataBestWeekend()

            for (day in 0..6) {
                val item = bestDao?.bestDao()?.getMonth((day + 1).toString(), num.toString())

                if (day == 0) {
                    if (item != null) {
                        weekItem.sun = BookListDataBest(
                            item.writer,
                            item.title,
                            item.bookImg,
                            item.bookCode,
                            item.info1,
                            item.info2,
                            item.info3,
                            item.info4,
                            item.info5,
                            item.info6,
                            item.number,
                            item.date,
                            item.type,
                            item.memo
                        )
                    }
                } else if (day == 1) {
                    if (item != null) {
                        weekItem.mon = BookListDataBest(
                            item.writer,
                            item.title,
                            item.bookImg,
                            item.bookCode,
                            item.info1,
                            item.info2,
                            item.info3,
                            item.info4,
                            item.info5,
                            item.info6,
                            item.number,
                            item.date,
                            item.type,
                            item.memo
                        )
                    }
                } else if (day == 2) {
                    if (item != null) {
                        weekItem.tue = BookListDataBest(
                            item.writer,
                            item.title,
                            item.bookImg,
                            item.bookCode,
                            item.info1,
                            item.info2,
                            item.info3,
                            item.info4,
                            item.info5,
                            item.info6,
                            item.number,
                            item.date,
                            item.type,
                            item.memo
                        )
                    }
                } else if (day == 3) {
                    if (item != null) {
                        weekItem.wed = BookListDataBest(
                            item.writer,
                            item.title,
                            item.bookImg,
                            item.bookCode,
                            item.info1,
                            item.info2,
                            item.info3,
                            item.info4,
                            item.info5,
                            item.info6,
                            item.number,
                            item.date,
                            item.type,
                            item.memo
                        )
                    }
                } else if (day == 4) {
                    if (item != null) {
                        weekItem.thur = BookListDataBest(
                            item.writer,
                            item.title,
                            item.bookImg,
                            item.bookCode,
                            item.info1,
                            item.info2,
                            item.info3,
                            item.info4,
                            item.info5,
                            item.info6,
                            item.number,
                            item.date,
                            item.type,
                            item.memo
                        )
                    }
                } else if (day == 5) {
                    if (item != null) {
                        weekItem.fri = BookListDataBest(
                            item.writer,
                            item.title,
                            item.bookImg,
                            item.bookCode,
                            item.info1,
                            item.info2,
                            item.info3,
                            item.info4,
                            item.info5,
                            item.info6,
                            item.number,
                            item.date,
                            item.type,
                            item.memo
                        )
                    }
                } else if (day == 6) {
                    if (item != null) {
                        weekItem.sat = BookListDataBest(
                            item.writer,
                            item.title,
                            item.bookImg,
                            item.bookCode,
                            item.info1,
                            item.info2,
                            item.info3,
                            item.info4,
                            item.info5,
                            item.info6,
                            item.number,
                            item.date,
                            item.type,
                            item.memo
                        )
                    }
                }
            }

            itemMonth.add(weekItem)
        }

        binding.blank.root.visibility = View.GONE
        binding.rviewBestMonth.visibility = View.VISIBLE
        adapterMonth.notifyDataSetChanged()
    }
}