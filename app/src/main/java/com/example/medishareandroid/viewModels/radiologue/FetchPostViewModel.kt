package com.example.medishareandroid.viewModels.radiologue

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.radiologue.Comment
import com.example.medishareandroid.models.radiologue.DisplayingPosts
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.Post
import com.example.medishareandroid.remote.PostsRequests
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UpvotesReq
import com.example.medishareandroid.remote.UpvotesRes
import com.example.medishareandroid.repositories.PreferencesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
class FetchPostViewModel : ViewModel() {

    private val api = RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)

    //var posts = mutableStateOf<List<Post>>(emptyList())
    val _postsResponse = MutableLiveData<List<Post>>()
    //val postsResponse: LiveData<List<Post>> get() = _postsResponse


    val _commentsResponse = MutableLiveData<List<Comment>>()
    //val commentsResponse: LiveData<List<Comment>> get() = _commentsResponse

    val _displayingPosts = MutableLiveData<List<DisplayingPosts>>()
    val displayingPosts: LiveData<List<DisplayingPosts>> get() = _displayingPosts
    fun fetchPosts(userId: String) {
        Log.d("tteste1", "____________homefetchPosts")

        api.fetchAllPosts(PostsRequests(userId)).enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    _postsResponse.value = response.body() ?: emptyList()
                    Log.d("tteste1", "____________1")
                    fetchComments {
                        val posts = _postsResponse.value ?: emptyList()



                        val comments = _commentsResponse.value ?: emptyList()

                        // Map each post to its associated comments
                        val displayingPostsList = posts.map { post ->
                            val relatedComments = comments.filter { comment -> comment.post == post.id }
                            post.timeAgo = getTimeAgo(post.timeAgo)
                            DisplayingPosts(post, relatedComments)
                        }

                        _displayingPosts.value = displayingPostsList
                        Log.d("FetchPostViewModel", _displayingPosts.value.toString())
                    }


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

    @SuppressLint("SimpleDateFormat")
    fun getTimeAgo(dateStr: String): String {
        val inputFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)")
        inputFormat.timeZone = TimeZone.getTimeZone("GMT")

        return try {
            val date = inputFormat.parse(dateStr)
            val currentDate = Date()
            val diff = currentDate.time - date.time

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            when {
                minutes < 1 -> "Just now"
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                else -> "$days days ago"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            "Unknown time"
        }
    }

    fun fetchComments(onComplete: () -> Unit){
        api.getComment().enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    _commentsResponse.value = response.body() ?: emptyList()
                    //Log.d("tteste1", "____________1")
                    onComplete()
                    //Log.d("FetchPostViewModel2", _commentsResponse.value.toString())
                } else {
                    // Handle the error
                    //Log.d("tteste1", "____________2")

                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                // Handle failure
                //Log.d("tteste1", "____________2")

            }
        })

    }
    fun setUpvotes(postId:String, userId: String) :Boolean{
        Log.d("1111","11111111555555551")

        var result =false
        api.setUpvotes(UpvotesReq(postId,userId)).enqueue(object : Callback<UpvotesRes> {
            override fun onResponse(call: Call<UpvotesRes>, response: Response<UpvotesRes>) {
                Log.d("1111","11111111555555551")

                if (response.isSuccessful) {
                    //_commentsResponse.value = response.body() ?: emptyList()
                    //Log.d("tteste1", "____________1")
                    result = response.body()!!.success
                    //Log.d("FetchPostViewModel2", _commentsResponse.value.toString())

                    // Handle the error
                    //Log.d("tteste1", "____________2")

                }
            }

            override fun onFailure(call: Call<UpvotesRes>, t: Throwable) {
                // Handle failure
                //Log.d("tteste1", "____________2")


            }
        })
        return result


    }

}
