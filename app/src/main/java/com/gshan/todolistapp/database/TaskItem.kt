package com.gshan.todolistapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TaskItem(

    @PrimaryKey(autoGenerate = true) val tid: Int,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "time") var time: String?,
    @ColumnInfo(name = "date") var date: String?): Serializable