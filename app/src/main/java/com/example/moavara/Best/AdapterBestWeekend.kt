package com.example.moavara.Best

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.databinding.ItemBooklistBestWeekendBinding


class AdapterBestWeekend(
    private var context : Context,
    private var items: ArrayList<ArrayList<BookListDataBest>? >,
    private var platform : String,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBooklistBestWeekendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val itemList = items[position]

            with(holder.binding) {
                val adapter = AdapterBestWeekendItem(context, itemList)

                rview.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rview.adapter = adapter

                adapter.setOnItemClickListener(object : AdapterBestWeekendItem.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        val item: BookListDataBest? = adapter.getItem(position)

                        if(platform == "MrBlue"){
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse( "https://www.mrblue.com/novel/${item?.bookCode}")
                            )
                            context.startActivity(intent)
                        } else {
                            val mBottomDialogBest = BottomDialogBest(
                                context,
                                item,
                                platform,
                                item?.number ?: 0,
                            )
                            mBottomDialogBest.show((context as AppCompatActivity).supportFragmentManager, null)
                        }
                    }
                })

                if (position == 0) {
                    tviewBestTop.text = "일요일 주간 베스트"
                } else if (position == 1) {
                    tviewBestTop.text = "월요일 주간 베스트"
                } else if (position == 2) {
                    tviewBestTop.text = "화요일 주간 베스트"
                } else if (position == 3) {
                    tviewBestTop.text = "수요일 주간 베스트"
                } else if (position == 4) {
                    tviewBestTop.text = "목요일 주간 베스트"
                } else if (position == 5) {
                    tviewBestTop.text = "금요일 주간 베스트"
                } else if (position == 6) {
                    tviewBestTop.text = "토요일 주간 베스트"
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class HolderBestWeekend internal constructor(val binding: ItemBooklistBestWeekendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {}

    }

    fun getItem(position: Int): java.util.ArrayList<BookListDataBest>? {
        return items[position]
    }

}