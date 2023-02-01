package com.example.skillcinemaapp.data.remote.premiers_dto

data class Item(
    val countries: List<Country>,
    val duration: Int?,
    val genres: List<Genre>,
    val kinopoiskId: Int,
    val nameEn: String?,
    val nameRu: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val premiereRu: String,
    val year: Int,
    var watched_status: Boolean?
)