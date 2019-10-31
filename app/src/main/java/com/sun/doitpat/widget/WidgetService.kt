package com.sun.doitpat.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViewsService
import com.sun.doitpat.util.Constants

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
            WidgetRemoteViewFactory(applicationContext, intent!!)

    companion object {
        fun getIntent(context: Context) = Intent(context, WidgetService::class.java).apply {
            putExtra(Constants.BUNDLE_EXTRA, WidgetListDataBackup.restoreWidgetData())
            data = Uri.parse(System.currentTimeMillis().toString())
        }
    }
}
