package com.example.medishareandroid.viewModels.radiologue

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UploadFileRes
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadFileViewModel:ViewModel() {



    fun uploadFileImage(filePath: String, imgTitle : String, userId: String, context: Context) {
        val file = File(filePath)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Create RequestBody for userId (to send as plain text)
        val userIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)
        val imgTitledRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), imgTitle)
        // Ensure the file exists
        if (!file.exists()) {
            Log.d("spotcha33", filePath)
            Toast.makeText(context, "File not found: $filePath", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)
            .uploadFile(multipartBody, userIdRequestBody, imgTitledRequestBody)  // pass the userId as RequestBody
            .enqueue(object : Callback<UploadFileRes> {
                override fun onResponse(call: Call<UploadFileRes>, response: Response<UploadFileRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Upload failed: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UploadFileRes>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }




}