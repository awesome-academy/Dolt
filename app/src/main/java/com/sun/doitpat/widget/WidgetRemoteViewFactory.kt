package com.sun.doitpat.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.sun.doitpat.R
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.util.Constants.BUNDLE_EXTRA
import com.sun.doitpat.util.Constants.EMPTY_SPACE
import com.sun.doitpat.util.Constants.EXTRA
import com.sun.doitpat.util.Constants.ITEMS_EXTRA

class WidgetRemoteViewFactory(
        private val context: Context,
        private val intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private var items: ArrayList<ToDo>? = arrayListOf()

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
                items?.get(position)?.time + EMPTY_SPACE + items?.get(position)?.place)
        val fillIntent = Intent().apply { putExtra(EXTRA, items?.get(position)?.id) }
        remoteView.setOnClickFillInIntent(R.id.layoutWidgetItem, fillIntent)
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
        items = intent.getBundleExtra(BUNDLE_EXTRA).getParcelableArrayList(ITEMS_EXTRA)
    }

    companion object {
        private const val DEFAULT_VIEW_TYPE = 1
    }

}
