package com.example.medishareandroid.viewModels


import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.medishareandroid.remote.ChangePasswordDto
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.StatusCode
import com.example.medishareandroid.remote.UserAPI
import com.example.medishareandroid.repositories.PreferencesRepository
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.MutableStateFlow
import android.graphics.Bitmap
import com.example.medishareandroid.remote.OcrAPI
import com.example.medishareandroid.remote.QrResponse
import kotlinx.coroutines.flow.StateFlow


// Lifecycle and ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// Android utilities
import android.graphics.BitmapFactory
import android.util.Base64
import android.content.Context
import android.util.Log

// Network and coroutine dependencies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class SettingsViewModel(private val preferencesRepository: PreferencesRepository) :ViewModel() {
    private val _passwordUpdated = mutableStateOf(false)
    private val _qrCodeBitmap = MutableStateFlow<Bitmap?>(null)
    var qrCodeBitmap: StateFlow<Bitmap?> = _qrCodeBitmap

    // MutableLiveData for the UI state
    //val userName = MutableLiveData<String?>()
    //val userEmail = MutableLiveData<String?>()

    // Méthode pour modifier l'état "passwordUpdated" pour afficher un message ou modifier l'UI
    fun setPasswordUpdated(updated: Boolean) {
        _passwordUpdated.value = updated
    }


    // Votre logique de mise à jour du mot de passe
    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
        context: Context,
        userId:String,
        navController: NavController
    ) {
        if (newPassword != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrofit call to change the password
        val userApi = RetrofitInstance.getRetrofit().create(UserAPI::class.java)
        val changePasswordDto = ChangePasswordDto(userId = userId, oldPassword = oldPassword, newPassword = newPassword)
        userApi.changePassword(changePasswordDto).enqueue(object : Callback<StatusCode> {
            override fun onResponse(call: Call<StatusCode>, response: Response<StatusCode>) {
                if (response.isSuccessful) {
                    // Get the message from the response body
                    val message = response.body()?.statusCode ?: "Password updated successfully"
                    Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()

                    // Update state and navigate back to settings screen
                    setPasswordUpdated(true)
                    navController.navigate("settings") {
                        popUpTo("settings") { inclusive = true }
                    }
                } else {
                    // Handle error message

                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    Log.d("SettingsViewModel", errorMsg)
                }
            }

            override fun onFailure(call: Call<StatusCode>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d("SettingsViewModel", "Failed to update password", t)
            }
        })
    }

    //qr
    fun fetchQrCode(userId: String, context: Context) {
        viewModelScope.launch {
            val ocrApi = RetrofitInstance.getRetrofit().create(OcrAPI::class.java)

            ocrApi.getQrCode(userId).enqueue(object : Callback<QrResponse> {
                override fun onResponse(call: Call<QrResponse>, response: Response<QrResponse>) {
                    if (response.isSuccessful) {
                        // Get the message from the response body
                        val message = response.body()?: "Password updated successfully"
                        //Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
                        try {
                            val base64String = response.body()?.qrCode ?: ""
                            val base64Data = base64String.split(",").getOrNull(1) ?: base64String
                           // Toast.makeText(context, base64Data, Toast.LENGTH_LONG).show()
                            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                            _qrCodeBitmap.value = bitmap
                            qrCodeBitmap=_qrCodeBitmap
                        } catch (e: Exception) {
                            // Handle errors if needed
                            Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
                           // _qrCodeBitmap.value = null
                        }

                    } else {
                        // Handle error message

                        val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                        Log.d("SettingsViewModel", errorMsg)
                    }
                }

                override fun onFailure(call: Call<QrResponse>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("SettingsViewModel", "Failed to update password", t)

                }
            })

        }
    }



}
