package com.sun.doitpat.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.model.ToDoDao

@Database(entities = [ToDo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun toDoDao(): ToDoDao

    companion object {

        private const val DATABASE_NAME = "todo"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }
}
