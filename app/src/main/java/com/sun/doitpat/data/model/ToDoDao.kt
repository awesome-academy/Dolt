package com.sun.doitpat.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.sun.doitpat.util.Constants.TODO_TABLE_NAME

@Dao
interface ToDoDao {

    @Query("SELECT * FROM $TODO_TABLE_NAME")
    suspend fun getAll(): LiveData<List<ToDo>>

    @Insert
    suspend fun insert(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Delete
    suspend fun delete(toDo: ToDo)
}
