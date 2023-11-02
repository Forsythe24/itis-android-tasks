package com.itis.itis_android_tasks.model

import androidx.annotation.DrawableRes

data class Friend(
    var id: Int,
    val name: String,
    val info: String,
    @DrawableRes val image: Int? = null,
    var isLiked: Boolean = false
)
