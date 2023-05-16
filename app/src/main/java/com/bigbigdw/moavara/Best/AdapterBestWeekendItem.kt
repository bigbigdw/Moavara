package com.bigbigdw.moavara.Best

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.databinding.CuItemBestWeekendBinding

class AdapterBestWeekendItem(
    private var items: ArrayList<BookListDataBest>?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = CuItemBestWeekendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val bookItem = items?.get(position)

            with(holder.binding) {
                if (bookItem != null) {
                    if(bookItem.bookCode != ""){
                        llayoutNull.visibility = View.GONE
                        tviewTitle.visibility = View.VISIBLE
                        tviewWriter.visibility = View.VISIBLE

                        Glide.with(root.context)
                            .load (bookItem.bookImg.replace("http://","https://"))
                            .into(iviewImg)

                        tviewTitle.text = bookItem.title
                        tviewWriter.text = bookItem.writer

                        if(bookItem.bookImg.isNotEmpty()){
                            when (position) {
                                0 -> {
                                    iviewRank.setImageResource(R.drawable.icon_best_1)
                                    iviewRank.visibility = View.VISIBLE
                                }
                                1 -> {
                                    iviewRank.setImageResource(R.drawable.icon_best_2)
                                    iviewRank.visibility = View.VISIBLE
                                }
                                2 -> {
                                    iviewRank.setImageResource(R.drawable.icon_best_3)
                                    iviewRank.visibility = View.VISIBLE
                                }
                                else -> {
                                    iviewRank.visibility = View.GONE
                                }
                            }
                        }

                    } else {
                        llayoutNull.visibility = View.VISIBLE
                        tviewTitle.visibility = View.GONE
                        tviewWriter.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    inner class HolderBestWeekend internal constructor(val binding: CuItemBestWeekendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            with(binding) {
                root.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION
                        && (items?.get(adapterPosition)?.bookCode ?: "") != ""
                    ) {
                        listener?.onItemClick(v, pos)
                    }
                }
            }
        }

    }

    fun getItem(position: Int): BookListDataBest? {
        return items?.get(position)
    }

}