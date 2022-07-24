package com.example.moavara.Search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.databinding.ItemSearchmoavaraBinding

class AdapterBestMoavara(
    private var holder: List<BookListDataBest>
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

            val item = this.holder[position]

//            if(isSearch){
//                if(item.title.contains(searchKeyword)){
//                    holder.itemView.visibility = View.GONE
//                    holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
//                } else {
//                    holder.itemView.visibility = View.VISIBLE
//                    holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                }
//            }

            if(item.title.contains("짝사랑")){
                holder.itemView.visibility = View.VISIBLE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }

            with(holder.binding){
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .circleCrop()
                    .into(ivewBookImg)

                tviewTitle.text = item.title
            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    fun search(keyword : String){
        searchKeyword = keyword
        Log.d("####", searchKeyword)
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

    fun getItem(position: Int): BookListDataBest {
        return holder[position]
    }

}