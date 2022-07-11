package com.example.moavara.Best

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.WeekendDate
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.BottomDialogBestBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.include_rank.*

class BottomDialogBest(
    private val mContext: Context,
    private val item: BookListDataBest,
    private val tabType: String,
    private val pos: Int
) :
    BottomSheetDialogFragment() {

    var UID = ""
    var userInfo = mRootRef.child("User")

    private var _binding: BottomDialogBestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomDialogBestBinding.inflate(inflater, container, false)
        val view = binding.root

        mRootRef.child("best").child(tabType).child(context?.getSharedPreferences("pref", MODE_PRIVATE)?.getString("GENRE", "") ?: "").child("week-list").child(DBDate.Week() + ((DBDate.DayInt() * 1000) + pos).toString()).child("trophyCount")

        UID = context?.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("UID", "").toString()

        with(binding) {

            tviewTitle.text = item.title
            tviewWriter.text = item.writer

            if (tabType == "MrBlue") {
                tviewInfo1.visibility = View.GONE
                tviewInfo2.visibility = View.GONE
                tviewInfo3.visibility = View.GONE
                tviewInfo4.visibility = View.GONE
                tviewInfo5.visibility = View.GONE
            } else if (tabType == "Kakao Stage" || tabType == "Naver Today" || tabType == "Naver Challenge" || tabType == "Naver") {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.GONE

                tviewInfo1.text = item.info1
                tviewInfo2.text = item.info2
                tviewInfo3.text = item.info3
                tviewInfo4.text = item.info4
            } else if (tabType == "Ridi" || tabType == "OneStore") {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.GONE
                tviewInfo5.visibility = View.GONE

                tviewInfo1.text = item.info1
                tviewInfo2.text = item.info2
                tviewInfo3.text = item.info3
            } else if (tabType == "Kakao" || tabType == "Joara" || tabType == "Joara Premium" || tabType == "Joara Nobless" || tabType == "Munpia" || tabType == "Toksoda") {
                tviewInfo1.visibility = View.VISIBLE
                tviewInfo2.visibility = View.VISIBLE
                tviewInfo3.visibility = View.VISIBLE
                tviewInfo4.visibility = View.VISIBLE
                tviewInfo5.visibility = View.VISIBLE

                tviewInfo1.text = item.info1
                tviewInfo2.text = item.info2
                tviewInfo3.text = item.info3
                tviewInfo4.text = item.info4
                tviewInfo5.text = item.info5
            }

            Glide.with(mContext)
                .load(item.bookImg)
                .into(iviewBookImg)

            when (item.number) {
                0 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_1)
                }
                1 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_2)
                }
                2 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_3)
                }
                3 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_4)
                }
                4 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_5)
                }
                5 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_6)
                }
                6 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_7)
                }
                7 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_8)
                }
                8 -> {
                    iviewRanking.setImageResource(R.drawable.icon_best_9)
                }
                else -> {
                    Log.d("bestRankImage", "NO_IMAGE")
                }
            }

            item.data?.let { getRankList(it) }

            llayoutBtnLeft.setOnClickListener {

                val group = BookListDataBestToday(
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
                    tabType,
                    item.status,
                    item.trophyCount,
                )

                userInfo.child(UID).child("book").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        userInfo.child(UID).child("book").child(dataSnapshot.childrenCount.toString()).setValue(group)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                Toast.makeText(requireContext(), "Pick 성공!", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            lviewDetail.setOnClickListener {
                val bookDetailIntent = Intent(mContext, ActivityBestDetail::class.java)
                bookDetailIntent.putExtra("BookCode", String.format("%s", item.bookCode))
                bookDetailIntent.putExtra("Type", String.format("%s", tabType))
                bookDetailIntent.putExtra("POSITION", pos)
                startActivity(bookDetailIntent)
                dismiss()
            }

            llayoutBtnRight.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getUrl()))
                startActivity(intent)
            }
        }

        return view
    }

    private fun getUrl(): String {

        return if (tabType == "MrBlue") {
            "https://www.mrblue.com/novel/" + item.bookCode
        } else if (tabType == "Naver Today") {
            item.bookCode
        } else if (tabType == "Naver Challenge") {
            item.bookCode
        } else if (tabType == "Naver") {
            item.bookCode
        } else if (tabType == "Kakao Stage") {
            "https://pagestage.kakao.com/novels/" + item.bookCode
        } else if (tabType == "Kakao") {
            "https://page.kakao.com/home?seriesId=" + item.bookCode
        } else if (tabType == "OneStore") {
            "https://onestory.co.kr/detail/" + item.bookCode
        } else if (tabType == "Ridi") {
            item.bookCode
        } else if (tabType == "Joara" || tabType == "Joara Premium" || tabType == "Joara Nobless") {
            "https://www.joara.com/book/" + item.bookCode
        }else if (tabType == "Munpia") {
            "https://novel.munpia.com/${item.bookCode}"
        } else if (tabType == "Toksoda") {
            "https://www.tocsoda.co.kr/product/productView?brcd=${item.bookCode}"
        }else ""

    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    fun getRankList(data : ArrayList<BookListDataBestAnalyze>){
        for(item in data){
            val itemDate = DBDate.getDateData(item.date)

            with(binding.includeRank) {
                if (itemDate != null) {
                    if(itemDate.week == DBDate.Week().toInt()){
                        when{
                            itemDate.date == 1 -> {
                                tviewRank1.visibility = View.VISIBLE

                                if(itemDate.date == DBDate.DayInt()){
                                    iviewRank1.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank1.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank1.text = (item.number + 1).toString()
                            }
                            itemDate.date == 2 -> {
                                tviewRank2.visibility = View.VISIBLE
                                if(itemDate.date == DBDate.DayInt()){
                                    iviewRank2.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank2.text = (item.number + 1).toString()
                                Log.d("@@@@", "HIHI")
                            }
                            itemDate.date == 3 -> {
                                tviewRank3.visibility = View.VISIBLE
                                if(itemDate.date == DBDate.DayInt()){
                                    iviewRank3.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank3.text = (item.number + 1).toString()
                            }
                            itemDate.date == 4 -> {
                                tviewRank4.visibility = View.VISIBLE
                                if(itemDate.date == DBDate.DayInt()){
                                    iviewRank4.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank4.text = (item.number + 1).toString()
                            }
                            itemDate.date == 5 -> {
                                tviewRank5.visibility = View.VISIBLE
                                if(itemDate.date == DBDate.DayInt()){
                                    iviewRank5.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank5.text = (item.number + 1).toString()
                            }
                            itemDate.date == 6 -> {
                                tviewRank6.visibility = View.VISIBLE
                                if(itemDate.date == DBDate.DayInt()){
                                    iviewRank6.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank6.text = (item.number + 1).toString()
                            }
                            itemDate.date == 7 -> {
                                tviewRank7.visibility = View.VISIBLE
                                if(itemDate.date == DBDate.DayInt()){
                                    iviewRank7.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank7.text = (item.number + 1).toString()
                            }
                        }
                    }
                }
            }
        }
    }
}