package com.bigbigdw.moavara.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.databinding.ItemSearchmoavaraBinding

class AdapterBestMoavara(
    private var holder: ArrayList<BestItemData>,
    private var searchHolder: ArrayList<BestItemData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isSearch = false
    var searchKeyword = ""

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemSearchmoavaraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.searchHolder[position]

            with(holder.binding){
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .into(iview)

                tviewTitle.text = item.title
                tviewWriter.text = item.writer
                tviewInfo1.text = item.info3
                tviewInfo2.text = item.info4
                tviewInfo3.text = item.info5
                tviewPlatform.text = item.type
            }
        }
    }

    override fun getItemCount(): Int {
        return searchHolder.size
    }

    fun search(keyword : String){

        val newHolder = ArrayList<BestItemData>()

        for(item in holder){
            if(item.title.contains(keyword)){
                newHolder.add(item)
            }
        }

        isSearch = true
        searchKeyword = keyword
        searchHolder = newHolder
        notifyDataSetChanged()
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemSearchmoavaraBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.llayoutWrap.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(v, pos)
                }
            }

        }


    }

    fun getItem(position: Int): BestItemData {
        return searchHolder[position]
    }

}