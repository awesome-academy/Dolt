package com.sun.doitpat.data.repository.impl

import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.model.ToDoDao
import com.sun.doitpat.data.repository.ToDoRepository

class ToDoRepositoryImpl(private val toDoDao: ToDoDao) : ToDoRepository {

    override suspend fun getNoAlertToDo() = toDoDao.getNoAlertToDo()

    override suspend fun getAlertToDo() = toDoDao.getAlertToDo()

    override suspend fun getCompletedToDo() = toDoDao.getCompletedToDo()

    override suspend fun getAllToDo() = toDoDao.getWidgetToDo()

    override suspend fun insertToDo(toDo: ToDo) = toDoDao.insert(toDo)

    override suspend fun updateToDo(toDo: ToDo) = toDoDao.update(toDo)

    override suspend fun deleteToDo(toDo: ToDo) = toDoDao.delete(toDo)

    override suspend fun getToDoById(id: Int) = toDoDao.getToDoById(id)

    override suspend fun getNewToDoId() = toDoDao.getNewToDoId()

}
