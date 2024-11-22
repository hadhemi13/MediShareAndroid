package com.example.medishareandroid.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
//import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File

interface OcrAPI{
    @GET("files")
    fun analyzeImage(@Query("imageName") imageName: String): Response<Map<String, Any>>

    @Multipart
    @POST("files/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody // Ensure userId is passed as a RequestBody
    ): Call<OCRResponse>
   /* @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody
    ): OCRResponse

*/
    @POST("files/getAllImages")
    suspend fun getAllImages(@Body userId: Map<String, String>): Response<List<Map<String, Any>>>

    @POST("files/getImageDetails")
    suspend fun getImageDetails(@Body imageId: Map<String, String>): Response<Map<String, Any>>
}



data class OCRResponse(
    val message: String,
    val filePath: String
   // val extractedData: Map<String, Any>? = null
)
data class OCRReq(
    val file:File,
    val userId:String
)
