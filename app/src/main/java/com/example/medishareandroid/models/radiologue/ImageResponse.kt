package com.example.medishareandroid.models.radiologue

data class ImageResponse(
    val _id :String,
    val imageName: String,
    val title: String
)
data class ImageResponseWrapper(
    val message: String,
    val data: List<ImageResponse>
)
data class ImageRequest(
    val userId: String,
    val patientId: String
)