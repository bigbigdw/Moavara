package com.example.moavara.Soon.Best

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.moavara.databinding.CuItemBestWeekendBinding

class ItemBestWeekend : LinearLayout {
    private val mBinding = CuItemBestWeekendBinding.inflate(LayoutInflater.from(context))

    constructor(mContext: Context) : super(mContext)
    constructor(mContext: Context, attrs: AttributeSet?) : super(mContext, attrs)
    constructor(mContext: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(mContext, attrs, defStyleAttr)

    fun setItem(bookImg: String, titile : String, writer : String, hasView : Boolean) {
        with(mBinding){
            if (hasView) {
                llayoutNull.visibility = View.GONE
                tviewTitle.visibility = View.VISIBLE
                tviewWriter.visibility = View.VISIBLE

                Glide.with(root.context)
                    .load(bookImg)
                    .into(iviewImg)

                tviewTitle.text = titile
                tviewWriter.text = writer
            } else {
                llayoutNull.visibility = View.VISIBLE
                tviewTitle.visibility = View.GONE
                tviewWriter.visibility = View.GONE
            }
        }
    }

    fun getBookDetail() {
        Log.d("####", "HIHI")
    }

    init {
        addView(mBinding.root)
    }
}