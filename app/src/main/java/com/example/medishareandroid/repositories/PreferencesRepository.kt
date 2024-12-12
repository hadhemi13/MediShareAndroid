package com.example.medishareandroid.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PreferencesRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("medi_share_prefs", Context.MODE_PRIVATE)

    // Clés des préférences
    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_ID = "id"
        private const val KEY_TOKEN = "token"
        private const val KEY_Role = "role"
    }
    fun isUserLoggedIn(): Flow<Boolean> = flow {
        Log.d("isUserLoggedIn","isUserLoggedIn")

        emit(sharedPreferences.getBoolean("is_logged_in", false))
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        //Log.d("setUserLoggedIn","_____________setUserLoggedIn"+isLoggedIn)
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }


    fun setName(name: String) {
        sharedPreferences.edit().putString(KEY_NAME, name).apply()
    }

    // Méthode pour récupérer le nom de l'utilisateur
    fun getName(): String? {
        return sharedPreferences.getString(KEY_NAME, null)
    }

    // Méthode pour enregistrer l'email de l'utilisateur
    fun setEmail(email: String) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply()
    }

    // Méthode pour récupérer l'email de l'utilisateur
    fun getEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    // Méthode pour enregistrer l'ID de l'utilisateur
    fun setId(id: String) {
        sharedPreferences.edit().putString(KEY_ID, id).apply()
    }

    // Méthode pour récupérer l'ID de l'utilisateur
    fun getId(): String? {
        return sharedPreferences.getString(KEY_ID, null)
    }
    fun setRole(role: String) {
        sharedPreferences.edit().putString(KEY_Role, role).apply()
    }
    fun getRole(): String? {
        return sharedPreferences.getString(KEY_Role, null)
    }
    // Méthode pour enregistrer le token de l'utilisateur
    fun setToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    // Méthode pour récupérer le token de l'utilisateur
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun clearLoginData() {
        sharedPreferences.edit().clear().apply()
        // Ensure the is_logged_in flag is set to false
        setUserLoggedIn(false)
    }
}
