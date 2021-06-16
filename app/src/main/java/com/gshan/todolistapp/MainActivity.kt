package com.gshan.todolistapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.gshan.todolistapp.adapter.TaskListAdapter
import com.gshan.todolistapp.callback.ActionCallBack
import com.gshan.todolistapp.config.DataConfig
import com.gshan.todolistapp.database.AppDatabase
import com.gshan.todolistapp.database.TaskItem
import com.gshan.todolistapp.utils.CustomDialogUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), ActionCallBack.DatePickerCallBack, ActionCallBack.TaskItemClick {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.toolbar_title) lateinit var toolbarTitle: TextView
    @BindView(R.id.recyclerView) lateinit var recyclerView: RecyclerView
    @BindView(R.id.tv_no_result) lateinit var noResult: TextView
    @BindView(R.id.task_count) lateinit var taskCount: TextView
    @BindView(R.id.today_title) lateinit var todayTitle: TextView

    lateinit var adapter: TaskListAdapter
    private var allTasks: MutableList<TaskItem?>? = null
    private lateinit var db: AppDatabase
    private lateinit var chooseDate: String
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("")
        toolbarTitle.text = "ToDo List"

        db = AppDatabase.getDatabase(this)!!
        chooseDate = DataConfig.getCurrentDate(this)

        allTasks = java.util.ArrayList()
        (allTasks as java.util.ArrayList<TaskItem?>).clear()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskListAdapter(this, allTasks, this)
        recyclerView.adapter = adapter

        Log.e(TAG, "CURRENT DATE: " + DataConfig.getCurrentDate(this))
        FetchTask(DataConfig.getCurrentDate(this)).execute()

    }

    override fun onResume() {
        super.onResume()
        FetchTask(chooseDate).execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        CustomDialogUtils.showDatePickerDialog(this, this)
        return true
    }

    override fun selectedDate(dateString: String?) {
        if (dateString.equals(DataConfig.getCurrentDate(this))) {
            todayTitle.setText("Today")
        }else
        {
            todayTitle.setText(DataConfig.formatDate(this, dateString!!))
        }
        chooseDate = dateString!!
        FetchTask(dateString).execute()
    }

    override fun clickItem(taskItem: TaskItem?, view: View?) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(this, TaskActivity::class.java)
                    intent.putExtra("Item", taskItem)
                    startActivity(intent)
                }
                R.id.menu_delete -> {
                    DeleteTask(taskItem!!).execute()
                }
            }
            true
        })
    }
    
    @OnClick(R.id.btn_add_new)
    fun clickAddNew() {
        val intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)
    }

    inner class FetchTask(var dateString: String) : CoroutineScope {
        private var job: Job = Job()
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job // to run code in Main(UI) Thread

        // call this method to cancel a coroutine when you don't need it anymore,
        // e.g. when user closes the screen
        fun cancel() {
            job.cancel()
        }

        fun execute() = launch {
            onPreExecute()
            val result = doInBackground() // runs in background thread without blocking the Main Thread
            onPostExecute(result)
        }

        private suspend fun doInBackground(): String = withContext(Dispatchers.IO) { // to run code in Background Thread
            allTasks!!.addAll(db.taskDao()!!.getTasksByDate(dateString)!!)
            delay(1000) // simulate async work
            return@withContext "Got Items by Date"
        }

        // Runs on the Main(UI) Thread
        private fun onPreExecute() {

        }

        // Runs on the Main(UI) Thread
        private fun onPostExecute(result: String) {
            Log.e(TAG, "ALL TASKS: " + allTasks!!.size)
            taskCount.setText(allTasks!!.size.toString() + " tasks")
            if (allTasks!!.isNotEmpty()) {
                noResult.setVisibility(View.GONE)
            } else {
                noResult.setVisibility(View.VISIBLE)
                allTasks?.clear()
            }

            adapter.notifyDataSetChanged()
        }
    }

    inner class DeleteTask(var taskItem: TaskItem) : CoroutineScope {
        private var job: Job = Job()
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job // to run code in Main(UI) Thread

        // call this method to cancel a coroutine when you don't need it anymore,
        // e.g. when user closes the screen
        fun cancel() {
            job.cancel()
        }

        fun DeleteTask(taskItem: TaskItem) {
            this.taskItem = taskItem
        }

        fun execute() = launch {
            onPreExecute()
            val result = doInBackground() // runs in background thread without blocking the Main Thread
            onPostExecute(result)
        }

        private suspend fun doInBackground(vararg params: Void?): Void? = withContext(Dispatchers.IO) { // to run code in Background Thread
            db!!.taskDao()!!.deleteTask(taskItem)
            return@withContext null
        }

        // Runs on the Main(UI) Thread
        private fun onPreExecute() {

        }

        // Runs on the Main(UI) Thread
        private fun onPostExecute(result: Void?) {
            onPostExecute(result)
            allTasks?.clear()
            FetchTask(chooseDate).execute()

        }
    }

}
