package com.example.medishareandroid.viewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.medishareandroid.remote.ChangePasswordDto
import com.example.medishareandroid.remote.Message
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.StatusCode
import com.example.medishareandroid.remote.UserAPI
import com.example.medishareandroid.repositories.PreferencesRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewModel(private val preferencesRepository: PreferencesRepository) :ViewModel() {
    private val _passwordUpdated = mutableStateOf(false)


    // MutableLiveData for the UI state
    val userName = MutableLiveData<String?>()
    val userEmail = MutableLiveData<String?>()

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
                Toast.makeText(context, t.message.toString() ?: "Network failure", Toast.LENGTH_SHORT).show()
                Log.d("SettingsViewModel", "Failed to update password", t)
            }
        })
    }
}