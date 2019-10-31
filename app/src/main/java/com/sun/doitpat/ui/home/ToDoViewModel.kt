package com.sun.doitpat.ui.home

import androidx.lifecycle.LiveData
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

    private val _list = MutableLiveData<List<ToDo>>()
    private val _widgetList = MutableLiveData<List<ToDo>>()
    val list: LiveData<List<ToDo>> get() = _list
    val widgetList: LiveData<List<ToDo>> get() = _widgetList

    init {
        getNoAlertToDo()
        getWidgetList()
    }

    private fun cancelNotification(tag: String) {
        WorkManager.getInstance().cancelAllWorkByTag(tag)
    }

    private fun getWidgetList() {
        viewModelScope.launch {
            _widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
    }

    fun deleteItem(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepository.deleteToDo(toDo)
            _widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
        cancelNotification(toDo.id.toString())
    }

    fun updateItem(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepository.updateToDo(toDo.copy(status = COMPLETED))
            _widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
        cancelNotification(toDo.id.toString())
    }

    fun undoItem(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepository.updateToDo(toDo.copy(status = NEW))
            _widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
    }

    fun getNoAlertToDo() {
        viewModelScope.launch {
            _list.value = toDoRepository.getNoAlertToDo().asReversed()
            _widgetList.value = toDoRepository.getAllToDo().asReversed()
        }
    }

    fun getAlertToDo() {
        viewModelScope.launch {
            _list.value = toDoRepository.getAlertToDo().asReversed()
        }
    }

    fun getCompletedToDo() {
        viewModelScope.launch {
            _list.value = toDoRepository.getCompletedToDo().asReversed()
        }
    }

}
