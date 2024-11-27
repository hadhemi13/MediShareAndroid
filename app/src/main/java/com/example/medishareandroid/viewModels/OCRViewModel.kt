package com.example.medishareandroid.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medishareandroid.R
import com.example.medishareandroid.remote.GetOCRReq
import com.example.medishareandroid.remote.Message
import com.example.medishareandroid.remote.OCR1Response
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
            .enqueue(object : Callback<OCR1Response> {
                override fun onResponse(call: Call<OCR1Response>, response: Response<OCR1Response>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
                        // Handle the response if needed
                    } else {
                        Toast.makeText(context, "Upload failed: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<OCR1Response>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    // LiveData for OCR responses
    private val _ocrResponses = MutableLiveData<List<OCRResponse>>()
    val ocrResponses: LiveData<List<OCRResponse>> get() = _ocrResponses

    // LiveData for errors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Function to fetch all images from the API
    fun fetchAllImages(userId: String,context : Context) {
        // Make sure the userId is not empty
        if (userId.isEmpty()) {
            _error.postValue("User ID is empty.")
            return
        }

        // Set loading state to true
        _isLoading.postValue(true)

        // Show loading or perform any necessary pre-call actions
        _error.postValue("Loading...")  // Example message to show a loading state

        // Create request body
        val getOCRReq = GetOCRReq(userId)

        // Make the API call using Retrofit
        api.getAllImages(getOCRReq).enqueue(object : Callback<List<OCRResponse>> {
            override fun onResponse(call: Call<List<OCRResponse>>, response: Response<List<OCRResponse>>) {
                // Set loading state to false after the API response
                _isLoading.postValue(false)


                //Toast.makeText(context, "Upload failed: ${response}", Toast.LENGTH_LONG).show()

                if (response.isSuccessful) {
                    // If successful, update the LiveData with the response data
                    response.body()?.let {
                        if (it.isNotEmpty()) {
                            _ocrResponses.postValue(it)  // Update the LiveData with the OCR responses
                            _error.postValue("")  // Clear any error message
                        } else {
                            _error.postValue("No images found.")
                        }
                    }
                } else {
                    // Handle API response failure
                    _error.postValue("Failed to fetch images: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<OCRResponse>>, t: Throwable) {
                // Set loading state to false after the failure
                _isLoading.postValue(false)

                // Handle network failure or exception
                _error.postValue("Error fetching images: ${t.message}")
            }
        })
    }
}

