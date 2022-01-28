package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBest
import com.example.moavara.DataBase.DataBest
import com.example.moavara.Joara.JoaraBestListResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.KaKao.BestResultKakao
import com.example.moavara.KaKao.RetrofitKaKao
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.R
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FragmentBestTodayApi(private val tabType : String) : Fragment() {
    private lateinit var db: DataBaseBest

    private var adapterToday: AdapterBestToday? = null
    private val items = ArrayList<BookListDataBestToday?>()
    var recyclerView: RecyclerView? = null

    lateinit var root: View
    lateinit var type: String

    var day = 0
    var week = 0
    var date = 0
    var month = 0
    var mNow: Long = 0
    var mDate: Date? = null
    var mFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    val mRootRef = FirebaseDatabase.getInstance().reference
    var conditionRef = mRootRef.child("best").child(tabType)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_best_today_tab, container, false)

        recyclerView = root.findViewById(R.id.rview_Best)

        adapterToday = AdapterBestToday(requireContext(), items)

        if(tabType == "Joara"){
            getBookListBestJoara(recyclerView)
        }

        if(tabType == "Kakao"){
            getBookListBestKakao(recyclerView)
        }

        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        date = Calendar.getInstance().get(Calendar.DATE)
        month = Calendar.getInstance().get(Calendar.MONTH)

        Log.d("@@@@", "locale = " + requireContext().resources.configuration.locales.get(0))
        Log.d("@@@@", "time = " + mFormat.format(Date(System.currentTimeMillis())))
        Log.d("@@@@", "day = $day")
        Log.d("@@@@", "week = $week")
        Log.d("@@@@", "date = $date")
        Log.d("@@@@", "monthly = $month")

        conditionRef = conditionRef.child(week.toString()).child(day.toString())

        db = Room.databaseBuilder(
            requireContext(),
            DataBaseBest::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

//        if(db.bestDao().getAllTotal() != null){
//            db.bestDao().deleteWeek(day)
//        }

        return root
    }

    private fun getBookListBestJoara(recyclerView: RecyclerView?) {
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val call: Call<JoaraBestListResult?>? = RetrofitJoara.getJoaraBookBest("today", "", "0")

        call!!.enqueue(object : Callback<JoaraBestListResult?> {
            override fun onResponse(
                call: Call<JoaraBestListResult?>,
                response: Response<JoaraBestListResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val books = it.bookLists

                        for (i in books!!.indices) {

                            val writerName = books[i].writerName
                            val subject = books[i].subject
                            val bookImg = books[i].bookImg
                            val intro = books[i].intro
                            val bookCode = books[i].bookCode
                            val cntChapter = books[i].cntChapter
                            val cntPageRead = books[i].cntPageRead
                            val cntFavorite = books[i].cntFavorite
                            val cntRecom = books[i].cntRecom

                            if (i < 10) {
                                db.bestDao().insert(
                                    DataBest(
                                        writerName,
                                        subject,
                                        bookImg,
                                        intro,
                                        bookCode,
                                        cntChapter,
                                        cntPageRead,
                                        cntFavorite,
                                        cntRecom,
                                        i + 1,
                                        day,
                                        week,
                                        date,
                                        month,
                                        10 - i,
                                        "Joara"
                                    )
                                )

                                conditionRef.child(i.toString()).setValue(                                    DataBest(
                                    writerName,
                                    subject,
                                    bookImg,
                                    intro,
                                    bookCode,
                                    cntChapter,
                                    cntPageRead,
                                    cntFavorite,
                                    cntRecom,
                                    i + 1,
                                    day,
                                    week,
                                    date,
                                    month,
                                    10 - i,
                                    "Joara"
                                ))
                            }

                            items!!.add(
                                BookListDataBestToday(
                                    writerName,
                                    subject,
                                    bookImg,
                                    intro,
                                    bookCode,
                                    cntChapter,
                                    cntPageRead,
                                    cntFavorite,
                                    cntRecom,
                                    i + 1,
                                    false
                                )
                            )
                        }
                    }
                    recyclerView!!.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapterToday
                    adapterToday!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }

    private fun getBookListBestKakao(recyclerView: RecyclerView?) {
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val call: Call<BestResultKakao?>? = RetrofitKaKao.getBestKakao("11", "0", "0", "2", "A")

        call!!.enqueue(object : Callback<BestResultKakao?> {
            override fun onResponse(
                call: Call<BestResultKakao?>,
                response: Response<BestResultKakao?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val list = it.list

                        for (i in list!!.indices) {

                            val writerName = list[i].author
                            val subject = list[i].title
                            val bookImg = "https://dn-img-page.kakao.com/download/resource?kid=" + list[i].image
                            val intro = list[i].description
                            val bookCode = list[i].series_id
                            val cntChapter = list[i].promotion_rate
                            val cntPageRead = list[i].read_count
                            val cntFavorite = list[i].like_count
                            val cntRecom = list[i].rating

                            if (i < 10) {
                                db.bestDao().insert(
                                    DataBest(
                                        writerName,
                                        subject,
                                        bookImg,
                                        intro,
                                        bookCode,
                                        cntChapter,
                                        cntPageRead,
                                        cntFavorite,
                                        cntRecom,
                                        i + 1,
                                        day,
                                        week,
                                        date,
                                        month,
                                        10 - i,
                                        "Kakao"
                                    )
                                )
                            }

                            items!!.add(
                                BookListDataBestToday(
                                    writerName,
                                    subject,
                                    bookImg,
                                    intro,
                                    bookCode,
                                    cntChapter,
                                    cntPageRead,
                                    cntFavorite,
                                    cntRecom,
                                    i + 1,
                                    false
                                )
                            )
                        }
                    }
                    recyclerView!!.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapterToday
                    adapterToday!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BestResultKakao?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        adapterToday!!.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }
}