package com.example.medishareandroid.viewModels.patient

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.chat.ChatResponse
import com.example.medishareandroid.models.chat.CreateDiscussionMessage
import com.example.medishareandroid.models.chat.DiscussionResponse
import com.example.medishareandroid.models.chat.MsgDouble
import com.example.medishareandroid.remote.ChatApi
import com.example.medishareandroid.remote.MessageReq
import com.example.medishareandroid.remote.RetrofitInstance

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ChatViewModel : ViewModel() {
    private val api = RetrofitInstance.getRetrofit().create(ChatApi::class.java)

    private val _chatResponse = MutableLiveData<ChatResponse>()
    val chatResponse: LiveData<ChatResponse> get() = _chatResponse

    private val _chatMessages = MutableLiveData<List<MsgDouble>>()
    val chatMessages: LiveData<List<MsgDouble>> get() = _chatMessages

    private val _discussionResponse = MutableLiveData<DiscussionResponse>()
    val discussionResponse: LiveData<DiscussionResponse> get() = _discussionResponse

    private val _currentDiscussionId = MutableLiveData<String>()
    val discussionId: LiveData<String> get() = _currentDiscussionId

    // Helper function to show Toast messages
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Create Chat
    fun createChat(message: String, context: Context, userId: String) {
        val createDiscussion = api.createDiscussion(userId = userId, message = CreateDiscussionMessage(message))
        createDiscussion.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _chatResponse.postValue(body)
                        // Refresh messages after chat creation
                        body.id?.let { getDiscussion(it, context, userId) }
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    showToast(context, errorMsg)
                    Log.e("ChatViewModel", errorMsg)
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                showToast(context, t.message ?: "Request failed")
                Log.e("ChatViewModel", "Chat creation failed", t)
            }
        })
    }

    // Send Message
    fun sendMsg(message: String, id: String, context: Context, userId: String) {
        val addMsg = api.addMessage(userId = userId, id, MessageReq( message))
        addMsg.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _chatResponse.postValue(body)
                        // Refresh messages after sending message
                        getDiscussion(id, context, userId)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    showToast(context, errorMsg)
                    Log.e("ChatViewModel", errorMsg)
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                showToast(context, t.message ?: "Request failed")
                Log.e("ChatViewModel", "Message sending failed", t)
            }
        })
    }

    // Fetch Discussion
    fun getDiscussion(id: String, context: Context, userId: String) {
        val getDisc = api.getDiscussion(userId = userId, discussionId = id)
        getDisc.enqueue(object : Callback<DiscussionResponse> {
            override fun onResponse(call: Call<DiscussionResponse>, response: Response<DiscussionResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _chatMessages.postValue(body.messages)
                        _currentDiscussionId.postValue(body._id)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    showToast(context, errorMsg)
                    Log.e("ChatViewModel", errorMsg)
                }
            }

            override fun onFailure(call: Call<DiscussionResponse>, t: Throwable) {
                showToast(context, t.message ?: "Request failed")
                Log.e("ChatViewModel", "Fetching discussion failed", t)
            }
        })
    }
}
