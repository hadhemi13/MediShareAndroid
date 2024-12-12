package com.example.medishareandroid.viewModels.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medishareandroid.repositories.PreferencesRepository

class AuthViewModelFactory(
    private val preferencesRepository: PreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(preferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

