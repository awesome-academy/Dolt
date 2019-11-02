package com.sun.doitpat.widget

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.sun.doitpat.R
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.util.Constants.ALERT
import com.sun.doitpat.util.Constants.BUNDLE_EXTRA
import com.sun.doitpat.util.Constants.COLOR_BLUE
import com.sun.doitpat.util.Constants.COLOR_GREEN
import com.sun.doitpat.util.Constants.COLOR_ORANGE
import com.sun.doitpat.util.Constants.COLOR_PURPLE
import com.sun.doitpat.util.Constants.COLOR_RED
import com.sun.doitpat.util.Constants.COLOR_YELLOW
import com.sun.doitpat.util.Constants.DEFAULT_COLOR
import com.sun.doitpat.util.Constants.EMPTY_SPACE
import com.sun.doitpat.util.Constants.EXTRA
import com.sun.doitpat.util.Constants.ITEMS_EXTRA
import com.sun.doitpat.util.Constants.NO_ALERT
import com.sun.doitpat.util.isLaterThanNow

class WidgetRemoteViewFactory(
    private val context: Context,
    private val intent: Intent?) : RemoteViewsService.RemoteViewsFactory {

    private var items: ArrayList<ToDo>? = arrayListOf()

    override fun onCreate() {
        updateList()
    }

    override fun onDataSetChanged() {
        updateList()
    }

    override fun getViewAt(position: Int): RemoteViews {
        val fillIntent = Intent().apply { putExtra(EXTRA, items?.get(position)?.id) }
        val remoteView = RemoteViews(context.packageName, R.layout.item_to_do_widget)

        when (items?.get(position)?.alertStatus) {
            NO_ALERT -> remoteView.setViewVisibility(R.id.textAlertStatus, View.GONE)
            ALERT -> remoteView.setViewVisibility(R.id.textAlertStatus, View.VISIBLE)
            else -> remoteView.setViewVisibility(R.id.textAlertStatus, View.GONE)
        }
        items?.get(position)?.createdTimeMillisecond?.let {
            if (!it.isLaterThanNow())
                remoteView.setViewVisibility(R.id.textAlertStatus, View.GONE)
        }

        remoteView.apply {
            setViewVisibility(R.id.textColorRed, View.GONE)
            setViewVisibility(R.id.textColorOrange, View.GONE)
            setViewVisibility(R.id.textColorGreen, View.GONE)
            setViewVisibility(R.id.textColorBlue, View.GONE)
            setViewVisibility(R.id.textColorPurple, View.GONE)
            setViewVisibility(R.id.textColorYellow, View.GONE)
            setViewVisibility(R.id.textColorDefault, View.GONE)
            setTextViewText(
                R.id.textWidgetItemTitle,
                items?.get(position)?.title)
            setTextViewText(
                R.id.textWidgetItemInformation,
                items?.get(position)?.time + EMPTY_SPACE + items?.get(position)?.place)
            setOnClickFillInIntent(R.id.layoutWidgetItem, fillIntent)
        }

        items?.get(position)?.let {
            val colorId = when (it.color) {
                COLOR_RED -> R.id.textColorRed
                COLOR_ORANGE -> R.id.textColorOrange
                COLOR_GREEN -> R.id.textColorGreen
                COLOR_BLUE -> R.id.textColorBlue
                COLOR_PURPLE -> R.id.textColorPurple
                COLOR_YELLOW -> R.id.textColorYellow
                DEFAULT_COLOR -> R.id.textColorDefault
                else -> R.id.textColorDefault
            }
            remoteView.setViewVisibility(colorId, View.VISIBLE)
        }
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
        items = intent?.getBundleExtra(BUNDLE_EXTRA)?.getParcelableArrayList(ITEMS_EXTRA)
    }

    companion object {
        private const val DEFAULT_VIEW_TYPE = 1
    }

}
