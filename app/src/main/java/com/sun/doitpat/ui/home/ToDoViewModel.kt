package com.sun.doitpat.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.util.Constants.COMPLETED
import com.sun.doitpat.util.Constants.NEW
import kotlinx.coroutines.launch

class ToDoViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    val list = MutableLiveData<List<ToDo>>()
    val widgetList = MutableLiveData<List<ToDo>>()

    init {
        getNoAlertToDo()
        getWidgetList()
    }

    private fun cancelNotification(tag: String) {
        WorkManager.getInstance().cancelAllWorkByTag(tag)
    }

    private fun getWidgetList() {
        viewModelScope.launch {
            widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
    }

    fun deleteItem(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepository.deleteToDo(toDo)
            widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
        cancelNotification(toDo.id.toString())
    }

    fun updateItem(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepository.updateToDo(toDo.copy(status = COMPLETED))
            widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
        cancelNotification(toDo.id.toString())
    }

    fun undoItem(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepository.updateToDo(toDo.copy(status = NEW))
            widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
    }

    fun getNoAlertToDo() {
        viewModelScope.launch {
            list.value = toDoRepository.getNoAlertToDo().asReversed()
            widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
    }

    fun getAlertToDo() {
        viewModelScope.launch {
            list.value = toDoRepository.getAlertToDo().asReversed()
        }
    }

    fun getCompletedToDo() {
        viewModelScope.launch {
            list.value = toDoRepository.getCompletedToDo().asReversed()
        }
    }

}
