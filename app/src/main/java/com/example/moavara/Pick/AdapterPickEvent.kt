package com.example.moavara.Pick

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.Search.EventData
import com.example.moavara.databinding.ItemPickEventBinding
import kotlin.collections.ArrayList

class AdapterPickEvent(items: List<EventData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemsList: ArrayList<EventData> = items as ArrayList<EventData>
    var memo = ""


    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, type: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemPickEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.itemsList!![position]

            with(holder.binding){
                Glide.with(holder.itemView.context)
                    .load(item!!.imgfile)
                    .into(iView)

                tviewTitle.text = item.title
                tviewPlatform.text = item.type
                tviewMemo.text = item.memo
                tviewGenre.text = item.genre

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

    override fun getItemCount(): Int {
        return if (itemsList == null) 0 else itemsList.size
    }

    fun editItem(items: EventData, position: Int) {
        itemsList[position] = items
        notifyItemChanged(position)
    }

    inner class ViewHolder internal constructor(val binding: ItemPickEventBinding) :
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
                                etviewMemo.text = tviewMemo.text as Editable?
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


            }
        }
    }

    fun getItem(position: Int): EventData {
        return itemsList[position]
    }

    fun getMemoEdit(): String {
        return memo
    }

    fun setMemoEdit(str : String){
        memo = str
    }

}