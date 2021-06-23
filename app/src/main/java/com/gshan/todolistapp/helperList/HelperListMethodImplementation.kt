package com.gshan.todolistapp.helperList

import com.gshan.todolistapp.database.AppDatabase
import com.gshan.todolistapp.database.TaskItem

class HelperListMethodImplementation(private val appDatabase: AppDatabase): HelperListMethod {

    override suspend fun insertData(model: TaskItem) = appDatabase.taskDao().insertTask(model)

    override suspend fun getDataByDate(dateString: String): List<TaskItem> = appDatabase.taskDao().getTasksByDate(dateString)

    override suspend fun updateData(model: TaskItem) = appDatabase.taskDao().updateTask(model)

    override suspend fun deleteData(model: TaskItem) = appDatabase.taskDao().deleteTask(model)

}