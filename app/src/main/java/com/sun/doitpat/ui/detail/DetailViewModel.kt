package com.sun.doitpat.ui.detail

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.notification.NotificationWorker
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.EMPTY_STRING
import com.sun.doitpat.util.Constants.ID
import com.sun.doitpat.util.Constants.PLACE
import com.sun.doitpat.util.Constants.TITLE
import com.sun.doitpat.util.TimeUtils
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class DetailViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    private val toDo = ToDo(title = EMPTY_STRING)
    private val item = MutableLiveData(toDo)

    var title = MutableLiveData<String>(EMPTY_STRING)
    var description = MutableLiveData<String>(EMPTY_STRING)
    var time = MutableLiveData<String>(EMPTY_STRING)
    var place = MutableLiveData<String>(EMPTY_STRING)
    var color = MutableLiveData<Int>(Color.WHITE)

    private var reminderTime: Long = 0
    private var editStatus = ADD_MODE

    fun addColor(color: Int) {
        this.color.value = when (color) {
            1 -> COLOR_RED
            2 -> COLOR_ORANGE
            3 -> COLOR_YELLOW
            4 -> COLOR_GREEN
            5 -> COLOR_BLUE
            6 -> COLOR_PURPLE
            7 -> DEFAULT_COLOR
            else -> DEFAULT_COLOR
        }
    }

    fun addTime(calendar: Calendar) {
        this.time.value = TimeUtils.timeToString(calendar)
        reminderTime = (calendar.timeInMillis - System.currentTimeMillis())
    }

    fun addToDo() {
        viewModelScope.launch {
            val toDo = item.value?.copy(
                    title = title.value.toString(),
                    description = description.value.toString(),
                    time = time.value.toString(),
                    place = place.value.toString(),
                    color = color.value.toString().toInt())
            toDo?.let { toDoRepository.insertToDo(it) }
        }
        setAlarm()
    }

    fun getToDo(id: Int) {
        if (id != DEFAULT_ID) {
            viewModelScope.launch {
                item.value = toDoRepository.getToDoById(id)
                setData()
            }
        }
        editStatus = EDIT_MODE
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

    private fun setAlarm() {
        if (reminderTime > 0) {
            item.value?.id?.let {
                if (editStatus == EDIT_MODE) {
                    WorkManager.getInstance().cancelAllWorkByTag(it.toString())
                }
                setNotification()
            }
        }
    }

    private fun setNotification() {
        item.value?.id?.let {
            val notificationData = Data.Builder()
                    .putInt(ID, it)
                    .putString(TITLE, title.value.toString())
                    .putString(PLACE, place.value.toString())
                    .build()
            val oneTimeWorkerRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                    .setInitialDelay(reminderTime, TimeUnit.MILLISECONDS)
                    .addTag(it.toString())
                    .setInputData(notificationData)
                    .build()
            WorkManager.getInstance().enqueue(oneTimeWorkerRequest)
        }
    }

    companion object {
        private const val COLOR_RED = -1876663
        private const val COLOR_ORANGE = -223690
        private const val COLOR_YELLOW = -16121
        private const val COLOR_GREEN = -6894049
        private const val COLOR_BLUE = -12803585
        private const val COLOR_PURPLE = -10791470
        private const val DEFAULT_COLOR = -2697514
        private const val EDIT_MODE = 1
        private const val ADD_MODE = 0
    }
}
