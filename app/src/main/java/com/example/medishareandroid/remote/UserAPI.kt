package com.example.medishareandroid.remote

import com.example.medishareandroid.models.LoginRequest
import com.example.medishareandroid.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface UserAPI {
    @POST("login")
    fun login(@Body user: LoginRequest): Call<User>
    @POST("signup") // Change this to your actual signup endpoint
    fun signupUser(@Body user: User): Call<Void>
    @POST("forgot-password")
    fun forgetPassword(@Body forgotPasswordDto: ForgotPasswordDto): Call<Message>
}
data class Message(
    val message: String
)
data class ForgotPasswordDto(
    val email: String
)
