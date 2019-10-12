package com.sun.doitpat.data.repository

import com.sun.doitpat.data.model.ToDo

interface ToDoRepository {

    suspend fun insertToDo(toDo: ToDo)
    suspend fun updateToDo(toDo: ToDo)
    suspend fun deleteToDo(toDo: ToDo)
}
