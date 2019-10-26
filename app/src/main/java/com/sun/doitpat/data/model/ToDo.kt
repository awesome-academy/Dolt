package com.sun.doitpat.data.model

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sun.doitpat.util.Constants.EMPTY_STRING

@Entity
data class ToDo(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    var description: String = EMPTY_STRING,

    @ColumnInfo(name = "time")
    var time: String = EMPTY_STRING,

    @ColumnInfo(name = "place")
    var place: String = EMPTY_STRING,

    @ColumnInfo(name = "color")
    var color: Int = Color.WHITE,

    @ColumnInfo(name = "status")
    var status: Int = 0
) : BaseObservable()
