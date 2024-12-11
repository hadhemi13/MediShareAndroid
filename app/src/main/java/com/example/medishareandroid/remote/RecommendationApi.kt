package com.example.medishareandroid.remote

import retrofit2.Call
//import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendationApi {


    @POST("ai/recommendations")
    fun fetchRecommendations(@Body recReq: RecReq): Call<ReqRes>

}


    data class RecReq(val userId: String)

    data class ReqRes(val success:Boolean, val data:List<Recommendation>)

    data class Recommendation(val _id: String, val title: String, val content:String,val user:String, val imageUrl:String)