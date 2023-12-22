package com.itis.itis_android_tasks.model

data class User(
    var id: String,
    var name: String,
    var phoneNumber: String,
    var email: String,
    var password: String,
    var deleteDate: String?
)
