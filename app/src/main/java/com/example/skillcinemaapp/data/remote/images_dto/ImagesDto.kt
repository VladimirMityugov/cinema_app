package com.example.skillcinemaapp.data.remote.images_dto

data class ImagesDto(
    val items: List<Image>,
    val total: Int,
    val totalPages: Int
)