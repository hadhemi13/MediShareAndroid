package com.example.medishareandroid.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface OcrAPI {
    @GET("files")
    fun analyzeImage(@Query("imageName") imageName: String): Response<Map<String, Any>>

    @Multipart
    @POST("files/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody // Ensure userId is passed as a RequestBody
    ): Call<OCR1Response>
    /* @Multipart
     @POST("upload")
     suspend fun uploadFile(
         @Part file: MultipartBody.Part,
         @Part("userId") userId: RequestBody
     ): OCRResponse

 */


    @GET("qr/{userId}")
    fun getQrCode(@Path("userId") userId: String): Call<QrResponse>
    @POST("files/getAllImages")
    fun getAllImages(@Body getOCRReq: GetOCRReq): Call<List<OCRResponse>>






    @POST("files/getImageDetails")
    suspend fun getImageDetails(@Body imageId: Map<String, String>): Response<Map<String, Any>>



    }

data class QrResponse(val qrCode: String)


data class OCR1Response(
    val message: String,
    val filePath: String
    // val extractedData: Map<String, Any>? = null
)

data class OCR1Req(
    val file: File,
    val userId: String
)


data class OCRResponse(
    val _id: String,
    val userId: String,
    val error: String? = null,
    val details: String? = null,
    val image_name: String,
    val patient_name: String? = null,
    val date: String? = null,
    val prescription: List<PrescriptionItem>? = null,
    val doctor: String? = null,
    val doctor_title: String? = null,
    val patient: Patient? = null,
    val prescription_title: String? = null,
    val items: List<PrescriptionItem>? = null,
    val tabs: List<Tab>? = null,
    val title: String? = null,
    val __v: Int
)

data class PrescriptionItem(
    val activity: String,
    val frequency: String,
    val location: String? = null
)

data class Patient(
    val name: String,
    val date: String
)

data class Tab(
    val name: String
)

data class GetOCRReq(
    val id: String
)
