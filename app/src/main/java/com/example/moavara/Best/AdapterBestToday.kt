package com.example.moavara.Best

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.R
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ItemBooklistBestTodayBinding

class AdapterBestToday(
    private var holder: List<BookListDataBest>,
    private var bookCodeItems: ArrayList<BookListDataBestAnalyze>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBooklistBestTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder[position]

            with(holder.binding){
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .circleCrop()
                    .into(ivewBookImg)

                tviewIndex.text = (position + 1).toString()
                tviewTitle.text = item.title

                try{
                    if(bookCodeItems[position].trophyCount > 1){
                        if(bookCodeItems[position].numberDiff > 0){
                            tviewNum.text =  "${bookCodeItems[position].numberDiff} (${bookCodeItems[position].trophyCount})"
                            tviewNum.setTextColor(Color.parseColor("#02BC77"))
                            iviewArrow.setImageResource(R.drawable.ic_arrow_drop_up_24px)
                            iviewArrow.visibility = View.VISIBLE
                        } else if(bookCodeItems[position].numberDiff < 0){
                            tviewNum.text =  "${bookCodeItems[position].numberDiff} (${bookCodeItems[position].trophyCount})"
                            tviewNum.setTextColor(Color.parseColor("#FF2366"))
                            iviewArrow.setImageResource(R.drawable.ic_arrow_drop_down_24px)
                            iviewArrow.visibility = View.VISIBLE
                        } else if(bookCodeItems[position].numberDiff == 0){
                            tviewNum.text = "-"
                            tviewNum.setTextColor(Color.parseColor("#ffffff"))
                            iviewArrow.visibility = View.GONE
                        }
                    } else {
                        tviewNum.text = "NEW"
                        tviewNum.setTextColor(Color.parseColor("#844DF3"))
                        iviewArrow.visibility = View.GONE
                    }

                } catch (e : IndexOutOfBoundsException){

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemBooklistBestTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            with(binding){
                ivewBookImg.background =GradientDrawable().apply {
                    cornerRadius = 100f.dpToPx()
                }

                BestWrap.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#0D0D0D"))
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 28f.dpToPx()
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

    fun getItem(position: Int): BookListDataBest {
        return holder[position]
    }

}