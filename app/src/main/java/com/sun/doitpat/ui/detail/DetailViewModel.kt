package com.sun.doitpat.ui.detail

import androidx.lifecycle.MutableLiveData
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository

class DetailViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    val item = MutableLiveData<ToDo>()

    val title get() = item.value?.title
    val description get() = item.value?.description
    val time get() = item.value?.time
    val place get() = item.value?.place
    val color get() = item.value?.color

    fun add() {
        TODO()
    }

}
