package com.example.moavara.Firebase

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat
import com.example.moavara.R
import com.example.moavara.Util.ActivityTest
import com.example.moavara.Util.Mining
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCM : FirebaseMessagingService() {

    // 토큰 생성
    override fun onNewToken(token: String) {
        Log.d("!!!!!", "Refreshed token: $token")

        // 토큰 값 따로 저장
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token",token).apply()
        editor.commit()

        Log.i("!!!!!", "토큰 저장 성공적")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Thread {
            Mining.runMining(applicationContext, "ALL")
            Mining.runMining(applicationContext, "ROMANCE")
            Mining.runMining(applicationContext, "BL")
            Mining.runMining(applicationContext, "FANTASY")
            Log.d("@@@@", "알람 옴!")
        }.start()

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

    private fun getCustomDesign(title: String?, message: String?): RemoteViews {
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.notification)
        remoteViews.setTextViewText(R.id.noti_title, title)
        remoteViews.setTextViewText(R.id.noti_message, message)
        remoteViews.setImageViewResource(R.id.noti_icon, R.mipmap.ic_launcher)
        return remoteViews
    }

    private fun showNotification(title: String?, message: String?) {
        val intent = Intent(this, ActivityTest::class.java)
        val channel_id = "channel"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, channel_id
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(uri)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder =
            builder.setContent(getCustomDesign(title, message))
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setSound(uri, null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }
}