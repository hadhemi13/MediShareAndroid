package com.example.medishareandroid.viewModels.radiologue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medishareandroid.models.radiologue.Prediction
import com.example.medishareandroid.models.radiologue.TumorDetectionRequest
import com.example.medishareandroid.models.radiologue.TumorDetectionResponse
import com.example.medishareandroid.remote.RadiologueApi
import com.example.medishareandroid.remote.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TumorDetectionViewModel : ViewModel() {
    private val api = RetrofitInstance.getRetrofit().create(RadiologueApi::class.java)

    private val _inferenceId = MutableLiveData<String>()
    val inferenceId: LiveData<String> get() = _inferenceId

    private val _predictions = MutableLiveData<List<Prediction>>()
    val predictions: LiveData<List<Prediction>> get() = _predictions

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _alertMessage = MutableLiveData<String>()
    val alertMessage: LiveData<String> get() = _alertMessage

    fun performTumorDetection(imagePath: String) {
        _isLoading.value = true
        api.detectTumor(TumorDetectionRequest(imagePath))
            .enqueue(object : Callback<TumorDetectionResponse> {
                override fun onResponse(
                    call: Call<TumorDetectionResponse>,
                    response: Response<TumorDetectionResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            _inferenceId.value = body.inference_id
                            _predictions.value = body.predictions

                            // Set the alert message based on the first prediction's confidence
                            body.predictions.firstOrNull()?.let { firstPrediction ->
                                _alertMessage.value = "Confidence: ${firstPrediction.confidence}%"
                            } ?: run {
                                _alertMessage.value = "Confidence: 0%"
                            }
                        } ?: run {
                            _error.value = "Empty response body"
                        }
                    } else {
                        _error.value = "Failed to detect tumor: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<TumorDetectionResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = "Error: ${t.localizedMessage}"
                }
            })
    }
}
