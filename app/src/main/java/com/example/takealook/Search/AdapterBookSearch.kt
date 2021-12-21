package com.example.takealook.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.R
import java.util.ArrayList


class AdapterBookSearch(private val mContext: Context, items: List<BookListData?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listData: ArrayList<BookListData?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = listData!![position]
            Glide.with(holder.itemView.context)
                .load(item!!.bookImg)
                .into(holder.image)


            holder.title.text = listData!![position]!!.title
            holder.writer.text = listData!![position]!!.writer
            holder.intro.text = listData!![position]!!.intro
            holder.bookCodeWrap.text = listData!![position]!!.bookCode
            holder.bookFav.text = listData!![position]!!.isFav
//            holder.category.text = listData!![position]!!.bookCategory
//            holder.textCntChapter.text = listData!![position]!!.cntChapter

            val topText: TextView = holder.topText
            val bar: TextView = holder.bar
            val category: TextView = holder.category

        }
    }

    fun textSetting(title: Int, color: Int, topText: TextView, bar: TextView, category: TextView) {
        topText.setText(title)
        topText.setTextColor(color)
        bar.setTextColor(color)
        category.setTextColor(color)
    }

    override fun getItemCount(): Int {
        return if (listData == null) 0 else listData!!.size
    }

    inner class MainBookViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var favon: ImageView
        var favoff: ImageView
        var title: TextView
        var writer: TextView
        var intro: TextView
        var topText: TextView
        var bookCodeWrap: TextView
        var bookFav: TextView
        var textCntChapter: TextView
        var bar: TextView
        var category: TextView
        var imgWrap: CardView
        var bookContentsWrapC: LinearLayout

        init {
            image = itemView.findViewById(R.id.Img_Book)
            title = itemView.findViewById(R.id.Text_Title)
            intro = itemView.findViewById(R.id.Text_Intro)
            writer = itemView.findViewById(R.id.Text_Writer)
            topText = itemView.findViewById(R.id.TopText)
            favon = itemView.findViewById(R.id.FavON)
            favoff = itemView.findViewById(R.id.FavOff)
            imgWrap = itemView.findViewById(R.id.Img_Wrap)
            bookCodeWrap = itemView.findViewById(R.id.BookCodeText)
            bookFav = itemView.findViewById(R.id.TextBookFav)
            bookContentsWrapC = itemView.findViewById(R.id.BookContentsWrapC)
            bar = itemView.findViewById(R.id.Bar)
            category = itemView.findViewById(R.id.Category)
            textCntChapter = itemView.findViewById(R.id.Text_CntChapter)

            val token = mContext.getSharedPreferences("LOGIN", AppCompatActivity.MODE_PRIVATE)
                .getString("TOKEN", "")

            bookContentsWrapC.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "BookDetail")
                }
            }

            if (token != "") {
                imgWrap.setOnClickListener { v: View? ->
                    if (favoff.visibility == View.VISIBLE) {
                        favoff.visibility = View.GONE
                        favon.visibility = View.VISIBLE
                    } else {
                        favoff.visibility = View.VISIBLE
                        favon.visibility = View.GONE
                    }
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos, "FAV")
                    }
                }
            } else {
                imgWrap.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "선호작을 등록하려면 로그인이 필요합니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun setItems(items: List<BookListData?>?) {
        listData = items as ArrayList<BookListData?>?
    }

    fun getItem(position: Int): BookListData? {
        return listData!![position]
    }

    init {
        listData = items as ArrayList<BookListData?>?
    }
}