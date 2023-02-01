package com.example.skillcinemaapp.data.remote.person_dto

data class PersonFilm(
    val description: String?,
    val filmId: Int,
    val general: Boolean,
    val nameEn: String?,
    val nameRu: String?,
    val professionKey: String,
    val rating: String?,
    var posterUri: String?
)