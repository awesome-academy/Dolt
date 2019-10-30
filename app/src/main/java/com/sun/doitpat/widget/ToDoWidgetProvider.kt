package com.sun.doitpat.widget

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import com.sun.doitpat.R
import android.appwidget.AppWidgetManager.getInstance
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import com.sun.doitpat.MainActivity
import com.sun.doitpat.util.Constants.BUNDLE_EXTRA
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.ID

class ToDoWidgetProvider : AppWidgetProvider() {

    private var bundle: Bundle? = Bundle()

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        context?.let { _context ->
            appWidgetIds?.let { _appWidgetIds -> updateWidget(_context, _appWidgetIds) }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        bundle = intent?.getBundleExtra(BUNDLE_EXTRA)
        context?.let { _context ->
            val appWidgetIds = intent?.getIntArrayExtra(ID)
            appWidgetIds?.let { updateWidget(_context, it) }
        }
    }

    private fun updateWidget(context: Context, appWidgetIds: IntArray) {
        val intent = Intent(context, WidgetService::class.java)
        intent.putExtra(BUNDLE_EXTRA, bundle)
        intent.data = Uri.parse(System.currentTimeMillis().toString())

        val widgetRemoteView = RemoteViews(context.packageName, R.layout.to_do_widget)
        val openAppClickIntent = Intent(context, MainActivity::class.java)
        val itemClickIntent = Intent(context, MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(itemClickIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val openAppPendingIntent = PendingIntent.getActivity(context, 0, openAppClickIntent, 0)
        widgetRemoteView.apply {
            setOnClickPendingIntent(R.id.buttonWidgetAdd, getIntent(context, DEFAULT_ID))
            setOnClickPendingIntent(R.id.textWidgetTitle, openAppPendingIntent)
            setRemoteAdapter(R.id.listWidgetItems, intent)
            setPendingIntentTemplate(R.id.listWidgetItems, pendingIntent)
        }

        getInstance(context).apply {
            notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listWidgetItems)
            updateAppWidget(appWidgetIds, widgetRemoteView)
        }
    }

    private fun getIntent(context: Context, itemId: Int): PendingIntent? {
        val intent = MainActivity.getIntent(context, itemId)
        val taskStackBuilder = TaskStackBuilder.create(context).apply {
            addParentStack(MainActivity::class.java)
            addNextIntent(intent)
        }
        return taskStackBuilder.getPendingIntent(itemId, PendingIntent.FLAG_UPDATE_CURRENT)
    }

}
