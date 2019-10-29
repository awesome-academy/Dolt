package com.sun.doitpat.data.model

import androidx.room.*
import com.sun.doitpat.util.Constants.TODO_TABLE_NAME

@Dao
interface ToDoDao {

    @Query("SELECT * FROM $TODO_TABLE_NAME")
    suspend fun getAll(): List<ToDo>

    @Query("SELECT * FROM $TODO_TABLE_NAME WHERE alertStatus = 0")
    suspend fun getNoAlertToDo(): List<ToDo>

    @Query("SELECT * FROM $TODO_TABLE_NAME WHERE alertStatus = $ALERT")
    suspend fun getAlertToDo(): List<ToDo>

    @Query("SELECT * FROM $TODO_TABLE_NAME WHERE status = $COMPLETED")
    suspend fun getCompletedToDo(): List<ToDo>

    @Query("SELECT * FROM $TODO_TABLE_NAME WHERE id = :id")
    suspend fun getToDoById(id: Int): ToDo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Delete
    suspend fun delete(toDo: ToDo)

    companion object {
        private const val COMPLETED = 1
        private const val ALERT = 1
    }
}
