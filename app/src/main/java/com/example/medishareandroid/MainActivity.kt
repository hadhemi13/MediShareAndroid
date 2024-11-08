package com.example.medishareandroid

import LaunchScreenContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import com.example.medishareandroid.user.Signup


import com.example.medishareandroid.user.ForgotPasswordScreen
import com.example.medishareandroid.user.LoginScreen
import com.example.medishareandroid.user.NewPassword
import com.example.medishareandroid.user.RecoveryCodeSceen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediSHareAndroidTheme {
                val navController = rememberNavController() // Initialize NavController
                val showToolbar = remember { mutableStateOf(true) }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "launchScreen" // Pas d'espaces ici
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
                        composable(route = "login") {
                            showToolbar.value = false
                            LoginScreen(navController, modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(16.dp) )
                        }
                        composable(route = "forgotPassword") {
                            showToolbar.value = true
                            ForgotPasswordScreen(navController, modifier = Modifier)
                        }
                        composable("forgotPasswordMail") {
                            RecoveryCodeSceen(navController = navController) // Appeler la fonction composable LaunchScreenContent
                        }
                        composable("newPassword/{resetToken}") { backStackEntry ->
                            NewPassword(navController = navController, resetToken = backStackEntry.arguments?.getString("resetToken")) // Appeler la fonction composable LaunchScreenContent
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

        Column  {
            val navController = rememberNavController()

            LoginScreen(navController)

        }

    }
}
