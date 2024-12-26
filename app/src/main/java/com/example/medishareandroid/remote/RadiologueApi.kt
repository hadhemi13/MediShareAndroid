package com.example.medishareandroid.remote

import com.example.medishareandroid.models.radiologue.Comment
import com.example.medishareandroid.models.radiologue.CommentRequest
import com.example.medishareandroid.models.radiologue.ImageResponse
import com.example.medishareandroid.models.radiologue.PostRequest
import com.example.medishareandroid.models.radiologue.TumorDetectionRequest
import com.example.medishareandroid.models.radiologue.TumorDetectionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


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

    @Multipart
    @POST("image/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody,@Part("title") title:RequestBody // Ensure userId is passed as a RequestBody
    ): Call<UploadFileRes>


    @GET("comment")
    fun getComment(): Call<List<Comment>>


    @POST("comment")
    fun createComment(@Body comment: CommentRequest): Call<Comment>


    @POST("tumor-detection")
    fun detectTumor(@Body request: TumorDetectionRequest): Call<TumorDetectionResponse>


    @POST("post/inc/upvotes")
    fun setUpvotes(@Body upvotesReq :UpvotesReq): Call<UpvotesRes>
}

data class UpvotesReq(val post_id: String, val userId: String )

data class UpvotesRes(val success: Boolean, val message: String )
data class PostsRequests(val userId: String)
data class UploadFileRes(
    val message: String,
    val filePath: String
)

data class Post(
    val id: String,
    val title: String,
    val upvotes: Int,
    var timeAgo: String,
    val subreddit: String,
    val Content: String,
    val author: String,
    val image: String,
    val profileImage: String,
    var statepost: Boolean
)
