package com.example.moavara.Firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.example.moavara.Main.ActivitySplash
import com.example.moavara.R
import com.example.moavara.Util.ActivityTest
import com.example.moavara.Util.BootReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCM : FirebaseMessagingService() {

    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null
    var it = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()) {
            showNotification(remoteMessage.data["title"], remoteMessage.data["message"])
        }

        if (remoteMessage.notification != null) {
            showNotification(
                remoteMessage.notification!!.title, remoteMessage.notification!!
                    .body
            )
        }
    }

    private fun showNotification(title: String?, message: String?) {

        // NotificationManager 객체 생성
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // API Level 26 버전 이상부터는 NotificationChannel을 사용하여 NotificationCompat.Builder를 생성하기에 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "모아바라"
            notificationBuilder = NotificationCompat.Builder(this, channelId)
        } else {
            // Api Level 26 버전 미만
            notificationBuilder = NotificationCompat.Builder(this)
        }

        // Builder의 setter를 사용하여 Notification 설정
        notificationBuilder?.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder?.setContentTitle(title)
        notificationBuilder?.setContentText(message)
        notificationBuilder?.setAutoCancel(true)
        notificationBuilder?.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.moabara_logo))

        // Activity Intent
        val mainIntent = Intent(this, ActivitySplash::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // 시스템에게 의뢰할 Intent를 담는 클래스
        val activityPendingIntent = PendingIntent.getActivity(this, 10, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        // 사용자가 알림을 클릭 시 이동할 PendingIntent
        notificationBuilder?.setContentIntent(activityPendingIntent)

        // BroadCastReceiver Intent
        val broadIntent = Intent(this, BootReceiver::class.java)
        val broadcastPendingIntent = PendingIntent.getBroadcast(this, 0, broadIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // ActionButton을 추가하여 클릭 시 PendingIntent 설정
        notificationBuilder?.addAction(NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "Action 문자열", broadcastPendingIntent).build())

        // BigPictureStyle Notification
        if(it == "pictureButton") {
            // Bitmap 생성
            val bigPicture = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val bigStyle = NotificationCompat.BigPictureStyle(notificationBuilder)
            // BigPictureStyle의 이미지 설정
            bigStyle.bigPicture(bigPicture)
            // Builder에 Style 설정
            notificationBuilder?.setStyle(bigStyle)
        } else if(it == "textButton") { // BigTextStyle Notification
            val bigTextStyle = NotificationCompat.BigTextStyle(notificationBuilder)
            bigTextStyle.setSummaryText("BigText 요약")
            bigTextStyle.bigText("HELLO")
            notificationBuilder?.setStyle(bigTextStyle)
        } else if(it == "inboxButton") { // InboxStyle Notification
            val inboxStyle = NotificationCompat.InboxStyle(notificationBuilder)
            inboxStyle.addLine("Activity")
            inboxStyle.addLine("BroadcastReceiver")
            inboxStyle.addLine("Service")
            inboxStyle.addLine("ContentProvider")
            inboxStyle.setSummaryText("Android Component")
            notificationBuilder?.setStyle(inboxStyle)
        } else if(it == "progressButton") { // Progress Notification
            val runnable = Runnable {
                for(i in 1..10) {
                    // 터치해도 꺼지지않게 설정
                    notificationBuilder?.setAutoCancel(false)
                    // 알림을 밀어서 삭제하지 못하게
                    notificationBuilder?.setOngoing(true)
                    notificationBuilder?.setProgress(10, i, false)
                    notificationManager?.notify(100, notificationBuilder?.build())
                    SystemClock.sleep(1000)
                }

                notificationBuilder?.setContentText("다운로드 완료")?.setProgress(0, 0, false)
                notificationManager?.notify(100, notificationBuilder?.build())
            }

            val thread = Thread(runnable)
            thread.start()
        } else if(it == "headsUpButton") { // HeadsUp Notification
            notificationBuilder?.setFullScreenIntent(activityPendingIntent, true)
        } else if(it == "messageButton") { // Message Style Notification
            val sender1 = Person.Builder()
                .setName("Lee")
                .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher))
                .build()

            val sender2 = Person.Builder()
                .setName("Kim")
                .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher))
                .build()

            // Create Image Message
            val message1 = NotificationCompat.MessagingStyle
                .Message("Hello", System.currentTimeMillis(), sender2)

            val message2 = NotificationCompat.MessagingStyle
                .Message("hi~~", System.currentTimeMillis(), sender1)

            val messagingStyle = NotificationCompat.MessagingStyle(sender1)
                .addMessage(message2)
                .addMessage(message1)

            notificationBuilder?.setStyle(messagingStyle)
        }

        // NotificationManger.nofity를 이용하여 Notification post
        notificationManager?.notify(100, notificationBuilder?.build())
    }
}