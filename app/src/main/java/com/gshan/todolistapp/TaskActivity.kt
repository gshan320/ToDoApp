package com.gshan.todolistapp

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.TextInputEditText
import com.gshan.todolistapp.callback.ActionCallBack
import com.gshan.todolistapp.config.DataConfig
import com.gshan.todolistapp.database.AppDatabase
import com.gshan.todolistapp.database.TaskItem
import com.gshan.todolistapp.utils.CustomDialogUtils


class TaskActivity : AppCompatActivity(), ActionCallBack.DatePickerCallBack, ActionCallBack.TimePickerCallBack {

    //Default Components
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.toolbar_title) lateinit var toolbarTitle: TextView

    //Database Entities
    @BindView(R.id.et_title) lateinit var taskTitle: TextInputEditText
    @BindView(R.id.et_time) lateinit var taskTime: TextInputEditText
    @BindView(R.id.et_date) lateinit var taskDate: TextInputEditText

    var db: AppDatabase? = null
    var parmItem: TaskItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        ButterKnife.bind(this)

        db = AppDatabase.getDatabase(this)
        parmItem = intent.getSerializableExtra("Item") as? TaskItem

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        toolbarTitle.text = if (parmItem != null) "Edit" else "Add New" + " Task"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (parmItem != null){
            taskTitle.setText(parmItem!!.title)
            taskTime.setText(parmItem!!.time)
            taskDate.setText(parmItem!!.date)
        }

    }
    
    @OnClick(R.id.btn_save)
    fun clickSave() {
        if (taskTime.text.toString().isEmpty() || taskTitle.text.toString().isEmpty() || taskDate.text.toString().isEmpty()) {
            Toast.makeText(this, "Please fill in required fields!", Toast.LENGTH_SHORT).show()
        } else {
            if (parmItem != null){
                parmItem!!.title = taskTitle.text.toString()
                parmItem!!.time = taskTime.text.toString()
                parmItem!!.date = taskDate.text.toString()
                DataConfig.updateStatus(this, parmItem!!)
                Toast.makeText(this, "Task Edited!", Toast.LENGTH_SHORT).show()
            }else {
                val taskItem = TaskItem(null, title = taskTitle.toString(), time = taskTime.toString(), date = taskDate.toString())
                taskItem.title = taskTitle.text.toString()
                taskItem.date = taskDate.text.toString()
                taskItem.time = taskTime.text.toString()

                DataConfig.addTask(this, taskItem)
                Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show()

                taskTitle.setText("")
                taskTime.setText("")
                taskDate.setText("")
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OnClick(R.id.et_date)
    fun chooseDate(){
        CustomDialogUtils.showDatePickerDialog(this, this)
    }

    @OnClick(R.id.et_time)
    fun chooseTime(){
        CustomDialogUtils.showTimePickerDialog(this, this)
    }

    override fun selectedDate(dateString: String?) {
        taskDate.setText(dateString)
    }

    override fun selectedTime(timeString: String?) {
        taskTime.setText(timeString)
    }

}