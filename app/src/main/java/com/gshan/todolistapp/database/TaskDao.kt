package com.gshan.todolistapp.database

import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    fun insertTask(taskItem: TaskItem?)

    @Query("SELECT * FROM taskitem WHERE date == :dateString")
    fun getTasksByDate(dateString: String?): Array<TaskItem?>?

    @Update
    fun updateTask(taskItem: TaskItem?)

    @Delete
    fun deleteTask(taskItem: TaskItem?)
}