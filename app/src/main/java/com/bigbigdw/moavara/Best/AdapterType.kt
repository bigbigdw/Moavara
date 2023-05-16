package com.bigbigdw.moavara.Best

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.BestType
import com.bigbigdw.moavara.Util.dpToPx
import com.bigbigdw.moavara.databinding.ItemBestTypeBinding

class AdapterType(private var holder: List<BestType>) :
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
        val view = ItemBestTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder[position]

            with(holder.binding){
                tveiwTitle.text = item.title

                if (item.type == "Joara") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_joara)
                        .into(iview)
                } else if (item.type == "Joara_Nobless") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_joara_nobless)
                        .into(iview)
                } else if (item.type == "Joara_Premium") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_joara_premium)
                        .into(iview)
                } else if (item.type == "Naver_Today") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_naver)
                        .into(iview)
                }  else if (item.type == "Naver_Challenge") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_naver_challenge)
                        .into(iview)
                } else if (item.type == "Naver") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_naver_challenge)
                        .into(iview)
                }else if (item.type == "Kakao") {
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
                    tveiwTitle.setTextColor(Color.parseColor("#EDE6FD"));

                    llayoutWrap.background = GradientDrawable().apply {
                        setColor(Color.parseColor("#0D0E10"))
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 100f.dpToPx()
                        setStroke(2f.dpToPx().toInt(), Color.parseColor("#621CEF"))
                    }
                } else {
                    tveiwTitle.setTextColor(Color.parseColor("#EDE6FD"));
                    llayoutWrap.background = GradientDrawable().apply {
                        setColor(Color.parseColor("#0D0E10"))
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 100f.dpToPx()
                        setStroke(2f.dpToPx().toInt(), Color.parseColor("#3E424B"))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemBestTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding){
                llayoutWrap.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#0D0E10"))
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 100f.dpToPx()
                    setStroke(2f.dpToPx().toInt(), Color.parseColor("#3E424B"))
                }

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