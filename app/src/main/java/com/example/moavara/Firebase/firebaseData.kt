package com.example.moavara.Firebase

class DataFCMBody(
    var to: String? = "/topics/all",
    var priority: String? = "high",
    var data: DataFCMBodyData? = null,
    var notification: DataFCMBodyNotification? = null,
)

class DataFCMBodyData(
    var title: String = "data title",
    var message: String = "data message"
)

class DataFCMBodyNotification(
    var title: String = "notification 타이틀",
    var body: String = "notification message",
    var sound: String = "default",
    var icon: String = "ic_stat_ic_notification",
    var type: String = "best",
    var click_action : String = "NOTIFICATION_CLICK",
)