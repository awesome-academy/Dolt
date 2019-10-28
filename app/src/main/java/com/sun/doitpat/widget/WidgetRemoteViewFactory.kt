package com.sun.doitpat.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.sun.doitpat.R
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.util.Constants.WIDGET_BROADCAST
import com.sun.doitpat.util.Constants.WIDGET_DATA_BROADCAST

class WidgetRemoteViewFactory(
        private val context: Context,
        private val intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private var items: ArrayList<ToDo>? = arrayListOf()

    init {
        items = intent.getBundleExtra(WIDGET_BROADCAST).getParcelableArrayList(WIDGET_DATA_BROADCAST)
    }

    override fun onCreate() {
        updateList()
    }

    override fun onDataSetChanged() {
        updateList()
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.item_to_do_widget)
        remoteView.setTextViewText(
                R.id.textWidgetItemTitle,
                items?.get(position)?.title)
        remoteView.setTextViewText(
                R.id.textWidgetItemInformation,
                items?.get(position)?.time +
                        context.resources.getString(R.string.title_information_separate) +
                        items?.get(position)?.place)
        return remoteView
    }

    override fun onDestroy() {
        items?.clear()
    }

    override fun getCount(): Int {
        items?.let {
            return@getCount it.size
        }
        return 0
    }

    override fun getViewTypeCount() = DEFAULT_VIEW_TYPE

    override fun hasStableIds() = true

    override fun getItemId(position: Int) = position.toLong()

    override fun getLoadingView() = RemoteViews(context.packageName, R.layout.to_do_widget)

    private fun updateList() {
        items = intent.getBundleExtra(WIDGET_BROADCAST).getParcelableArrayList(WIDGET_DATA_BROADCAST)
    }

    companion object {
        private const val DEFAULT_VIEW_TYPE = 1
    }

}
