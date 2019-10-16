package com.sun.doitpat.ui

import androidx.recyclerview.widget.DiffUtil
import com.sun.doitpat.data.model.ToDo

class ToDoDiffCallBack : DiffUtil.ItemCallback<ToDo>() {

    override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo) = oldItem == newItem

}
