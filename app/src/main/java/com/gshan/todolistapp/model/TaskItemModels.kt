package com.gshan.todolistapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TaskItemModels: Serializable{

    @SerializedName("tid")
    var tid = ""

    @SerializedName("title")
    var title = ""

    @SerializedName("time")
    var time = ""

    @SerializedName("date")
    var date = ""

}