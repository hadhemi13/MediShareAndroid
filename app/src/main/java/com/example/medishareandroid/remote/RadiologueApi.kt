package com.example.medishareandroid.remote

import com.example.medishareandroid.models.radiologue.ImageResponse
import com.example.medishareandroid.models.radiologue.PostRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface RadiologueApi {

    @POST("post/posts")
    fun fetchAllPosts(
        @Body postsRequests: PostsRequests
    ): Call<List<Post>>

    @POST("image/getAllImages")
    fun getAllImages(
        @Body postsRequests: PostsRequests
    ): Call<List<ImageResponse>>


    @POST("post")
    fun createPost(
        @Body postsRequest: PostRequest
    ): Call<Post>

}

data class PostsRequests(val userId: String)


data class Post(
    val id: String,
    val title: String,
    val upvotes: Int,
    val timeAgo: String,
    val subreddit: String,
    val Content: String,
    val author: String,
    val image: String,
    val profileImage: String,
    val statePost: Boolean
)
