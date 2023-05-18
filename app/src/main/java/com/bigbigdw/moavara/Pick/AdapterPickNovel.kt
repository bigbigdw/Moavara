package com.bigbigdw.moavara.Pick

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
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
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Main.DialogConfirm
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.BestItemData
import com.bigbigdw.moavara.Util.applyingTextColor
import com.bigbigdw.moavara.Util.dpToPx
import com.bigbigdw.moavara.databinding.ItemPickNovelBinding
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*


class AdapterPickNovel(private var context: Context, private var itemsList: ArrayList<BestItemData>, private var fragment: FragmentPickTabNovel, private var UserInfo : DataBaseUser, private var firebaseAnalytics: FirebaseAnalytics) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var memo = ""

    val Novel = mRootRef.child("User").child(UserInfo.UID).child("Novel")

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, type: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemPickNovelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                etviewMemo.text = SpannableStringBuilder(item.memo)
                tviewInfo1.text = item.date

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
                            .load(R.drawable.search_logo_joara)
                            .into(iviewPlatfrom)
                    }
                    "Naver_Challenge" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_naver_challenge)
                            .into(iviewPlatfrom)
                    }
                    "Naver_Today" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_naver_series)
                            .into(iviewPlatfrom)
                    }
                    "Naver" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_naver_best)
                            .into(iviewPlatfrom)
                    }
                    "Kakao" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_kakao)
                            .into(iviewPlatfrom)
                    }
                    "Kakao_Stage" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_kakao_stage)
                            .into(iviewPlatfrom)
                    }
                    "Munpia" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_munpia)
                            .into(iviewPlatfrom)
                    }
                    "OneStore" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_onestory)
                            .into(iviewPlatfrom)
                    }
                    "Ridi" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_ridi)
                            .into(iviewPlatfrom)
                    }
                    "Toksoda" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_toksoda)
                            .into(iviewPlatfrom)
                    }
                    "MrBlue" -> {
                        Glide.with(holder.itemView.context)
                            .load(R.drawable.search_logo_mrblue)
                            .into(iviewPlatfrom)
                    }
                }

                if (item.type == "MrBlue") {
                    tviewInfo1.visibility = View.GONE
                    tviewInfo2.visibility = View.GONE
                    tviewInfo3.visibility = View.GONE
                    tviewInfo4.visibility = View.GONE
                    tviewBar.visibility = View.GONE
                }   else if(item.type == "Toksoda"){
                    tviewInfo1.visibility = View.VISIBLE
                    tviewBar.visibility = View.VISIBLE
                    tviewInfo2.visibility = View.GONE
                    tviewInfo3.visibility = View.VISIBLE
                    tviewInfo4.visibility = View.VISIBLE

                    tviewInfo1.text = item.info2

                    val info3 = SpannableStringBuilder("조회 수 : ${item.info3}" )
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder("선호작 수 : ${item.info5}")
                    info5.applyingTextColor(
                        "선호작 수 : ",
                        "#6E7686"
                    )

                    tviewInfo3.text = info3
                    tviewInfo4.text = info5
                } else if (item.type == "Naver" || item.type == "Naver_Today" || item.type == "Naver_Challenge") {
                    tviewInfo1.text = item.info1
                    tviewInfo1.visibility = View.VISIBLE
                    tviewBar.visibility = View.VISIBLE
                    tviewInfo2.visibility = View.VISIBLE
                    tviewInfo3.visibility = View.VISIBLE

                    if(item.type == "Naver_Challenge"){
                        tviewInfo4.visibility = View.GONE
                    } else {
                        tviewInfo4.visibility = View.VISIBLE
                    }

                    val info3 = SpannableStringBuilder("별점 수 : ${item.info3.replace("별점", "")}")
                    info3.applyingTextColor(
                        "별점 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("조회 수 : ${item.info4.replace("조회", "조회 수 : ")}" )
                    info4.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder("관심 : ${item.info5.replace("관심", "관심 : ")}")
                    info5.applyingTextColor(
                        "관심 : ",
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5
                }  else if (item.type == "Kakao_Stage") {
                    tviewInfo1.text = item.info2

                    tviewInfo1.visibility = View.VISIBLE
                    tviewBar.visibility = View.VISIBLE
                    tviewInfo2.visibility = View.GONE
                    tviewInfo3.visibility = View.VISIBLE
                    tviewInfo4.visibility = View.VISIBLE

                    val info3 = SpannableStringBuilder("조회 수 : ${item.info3.replace("별점", "별점 : ")}" )
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("선호작 수 : ${item.info4.replace("조회", "조회 수 : ")}" )
                    info4.applyingTextColor(
                        "선호작 수 : ",
                        "#6E7686"
                    )

                    tviewInfo3.text = info3
                    tviewInfo4.text = info4
                } else if (item.type == "Ridi") {
                    tviewInfo1.text = item.info1
                    tviewInfo1.visibility = View.VISIBLE
                    tviewBar.visibility = View.VISIBLE
                    tviewInfo2.visibility = View.GONE
                    tviewInfo3.visibility = View.VISIBLE
                    tviewInfo4.visibility = View.VISIBLE

                    val info3 = SpannableStringBuilder("추천 수 : ${item.info3}" )
                    info3.applyingTextColor(
                        "추천 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("평점 : ${item.info4}")
                    info4.applyingTextColor(
                        "평점 : ",
                        "#6E7686"
                    )

                    tviewInfo3.text = info3
                    tviewInfo4.text = info4
                } else if (item.type == "OneStore") {
                    tviewInfo1.visibility = View.GONE
                    tviewInfo2.visibility = View.VISIBLE
                    tviewInfo3.visibility = View.VISIBLE
                    tviewInfo4.visibility = View.VISIBLE

                    val info3 = SpannableStringBuilder("조회 수 : ${item.info3.replace("별점", "별점 : ")}" )
                    info3.applyingTextColor(
                        "조회 수 : ",
                        "#6E7686"
                    )

                    val info4 = SpannableStringBuilder("평점 : ${item.info4.replace("조회", "조회 수 : ")}" )
                    info4.applyingTextColor(
                        "평점 : ",
                        "#6E7686"
                    )

                    val info5 = SpannableStringBuilder("댓글 수 : ${item.info5.replace("관심", "관심 : ")}" )
                    info5.applyingTextColor(
                        "댓글 수 : ",
                        "#6E7686"
                    )

                    tviewInfo2.text = info3
                    tviewInfo3.text = info4
                    tviewInfo4.text = info5
                } else if (item.type == "Kakao" || item.type == "Munpia" || item.type == "Toksoda" || item.type == "Joara" || item.type == "Joara_Premium" || item.type == "Joara_Nobless" || item.type == "Munpia" ) {
                    tviewInfo1.visibility = View.VISIBLE
                    tviewBar.visibility = View.VISIBLE
                    tviewInfo2.visibility = View.VISIBLE
                    tviewInfo3.visibility = View.VISIBLE
                    tviewInfo4.visibility = View.VISIBLE

                    if(item.type == "Joara" || item.type == "Joara_Premium" || item.type == "Joara_Nobless"){
                        tviewInfo1.text = item.info2

                        val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
                        info3.applyingTextColor(
                            "조회 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder("선호작 수 : ${item.info4}")
                        info4.applyingTextColor(
                            "선호작 수 : ",
                            "#6E7686"
                        )

                        val info5 = SpannableStringBuilder("추천 수 : ${item.info5}")
                        info5.applyingTextColor(
                            "추천 수 : ",
                            "#6E7686"
                        )

                        tviewInfo2.text = info3
                        tviewInfo3.text = info4
                        tviewInfo4.text = info5

                    } else if(item.type == "Kakao"){
                        tviewInfo1.text = item.info2

                        val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
                        info3.applyingTextColor(
                            "조회 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder("추천 수 : ${item.info4}")
                        info4.applyingTextColor(
                            "추천 수 : ",
                            "#6E7686"
                        )

                        val info5 = SpannableStringBuilder("평점 : ${item.info5}")
                        info5.applyingTextColor(
                            "평점 : ",
                            "#6E7686"
                        )

                        tviewInfo2.text = info3
                        tviewInfo3.text = info4
                        tviewInfo4.text = info5
                    } else if(item.type == "Munpia"){
                        tviewInfo1.text = item.info2

                        val info3 = SpannableStringBuilder("조회 수 : ${item.info3}")
                        info3.applyingTextColor(
                            "조회 수 : ",
                            "#6E7686"
                        )

                        val info4 = SpannableStringBuilder("방문 수 : ${item.info4}")
                        info4.applyingTextColor(
                            "방문 수 : ",
                            "#6E7686"
                        )

                        val info5 = SpannableStringBuilder("선호작 수 : ${item.info5}")
                        info5.applyingTextColor(
                            "선호작 수 : ",
                            "#6E7686"
                        )

                        tviewInfo2.text = info3
                        tviewInfo3.text = info4
                        tviewInfo4.text = info5
                    } else {
                        tviewInfo1.text = item.info2
                        tviewInfo2.text = item.info3
                        tviewInfo3.text = item.info4
                        tviewInfo4.text = item.info5
                    }
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

        val bundle = Bundle()
        bundle.putString("PICK_NOVEL_STATUS", "DELETE")
        firebaseAnalytics.logEvent("PICK_FragmentPickTabNovel", bundle)
    }

    // 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(itemsList, fromPos, toPos)
        getItem(fromPos).number = getItem(toPos).number
        getItem(toPos).number = getItem(fromPos).number
        notifyItemMoved(fromPos, toPos)
        for(i in itemsList.indices){
            mRootRef.child("User").child(UserInfo.UID).child("Novel").child("book").child(itemsList[i].bookCode).child("number").setValue((itemsList.size - i))
        }

        val bundle = Bundle()
        bundle.putString("PICK_NOVEL_STATUS", "SWAP")
        firebaseAnalytics.logEvent("PICK_FragmentPickTabNovel", bundle)
    }

    fun selectedItem(position: Int) {
        Toast.makeText(context, "이동하실 위치로 드래그해주세요.", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun editItem(position: Int) {
        getItem(position).memo = getMemoEdit()
        notifyItemChanged(position)
    }

    inner class ViewHolder internal constructor(val binding: ItemPickNovelBinding) :
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
                                Novel.child("book").child(getItem(adapterPosition).bookCode).removeValue()
                                Novel.child("bookCode").child(getItem(adapterPosition).bookCode).removeValue()
                                removeData(adapterPosition)
                                Toast.makeText(context, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                dialogLogin?.dismiss()
                                fragment.initScreen(itemCount)
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

                etviewMemo.setOnEditorActionListener(object : OnEditorActionListener {
                    override fun onEditorAction(
                        textView: TextView,
                        i: Int,
                        keyEvent: KeyEvent?
                    ): Boolean {
                        when (i) {
                            EditorInfo.IME_ACTION_DONE -> {
                                val pos = adapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    Novel.child("book").child(getItem(adapterPosition).bookCode).child("memo").setValue(getMemoEdit())
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

    fun getItem(position: Int): BestItemData {
        return itemsList[position]
    }

    fun getMemoEdit(): String {
        return memo
    }

    fun setMemoEdit(str : String){
        memo = str
    }

}