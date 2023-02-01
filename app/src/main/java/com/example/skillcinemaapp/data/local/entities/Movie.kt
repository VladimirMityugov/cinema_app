package com.example.skillcinemaapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.skillcinemaapp.data.remote.movie_info_dto.Country
import com.example.skillcinemaapp.data.remote.movie_info_dto.Genre

@Entity(tableName = "Movie")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val movieId: Int,
    @ColumnInfo(name = "poster")
    val posterUri:String?,
    @ColumnInfo(name = "rating")
    val rating:Double?,
    @ColumnInfo(name = "genre")
    val genre:String?,
    @ColumnInfo(name = "movieName")
    val movieName:String?,
    @ColumnInfo(name = "country")
    val country: String?,
    @ColumnInfo(name = "logoUrl")
    val logoUrl: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "filmLength")
    val filmLength: Int?,
    @ColumnInfo(name = "imdbId")
    val imdbId: String?,
    @ColumnInfo(name = "nameEn")
    val nameEn: String?,
    @ColumnInfo(name = "ratingAgeLimits")
    val ratingAgeLimits: String?,
    @ColumnInfo(name = "shortDescription")
    val shortDescription: String?,
    @ColumnInfo(name = "shortFilm")
    val shortFilm: Boolean?,
    @ColumnInfo(name = "serial")
    val serial: Boolean?,
    @ColumnInfo(name = "year")
    val year: Int?
)
