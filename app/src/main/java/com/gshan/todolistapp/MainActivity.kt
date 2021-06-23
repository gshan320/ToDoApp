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
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), ActionCallBack.DatePickerCallBack, ActionCallBack.TaskItemClick {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.toolbar_title) lateinit var toolbarTitle: TextView
    @BindView(R.id.recyclerView) lateinit var recyclerView: RecyclerView
    @BindView(R.id.tv_no_result) lateinit var noResult: TextView
    @BindView(R.id.task_count) lateinit var taskCount: TextView
    @BindView(R.id.today_title) lateinit var todayTitle: TextView

    lateinit var adapter: TaskListAdapter
    private lateinit var allTasks: List<TaskItem>
    private lateinit var db: AppDatabase
    private lateinit var chooseDate: String
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        Log.d("lifecycle","onCreate invoked");

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        toolbarTitle.text = "ToDo List"

        db = AppDatabase.getDatabase(this)
        chooseDate = DataConfig.getCurrentDate(this)

        allTasks = ArrayList()
        (allTasks as ArrayList<TaskItem>).clear()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskListAdapter(this, allTasks, this)
        recyclerView.adapter = adapter

        Log.e(TAG, "CURRENT DATE: " + DataConfig.getCurrentDate(this))
        fetchTask(DataConfig.getCurrentDate(this))

    }

    override fun onStart() {
        super.onStart()
        Log.d("lifecycle", "onStart invoked")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "onStart invoked")
        fetchTask(chooseDate)
    }

    override fun onStop() {
        super.onStop()
        Log.d("lifecycle", "onStop invoked")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "onRestart invoked")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle","onDestroy invoked")
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
            todayTitle.text = "Today"
        }else
        {
            todayTitle.text = DataConfig.formatDate(this, dateString!!)
        }
        chooseDate = dateString!!
        fetchTask(dateString)
    }
    
    @OnClick(R.id.btn_add_new)
    fun clickAddNew() {
        val intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)
    }

    override fun clickItem(taskItem: TaskItem, view: View) {
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
                    DataConfig.deleteTask(this, taskItem)
                    (allTasks as ArrayList<TaskItem>).clear()
                    fetchTask(chooseDate)
                }
            }
            true
        })
    }

    private fun fetchTask(dateString: String){

        val getTasksList =  DataConfig.getTasksByDate(this, {
            (allTasks as ArrayList).addAll(it)
                                                            },dateString)
        //(allTasks as ArrayList).addAll((db.taskDao().getTasksByDate(dateString)))

        Log.e(TAG, "ALL TASKS: " + allTasks.size)
        taskCount.text = (allTasks.size.toString() + " tasks")
            if (allTasks.isNotEmpty()) {
                noResult.visibility = View.GONE
            } else {
                noResult.visibility = View.VISIBLE
                (allTasks as ArrayList<TaskItem>).clear()
            }

            adapter.notifyDataSetChanged()
    }

}
