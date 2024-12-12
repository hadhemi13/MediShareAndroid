package com.example.medishareandroid.viewModels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medishareandroid.repositories.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val preferencesRepository: PreferencesRepository) : ViewModel() {
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            preferencesRepository.isUserLoggedIn().collect { isLoggedIn ->
                _isUserLoggedIn.value = isLoggedIn
            }
        }
    }

    fun loginUser() {
        // Assuming login validation logic here
        viewModelScope.launch {
            preferencesRepository.setUserLoggedIn(true)
            _isUserLoggedIn.value = true

        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            preferencesRepository.setUserLoggedIn(false)
            _isUserLoggedIn.value = false
        }
    }
}
