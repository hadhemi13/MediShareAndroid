package com.example.medishareandroid.models.radiologue

import com.example.medishareandroid.remote.Post

data class DisplayingPosts(val post: Post, var comments: List<Comment>)
