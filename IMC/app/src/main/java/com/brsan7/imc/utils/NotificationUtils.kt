package com.brsan7.imc.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.brsan7.imc.HistoricoActivity
import com.brsan7.imc.R

lateinit var notificationChannel: NotificationChannel
lateinit var notificationManager: NotificationManager
lateinit var builder: NotificationCompat.Builder

fun Context.showNotification(chanelId: String, title: String, body: String){
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(this,HistoricoActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        notificationChannel = NotificationChannel(chanelId,body,NotificationManager.IMPORTANCE_HIGH).apply {
            lightColor = Color.BLUE
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(notificationChannel)
        builder = NotificationCompat.Builder(this,chanelId).apply {
            setSmallIcon(R.drawable.logo_imc)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            setContentIntent(pendingIntent)
        }
        notificationManager.notify(chanelId.toInt(), builder.build())
    }
    else{
        builder = NotificationCompat.Builder(this,chanelId).apply {
            setSmallIcon(R.drawable.logo_imc)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            setContentIntent(pendingIntent)
            setPriority(NotificationCompat.PRIORITY_HIGH)

        }
        notificationManager.notify(chanelId.toInt(), builder.build())
    }
}