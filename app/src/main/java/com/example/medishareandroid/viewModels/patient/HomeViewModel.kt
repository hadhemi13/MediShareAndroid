package com.example.medishareandroid.viewModels.patient

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medishareandroid.remote.RecReq
import com.example.medishareandroid.remote.Recommendation
import com.example.medishareandroid.remote.RecommendationApi
import com.example.medishareandroid.remote.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _userName = "000000"
    val userName: String get() = _userName

    private val _clinics = listOf(
        Clinic("Clinic A", "Cardiology", "08:00 AM ","- 05:00 PM"),
        Clinic("Clinic B", "Neurology", "09:00 AM ","- 06:00 PM")
    )
    val clinics: List<Clinic> get() = _clinics
    private val _recommendations = mutableStateOf<List<Recommendation>>(emptyList())
    val recommendations: State<List<Recommendation>> = _recommendations

    fun fetchRecommendations(userId: String) {
        viewModelScope.launch {
            try {
                val api = RetrofitInstance.getRetrofit().create(RecommendationApi::class.java)
                val response = api.fetchRecommendations(RecReq(userId))
               // _recommendations.value = response.recommendations
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
data class Clinic(
    val name: String,
    val specialization: String,
    val openHour: String,
    val closeHour: String
)