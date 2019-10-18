package com.sun.doitpat.data.repository.impl

import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.model.ToDoDao
import com.sun.doitpat.data.repository.ToDoRepository

class ToDoRepositoryImpl(private val toDoDao: ToDoDao) : ToDoRepository {

    override suspend fun getAllToDo() = toDoDao.getAll()

    override suspend fun insertToDo(toDo: ToDo) = toDoDao.insert(toDo)

    override suspend fun updateToDo(toDo: ToDo) = toDoDao.update(toDo)

    override suspend fun deleteToDo(toDo: ToDo) = toDoDao.delete(toDo)

}
