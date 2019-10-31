package com.sun.doitpat.widget

import android.os.Bundle

object WidgetListDataBackup {

    private var storedBundle = Bundle()

    fun storeWidgetData(bundle: Bundle) {
        storedBundle = bundle
    }

    fun restoreWidgetData() = storedBundle

}
