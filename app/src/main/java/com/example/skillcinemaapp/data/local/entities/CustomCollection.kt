package com.example.skillcinemaapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CustomCollection", primaryKeys = ["collectionName", "movieId"])
data class CustomCollection(
    @ColumnInfo(name = "collectionName")
    val collectionName: String,
    @ColumnInfo(name = "movieId")
    val movieId:Int
)
