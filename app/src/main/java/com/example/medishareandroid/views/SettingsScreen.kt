package com.example.medishareandroid.views


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme


@Composable
fun SettingsScreen(navController: NavController, userPreferences: PreferencesRepository,navController2: NavController) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings and Activity",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Update Password Button
        Button(
            onClick = { navController.navigate("changePassword") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.sign)),

            ) {
            Text(text = "Update Password")
        }

        // Logout Button
        Button(
            onClick = {
                userPreferences.clearLoginData() // Logout logic
                navController2.navigate("login") // Navigate to login screen
                Toast.makeText(navController2.context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.sign)),

            ) {
            Text(text = "Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    MediSHareAndroidTheme {
        SettingsScreen(navController = navController, userPreferences = PreferencesRepository(context),
            rememberNavController()
        )
    }

}
