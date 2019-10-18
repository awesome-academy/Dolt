package com.sun.doitpat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    private lateinit var toDos: LiveData<List<ToDo>>

    init {
        viewModelScope.launch {
            toDos = toDoRepository.getAllToDo()
        }
    }

    fun getList() = toDos
}
