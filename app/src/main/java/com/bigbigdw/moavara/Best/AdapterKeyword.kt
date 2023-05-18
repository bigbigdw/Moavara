package com.bigbigdw.moavara.Best

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bigbigdw.moavara.DataBase.BestType
import com.bigbigdw.moavara.Util.dpToPx
import com.bigbigdw.moavara.databinding.ItemBestTypeBinding

class AdapterKeyword(private var holder: List<BestType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

                iview.visibility = View.GONE

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
}