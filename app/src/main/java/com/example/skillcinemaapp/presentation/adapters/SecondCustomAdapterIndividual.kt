package com.example.skillcinemaapp.presentation.adapters

import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom


class SecondCustomAdapterIndividual(
    val onSecondCustomItemClick: (ItemCustom) -> Unit
    ) : FirstCustomAdapterIndividual(onSecondCustomItemClick)