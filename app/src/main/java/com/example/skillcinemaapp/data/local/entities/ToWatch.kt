package com.example.skillcinemaapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ToWatch")
data class ToWatch(
    @PrimaryKey
    @ColumnInfo(name = "toWatchId")
    val toWatchId: Int
)
