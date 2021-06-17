package com.gshan.todolistapp.database

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity
data class TaskItem (
    @PrimaryKey(autoGenerate = true)
    var tid: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "time")
    var time: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
): Serializable