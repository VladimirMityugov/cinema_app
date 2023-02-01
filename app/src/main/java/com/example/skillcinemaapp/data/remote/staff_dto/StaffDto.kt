package com.example.skillcinemaapp.data.remote.staff_dto

data class StaffDto(
    val description: String?,
    val nameEn: String?,
    val nameRu: String?,
    val posterUrl: String,
    val professionKey: String,
    val professionText: String,
    val staffId: Int
)