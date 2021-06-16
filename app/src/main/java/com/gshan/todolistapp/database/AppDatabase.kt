package com.gshan.todolistapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        var INSTANCE_DB: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE_DB == null) {
                INSTANCE_DB = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java, "todolist db").build()
            }
            return INSTANCE_DB
        }
    }
}