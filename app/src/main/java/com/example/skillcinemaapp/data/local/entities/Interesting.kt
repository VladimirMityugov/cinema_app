package com.example.skillcinemaapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Interesting")
data class Interesting(
    @PrimaryKey
    @ColumnInfo(name = "interestingId")
    val interestingId: Int
)
