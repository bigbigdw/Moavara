package com.example.moavara.Community

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Search.CommunityBoard
import com.example.moavara.databinding.ItemCommunityBinding


class AdapterCommunity(items: List<CommunityBoard?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<CommunityBoard?>? = items as ArrayList<CommunityBoard?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.holder!![position]

            if (item != null) {
                Glide.with(holder.itemView.context)
                    .load(R.mipmap.ic_launcher)
                    .circleCrop()
                    .into(holder.binding.iView)

                if(item.title.contains("조아라")){
                    val title = SpannableStringBuilder(item.title)
                    title.applyingTextColor(
                        "조아라",
                        "#844DF3"
                    )
                    holder.binding.tviewTitle.text = title
                } else {
                    holder.binding.tviewTitle.text = item.title
                }

                holder.binding.tviewDate.text = item.date
            }

        }
    }

    /**
     * 텍스트 문자중 일부를 컬러 텍스트로 변경
     * @param str 변경 문자
     * @param color 변경할 색
     * */
    fun SpannableStringBuilder.applyingTextColor(str: String, color: String) {
        this.setSpan(

            ForegroundColorSpan(Color.parseColor(color)),
            indexOf(str),
            indexOf(str) + str.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }


    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHolder internal constructor(val binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.llayoutWrap.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(it, pos)
                }
            }
        }
    }

    fun getItem(position: Int): CommunityBoard? {
        return holder!![position]
    }

}