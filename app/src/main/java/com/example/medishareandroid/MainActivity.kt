package com.example.medishareandroid

import AuthScreen
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import com.example.medishareandroid.views.Signup


import com.example.medishareandroid.views.ForgotPasswordScreen
import com.example.medishareandroid.views.LoginScreen
import com.example.medishareandroid.views.NewPassword
import com.example.medishareandroid.views.RecoveryCodeSceen
import com.example.medishareandroid.viewModels.AuthViewModel
import com.example.medishareandroid.viewModels.AuthViewModelFactory
import com.example.medishareandroid.views.EditProfileScreen
import com.example.medishareandroid.views.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferencesRepository = PreferencesRepository(this)

        enableEdgeToEdge()
        setContent {
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(preferencesRepository)
            )
            MediSHareAndroidTheme {
                val navController = rememberNavController() // Initialize NavController
                val showToolbar = remember { mutableStateOf(true) }
                val context = LocalContext.current
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
                            AuthScreen(viewModel, navController) // Appeler la fonction composable LaunchScreenContent
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
                                .padding(16.dp) ){viewModel.loginUser()}
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
                        composable("homePage") {
                            HomeScreen(navController
                               /* navController = navController, modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(16.dp)*/
                            )
                        }
                        composable("editProfileScreen"){
                            EditProfileScreen(context, navController)
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
            val context = LocalContext.current

            val preferencesRepository = PreferencesRepository(context)
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(preferencesRepository)
            )
            LoginScreen(navController){viewModel.loginUser()}

        }

    }
}
