package com.example.medishareandroid.viewModels

import androidx.lifecycle.ViewModel
import com.example.medishareandroid.R
import com.example.medishareandroid.models.Visit

class ProfileViewModel : ViewModel() {
    val username: String = "Username"
    val bio: String = "This is a bio for the user."

    val visits: List<Visit> = listOf(
       /* Visit(R.drawable.visit1),
        Visit(R.drawable.visit2),
        Visit(R.drawable.visit3),*/
        // Ajoutez d’autres visites ici
    )
}
