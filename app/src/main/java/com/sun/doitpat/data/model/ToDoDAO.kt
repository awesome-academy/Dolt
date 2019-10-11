package com.sun.doitpat.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.sun.doitpat.util.Constants.TODO_TABLE_NAME

@Dao
interface ToDoDAO {

    @Query("SELECT * FROM $TODO_TABLE_NAME")
    fun getAll(): LiveData<List<ToDo>>

    @Insert
    fun insert(toDo: ToDo)

    @Update
    fun update(toDo: ToDo)

    @Delete
    fun delete(toDo: ToDo)
}
