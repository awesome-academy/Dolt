package com.sun.doitpat.data.model

import androidx.room.*
import com.sun.doitpat.util.Constants.TODO_TABLE_NAME

@Dao
interface ToDoDao {

    @Query("SELECT * FROM $TODO_TABLE_NAME")
    suspend fun getAll(): List<ToDo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Delete
    suspend fun delete(toDo: ToDo)
}
