package com.example.skillcinemaapp.data.remote.custom_selection_dto

data class CustomSelectionDto(
    val items: List<ItemCustom>,
    val total: Int,
    val totalPages: Int
)