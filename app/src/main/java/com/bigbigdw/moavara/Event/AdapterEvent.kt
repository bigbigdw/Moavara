package com.bigbigdw.moavara.Event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.DataBase.EventDataGroup
import com.bigbigdw.moavara.databinding.ItemEventBinding

class AdapterEvent(private var items: List<EventDataGroup>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, type: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHoderEvent(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHoderEvent) {

            val item = items[position]

            with(holder.binding){
                if(item.left?.imgfile == ""){
                    iviewLeft.visibility = View.INVISIBLE
                    tviewLeft.text = item.left?.title
                } else {
                    iviewLeft.visibility = View.VISIBLE
                    Glide.with(holder.itemView.context)
                        .load(item.left?.imgfile)
                        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                        .into(iviewLeft)
                }

                if(item.right?.imgfile == ""){
                    iviewRight.visibility = View.INVISIBLE
                    tviewRight.text = item.right?.title
                } else {
                    iviewRight.visibility = View.VISIBLE
                    Glide.with(holder.itemView.context)
                        .load(item.right?.imgfile)
                        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                        .into(iviewRight)
                }
            }



        }
    }

    override fun getItemCount(): Int {
        return if (items == null) 0 else items.size
    }

    inner class ViewHoderEvent internal constructor(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding){
                flayoutLeft.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "Left")
                    }
                }

                flayoutRight.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "Right")
                    }
                }
            }
        }


    }

    fun getItem(position: Int): EventDataGroup {
        return items[position]
    }

}