package com.sun.doitpat.data.repository

import com.sun.doitpat.data.model.ToDo

interface ToDoRepository {

    suspend fun getAllToDo(): List<ToDo>
    suspend fun getNoAlertToDo(): List<ToDo>
    suspend fun getAlertToDo(): List<ToDo>
    suspend fun getCompletedToDo(): List<ToDo>
    suspend fun insertToDo(toDo: ToDo)
    suspend fun updateToDo(toDo: ToDo)
    suspend fun deleteToDo(toDo: ToDo)
    suspend fun getToDoById(id: Int): ToDo
}
