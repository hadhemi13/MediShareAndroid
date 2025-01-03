package com.example.medishareandroid.models.radiologue

data class Patient(
    val _id: String,
    val name: String,
    val email: String
)
data class PatientResponse(
    val message: String,
    val data: List<Patient>
)