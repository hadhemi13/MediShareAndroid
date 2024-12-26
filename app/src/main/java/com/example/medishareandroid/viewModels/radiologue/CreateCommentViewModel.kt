package com.example.medishareandroid.viewModels.radiologue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.radiologue.Comment
import com.example.medishareandroid.models.radiologue.CommentRequest
import com.example.medishareandroid.remote.Post
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateCommentViewModel : ViewModel() {
    private val api = RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)
    val _result = MutableLiveData<Comment>()
    val result: LiveData<Comment> get() = _result

    // LiveData for loading state (true when the comment is being created)
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData for error handling
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun createComment(
        postId: String,
        userId: String,
        comment: String
    ) {
        _isLoading.value = true
        api.createComment(CommentRequest(postId, comment, userId))
            .enqueue(object : Callback<Comment> {
                override fun onResponse(
                    call: Call<Comment>,
                    response: Response<Comment>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _result.value = it
                        } ?: run {
                            _error.value = "Empty response body"
                        }
                    } else {
                        _error.value = "Failed to create comment: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = "Error to create comment: ${t.localizedMessage}"
                }
            })
    }
}