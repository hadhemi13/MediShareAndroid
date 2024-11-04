package com.example.medishareandroid

import LaunchScreenContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import com.example.medishareandroid.user.Signup
import com.example.medishareandroid.user.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediSHareAndroidTheme {
                val navController = rememberNavController() // Initialize NavController

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "signup" // Pas d'espaces ici
                    ) { // Définissez le NavHost
                        composable("launchScreen") {
                            LaunchScreenContent(navController = navController) // Appeler la fonction composable LaunchScreenContent
                        }
                        composable("signup") {
                            Signup(
                                navController = navController, modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(16.dp)
                            )
                        }
                        composable("loginScreen") {
                            LoginScreen() // Remplacez par votre écran de connexion
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MediSHareAndroidTheme {
        LoginScreen()
    }
}
