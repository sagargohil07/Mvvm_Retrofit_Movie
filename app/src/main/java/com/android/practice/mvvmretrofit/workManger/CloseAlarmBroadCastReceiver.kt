package com.android.practice.mvvmretrofit.workManger

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log

class CloseAlarmBroadCastReceiver() : BroadcastReceiver()  {

    private lateinit var _context: Context
    private lateinit var _intent: Intent
    private var notificationID: Int = 2

    override fun onReceive(context: Context?, intent: Intent?) {

        _context = context!!
        _intent = intent!!

        val notificationManager = _context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationID)

        AlarmBroadCastReceiver.RingtoneInstance.ringtone.stop()

    }


}

