package com.sun.doitpat.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sun.doitpat.MainActivity
import com.sun.doitpat.R
import com.sun.doitpat.util.Constants.CHANNEL_ID
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.ID
import com.sun.doitpat.util.Constants.PLACE
import com.sun.doitpat.util.Constants.TITLE

class NotificationWorker(
        private val context: Context,
        params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val priorityStatus = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(CHANNEL_ID, NOTIFICATION_NAME, priorityStatus)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(inputData.getString(TITLE))
                .setContentText(inputData.getString(PLACE))
                .setContentIntent(getIntent(inputData.getInt(ID, DEFAULT_ID)))
                .setChannelId(CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setLights(Color.WHITE, DEFAULT_LIGHT_TIME, DEFAULT_LIGHT_TIME)
                .build()
        notificationManager.notify(inputData.getInt(ID, 0), notification)
        return Result.success()
    }

    private fun getIntent(itemId: Int): PendingIntent? {
        val intent = MainActivity.getIntent(context, itemId)
        val taskStackBuilder = TaskStackBuilder.create(context).apply {
            addParentStack(MainActivity::class.java)
            addNextIntent(intent)
        }
        return taskStackBuilder.getPendingIntent(itemId, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        private const val NOTIFICATION_NAME = "Notification Name"
        private const val DEFAULT_LIGHT_TIME = 1000
    }
}
