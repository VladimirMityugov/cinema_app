package com.example.skillcinemaapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Favorites")
data class Favorites(
    @PrimaryKey
    @ColumnInfo(name = "favoritesId")
    val favoritesId: Int
)
