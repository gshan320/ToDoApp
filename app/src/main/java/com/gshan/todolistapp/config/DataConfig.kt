package com.gshan.todolistapp.config

import android.content.Context
import com.gshan.todolistapp.database.AppDatabase
import com.gshan.todolistapp.database.TaskItem
import com.gshan.todolistapp.helperList.HelperListMethodImplementation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object DataConfig {
    fun getCurrentDate(context: Context): String {
        //dd-mm-yyyy
        val calendar = Calendar.getInstance()
        return SimpleDateFormat("dd-M-yyyy").format(calendar.time)
    }

    fun formatDate(context: Context, dateString: String): String {
        val simpleDateFormat = SimpleDateFormat("dd-M-yyyy")
        try {
            val parseDate = simpleDateFormat.parse(dateString)
            return SimpleDateFormat("dd-M-yyyy").format(parseDate)
        } catch (e: Exception) {
        }
        return dateString
    }

    //Save Tasks Into Database
    fun addTask(context: Context, params: TaskItem) {
        val dbHelper = HelperListMethodImplementation(AppDatabase.getDatabase(context))
        CoroutineScope(Dispatchers.Default).launch {
            dbHelper.insertData(params)
        }
    }

    //Delete Task from Database
    fun deleteTask(context: Context, params: TaskItem) {
        val dbHelper = HelperListMethodImplementation(AppDatabase.getDatabase(context))
        CoroutineScope(Dispatchers.Default).launch {
            dbHelper.deleteData(params)
        }
    }

    //Update Edited Task Details
    fun updateStatus(context: Context, params: TaskItem){
        val dbHelper = HelperListMethodImplementation(AppDatabase.getDatabase(context))
        CoroutineScope(Dispatchers.Default).launch {
            dbHelper.updateData(params)
        }
    }

    //Get Tasks By Date
    fun getTasksByDate(context: Context, func: (List<TaskItem>) -> Boolean, dateString: String){
        val dbHelper = HelperListMethodImplementation(AppDatabase.getDatabase(context))
        CoroutineScope(Dispatchers.Default).launch {
            var taskList = dbHelper.getDataByDate(dateString)
            func.invoke(taskList)
        }
    }

//    fun getTasksByDate(context: Context, dateString: String) {
//        val dbHelper = HelperListMethodImplementation(AppDatabase.getDatabase(context))
//        CoroutineScope(Dispatchers.Default).launch {
//            dbHelper.getDataByDate(dateString)
//        }
//    }

}