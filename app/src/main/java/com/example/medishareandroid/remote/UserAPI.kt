package com.example.medishareandroid.remote

import com.example.medishareandroid.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface UserAPI {
    @POST("login")
    fun login(@Body user: User): Call<User>
}
