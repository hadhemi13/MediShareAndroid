package com.example.medishareandroid.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medishareandroid.R
import com.example.medishareandroid.remote.Message
import com.example.medishareandroid.remote.OCRReq
import com.example.medishareandroid.remote.OCRResponse
import com.example.medishareandroid.remote.OcrAPI
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.StatusCode
import com.example.medishareandroid.repositories.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class OCRViewModel : ViewModel() {


    val api = RetrofitInstance.getRetrofit().create(OcrAPI::class.java)
    fun analyzeImage(imageName: String, onResult: (Map<String, Any>?) -> Unit) {
        viewModelScope.launch {
            val response = api.analyzeImage(imageName)
            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }
    }

   /* fun uploadFile(filePath: String, userId: String, context: Context) {
        val prefs = PreferencesRepository(context)
        val file = File(filePath)
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        RetrofitInstance.getRetrofit().create(OcrAPI::class.java)
            .uploadFile(OCRReq(file, prefs.getId().toString()))
            .enqueue(object : Callback<OCRResponse> {
                override fun onResponse(call: Call<OCRResponse>, response: Response<OCRResponse>) {
                    if (response.isSuccessful) {
                        // Get the message from the response body
                        val message = response.body()?.message ?: "Unexpected response"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        //  navController.navigate("forgotPasswordMail")
                    } else {
                        // Handle error message
                        val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                        Log.d("1", errorMsg)
                    }
                }

                override fun onFailure(call: Call<OCRResponse>, t: Throwable) {
                    Toast.makeText(context, t.message ?: "Network failure", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            )
    }
*/
   fun uploadFile(filePath: String, userId: String, context: Context) {
       // Ensure the file exists
       val file = File(filePath)
       if (!file.exists()) {
           Log.d("spotcha33", filePath)
           Toast.makeText(context, "File not found: $filePath", Toast.LENGTH_SHORT).show()
           return
       }

       // Create RequestBody for the file
       val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
       val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

       // Create RequestBody for userId (to send as plain text)
       val userIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)

       // Make the API call
       RetrofitInstance.getRetrofit().create(OcrAPI::class.java)
           .uploadFile(multipartBody, userIdRequestBody)  // pass the userId as RequestBody
           .enqueue(object : Callback<OCRResponse> {
               override fun onResponse(call: Call<OCRResponse>, response: Response<OCRResponse>) {
                   if (response.isSuccessful) {
                       Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
                       // Handle the response if needed
                   } else {
                       Toast.makeText(context, "Upload failed: ${response.message()}", Toast.LENGTH_LONG).show()
                   }
               }

               override fun onFailure(call: Call<OCRResponse>, t: Throwable) {
                   Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
               }
           })
   }

    /*suspend fun uploadFile(filePath: String, userId: String, context: Context) {
       val prefs = PreferencesRepository(context)
       val file = File(filePath)

       // Prepare file and userId for multipart request
       val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
       val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
       val userIdBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), prefs.getId().toString())

       try {
           val response = withContext(Dispatchers.IO) {
               RetrofitInstance.getRetrofit()
                   .create(OcrAPI::class.java)
                   .uploadFile(body, userIdBody)
           }

           // Handle success
           Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
       } catch (e: Exception) {
           // Handle failure
           Toast.makeText(context, e.message ?: "An error occurred", Toast.LENGTH_SHORT).show()
           Log.e("UploadFile", "Error: ${e.message}", e)
       }
   }*/
    fun getAllImages(userId: String, onResult: (List<Map<String, Any>>?) -> Unit) {
        viewModelScope.launch {
            val response = api.getAllImages(mapOf("id" to userId))
            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }
    }

    fun getImageDetails(imageId: String, onResult: (Map<String, Any>?) -> Unit) {
        viewModelScope.launch {
            val response = api.getImageDetails(mapOf("id" to imageId))
            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }
    }
}
