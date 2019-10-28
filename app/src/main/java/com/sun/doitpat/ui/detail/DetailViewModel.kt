package com.sun.doitpat.ui.detail

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.EMPTY_STRING
import com.sun.doitpat.util.TimeUtils
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    private val toDo = ToDo(title = EMPTY_STRING)
    private val item = MutableLiveData(toDo)

    var title = MutableLiveData<String>(EMPTY_STRING)
    var description = MutableLiveData<String>(EMPTY_STRING)
    var time = MutableLiveData<String>(EMPTY_STRING)
    var place = MutableLiveData<String>(EMPTY_STRING)
    var color = MutableLiveData<Int>(Color.WHITE)

    fun addColor(color: Int) {
        this.color.value = when (color) {
            1 -> COLOR_RED
            2 -> COLOR_ORANGE
            3 -> COLOR_YELLOW
            4 -> COLOR_GREEN
            5 -> COLOR_BLUE
            6 -> COLOR_PURPLE
            7 -> NO_COLOR
            else -> NO_COLOR
        }
    }

    fun addTime(calendar: Calendar) {
        this.time.value = TimeUtils.timeToString(calendar)
    }

    fun addToDo() = viewModelScope.launch {
        val toDo = item.value?.copy(
                title = title.value.toString(),
                description = description.value.toString(),
                time = time.value.toString(),
                place = place.value.toString(),
                color = color.value.toString().toInt())
        toDo?.let { toDoRepository.insertToDo(it) }
    }

    fun getToDo(id: Int) {
        if (id != DEFAULT_ID) {
            viewModelScope.launch {
                item.value = toDoRepository.getToDoById(id)
                setData()
            }
        }
    }

    private fun setData() {
        item.value?.let {
            title.value = it.title
            description.value = it.description
            time.value = it.time
            place.value = it.place
            color.value = it.color
        }
    }

    companion object {
        private const val COLOR_RED = -1876663
        private const val COLOR_ORANGE = -223690
        private const val COLOR_YELLOW = -16121
        private const val COLOR_GREEN = -6894049
        private const val COLOR_BLUE = -12803585
        private const val COLOR_PURPLE = -10791470
        private const val NO_COLOR = 0
    }
}
