package com.bigbigdw.moavara.User

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.FCMAlert
import com.bigbigdw.moavara.databinding.ItemNoticesBinding

class AdapterNotice(
    private var holder: List<FCMAlert>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPos = -1

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemNoticesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder[position]

            val year = item.date.substring(0,4)
            val month = item.date.substring(4,6)
            val day = item.date.substring(6,8)

            with(holder.binding){
                tviewDate.text = "${year}년 ${month}월 ${day}일"
                tviewTitle.text = item.title
                tviewBody.text = item.body

                if(selectedPos == position){
                    if(tviewBody.visibility == View.VISIBLE){
                        tviewBody.visibility = View.GONE
                        ivewBookImg.setImageResource(R.drawable.ic_down_24px)
                    } else {
                        tviewBody.visibility = View.VISIBLE
                        ivewBookImg.setImageResource(R.drawable.ic_up_24px)
                    }
                } else {
                    tviewBody.visibility = View.GONE
                    ivewBookImg.setImageResource(R.drawable.ic_down_24px)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemNoticesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {

                llayoutWrap.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos)

                        if(selectedPos == pos){
                            selectedPos = -1
                        } else {
                            selectedPos = pos
                        }
                        notifyDataSetChanged()

                    }
                }
            }
        }
    }

    fun getItem(position: Int): FCMAlert {
        return holder[position]
    }

}