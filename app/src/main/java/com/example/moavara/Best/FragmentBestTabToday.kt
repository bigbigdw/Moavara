package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.DataBase.*
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Search.BookListDataBestAnalyze
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestTabTodayBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class FragmentBestTabToday(private val platform: String, private val UserInfo: DataBaseUser) :
    Fragment(), BestTodayListener {

    private var adapterToday: AdapterBestToday? = null

    private val items = ArrayList<BookListDataBest>()
    private val bookCodeItems = ArrayList<BookListDataBestAnalyze>()
    var status = ""
    lateinit var root: View
    private var _binding: FragmentBestTabTodayBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var bestDao: DBBest? = null
    var bestDaoBookCode: DBBestBookCode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestTabTodayBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAnalytics = Firebase.analytics

        bestDao = Room.databaseBuilder(
            requireContext(),
            DBBest::class.java,
            "Today_${platform}_${UserInfo.Genre}"
        ).allowMainThreadQueries().build()

        bestDaoBookCode = Room.databaseBuilder(
            requireContext(),
            DBBestBookCode::class.java,
            "Today_${platform}_${UserInfo.Genre}_BookCode"
        ).allowMainThreadQueries().build()

        adapterToday = AdapterBestToday(items, bookCodeItems)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        binding.blank.root.visibility = View.VISIBLE
        binding.blank.tviewblank.text = "작품을 불러오는 중..."
        binding.rviewBest.visibility = View.GONE

        if(bestDao?.bestDao()?.getAll()?.size == 0){
            getBookListToday()
        } else {
            setRoomData()
        }

        adapterToday?.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBest? = adapterToday?.getItem(position)

                val bundle = Bundle()
                bundle.putString("BEST_PLATFORM", item?.type)
                bundle.putString("BEST_BOTTOM_DIALOG_FROM", "Today")
                firebaseAnalytics.logEvent("BEST_BottomDialogBest", bundle)

                val mBottomDialogBest = BottomDialogBest(
                    requireContext(),
                    item,
                    platform,
                    position,
                    UserInfo,
                    firebaseAnalytics
                )

                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })

        return view
    }

    private fun getBookListToday() {

        bestDao?.bestDao()?.initAll()

        BestRef.getBestDataToday(platform, UserInfo.Genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)

                    if (group != null) {

                        items.add(BookListDataBest(
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
                        ))

                        if(dataSnapshot.exists()){
                            bestDao?.bestDao()?.insert(
                                RoomBookListDataBest(
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
                    }
                }

                getBestTodayList(items, true)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    override fun getBestTodayList(items: ArrayList<BookListDataBest>, status: Boolean) {

        bestDaoBookCode?.bestDaoBookCode()?.initAll()

        BestRef.getBookCode(platform, UserInfo.Genre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (bookCodeList in items) {

                    val item = dataSnapshot.child(bookCodeList.bookCode)

                    if (item.childrenCount > 1) {

                        val bookCodes = ArrayList<BookListDataBestAnalyze>()

                        for(childItem in item.children){

                            val group: BookListDataBestAnalyze? = childItem.getValue(BookListDataBestAnalyze::class.java)

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

                        bestDaoBookCode?.bestDaoBookCode()?.insert(
                            RoomBookListDataBestAnalyze(
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

                    } else if (item.childrenCount.toInt() == 1) {

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

                            bestDaoBookCode?.bestDaoBookCode()?.insert(
                                RoomBookListDataBestAnalyze(
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

                binding.blank.root.visibility = View.GONE
                binding.rviewBest.visibility = View.VISIBLE

                adapterToday?.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setRoomData(){
        val bookItem = bestDao?.bestDao()?.getAll()
        val bookCodeItem = bestDaoBookCode?.bestDaoBookCode()?.get()
        binding.blank.root.visibility = View.GONE
        binding.rviewBest.visibility = View.VISIBLE

        if (bookItem != null) {
            for(item in bookItem){
                items.add(BookListDataBest(
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
                ))
            }
        }

        if (bookCodeItem != null) {
            for(item in bookCodeItem){
                bookCodeItems.add(
                    BookListDataBestAnalyze(
                        item.info1,
                        item.info2,
                        item.info3,
                        item.info4,
                        item.number,
                        item.date,
                        item.numberDiff,
                        item.trophyCount,
                    )
                )
            }
        }

        adapterToday?.notifyDataSetChanged()
    }
}