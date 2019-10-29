package com.sun.doitpat.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.model.ToDoDao
import com.sun.doitpat.util.Constants.TODO_TABLE_NAME

@Database(entities = [ToDo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun toDoDao(): ToDoDao

    companion object {

        private const val DATABASE_NAME = "todo"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()
        private val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $TODO_TABLE_NAME ADD COLUMN alertStatus INTEGER NOT NULL DEFAULT 0")
            }
        }

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
        ).addMigrations(MIGRATION_1_2).build()
    }

}
