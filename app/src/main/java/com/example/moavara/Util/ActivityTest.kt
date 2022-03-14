package com.example.moavara.Util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R
import java.util.*

class ActivityTest : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private var onetimeAlarmToggle : ToggleButton? = null
    private var periodicAlarmToggle : ToggleButton? = null
    private var exactPeriodicAlarmToggle : ToggleButton? = null
    private var realtimePeriodicAlarmToggle : ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        onetimeAlarmToggle = findViewById(R.id.onetimeAlarmToggle)
        periodicAlarmToggle = findViewById(R.id.periodicAlarmToggle)
        exactPeriodicAlarmToggle = findViewById(R.id.exactPeriodicAlarmToggle)
        realtimePeriodicAlarmToggle = findViewById(R.id.realtimePeriodicAlarmToggle)

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        onetimeAlarmToggle!!.setOnCheckedChangeListener { _, isChecked ->
            val toastMessage = if (isChecked) {
                val triggerTime = (SystemClock.elapsedRealtime()
                        + 60 * 1000)
                alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
                "Onetime Alarm On"
            } else {
                alarmManager.cancel(pendingIntent)
                "Onetime Alarm Off"
            }
            Log.d(TAG, toastMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        periodicAlarmToggle!!.setOnCheckedChangeListener { _, isChecked ->
            val toastMessage: String = if (isChecked) {
                val repeatInterval: Long = 6000L
                val triggerTime = (SystemClock.elapsedRealtime()
                        + repeatInterval)
                alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime, repeatInterval,
                    pendingIntent
                )
                "Periodic Alarm On"
            } else {
                alarmManager.cancel(pendingIntent)
                "Periodic Alarm Off"
            }
            Log.d(TAG, toastMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        exactPeriodicAlarmToggle!!.setOnCheckedChangeListener { _, isChecked ->
            val toastMessage: String = if (isChecked) {
                val repeatInterval: Long = 60 * 1000
                val triggerTime = (SystemClock.elapsedRealtime()
                        + repeatInterval)
                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime, repeatInterval,
                    pendingIntent
                )
                "Exact periodic Alarm On"
            } else {
                alarmManager.cancel(pendingIntent)
                "Exact periodic Alarm Off"
            }
            Log.d(TAG, toastMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        realtimePeriodicAlarmToggle!!.setOnCheckedChangeListener { _, isChecked ->
            val toastMessage: String = if (isChecked) {
                val repeatInterval: Long = 60 * 1000 // 15min
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 20)
                    set(Calendar.MINUTE, 25)
                }

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    repeatInterval,
                    pendingIntent
                )
                "Realtime periodic Alarm On"
            } else {
                alarmManager.cancel(pendingIntent)
                "Realtime periodic Alarm Off"
            }
            Log.d(TAG, toastMessage)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

    }

}