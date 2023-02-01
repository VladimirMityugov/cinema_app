package com.example.skillcinemaapp.data.remote.custom_selection_dto


data class ItemCustom(
    val countries: List<CountryCustom>,
    val genres: List<GenreCustom>,
    val imdbId: String?,
    val kinopoiskId: Int,
    val nameEn: Any?,
    val nameOriginal: String?,
    val nameRu: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val ratingImdb: Double?,
    val ratingKinopoisk: Double?,
    val type: String,
    val year: Int?,
    var watchedStatus:Boolean?
)