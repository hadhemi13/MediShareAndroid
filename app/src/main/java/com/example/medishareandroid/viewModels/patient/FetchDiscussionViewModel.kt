package com.example.medishareandroid.viewModels.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medishareandroid.remote.ChatApi
import com.example.medishareandroid.remote.DiscussionRes
import com.example.medishareandroid.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchDiscussionViewModel() : ViewModel() {
    val api = RetrofitInstance.getRetrofit().create(ChatApi::class.java)
    private val _discussions = MutableLiveData<List<DiscussionRes>>()
    val discussions: LiveData<List<DiscussionRes>> get() = _discussions

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchDiscussions(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            api.getAllDiscussions(userId).enqueue(object : Callback<List<DiscussionRes>> {
                override fun onResponse(
                    call: Call<List<DiscussionRes>>,
                    response: Response<List<DiscussionRes>>
                ) {
                    if (response.isSuccessful) {
                        _discussions.postValue(response.body() ?: emptyList())
                    } else {
                        _errorMessage.postValue("Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<DiscussionRes>>, t: Throwable) {
                    _errorMessage.postValue("Failure: ${t.message}")
                }
            })
        }
    }
}
