package com.sun.doitpat.ui.home

import com.sun.doitpat.R
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.base.BaseListAdapter

class ToDoAdapter : BaseListAdapter<ToDo>(ToDoDiffCallBack()) {

    override fun getItemViewType(position: Int) = R.layout.item_reminder
}
