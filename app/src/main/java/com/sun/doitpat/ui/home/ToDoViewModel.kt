package com.sun.doitpat.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    private var toDos: MutableLiveData<List<ToDo>> = MutableLiveData()

    init {
        viewModelScope.launch {
            toDos.value = toDoRepository.getAllToDo()
        }
    }

    fun getList() = toDos
}
