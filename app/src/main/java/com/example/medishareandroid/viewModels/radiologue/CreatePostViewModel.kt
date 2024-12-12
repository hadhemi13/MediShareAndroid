package com.example.medishareandroid.viewModels.radiologue

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.radiologue.ImageResponse
import com.example.medishareandroid.models.radiologue.PostRequest
import com.example.medishareandroid.remote.Post
import com.example.medishareandroid.remote.PostsRequests
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePostViewModel : ViewModel() {

    private val api = RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)

    fun createPost(
        title: String,
        imageId: String,
        content: String,
        userid: String,
        subreddit: String
    ) {
        Log.d("tteste1", "____________homefetchPosts")

        api.createPost(PostRequest(title,imageId,content, userid, subreddit)).enqueue(object : Callback<Post> {
            override fun onResponse(
                call: Call<Post>,
                response: Response<Post>
            ) {
                if (response.isSuccessful) {
                    Log.d("tteste1", "____________1")

                } else {
                    Log.d("tteste1", "____________2")

                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {

                Log.d("tteste1", "____________2")

            }
        })
    }


}