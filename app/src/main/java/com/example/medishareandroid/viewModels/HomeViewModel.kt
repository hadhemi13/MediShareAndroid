package com.example.medishareandroid.viewModels

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _userName = "000000"
    val userName: String get() = _userName

    private val _clinics = listOf(
        Clinic("Clinic A", "Cardiology", "08:00 AM ","- 05:00 PM"),
        Clinic("Clinic B", "Neurology", "09:00 AM ","- 06:00 PM")
    )
    val clinics: List<Clinic> get() = _clinics
}
data class Clinic(
    val name: String,
    val specialization: String,
    val openHour: String,
    val closeHour: String
)