package com.gshan.todolistapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.gshan.todolistapp.R
import com.gshan.todolistapp.adapter.TaskListAdapter.TaskListViewHolder
import com.gshan.todolistapp.callback.ActionCallBack.TaskItemClick
import com.gshan.todolistapp.database.TaskItem

class TaskListAdapter(var context: Context, var data: MutableList<TaskItem?>?, var callback: TaskItemClick) :
    RecyclerView.Adapter<TaskListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        return TaskListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.taskName!!.text = data!![position]!!.title
        holder.time!!.text = data?.get(position)!!.time
        holder.more!!.setOnClickListener(View.OnClickListener { v ->
            callback.clickItem(
                data!![position], v
            )
        })
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    inner class TaskListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.task_name)
        var taskName: TextView? = null

        @JvmField
        @BindView(R.id.time)
        var time: TextView? = null

        @JvmField
        @BindView(R.id.iv_more)
        var more: ImageView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}