package com.sun.doitpat.widget

import com.sun.doitpat.R
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS
import android.appwidget.AppWidgetManager.getInstance
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.util.Constants.WIDGET_BROADCAST
import com.sun.doitpat.util.Constants.WIDGET_DATA_BROADCAST

class ToDoWidgetProvider : AppWidgetProvider() {

    private var items: ArrayList<ToDo>? = arrayListOf()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(WIDGET_DATA_BROADCAST, items)
        updateWidget(context, appWidgetIds, bundle)

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var appWidgetIds: IntArray = intArrayOf()
        items = intent?.getParcelableArrayListExtra(WIDGET_BROADCAST)
        intent?.getIntArrayExtra(EXTRA_APPWIDGET_IDS)?.let {
            appWidgetIds = it
        }
        val bundle = Bundle()
        bundle.putParcelableArrayList(WIDGET_DATA_BROADCAST, items)
        context?.let {
            updateWidget(it, appWidgetIds, bundle)
        }
    }

    private fun updateWidget(context: Context, appWidgetIds: IntArray, bundle: Bundle) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, WidgetService::class.java)
            intent.putExtra(WIDGET_BROADCAST, bundle)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val widRemoteView = RemoteViews(context.packageName, R.layout.to_do_widget)
            widRemoteView.setRemoteAdapter(R.id.listWidgetItems, intent)
            getInstance(context).updateAppWidget(appWidgetId, widRemoteView)
        }
    }

}
