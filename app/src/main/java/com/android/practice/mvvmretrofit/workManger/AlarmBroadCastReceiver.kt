package com.android.practice.mvvmretrofit.workManger

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.practice.mvvmretrofit.R

class AlarmBroadCastReceiver : BroadcastReceiver()  {

    private lateinit var alarmUri: Uri

    private lateinit var _context: Context
    private lateinit var _intent: Intent
    private var alarmChannelId: Int = 0
    private var notificationID: Int = 2
    private lateinit var closeAlarmBroadCastReceiver: CloseAlarmBroadCastReceiver

    object RingtoneInstance{
        lateinit var ringtone: Ringtone
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("alarm", "onReceive: Alarm Bell , Alarm just fired")
        val id = intent?.getStringExtra("id")

        _context = context!!
        _intent = intent!!

        //play Ringtone alarm when alarmBroadcast Received
        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if(alarmUri==null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        RingtoneInstance.ringtone = RingtoneManager.getRingtone(context, alarmUri)
        RingtoneInstance.ringtone.play()

        alarmChannelId = id!!.toInt()
        showNotification(alarmChannelId.toString())

        /*val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        backgroundExecutor.schedule(Runnable {
            ringtone.stop()
        }, 5, TimeUnit.SECONDS)*/

    }


    private fun showNotification(alarmId: String?) {
        val channelName = "alarm"
        val intent = Intent(_context, CloseAlarmBroadCastReceiver::class.java)

        intent.putExtra("alarmId", alarmId?.toInt())
        val pendingIntent =if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(_context, 0, intent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        }else{
            PendingIntent.getBroadcast(_context, 0, intent, FLAG_UPDATE_CURRENT)
        }

        val mChannel = NotificationChannelCompat.Builder(
            notificationID.toString(), NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setName(channelName)
            setDescription(" ")
            setLightsEnabled(true)
            setLightColor(Color.RED)
            setVibrationEnabled(true)
            setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
        }.build()

        val notificationBuilder = NotificationCompat.Builder(_context, notificationID.toString())
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Alarm")
            .setContentText("It's time to wake up")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .addAction(R.drawable.ic_stop, "Stop", pendingIntent)
            .setOngoing(true)
            .setChannelId(notificationID.toString()).build()

        NotificationManagerCompat.from(_context).createNotificationChannel(mChannel)

        NotificationManagerCompat.from(_context).notify(notificationID, notificationBuilder)

    }


}