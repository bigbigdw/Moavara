package com.example.moavara.Pick

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.databinding.ItemPickEventBinding
import com.example.moavara.databinding.ItemTestBinding
import java.util.*
import kotlin.collections.ArrayList

class AdapterPickNovel (private var context: Context, private var itemsList: ArrayList<BookListDataBest>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var memo = ""

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, type: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.itemsList[position]

            with(holder.binding){
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .into(iView)

                tviewTitle.text = item.title
                tviewWriter.text = item.writer
                tviewMemo.text = item.memo
                tviewInfo1.text = item.date

                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                    etviewMemo.addTextChangedListener(object :
                        TextWatcher {
                        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                            Log.d("holder.Comment_EditText", "beforeTextChanged")
                        }

                        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                            tviewMemo.text = text
                            setMemoEdit(text.toString())
                        }

                        override fun afterTextChanged(s: Editable) {
                            Log.d("holder.Comment_EditText", "onTextChanged")
                        }
                    })
                }
            }
        }
    }

    // -----------------데이터 조작함수 추가-----------------

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        itemsList.removeAt(position)
        notifyItemRemoved(position)
    }

    // 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(itemsList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun editItem(items: BookListDataBest, position: Int) {
        itemsList[position] = items
        notifyItemChanged(position)
    }

    inner class ViewHolder internal constructor(val binding: ItemTestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding){
                iView.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos, "Img")
                    }
                }

                tviewbtnMemo.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos, "Memo")

                        if(llayoutWrapMemo.visibility == View.VISIBLE){
                            llayoutWrapMemo.visibility = View.GONE
                        } else {
                            llayoutWrapMemo.visibility = View.VISIBLE
                        }
                    }
                }

                tviewEdit.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos, "Edit")

                        if(etviewMemo.visibility == View.VISIBLE){
                            etviewMemo.visibility = View.GONE
                            tviewMemo.visibility = View.VISIBLE
                        } else {
                            etviewMemo.visibility = View.VISIBLE

                            if(tviewMemo.text == ""){
                                etviewMemo.hint = "메모를 입력해주세요"
                            } else {
                                val editable: Editable = SpannableStringBuilder(tviewMemo.text)
                                etviewMemo.text = editable
                            }

                            tviewMemo.visibility = View.GONE
                        }
                    }
                }

                tviewbtnConfirm.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos, "Confirm")
                        etviewMemo.visibility = View.GONE
                        tviewMemo.visibility = View.VISIBLE
                    }
                }

                tviewbtnDel.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos, "Delete")
                    }
                }

            }
        }
    }

    fun getItem(position: Int): BookListDataBest {
        return itemsList[position]
    }

    fun getMemoEdit(): String {
        return memo
    }

    fun setMemoEdit(str : String){
        memo = str
    }

}