package com.itis.itis_android_tasks.model



data class Book (
    val id: String,
    val title: String,
    val description: String,
    val publicationYear: Int,
    val rating: Double,
    var isFavorite: Boolean
    )
