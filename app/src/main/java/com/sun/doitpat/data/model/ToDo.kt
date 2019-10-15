package com.sun.doitpat.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDo (

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "place")
    val place: String,

    @ColumnInfo(name = "color")
    val color: Int,

    @ColumnInfo(name = "status")
    var status: Int
)
