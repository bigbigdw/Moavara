package com.example.moavara.Search

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.R
import com.example.moavara.Util.applyingTextColor
import com.example.moavara.databinding.ItemSearchBinding


class AdapterBookSearch(private var holder: ArrayList<BookListDataBest>, private var query: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

            val item = this.holder[position]

            with(holder.binding) {

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

                if(item.writer.contains(query)){
                    val writer = SpannableStringBuilder(item.writer)
                    writer.applyingTextColor(
                        query,
                        "#844DF3"
                    )
                    tviewWriter.text = writer
                } else {
                    tviewWriter.text = item.writer
                }

                if(item.title.contains(query)){
                    val title = SpannableStringBuilder(item.title)
                    title.applyingTextColor(
                        query,
                        "#844DF3"
                    )
                    tviewTitle.text = title
                } else {
                    tviewTitle.text = item.title
                }

                if (item.info1.isEmpty()) {
                    tviewIntro.visibility = View.GONE
                    tviewBar.visibility = View.GONE
                } else {
                    tviewIntro.visibility = View.VISIBLE
                    tviewBar.visibility = View.VISIBLE
                }

                if (item.info2.isEmpty()) {
                    tviewInfo.visibility = View.GONE
                } else {
                    tviewInfo.visibility = View.VISIBLE
                }

                if (item.info3.isEmpty()) {
                    tviewInfo3.visibility = View.GONE
                } else {
                    tviewInfo3.visibility = View.VISIBLE
                }

                if (item.info4.isEmpty()) {
                    tviewInfo4.visibility = View.GONE
                } else {
                    tviewInfo4.visibility = View.VISIBLE
                }

                if (item.info5.isEmpty()) {
                    tviewInfo5.visibility = View.GONE
                } else {
                    tviewInfo5.visibility = View.VISIBLE
                }

                when (item.type) {
                    "Joara" -> {

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
                        tviewInfo5.text = info3
                        tviewInfo4.text = info4
                        tviewInfo3.text = info5
                        tviewIntro.text = item.info1
                    }
                    "Kakao" -> {

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
                        tviewInfo5.text = info3
                        tviewInfo4.text = info4
                    }
                    "Kakao_Stage" -> {

                        val info3 = SpannableStringBuilder("선호작 수 : ${item.info3}")
                        info3.applyingTextColor(
                            "선호작 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder("조회 수 : ${item.info4}")
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
                        tviewInfo5.text = info3
                        tviewInfo4.text = info4
                        tviewInfo3.text = info5
                        tviewIntro.text = item.info1
                    }
                    "Naver", "Naver_Today", "Naver_Challenge" -> {
                        tviewInfo.text = item.info2
                    }
                    "Munpia" -> {

                        val info3 = SpannableStringBuilder(item.info5.replace("선호 ", "선호작 수 : "))
                        info3.applyingTextColor(
                            "선호작 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder(item.info4.replace("추천 ", "추천 수 : "))
                        info4.applyingTextColor(
                            "추천 수 : ",
                            "#6E7686"
                        )

                        val info5 = SpannableStringBuilder(item.info3.replace("조회 ", "조회 수 : "))
                        info5.applyingTextColor(
                            "조회 수 : ",
                            "#6E7686"
                        )

                        tviewInfo.text = item.info2
                        tviewInfo5.text = info5
                        tviewInfo4.text = info4
                        tviewInfo3.text = info3
                        tviewIntro.text = item.info1
                    }
                    "Toksoda" -> {

                        val info5 = SpannableStringBuilder("댓글 수 : ${item.info5}")
                        info5.applyingTextColor(
                            "댓글 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder("선호작 수 : ${item.info4}")
                        info4.applyingTextColor(
                            "선호작 수 : ",
                            "#6E7686"
                        )

                        val info3 = SpannableStringBuilder("장르 : ${item.info3}")
                        info3.applyingTextColor(
                            "장르 : ",
                            "#6E7686"
                        )

                        tviewInfo.text = item.info2
                        tviewInfo5.text = info5
                        tviewInfo4.text = info4
                        tviewInfo3.text = info3
                        tviewIntro.text = item.info1
                    }
                    "MrBlue" -> {

                        val info5 = SpannableStringBuilder("${item.info3.replace("리뷰 ", "평가 수 : ").replace("건", "")}")
                        info5.applyingTextColor(
                            "평가 수 : ",
                            "#6E7686"
                        )


                        val info4 = SpannableStringBuilder(item.info4)
                        info4.applyingTextColor(
                            "권 완결",
                            "#6E7686"
                        )

                        val info3 = SpannableStringBuilder("출판사 : ${item.info5}")
                        info3.applyingTextColor(
                            "출판사 : ",
                            "#6E7686"
                        )

                        tviewInfo.text = item.info2
                        tviewInfo5.text = info5
                        tviewInfo4.text = info3
                        tviewInfo3.text = info4
                        tviewIntro.text = item.info1
                    }
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