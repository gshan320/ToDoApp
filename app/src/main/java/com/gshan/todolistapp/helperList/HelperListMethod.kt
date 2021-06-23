package com.gshan.todolistapp.helperList

import com.gshan.todolistapp.database.TaskItem

interface HelperListMethod {

    suspend fun insertData(model: TaskItem)
    suspend fun getDataByDate(dateString: String): List<TaskItem>
    suspend fun updateData(model: TaskItem)
    suspend fun deleteData(model: TaskItem)
}