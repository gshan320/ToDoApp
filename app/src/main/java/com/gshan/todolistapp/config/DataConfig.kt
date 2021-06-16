package com.gshan.todolistapp.config

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object DataConfig {
    fun getCurrentDate(context: Context?): String {
        //dd-mm-yyyy
        val calendar = Calendar.getInstance()
        return SimpleDateFormat("dd-M-yyyy").format(calendar.time)
    }

    fun formatDate(context: Context?, dateString: String): String {
        val simpleDateFormat = SimpleDateFormat("dd-M-yyyy")
        try {
            val parseDate = simpleDateFormat.parse(dateString)
            return SimpleDateFormat("dd MM").format(parseDate)
        } catch (e: Exception) {
        }
        return dateString
    }
}