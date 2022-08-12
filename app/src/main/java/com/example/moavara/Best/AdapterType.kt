package com.example.moavara.Best

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Search.BestType
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ItemBestTypeBinding

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

                if (item.type == "Joara" || item.type == "Joara_Nobless" || item.type == "Joara_Premium") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_joara)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "Naver_Challenge" || item.type == "Naver_Today" || item.type == "Naver") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_naver)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "Kakao") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_kakao)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "Kakao_Stage") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_kakaostage)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "Munpia") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_munpia)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "OneStore") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_onestore)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "Ridi") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_ridibooks)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "Toksoda") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_toksoda)
                        .circleCrop()
                        .into(iview)
                } else if (item.type == "MrBlue") {
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.logo_mrblue)
                        .circleCrop()
                        .into(iview)
                }

                if(getSelectedBtn() == position){
                    tveiwTitle.setTextColor(Color.parseColor("#EDE6FD"));

                    llayoutWrap.background = GradientDrawable().apply {
                        setColor(Color.parseColor("#621CEF"))
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 100f.dpToPx()
                        setStroke(1f.dpToPx().toInt(), Color.parseColor("#621CEF"))
                    }
                } else {
                    tveiwTitle.setTextColor(Color.parseColor("#EDE6FD"));
                    llayoutWrap.background = GradientDrawable().apply {
                        setColor(Color.parseColor("#0D0E10"))
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 100f.dpToPx()
                        setStroke(1f.dpToPx().toInt(), Color.parseColor("#3E424B"))
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