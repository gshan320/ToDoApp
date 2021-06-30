package com.gshan.todolistapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskItem::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE_DB: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE_DB ?: synchronized(this) {
                INSTANCE_DB ?: buildRoomDB(context).also { INSTANCE_DB = it}
            }
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "TASK_DAO_ROOM_DATABASE"
            )
                //.allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}