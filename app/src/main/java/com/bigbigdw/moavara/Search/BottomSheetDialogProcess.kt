package com.bigbigdw.moavara.Search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bigbigdw.moavara.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogProcess( private val mContext: Context) :
    BottomSheetDialogFragment() {

    private var tviewTitle: TextView? = null
    private var tviewBody: TextView? = null
    private var markingBtn: LinearLayout? = null
    private var urlWrap1: LinearLayout? = null
    private var urlWrap2: LinearLayout? = null
    private var tviewUrl1: TextView? = null
    private var tviewUrl2: TextView? = null

    var focused : String? = null
    private var tviewBtnText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.bottomdialog_process, container, false)

        focused = mContext.getSharedPreferences("PROCESS", AppCompatActivity.MODE_PRIVATE).getString("FOCUS", "")

        tviewTitle = v.findViewById(R.id.tview_Title)
        tviewBody = v.findViewById(R.id.tview_Body)
        markingBtn = v.findViewById(R.id.llayout_btn)
        urlWrap1 = v.findViewById(R.id.llayout_url1)
        urlWrap2 = v.findViewById(R.id.llayout_url2)
        tviewUrl1 = v.findViewById(R.id.tview_url1)
        tviewUrl2 = v.findViewById(R.id.tview_url2)
        tviewBtnText = v.findViewById(R.id.tview_btnText)

        return v
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}