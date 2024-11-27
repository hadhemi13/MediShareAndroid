//remote/UserApi.kt
package com.example.medishareandroid.remote

import com.example.medishareandroid.models.LoginRequest
import com.example.medishareandroid.models.User
import com.example.medishareandroid.models.SignupRequest
import retrofit2.Call
import retrofit2.http.Body
//import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserAPI {
    @POST("auth/login")
    fun login(@Body user: LoginRequest): Call<User>

    @POST("auth/signup") // Change this to your actual signup endpoint
    fun signupUser(@Body user: SignupRequest): Call<Void>

    @POST("auth/forgot-password")
    fun forgetPassword(@Body forgotPasswordDto: ForgotPasswordDto): Call<Message>

    @POST("auth/verify-otp")
    fun verifyOtp(@Body verifyOtpDto: VerifyOtpDto): Call<ResetToken>

    @PUT("auth/reset-password")
    fun resetPassword(@Body resetPasswordDto: ResetPasswordDto): Call<Message>

    @PATCH("user/{id}")
    fun editProfile(@Path("id") id: String, @Body editProfileDto: EditProfileDto): Call<User>


    @PUT("auth/change-password")
    fun changePassword(@Body changePasswordDto: ChangePasswordDto): Call<StatusCode>
    //@GET("auth/google/callback")
}

//////
data class ChangePasswordDto(
    val userId: String,
    val oldPassword: String,
    val newPassword: String
)
//////
data class EditProfileDto(
    val name: String,
    val email: String
)

//////
data class Message(
    val message: String
)
//////
data class StatusCode(
    val statusCode: Int
)
//////
data class ResetToken(
    val resetToken: String
)

//////
data class ForgotPasswordDto(
    val email: String
)

/////
data class VerifyOtpDto(
    val recoveryCode: String
)

////
data class ResetPasswordDto(
    val resetToken: String,
    val newPassword: String
)

