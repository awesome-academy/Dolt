package com.sun.doitpat.widget

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import com.sun.doitpat.R
import android.appwidget.AppWidgetManager.getInstance
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import com.sun.doitpat.MainActivity
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.util.Constants
import com.sun.doitpat.util.Constants.BUNDLE_EXTRA
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.WIDGET_UPDATE_FROM_APP_ACTION

class ToDoWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        context?.let { _context ->
            appWidgetIds?.let { _appWidgetIds -> updateWidget(_context, _appWidgetIds) }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == WIDGET_UPDATE_FROM_APP_ACTION) {
            WidgetListDataBackup.storeWidgetData(intent.getBundleExtra(BUNDLE_EXTRA))
        }
        context?.let { _context ->
            val appWidgetIds = getInstance(_context).getAppWidgetIds(
                ComponentName(_context, this::class.java))
            updateWidget(_context, appWidgetIds)
        }
    }

    private fun updateWidget(context: Context, appWidgetIds: IntArray) {

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
            setRemoteAdapter(R.id.listWidgetItems, WidgetService.getIntent(context))
            setEmptyView(R.id.listWidgetItems, R.id.textWidgetEmptyList)
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

    companion object {
        fun getIntent(context: Context, items: ArrayList<ToDo>) =
            Intent(context, ToDoWidgetProvider::class.java).apply {
                action = WIDGET_UPDATE_FROM_APP_ACTION
                putExtra(BUNDLE_EXTRA, Bundle().apply {
                    putParcelableArrayList(Constants.ITEMS_EXTRA, items)
                })
            }

    }

}
