package com.example.moavara.Util

import android.content.Context
import java.util.HashMap

object Param {

    fun getItemAPI(mContext: Context?): MutableMap<String?, Any> {

        val Param: MutableMap<String?, Any> = HashMap()

        mContext ?: return Param

        Param["api_key"] = "mw_8ba234e7801ba288554ca07ae44c7"
        Param["ver"] = "2.6.3"
        Param["device"] = "mw"
        Param["deviceuid"] = "5127d5951c983034a16980c8a893ac99d16dbef988ee36882b793aa14ad33604"
        Param["devicetoken"] = "mw"

        return Param
    }
}