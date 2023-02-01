package com.example.skillcinemaapp.data.remote.series_info_dto

data class Episode(
    val episodeNumber: Int,
    val nameEn: String?,
    val nameRu: String?,
    val releaseDate: String?,
    val seasonNumber: Int,
    val synopsis: String?
)