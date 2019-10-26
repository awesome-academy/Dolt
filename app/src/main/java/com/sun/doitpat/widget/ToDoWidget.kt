package com.sun.doitpat.widget

import com.sun.doitpat.R
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class ToDoWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        TODO()
    }

    override fun onDisabled(context: Context) {
        TODO()
    }

    companion object {

        internal fun updateAppWidget(
                context: Context,
                appWidgetManager: AppWidgetManager,
                appWidgetId: Int) {

            val views = RemoteViews(context.packageName, R.layout.to_do_widget)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
