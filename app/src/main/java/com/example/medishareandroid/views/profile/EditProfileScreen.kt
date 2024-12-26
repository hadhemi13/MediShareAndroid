package com.example.medishareandroid.views.profile
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.models.User
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import com.example.medishareandroid.remote.EditProfileDto
import com.example.medishareandroid.repositories.PreferencesRepository


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EditProfileScreen(
    context: Context,
    navController: NavHostController
) {
    val preferencesRepository = PreferencesRepository(context)
    val userAPI = RetrofitInstance.getRetrofit().create(UserAPI::class.java)

    var name by remember { mutableStateOf(preferencesRepository.getName() ?: "") }
    var email by remember { mutableStateOf(preferencesRepository.getEmail() ?: "") }
    val userId = preferencesRepository.getId()

    Column (
        modifier = Modifier
            .fillMaxSize()

            .padding(16.dp)
    ) {
        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Champ de texte pour le nom
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Champ de texte pour l'email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour sauvegarder les modifications
        Button (
            onClick = {
                if (userId != null) {
                    val editProfileDto = EditProfileDto(name, email)
                    updateUserProfile(context, userAPI, editProfileDto, userId) {
                        preferencesRepository.setName(name)
                        preferencesRepository.setEmail(email)
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // Retour à la page précédente
                    }
                } else {
                    Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.sign)),

        ) {
            Text("Save Changes")
        }
    }
}

fun updateUserProfile(
    context: Context,
    userAPI: UserAPI,
    editProfileDto: EditProfileDto,
    userId:String,
    onSuccess: () -> Unit
) {
    userAPI.editProfile(userId, editProfileDto).enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful) {
                onSuccess()
            } else {
                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview(){
    val context= LocalContext.current
    val navController = rememberNavController()
    EditProfileScreen(context, navController)
}