package com.sun.doitpat.ui

import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.base.BaseListAdapter

class ToDoAdapter : BaseListAdapter<ToDo>(ToDoDiffCallBack())
