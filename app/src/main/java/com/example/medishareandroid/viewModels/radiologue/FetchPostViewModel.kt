package com.example.medishareandroid.viewModels.radiologue

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medishareandroid.remote.OCRResponse
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.Post
import com.example.medishareandroid.remote.PostsRequests
import com.example.medishareandroid.remote.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchPostViewModel : ViewModel() {

    private val api = RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)
    //var posts = mutableStateOf<List<Post>>(emptyList())
    val _postsResponse = MutableLiveData<List<Post>>()

    val postsResponse: LiveData<List<Post>> get() = _postsResponse

    fun fetchPosts(userId: String) {
        Log.d("tteste1", "____________homefetchPosts")

            api.fetchAllPosts(PostsRequests(userId)).enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        _postsResponse.value = response.body() ?: emptyList()
                        Log.d("tteste1", "____________1")

                        Log.d("FetchPostViewModel2", _postsResponse.value.toString())
                    } else {
                        // Handle the error
                        Log.d("tteste1", "____________2")

                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    // Handle failure
                    Log.d("tteste1", "____________2")

                }
            })
        }

}
