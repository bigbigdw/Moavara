package com.example.moavara.Pick

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.Main.DialogConfirm
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.EventData
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ItemPickEventBinding
import java.util.*

class AdapterPickEvent(private var context : Context, private var itemsList: ArrayList<EventData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var memo = ""

    var userInfo = mRootRef.child("User")
    val UID = context.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
        ?.getString("UID", "").toString()
    val Event = userInfo.child(UID).child("Event")

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

            val item = this.itemsList[position]

            with(holder.binding){

                if(item.imgfile == ""){
                    iView.visibility = View.INVISIBLE
                    iviewBlank.visibility = View.VISIBLE
                } else {
                    iView.visibility = View.VISIBLE
                    iviewBlank.visibility = View.GONE
                    Glide.with(holder.itemView.context)
                        .load(item.imgfile)
                        .into(iView)
                }

                tviewTitle.text = item.title
                etviewMemo.text = SpannableStringBuilder(item.memo)

                llayoutMemo.background = GradientDrawable().apply {
                    setColor(Color.parseColor("#26292E"))
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 4f.dpToPx()
                    setStroke(1f.dpToPx().toInt(), Color.parseColor("#3E424B"))
                }

                if(item.memo == ""){
                    etviewMemo.hint = SpannableStringBuilder("메모가 없습니다.")
                }

                when (item.type) {
                    "Joara", "Joara_Nobless", "Joara_Premium" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_joara)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "조아라 이벤트"
                    }
                    "Naver_Challenge", "Naver_Today", "Naver" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_naver)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "네이버 이벤트"
                    }
                    "Kakao" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_kakao)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "카카오 이벤트"
                    }
                    "Kakao_Stage" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_kakaostage)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "카카오 스테이지 이벤트"
                    }
                    "Munpia" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_munpia)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "문피아 이벤트"
                    }
                    "OneStore" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_onestore)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "원스토리 이벤트"
                    }
                    "Ridi" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_ridibooks)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "리디북스 이벤트"
                    }
                    "Toksoda" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_toksoda)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "톡소다 이벤트"
                    }
                    "MrBlue" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.logo_mrblue)
                            .into(iviewPlatfrom)
                        tviewInfo.text = "미스터블루 이벤트"
                    }
                    else -> {}
                }

                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                    etviewMemo.addTextChangedListener(object :
                        TextWatcher {
                        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                            Log.d("holder.Comment_EditText", "beforeTextChanged")
                        }

                        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
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
        getItem(fromPos).number = getItem(toPos).number
        getItem(toPos).number = getItem(fromPos).number
        notifyItemMoved(fromPos, toPos)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun editItem(position: Int) {
        getItem(position).memo = getMemoEdit()
        notifyItemChanged(position)
    }

    inner class ViewHolder internal constructor(val binding: ItemPickEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding){
                swipeView.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos, "Item")
                    }
                }

                llayoutDel.setOnClickListener {
                    var dialogLogin: DialogConfirm? = null

                    // 안내 팝업
                    dialogLogin = DialogConfirm(
                        context,
                        "[${getItem(adapterPosition).title}]을(를) 삭제하시겠습니까?",
                        "",
                        {
                            dialogLogin?.dismiss()
                        },
                        { v: View? ->

                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                Event.child(getItem(adapterPosition).link).removeValue()
                                removeData(adapterPosition)
                                Toast.makeText(context, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                dialogLogin?.dismiss()
                            }
                        },
                        "취소",
                        "삭제"
                    )

                    dialogLogin.window?.setBackgroundDrawable(
                        ColorDrawable(
                            Color.TRANSPARENT)
                    )
                    dialogLogin.show()
                }

                etviewMemo.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        textView: TextView,
                        i: Int,
                        keyEvent: KeyEvent?
                    ): Boolean {
                        when (i) {
                            EditorInfo.IME_ACTION_DONE -> {
                                val pos = adapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    Event.child(getItem(adapterPosition).link).child("memo").setValue(getMemoEdit())
                                    listener?.onItemClick(textView, pos, "Confirm")
                                }
                            }
                            else -> return false
                        }

                        // 내용 비우고 다시 이벤트 할수있게 선택
                        textView.clearFocus()
                        textView.isFocusable = false
                        textView.text = ""
                        textView.isFocusableInTouchMode = true
                        textView.isFocusable = true
                        return true
                    }
                })

                llayoutMemo.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(it, pos, "Edit")
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