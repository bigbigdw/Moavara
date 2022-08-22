package com.example.moavara.Search

import android.text.SpannableStringBuilder
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.R
import com.example.moavara.Util.applyingTextColor
import com.example.moavara.databinding.ItemSearchBinding


class AdapterBookSearch(private var holder: ArrayList<BookListDataBest>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item =  this.holder[position]

            with(holder.binding){

                Glide.with(holder.itemView.context)
                        .load(item.bookImg)
                        .into(iView)

                when (item.type) {
                    "Joara" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_joara)
                            .into(iviewPlatfrom)
                    }
                    "Naver_Challenge", "Naver_Today", "Naver" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_naver)
                            .into(iviewPlatfrom)
                    }
                    "Kakao" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_kakao)
                            .into(iviewPlatfrom)
                    }
                    "Kakao_Stage" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_kakaostage)
                            .into(iviewPlatfrom)
                    }
                    "Munpia" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_munpia)
                            .into(iviewPlatfrom)
                    }
                    "OneStore" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_onestore)
                            .into(iviewPlatfrom)
                    }
                    "Ridi" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_ridibooks)
                            .into(iviewPlatfrom)
                    }
                    "Toksoda" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_toksoda)
                            .into(iviewPlatfrom)
                    }
                    "MrBlue" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_mrblue)
                            .into(iviewPlatfrom)
                    }
                    else -> {}
                }

                tviewTitle.text = item.title
                tviewWriter.text = item.writer

//                tviewInfo1.text = item.info1
//                tviewInfo3.text = item.info2
//                tviewInfo4.text = item.info3
//                tviewInfo5.text = item.info4
//                tviewIntro.text = item.info5
//
//                if (item.type == "MrBlue") {
//                    tviewInfo1.visibility = View.GONE
//                    tviewInfo3.visibility = View.GONE
//                    tviewInfo4.visibility = View.GONE
//                    tviewInfo5.visibility = View.GONE
//                }   else if(item.type == "Toksoda"){
//                    tviewInfo1.visibility = View.VISIBLE
//                    tviewInfo3.visibility = View.GONE
//                    tviewInfo4.visibility = View.VISIBLE
//                    tviewInfo5.visibility = View.VISIBLE
//
//                    tviewInfo1.text = item.info2
//
//                    val info3 = SpannableStringBuilder("조회 수 : ${item.info1}" )
//                    info3.applyingTextColor(
//                        "조회 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info5 = SpannableStringBuilder("선호작 수 : ${item.info2}")
//                    info5.applyingTextColor(
//                        "선호작 수 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo4.text = info3
//                    tviewInfo5.text = info5
//                } else if (item.type == "Naver" || item.type == "Naver_Today" || item.type == "Naver_Challenge") {
//                    tviewInfo1.text = item.info1
//                    tviewInfo1.visibility = View.VISIBLE
//                    tviewInfo3.visibility = View.VISIBLE
//                    tviewInfo4.visibility = View.VISIBLE
//                    tviewInfo5.visibility = View.VISIBLE
//
//                    val info3 = SpannableStringBuilder("별점 수 : ${item.info3.replace("별점", "별점 : ")}")
//                    info3.applyingTextColor(
//                        "별점 : ",
//                        "#6E7686"
//                    )
//
//                    val info4 = SpannableStringBuilder("조회 수 : ${item.info2.replace("조회", "조회 수 : ")}" )
//                    info4.applyingTextColor(
//                        "조회 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info5 = SpannableStringBuilder("관심 : ${item.info3.replace("관심", "관심 : ")}")
//                    info5.applyingTextColor(
//                        "관심 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo3.text = info3
//                    tviewInfo4.text = info4
//                    tviewInfo5.text = info5
//                }  else if (item.type == "Kakao_Stage") {
//                    tviewInfo1.text = item.info2
//
//                    tviewInfo1.visibility = View.VISIBLE
//                    tviewInfo3.visibility = View.GONE
//                    tviewInfo4.visibility = View.VISIBLE
//                    tviewInfo5.visibility = View.VISIBLE
//
//                    val info3 = SpannableStringBuilder("조회 수 : ${item.info3.replace("별점", "별점 : ")}" )
//                    info3.applyingTextColor(
//                        "조회 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info4 = SpannableStringBuilder("선호작 수 : ${item.info2.replace("조회", "조회 수 : ")}" )
//                    info4.applyingTextColor(
//                        "선호작 수 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo4.text = info3
//                    tviewInfo5.text = info4
//                } else if (item.type == "Ridi") {
//                    tviewInfo1.text = item.info1
//                    tviewInfo1.visibility = View.VISIBLE
//                    tviewInfo3.visibility = View.GONE
//                    tviewInfo4.visibility = View.VISIBLE
//                    tviewInfo5.visibility = View.VISIBLE
//
//                    val info3 = SpannableStringBuilder("추천 수 : ${item.info3}" )
//                    info3.applyingTextColor(
//                        "추천 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info4 = SpannableStringBuilder("평점 : ${item.info2}")
//                    info4.applyingTextColor(
//                        "평점 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo4.text = info3
//                    tviewInfo5.text = info4
//                } else if (item.type == "OneStore") {
//                    tviewInfo1.visibility = View.GONE
//                    tviewInfo3.visibility = View.VISIBLE
//                    tviewInfo4.visibility = View.VISIBLE
//                    tviewInfo5.visibility = View.VISIBLE
//
//                    val info3 = SpannableStringBuilder("조회 수 : ${item.info3.replace("별점", "별점 : ")}" )
//                    info3.applyingTextColor(
//                        "조회 수 : ",
//                        "#6E7686"
//                    )
//
//                    val info4 = SpannableStringBuilder("평점 : ${item.info2.replace("조회", "조회 수 : ")}" )
//                    info4.applyingTextColor(
//                        "평점 : ",
//                        "#6E7686"
//                    )
//
//                    val info5 = SpannableStringBuilder("댓글 수 : ${item.info3.replace("관심", "관심 : ")}" )
//                    info5.applyingTextColor(
//                        "댓글 수 : ",
//                        "#6E7686"
//                    )
//
//                    tviewInfo3.text = info3
//                    tviewInfo4.text = info4
//                    tviewInfo5.text = info5
//                } else if (item.type == "Kakao" || item.type == "Munpia" || item.type == "Toksoda" || item.type == "Joara" || item.type == "Joara_Premium" || item.type == "Joara_Nobless" || item.type == "Munpia" ) {
//                    tviewInfo1.visibility = View.VISIBLE
//                    tviewInfo3.visibility = View.VISIBLE
//                    tviewInfo4.visibility = View.VISIBLE
//                    tviewInfo5.visibility = View.VISIBLE
//
//                    if(item.type == "Joara" || item.type == "Joara_Premium" || item.type == "Joara_Nobless"){
//                        tviewInfo1.text = item.info2
//
//                        val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
//                        info3.applyingTextColor(
//                            "조회 수 : ",
//                            "#6E7686"
//                        )
//
//                        val info4 = SpannableStringBuilder("선호작 수 : ${item.info2}")
//                        info4.applyingTextColor(
//                            "선호작 수 : ",
//                            "#6E7686"
//                        )
//
//                        val info5 = SpannableStringBuilder("추천 수 : ${item.info3}")
//                        info5.applyingTextColor(
//                            "추천 수 : ",
//                            "#6E7686"
//                        )
//
//                        tviewInfo3.text = info3
//                        tviewInfo4.text = info4
//                        tviewInfo5.text = info5
//
//                    } else if(item.type == "Kakao"){
//                        tviewInfo1.text = item.info2
//
//                        val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
//                        info3.applyingTextColor(
//                            "조회 수 : ",
//                            "#6E7686"
//                        )
//
//                        val info4 = SpannableStringBuilder("추천 수 : ${item.info2}")
//                        info4.applyingTextColor(
//                            "추천 수 : ",
//                            "#6E7686"
//                        )
//
//                        val info5 = SpannableStringBuilder("평점 : ${item.info3}")
//                        info5.applyingTextColor(
//                            "평점 : ",
//                            "#6E7686"
//                        )
//
//                        tviewInfo3.text = info3
//                        tviewInfo4.text = info4
//                        tviewInfo5.text = info5
//                    } else if(item.type == "Munpia"){
//                        tviewInfo1.text = item.info2
//
//                        val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
//                        info3.applyingTextColor(
//                            "조회 수 : ",
//                            "#6E7686"
//                        )
//
//                        val info4 = SpannableStringBuilder("방문 수 : ${item.info2}")
//                        info4.applyingTextColor(
//                            "방문 수 : ",
//                            "#6E7686"
//                        )
//
//                        val info5 = SpannableStringBuilder("선호작 수 : ${item.info3}")
//                        info5.applyingTextColor(
//                            "선호작 수 : ",
//                            "#6E7686"
//                        )
//
//                        tviewInfo3.text = info3
//                        tviewInfo4.text = info4
//                        tviewInfo5.text = info5
//                    } else {
//                        tviewInfo1.text = item.writer
//                        tviewInfo3.text = item.info1
//                        tviewInfo4.text = item.info2
//                        tviewInfo5.text = item.info3
//                    }
//                }

                if(item.info1.isEmpty()){
                    tviewIntro.visibility = View.GONE
                } else {
                    tviewIntro.visibility = View.VISIBLE
                }

                if(item.info2.isEmpty()){
                    tviewInfo.visibility = View.GONE
                } else {
                    tviewInfo.visibility = View.VISIBLE
                }

                if(item.info3.isEmpty()){
                    tviewInfo3.visibility = View.GONE
                } else {
                    tviewInfo3.visibility = View.VISIBLE
                }

                if(item.info4.isEmpty()){
                    tviewInfo4.visibility = View.GONE
                } else {
                    tviewInfo4.visibility = View.VISIBLE
                }

                if(item.info5.isEmpty()){
                    tviewInfo5.visibility = View.GONE
                } else {
                    tviewInfo5.visibility = View.VISIBLE
                }

                if(item.type == "Joara"){

                    val info2 = SpannableStringBuilder("쟝르 : ${item.info2}")
                    info2.applyingTextColor(
                        "쟝르 : ",
                        "#6E7686"
                    )

                    val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("선호작 수 : ${item.info4}")
                    info4.applyingTextColor(
                        "선호작 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder("추천 수 : ${item.info5}")
                    info5.applyingTextColor(
                        "추천 수 : ",
                        "#6E7686"
                    )

                    tviewInfo.text = info2
                    tviewInfo3.text = info3
                    tviewInfo4.text = info4
                    tviewInfo5.text = info5
                    tviewIntro.text = item.info1
                } else if(item.type == "Kakao"){

                    val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder(item.info4)
                    info4.applyingTextColor(
                        "총 ",
                        "#6E7686"
                    )

                    info4.applyingTextColor(
                        "화",
                        "#6E7686"
                    )

                    tviewInfo.text = item.info2
                    tviewInfo4.text = info3
                    tviewInfo5.text = info4
                }  else if(item.type == "Kakao_Stage"){

                    val info3 = SpannableStringBuilder("선호작 수 : ${item.info3}" )
                    info3.applyingTextColor(
                        "선호작 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("조회 수 : ${item.info4}" )
                    info4.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder(item.info5)
                    info5.applyingTextColor(
                        "총 ",
                        "#6E7686"
                    )

                    info5.applyingTextColor(
                        "화",
                        "#6E7686"
                    )

                    tviewInfo.text = item.info2
                    tviewInfo3.text = info3
                    tviewInfo4.text = info4
                    tviewInfo5.text = info5
                    tviewIntro.text = item.info1
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun getItem(position: Int): BookListDataBest {
        return holder[position]
    }
}