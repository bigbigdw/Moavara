package com.example.takealook.Best

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.Main.ActivityMain
import com.example.takealook.Search.BookListDataBestToday
import com.example.takealook.R
import com.example.takealook.Search.BookListDataBestWeekend
import java.util.ArrayList

class AdapterBestWeekend(
    private val mContext: Context, items:
    ArrayList<BookListDataBestWeekend?>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BookListDataBestWeekend?> = items

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booklist_best_weekend, parent, false)
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val items = item[position]

            if(items!!.mon != null){
                Glide.with(holder.itemView.context)
                    .load(items.mon!!.bookImg)
                    .into(holder.iviewBookImg1)
            }

            if(items.tue != null){
                Glide.with(holder.itemView.context)
                    .load(items.tue!!.bookImg)
                    .into(holder.iviewBookImg2)
            }

            if(items.wed != null){
                Glide.with(holder.itemView.context)
                    .load(items.wed!!.bookImg)
                    .into(holder.iviewBookImg3)
            }

            if(items.thur != null){
                Glide.with(holder.itemView.context)
                    .load(items.thur!!.bookImg)
                    .into(holder.iviewBookImg1)
            }

            if(items!!.fri != null){
                Glide.with(holder.itemView.context)
                    .load(items.fri!!.bookImg)
                    .into(holder.iviewBookImg1)
            }

            if(items.sat != null){
                Glide.with(holder.itemView.context)
                    .load(items.sat!!.bookImg)
                    .into(holder.iviewBookImg1)
            }

            if(items.sun != null){
                Glide.with(holder.itemView.context)
                    .load(items.sun!!.bookImg)
                    .into(holder.iviewBookImg1)
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class HolderBestWeekend internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var cvieWrap1: CardView = itemView.findViewById(R.id.cview_Wrap1)
        var iviewBookImg1: ImageView = itemView.findViewById(R.id.iview_BookImg1)
        var llayoutCover1: LinearLayout = itemView.findViewById(R.id.llayout_Cover1)

        var cvieWrap2: CardView = itemView.findViewById(R.id.cview_Wrap2)
        var iviewBookImg2: ImageView = itemView.findViewById(R.id.iview_BookImg2)
        var llayoutCover2: LinearLayout = itemView.findViewById(R.id.llayout_Cover2)

        var cvieWrap3: CardView = itemView.findViewById(R.id.cview_Wrap3)
        var iviewBookImg3: ImageView = itemView.findViewById(R.id.iview_BookImg3)
        var llayoutCover3: LinearLayout = itemView.findViewById(R.id.llayout_Cover3)

        var cvieWrap4: CardView = itemView.findViewById(R.id.cview_Wrap4)
        var iviewBookImg4: ImageView = itemView.findViewById(R.id.iview_BookImg4)
        var llayoutCover4: LinearLayout = itemView.findViewById(R.id.llayout_Cover4)

        var cvieWrap5: CardView = itemView.findViewById(R.id.cview_Wrap5)
        var iviewBookImg5: ImageView = itemView.findViewById(R.id.iview_BookImg5)
        var llayoutCover5: LinearLayout = itemView.findViewById(R.id.llayout_Cover5)

        var cvieWrap6: CardView = itemView.findViewById(R.id.cview_Wrap6)
        var iviewBookImg6: ImageView = itemView.findViewById(R.id.iview_BookImg6)
        var llayoutCover6: LinearLayout = itemView.findViewById(R.id.llayout_Cover6)

        var cvieWrap7: CardView = itemView.findViewById(R.id.cview_Wrap7)
        var iviewBookImg7: ImageView = itemView.findViewById(R.id.iview_BookImg7)
        var llayoutCover7: LinearLayout = itemView.findViewById(R.id.llayout_Cover7)

    }

    fun getItem(position: Int): BookListDataBestWeekend? {
        return item[position]
    }

}