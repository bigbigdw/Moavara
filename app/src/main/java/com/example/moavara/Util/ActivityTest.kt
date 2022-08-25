package com.example.moavara.Util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.example.moavara.Main.ActivityMain
import com.example.moavara.R

class ActivityTest : AppCompatActivity() {

    lateinit var normalButton: Button
    lateinit var pictureButton: Button
    lateinit var textButton: Button
    lateinit var inboxButton: Button
    lateinit var progressButton: Button
    lateinit var headsUpButton: Button
    lateinit var messageButton: Button

    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // 버튼 초기화
        normalButton = findViewById(R.id.normal_noti_button)
        pictureButton = findViewById(R.id.picture_noti_button)
        textButton = findViewById(R.id.text_noti_button)
        inboxButton = findViewById(R.id.inbox_noti_button)
        progressButton = findViewById(R.id.progress_noti_button)
        headsUpButton = findViewById(R.id.headsup_noti_button)
        messageButton = findViewById(R.id.message_noti_button)

        // 버튼에 리스너 설정
        normalButton.setOnClickListener(buttonClickListener)
        pictureButton.setOnClickListener(buttonClickListener)
        textButton.setOnClickListener(buttonClickListener)
        inboxButton.setOnClickListener(buttonClickListener)
        progressButton.setOnClickListener(buttonClickListener)
        headsUpButton.setOnClickListener(buttonClickListener)
        messageButton.setOnClickListener(buttonClickListener)
    }

    // 버튼 클릭 리스너
    val buttonClickListener = View.OnClickListener {
        // NotificationManager 객체 생성
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // API Level 26 버전 이상부터는 NotificationChannel을 사용하여 NotificationCompat.Builder를 생성하기에 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "one-channel"
            val channelName = "Channel One"
            val channelDescription = "Channel One Description"

            var notificationChannel: NotificationChannel? = null
            // HeadsUp은 Importance를 High로 설정해야하기에 분기
            // NotificationChannel 객체 생성
            if(it == headsUpButton) {
                notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            } else {
                notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            }

            notificationChannel.description = channelDescription
            // NotificationChannel이 등록된 Builder
            // 이 Builder에 의해 만들어진 Notification은 이곳에 등록된 Channel에 의해 관리
            notificationManager?.createNotificationChannel(notificationChannel)
            notificationBuilder = NotificationCompat.Builder(this, channelId)
        } else {
            // Api Level 26 버전 미만
            notificationBuilder = NotificationCompat.Builder(this)
        }

        // Builder의 setter를 사용하여 Notification 설정
        notificationBuilder?.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder?.setContentTitle("Content Title")
        notificationBuilder?.setContentText("Content Message")
        notificationBuilder?.setAutoCancel(true)

//        notificationBuilder?.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.booktest))

        // Activity Intent
        val mainIntent = Intent(this, ActivityMain::class.java).apply {
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
        notificationBuilder?.addAction(NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "모아바라 열기", broadcastPendingIntent).build())

        // BigPictureStyle Notification
        if(it == pictureButton) {
            // Bitmap 생성
            val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.booktest)
            val bigStyle = NotificationCompat.BigPictureStyle(notificationBuilder)
            // BigPictureStyle의 이미지 설정
            bigStyle.bigPicture(bigPicture)
            // Builder에 Style 설정
            notificationBuilder?.setStyle(bigStyle)
        } else if(it == textButton) { // BigTextStyle Notification
            val bigTextStyle = NotificationCompat.BigTextStyle(notificationBuilder)
            bigTextStyle.setSummaryText("BigText 요약")
            bigTextStyle.bigText("resources.getString(R.string.big_text)")
            notificationBuilder?.setStyle(bigTextStyle)
        } else if(it == inboxButton) { // InboxStyle Notification
            val inboxStyle = NotificationCompat.InboxStyle(notificationBuilder)
            inboxStyle.addLine("Activity")
            inboxStyle.addLine("BroadcastReceiver")
            inboxStyle.addLine("Service")
            inboxStyle.addLine("ContentProvider")
            inboxStyle.setSummaryText("Android Component")
            notificationBuilder?.setStyle(inboxStyle)
        } else if(it == progressButton) { // Progress Notification
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
        } else if(it == headsUpButton) { // HeadsUp Notification
            notificationBuilder?.setFullScreenIntent(activityPendingIntent, true)
        } else if(it == messageButton) { // Message Style Notification
            val sender1 = Person.Builder()
                .setName("Lee")
                .setIcon(IconCompat.createWithResource(this, R.drawable.booktest))
                .build()

            val sender2 = Person.Builder()
                .setName("Kim")
                .setIcon(IconCompat.createWithResource(this, R.drawable.booktest))
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