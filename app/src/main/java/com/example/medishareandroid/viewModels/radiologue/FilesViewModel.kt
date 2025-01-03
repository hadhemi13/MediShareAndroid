package com.example.medishareandroid.viewModels.radiologue

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.radiologue.ImageRequest
import com.example.medishareandroid.models.radiologue.ImageResponse
import com.example.medishareandroid.models.radiologue.ImageResponseWrapper
import com.example.medishareandroid.remote.PostsRequests
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.RetrofitInstance

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilesViewModel : ViewModel() {

    private val api = RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)
    private val _imagesResponse = MutableLiveData<List<ImageResponse>>()
    val imagesResponse: LiveData<List<ImageResponse>> get() = _imagesResponse

    fun fetchImages(userId: String, patientId: String) {
        val imageRequests = ImageRequest(userId, patientId)

        api.getImagesByPatient(imageRequests).enqueue(object : Callback<ImageResponseWrapper> {
            override fun onResponse(call: Call<ImageResponseWrapper>, response: Response<ImageResponseWrapper>) {
                if (response.isSuccessful) {
                    _imagesResponse.value = response.body()?.data ?: emptyList()
                    Log.d("FilesViewModel", "Images fetched: ${_imagesResponse.value}")
                } else {
                    Log.d("FilesViewModel", "Failed to fetch images")
                }
            }

            override fun onFailure(call: Call<ImageResponseWrapper>, t: Throwable) {
                Log.d("FilesViewModel", "Error: ${t.message}")
            }
        })
    }

}
