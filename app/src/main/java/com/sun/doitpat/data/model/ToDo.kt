package com.sun.doitpat.data.model

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDo(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "place")
    val place: String,

    @ColumnInfo(name = "color")
    var color: Int = Color.WHITE,

    @ColumnInfo(name = "status")
    var status: Int = 0
) : BaseObservable()
