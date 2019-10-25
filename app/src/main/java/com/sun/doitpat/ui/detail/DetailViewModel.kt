package com.sun.doitpat.ui.detail

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    private val item = MutableLiveData<ToDo>()

    var title = item.value?.title
    var description = item.value?.description
    var time = item.value?.time
    var place = item.value?.place
    var color = item.value?.color
    var inputColor = Color.WHITE

    fun addColor(color: Int) {
        inputColor = when (color) {
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

    fun addTime(time: String) {
        this.time = time
    }

    fun add() = viewModelScope.launch {
        val toDo = item.value?.copy(
                title = title.toString(),
                description = description.toString(),
                time = time.toString(),
                place = place.toString(),
                color = inputColor)
        toDo?.let { toDoRepository.insertToDo(it) }
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
