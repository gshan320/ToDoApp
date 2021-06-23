package com.gshan.todolistapp.utils

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import com.gshan.todolistapp.R
import com.gshan.todolistapp.callback.ActionCallBack

object CustomDialogUtils {

    var TAG: String = CustomDialogUtils::class.java.simpleName
    var selectedDate: String = ""
    var selectedTime: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePickerDialog(context: Context?, callback: ActionCallBack.DatePickerCallBack) {

        val dialog = AlertDialog.Builder(context)
        val dateDialog: View =
            LayoutInflater.from(context).inflate(R.layout.layout_date_picker_dialog, null)
        val calendarView = dateDialog.findViewById<CalendarView>(R.id.calendarView)

        dialog.setView(dateDialog)

        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            selectedDate = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
        }

        dialog.setPositiveButton("Update") { dialog, which ->
            dialog.dismiss()
            callback.selectedDate(selectedDate)
        }
        dialog.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }

        val alert = dialog.create()
        alert.show()
    }

    fun showTimePickerDialog(context: Context?, callback: ActionCallBack.TimePickerCallBack){
        val dialog = AlertDialog.Builder(context)
        val timeDialog: View = LayoutInflater.from(context).inflate(R.layout.layout_time_picker_dialog, null)
        val timePicker = timeDialog.findViewById<TimePicker>(R.id.time_picker)

        dialog.setView(timeDialog)

        timePicker.setOnTimeChangedListener { timePickerView, hourOfDay, minute ->
           selectedTime  = "$hourOfDay:$minute"
        }

        dialog.setView(timeDialog)
        dialog.setPositiveButton("OK"){ dialog, which ->
            dialog.dismiss()
            callback.selectedTime(selectedTime)
        }
        dialog.setNegativeButton("Cancel"){dialog, which -> dialog.dismiss()}
        val alert = dialog.create()
        alert.show()
    }
}