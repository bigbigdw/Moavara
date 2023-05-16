package com.bigbigdw.moavara.Util

import android.content.Context
import java.util.HashMap

object Param {

    fun getItemAPI(mContext: Context?): MutableMap<String?, Any> {

        val Param: MutableMap<String?, Any> = HashMap()

        mContext ?: return Param

        Param["api_key"] = "android_iK3303O982ab8e2391dp9498"
        Param["ver"] = "2.8.5"
        Param["device"] = "android"
        Param["deviceuid"] = "fp9v-165SxOz0bpxl7JxZW%3AAPA91bH7o-VcJ6LKjIDZ44LGUJiymDpM-ks7CJ3nnnQBr1zDTKsc4vaa1OPRIZ6jPEVrT5q4hJ6Q5mdhxB4GMIB9XQL5Lbd4JWlBauQQ8REZpc0y6fbjpE7V1mq1xTP4CUMTtQqvtABJ"
        Param["devicetoken"] = "NULL"

        return Param
    }
}