//remote/UserApi.kt
package com.example.medishareandroid.remote

import com.example.medishareandroid.models.LoginRequest
import com.example.medishareandroid.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT


interface UserAPI {
    @POST("login")
    fun login(@Body user: LoginRequest): Call<User>
    @POST("signup") // Change this to your actual signup endpoint
    fun signupUser(@Body user: User): Call<Void>
    @POST("forgot-password")
    fun forgetPassword(@Body forgotPasswordDto: ForgotPasswordDto): Call<Message>
    @POST("verify-otp")
    fun verifyOtp(@Body verifyOtpDto: VerifyOtpDto): Call<ResetToken>
    @PUT("reset-password")
    fun resetPassword(@Body resetPasswordDto: ResetPasswordDto): Call<Message>
}
data class Message(
    val message: String
)
data class ResetToken(
    val resetToken: String
)
data class ForgotPasswordDto(
    val email: String
)
data class VerifyOtpDto(
    val recoveryCode: String
)
data class ResetPasswordDto(
    val resetToken: String,
    val newPassword: String
)