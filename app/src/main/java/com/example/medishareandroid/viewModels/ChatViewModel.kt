package com.example.medishareandroid.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.chat.ChatDiscussion
import com.example.medishareandroid.models.chat.ChatResponse
import com.example.medishareandroid.models.chat.CreateDiscussionMessage
import com.example.medishareandroid.models.chat.DiscussionResponse
import com.example.medishareandroid.models.chat.MsgDouble
import com.example.medishareandroid.remote.ChatApi
import com.example.medishareandroid.remote.RetrofitInstance

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel : ViewModel() {
    val api = RetrofitInstance.getRetrofit().create(ChatApi::class.java)
    private val _chatResponse = MutableLiveData<ChatResponse>()
    val chatResponse: LiveData<ChatResponse> get() = _chatResponse


    fun createChat(
        message: String,
        context: Context,
        userId: String,
    ) {
        Log.d("requet xreate chatt",message+"/"+userId)
        // Retrofit call to change the password
        val createDiscussion = api.createDiscussion(userId = userId, message = CreateDiscussionMessage(message))
        Log.d("message requet xreate chatt",createDiscussion.toString())
        createDiscussion.enqueue(object : Callback<ChatResponse> {

            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                //Log.d("message resposne xreate chatt", response.body()!!.aiResponse)
                Log.d("Request URL", call.request().url.toString())
                Log.d("Request body", call.request().body.toString())

                Log.d("Response Code", response.code().toString())
                Log.d("Response Body", response.body()?.toString() ?: "Null Body")
                Log.d("Error Body", response.errorBody()?.string() ?: "No Error Body")
                if (response.isSuccessful) {

                    response.body()?.let { body ->
                        _chatResponse.postValue(body) // Update LiveData with the dynamic response
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    Log.d("SettingsViewModel", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                Log.d("message resposne xreate chatt3", t.message.toString())
                Log.d("message resposne xreate chatt4", call.toString())


                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d("SettingsViewModel", "Failed to update password", t)
            }
        })

    }


    fun sendMsg(
        message: String,
        id: String,
        context: Context,
        userId: String,
    ) {

        // Retrofit call to change the password

        val addMsg = api.addMessage(userId = userId, id, message)
        addMsg.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {

                    response.body()?.let { body ->
                        _chatResponse.postValue(body) // Update LiveData with the dynamic response
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    Log.d("SettingsViewModel", errorMsg)
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d("SettingsViewModel", "Failed to update password", t)
            }
        })

    }
    private val _chatMessages = MutableLiveData<List<MsgDouble>>()
    val chatMessages: LiveData<List<MsgDouble>> get() = _chatMessages
    private val _discussionResponse = MutableLiveData<DiscussionResponse>()
    val discussionResponse: LiveData<DiscussionResponse> get() = _discussionResponse
    private val _currentDiscussionId = MutableLiveData<String>()
    val discussionId: LiveData<String> get() = _currentDiscussionId
    fun getDiscussion(
        id: String,
        context: Context,
        userId: String,
    ) {
        val getDisc = api.getDiscussion(userId = userId, discussionId=id)

        getDisc.enqueue(object : Callback<DiscussionResponse> {
            override fun onResponse(call: Call<DiscussionResponse>, response: Response<DiscussionResponse>) {
                if (response.isSuccessful) {

                    response.body()?.let { body ->
                        _chatMessages.postValue(body.messages) // Update LiveData with the dynamic response
                        _currentDiscussionId.postValue(body._id)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    Log.d("SettingsViewModel", errorMsg)
                }
            }

            override fun onFailure(call: Call<DiscussionResponse>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d("SettingsViewModel", "Failed to update password", t)
            }
        })

    }
}