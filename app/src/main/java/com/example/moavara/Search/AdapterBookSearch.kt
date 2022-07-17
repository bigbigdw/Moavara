package com.example.moavara.Search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.databinding.ItemSearchBinding
import java.util.*


class AdapterBookSearch(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listData = ArrayList<BookListData>()

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

            with(holder.binding){
                val item = listData[position]
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .into(iview)

                tviewTitle.text = listData[position].title
                tviewWriter.text = listData[position].writer
                tviewIntro.text = listData[position].intro
                tviewInfo1.text = listData[position].bookCode
                tviewInfo2.text = listData[position].isFav
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun setItems(items: ArrayList<BookListData>) {
        val cmpAsc: Comparator<BookListData> =
            Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
        Collections.sort(items, cmpAsc)
        Log.d("####_!!!",items.toString())
        listData.addAll(items)
        Log.d("####_!!!","HIHI")
    }

    fun getItem(position: Int): BookListData {
        return listData[position]
    }
}