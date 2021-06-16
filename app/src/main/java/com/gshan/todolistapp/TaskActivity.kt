package com.gshan.todolistapp

import android.os.Build
import android.os.Bundle
import android.view.View
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
import com.gshan.todolistapp.database.AppDatabase
import com.gshan.todolistapp.database.TaskItem
import com.gshan.todolistapp.utils.CustomDialogUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


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
        parmItem = intent.getSerializableExtra("item") as? TaskItem

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        toolbarTitle.text = if (parmItem != null) "Edit" else "Add New" + " Task"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View){
                finish()
            }
        })

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
                AddTask(parmItem!!).execute();
            }else {
                val taskItem = TaskItem(0, title = taskTitle.toString(), time = taskTime.toString(), date = taskDate.toString())
                taskItem.title = taskTitle.text.toString()
                taskItem.date = taskDate.text.toString()
                taskItem.time = taskTime.text.toString()
                db!!.taskDao()!!.insertTask(taskItem)
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

    inner class AddTask(var taskItem: TaskItem) : CoroutineScope {
        private var job: Job = Job()
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job // to run code in Main(UI) Thread

        // call this method to cancel a coroutine when you don't need it anymore,
        // e.g. when user closes the screen
        fun cancel() {
            job.cancel()
        }

        fun AddTask(taskItem: TaskItem) {
            this.taskItem = taskItem
        }

        fun execute() = launch {
            onPreExecute()
            val result = doInBackground() // runs in background thread without blocking the Main Thread
            onPostExecute(result)
        }

        private suspend fun doInBackground(vararg params: Void?): Void? = withContext(Dispatchers.IO) { // to run code in Background Thread
            if(parmItem != null){
                db!!.taskDao()!!.updateTask(taskItem)
            }else{
                db!!.taskDao()!!.insertTask(taskItem)
            }
            delay(1000) // simulate async work
            return@withContext null
        }

        // Runs on the Main(UI) Thread
        private fun onPreExecute() {

        }

        // Runs on the Main(UI) Thread
        private fun onPostExecute(result: Void?) {
            onPostExecute(result)
            taskTitle.setText("")
            taskTime.setText("")
            taskDate.setText("")
            }
        }

}