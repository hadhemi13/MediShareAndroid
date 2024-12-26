package com.example.medishareandroid.remote

import com.example.medishareandroid.models.chat.ChatResponse
import com.example.medishareandroid.models.chat.CreateDiscussionMessage
import com.example.medishareandroid.models.chat.DiscussionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @POST("chat/{userId}/new")
     fun createDiscussion(
        @Path("userId") userId: String,
        @Body message: CreateDiscussionMessage
    ): Call<ChatResponse>

    @POST("chat/{userId}/{discussionId}")
     fun addMessage(
        @Path("userId") userId: String,
        @Path("discussionId") discussionId: String,
        @Body message: MessageReq
    ): Call<ChatResponse>

    @GET("chat/{userId}/{discussionId}")
     fun getDiscussion(
        @Path("userId") userId: String,
        @Path("discussionId") discussionId: String
    ): Call<DiscussionResponse>

    @GET("chat/{userId}")
    fun getAllDiscussions(
        @Path("userId") userId: String,
    ): Call<List<DiscussionRes>>
}
data class DiscussionRes (val _id:String,val title: String)

data class MessageReq(val message:String)