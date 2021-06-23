package com.gshan.todolistapp.callback

import android.view.View
import com.gshan.todolistapp.database.TaskItem

interface ActionCallBack {
    interface DatePickerCallBack {
        fun selectedDate(dateString: String?)
    }

    interface TimePickerCallBack {
        fun selectedTime(timeString: String?)
    }

    interface TaskItemClick {
        fun clickItem(taskItem: TaskItem, view: View)
    }
}