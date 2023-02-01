package com.example.skillcinemaapp.data.remote.person_dto

data class PersonDto(
    val age: Int?,
    val birthday: String?,
    val birthplace: String?,
    val death: Any?,
    val deathplace: String?,
    val facts: List<Any>?,
    val films: List<PersonFilm>?,
    val growth: Int?,
    val hasAwards: Int?,
    val nameEn: String?,
    val nameRu: String?,
    val personId: Int,
    val posterUrl: String,
    val profession: String?,
    val sex: String?,
    val spouses: List<Any>?,
    val webUrl: String?
)