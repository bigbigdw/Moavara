package com.example.moavara.Firebase

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//WorkManager
class FWorkManagerResult {
    @SerializedName("message_id")
    @Expose
    val message_id: String = ""
}

class FCMBody {
    @SerializedName("to")
    @Expose
    val message_id: String = "/topics/all"

    @SerializedName("priority")
    @Expose
    val priority: String = "high"

    val data : FCMBodyData? = null

    val notification : FCMBodyNotification? = null
}

class FCMBodyData {
    @SerializedName("title")
    @Expose
    val title: String = "data title"

    @SerializedName("message")
    @Expose
    val message: String = "data message"
}

class FCMBodyNotification {
    @SerializedName("title")
    @Expose
    val title: String = "notification 타이틀"

    @SerializedName("body")
    @Expose
    val body: String = "notification message"

    @SerializedName("sound")
    @Expose
    val sound: String = "default"

    @SerializedName("icon")
    @Expose
    val icon: String = "ic_stat_ic_notification"
}
