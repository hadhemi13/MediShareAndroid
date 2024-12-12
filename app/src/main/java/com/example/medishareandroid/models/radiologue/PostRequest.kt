package com.example.medishareandroid.models.radiologue

data class PostRequest(
    val title: String,
    val imageId: String,
    val content: String,
    val userid: String,
    val subreddit: String
)
