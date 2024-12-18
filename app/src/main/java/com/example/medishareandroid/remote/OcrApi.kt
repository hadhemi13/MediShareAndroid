package com.example.medishareandroid.remote

import android.os.Parcelable
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
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


interface OcrAPI {
    @GET("files")
    fun analyzeImage(@Query("imageName") imageName: String): Response<Map<String, Any>>

    @Multipart
    @POST("files/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody // Ensure userId is passed as a RequestBody
    ): Call<OCR1Response>



    @GET("qr/{userId}")
    fun getQrCode(@Path("userId") userId: String): Call<QrResponse>

    @POST("files/getAllImages")
    fun getAllImages(@Body getOCRReq: GetOCRReq): Call<List<OCRResponse>>


    @POST("files/getImageDetails")
    fun getOcrById(@Body ocrById: GetOcrByIdRequest): Call<Map<String, Any>>


}

data class GetOcrByIdRequest(val id: String)


data class QrResponse(val qrCode: String)


data class OCR1Response(
    val message: String,
    val filePath: String
)



@Parcelize
data class OCRResponse(
    val _id: String,
    val description: String,
    val userId: String,
    val error: String? = null,
    val details: String? = null,
    val image_name: String,
    val patient_name: String? = null,
    val date: String? = null,
    val prescription: @RawValue List<PrescriptionItem>? = null,
    val doctor: String? = null,
    val doctor_title: String? = null,
    val patient: @RawValue Patient? = null,
    val prescription_title: String? = null,
    val items: @RawValue List<PrescriptionItem>? = null,
    val tabs: @RawValue List<Tab>? = null,
    val title: String? = null,
    val __v: Int
) : Parcelable

data class PrescriptionItem(
    val activity: String,
    val frequency: String,
    val location: String? = null
)

data class Patient(val name: String, val date: String)

data class Tab(val name: String)

data class GetOCRReq(val id: String)

