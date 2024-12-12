//models/User.kt
package com.example.medishareandroid.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val userId:String,
    val userName: String,
    val userEmail: String,
    val accessToken: String,
    val refreshToken: String,
    val userRole: String,
)
