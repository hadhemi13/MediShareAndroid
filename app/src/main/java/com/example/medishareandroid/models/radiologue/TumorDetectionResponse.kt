package com.example.medishareandroid.models.radiologue

data class TumorDetectionResponse(
    val inference_id: String,
    val time: Double,
    val image: ImageDetails,
    val predictions: List<Prediction>
)

data class ImageDetails(
    val width: Int,
    val height: Int
)

data class Prediction(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double,
    val confidence: Double,
    val className: String,
    val class_id: Int,
    val detection_id: String
)
