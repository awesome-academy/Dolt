package com.sun.doitpat.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    val list = MutableLiveData<List<ToDo>>()

    init {
        viewModelScope.launch {
            list.value = toDoRepository.getAllToDo()
        }
    }

}
