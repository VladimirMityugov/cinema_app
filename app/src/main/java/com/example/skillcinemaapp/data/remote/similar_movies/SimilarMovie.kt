package com.example.skillcinemaapp.data.remote.similar_movies

data class SimilarMovie(
    val filmId: Int,
    val nameEn: String?,
    val nameOriginal: String?,
    val nameRu: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val relationType: String
)