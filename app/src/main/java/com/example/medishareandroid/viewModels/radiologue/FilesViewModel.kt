package com.example.medishareandroid.viewModels.radiologue

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.radiologue.ImageResponse
import com.example.medishareandroid.remote.PostsRequests
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.RetrofitInstance

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilesViewModel : ViewModel() {

    private val api = RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)
    val _imagesResponse = MutableLiveData<List<ImageResponse>>()

    val imagesResponse: LiveData<List<ImageResponse>> get() = _imagesResponse


    fun fetchImages(userId: String) {
        Log.d("tteste1", "____________homefetchPosts")

        api.getAllImages(PostsRequests(userId)).enqueue(object : Callback<List<ImageResponse>> {
            override fun onResponse(call: Call<List<ImageResponse>>, response: Response<List<ImageResponse>>) {
                if (response.isSuccessful) {
                    _imagesResponse.value = response.body() ?: emptyList()
                    Log.d("tteste1", "____________1")

                    Log.d("FetchPostViewModel2", _imagesResponse.value.toString())
                } else {
                    Log.d("tteste1", "____________2")

                }
            }

            override fun onFailure(call: Call<List<ImageResponse>>, t: Throwable) {

                Log.d("tteste1", "____________2")

            }
        })
    }


}
