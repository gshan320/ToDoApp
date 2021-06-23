package com.gshan.todolistapp.database

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity
data class TaskItem (
    @PrimaryKey(autoGenerate = true)
    var tid: Int? = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "date")
    var date: String,
): Serializable