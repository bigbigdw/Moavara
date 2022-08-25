package com.example.moavara.Search

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ItemSearchKeywordBinding

class AdapterSearchKeyword(private var holder: List<BestType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selected = 0

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemSearchKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder[position]

            with(holder.binding){
                tveiwTitle.text = item.title

                if (item.type == "Joara" || item.type == "Joara_Nobless" || item.type == "Joara_Premium") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_joara)
                        .into(iview)
                } else if (item.type == "Naver_Challenge" || item.type == "Naver_Today" || item.type == "Naver") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_naver)
                        .into(iview)
                } else if (item.type == "Kakao") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_kakao)
                        .into(iview)
                } else if (item.type == "Kakao_Stage") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_kakaostage)
                        .into(iview)
                } else if (item.type == "Munpia") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_munpia)
                        .into(iview)
                } else if (item.type == "OneStore") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_onestore)
                        .into(iview)
                } else if (item.type == "Ridi") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_ridibooks)
                        .into(iview)
                } else if (item.type == "Toksoda") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_toksoda)
                        .into(iview)
                } else if (item.type == "MrBlue") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_mrblue)
                        .into(iview)
                } else {
                    Glide.with(holder.itemView.context)
                        .load(R.mipmap.ic_launcher)
                        .into(iview)
                }

                if(getSelectedBtn() == position){
                    tveiwTitle.setTextColor(Color.parseColor("#EDE6FD"))
                    llayoutShadow.visibility = View.GONE

                    cviewWrap.background = GradientDrawable().apply {
                        setStroke(2f.dpToPx().toInt(), Color.parseColor("#844DF3"))
                        cornerRadius = 15f.dpToPx()
                    }
                } else {
                    llayoutShadow.visibility = View.VISIBLE
                    tveiwTitle.setTextColor(Color.parseColor("#565C69"))

                    cviewWrap.background = GradientDrawable().apply {
                        cornerRadius = 15f.dpToPx()
                        setStroke(0f.dpToPx().toInt(), Color.parseColor("#844DF3"))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemSearchKeywordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding){

                llayoutWrap.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos)
                    }
                }
            }
        }


    }

    fun getItem(position: Int): BestType {
        return holder[position]
    }

    fun setSelectedBtn(select: Int) {
        selected = select
    }

    fun getSelectedBtn(): Int {
        return selected
    }

}