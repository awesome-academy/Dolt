package com.sun.doitpat.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.sun.doitpat.base.BaseViewModel
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.notification.NotificationWorker
import com.sun.doitpat.util.Constants.ALERT
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.EMPTY_STRING
import com.sun.doitpat.util.Constants.ID
import com.sun.doitpat.util.Constants.PLACE
import com.sun.doitpat.util.Constants.TITLE
import com.sun.doitpat.util.Constants.COLOR_BLUE
import com.sun.doitpat.util.Constants.COLOR_GREEN
import com.sun.doitpat.util.Constants.COLOR_ORANGE
import com.sun.doitpat.util.Constants.COLOR_PURPLE
import com.sun.doitpat.util.Constants.COLOR_RED
import com.sun.doitpat.util.Constants.COLOR_YELLOW
import com.sun.doitpat.util.Constants.DEFAULT_CHECK_ID
import com.sun.doitpat.util.Constants.DEFAULT_COLOR
import com.sun.doitpat.util.Constants.NEW
import com.sun.doitpat.util.Constants.NO_ALERT
import com.sun.doitpat.util.TimeUtils
import com.sun.doitpat.util.isLaterThanNow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class DetailViewModel(private val toDoRepository: ToDoRepository) : BaseViewModel() {

    private val item = MutableLiveData<ToDo>()
    private var highestId = DEFAULT_ID
    private var editStatus = ADD_MODE
    private val _reminderTime = MutableLiveData(0L)

    val title = MutableLiveData(EMPTY_STRING)
    val description = MutableLiveData(EMPTY_STRING)
    val time = MutableLiveData(EMPTY_STRING)
    val place = MutableLiveData(EMPTY_STRING)
    val color = MutableLiveData(DEFAULT_COLOR)
    val alertStatus = MutableLiveData(0)
    val itemStatus = MutableLiveData(NEW)
    val reminderTime get() = _reminderTime.value

    private fun setData() {
        item.value?.let {
            title.value = it.title
            description.value = it.description
            time.value = it.time
            place.value = it.place
            color.value = it.color
            alertStatus.value = it.alertStatus
            itemStatus.value = it.status
            _reminderTime.value = it.createdTimeMillisecond
        }
    }

    private fun setNewData() =
        viewModelScope.launch {
            withContext(coroutineContext) { highestId = toDoRepository.getNewToDoId() ?: highestId }
            withContext(coroutineContext) {
                item.value = ToDo(
                    id = (highestId + INCREMENT),
                    title = EMPTY_STRING
                )
            }
        }

    private fun setAlarm(id: Int) {
        setNotification(id)
    }

    private fun setNotification(id: Int) {
        val notificationData = Data.Builder()
            .putInt(ID, id)
            .putString(TITLE, title.value?.toString())
            .putString(PLACE, place.value?.toString())
            .build()
        val oneTimeWorkerRequest =
            OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setInitialDelay(
                    _reminderTime.value.toString().toLong() - System.currentTimeMillis(),
                    TimeUnit.MILLISECONDS)
                .addTag(id.toString())
                .setInputData(notificationData)
                .build()
        WorkManager.getInstance().enqueue(oneTimeWorkerRequest)
    }

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
        time.value = TimeUtils.timeToString(calendar)
        _reminderTime.value = calendar.timeInMillis
    }

    fun addToDo() {
        viewModelScope.launch {
            val toDo = item.value?.copy(
                title = title.value.toString(),
                description = description.value.toString(),
                time = time.value.toString(),
                place = place.value.toString(),
                alertStatus = alertStatus.value.toString().toInt(),
                color = color.value.toString().toInt(),
                status = itemStatus.value.toString().toInt(),
                createdTimeMillisecond = _reminderTime.value)
            toDo?.let { toDoRepository.insertToDo(it) }
        }
        item.value?.id?.let { createNotification(it) }
    }

    private fun createNotification(id: Int) {
        _reminderTime.value?.let {
            if (editStatus == EDIT_MODE) WorkManager.getInstance().cancelAllWorkByTag(id.toString())
            if (alertStatus.value == ALERT && it.isLaterThanNow()) setAlarm(id)
        }
    }

    fun clearTime() {
        this.time.value = EMPTY_STRING
        setReminder(NO_ALERT)
    }

    fun setReminder(alertStatus: Int) {
        this.alertStatus.value = alertStatus
        if (alertStatus == ALERT) itemStatus.value = NEW
    }

    fun getToDo(id: Int) {
        if (id != DEFAULT_ID && id != DEFAULT_CHECK_ID) {
            editStatus = EDIT_MODE
            viewModelScope.launch {
                item.value = toDoRepository.getToDoById(id)
                setData()
            }
        } else {
            editStatus = ADD_MODE
            setNewData()
        }
    }

    fun setCompletedStatus(status: Int) {
        this.itemStatus.value = status
        this.alertStatus.value = NO_ALERT
    }

    companion object {
        private const val EDIT_MODE = 1
        private const val ADD_MODE = 0
        private const val INCREMENT = 1
    }
}
