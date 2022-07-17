package com.example.moavara.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.databinding.ItemSearchBinding


class AdapterBookSearch(items: List<BookListData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BookListData> = items as ArrayList<BookListData>

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
                        .into(iview)

                tviewTitle.text = item.title
                tviewWriter.text = item.writer
                tviewIntro.text = item.intro
                tviewInfo1.text = item.bookCode
                tviewInfo2.text = item.isFav
            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun getItem(position: Int): BookListData {
        return holder[position]
    }
}