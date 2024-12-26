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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import com.example.medishareandroid.views.authScreens.Signup


import com.example.medishareandroid.views.authScreens.ForgotPasswordScreen
import com.example.medishareandroid.views.authScreens.LoginScreen
import com.example.medishareandroid.views.authScreens.NewPassword
import com.example.medishareandroid.views.authScreens.RecoveryCodeSceen
import com.example.medishareandroid.viewModels.auth.AuthViewModel
import com.example.medishareandroid.viewModels.auth.AuthViewModelFactory
import com.example.medishareandroid.views.profile.ChangePasswordScreen
import com.example.medishareandroid.views.profile.EditProfileScreen
import com.example.medishareandroid.views.patient.ExactDesignScreen
import com.example.medishareandroid.views.patient.FolderScreen
import com.example.medishareandroid.views.patient.HomeScreen
import com.example.medishareandroid.views.patient.OCRScreen
import com.example.medishareandroid.views.patient.OcrItemScreen
import com.example.medishareandroid.views.patient.RecommandationScreen
import com.example.medishareandroid.views.patient.ScreenContent
import com.example.medishareandroid.views.radiologue.RadiologueNavController

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
                        composable("changePassword") {
                            ChangePasswordScreen(navController, preferencesRepository)
                        }

                        composable("home") { ExactDesignScreen(navController) }
                        composable("search") { ScreenContent() }
                        // composable("profile") { ProfileScreen(viewModel, navController) }
                        composable("editProfileScreen") {
                            EditProfileScreen(context, navController)
                        }
                     
                        composable("changePassword") {
                            ChangePasswordScreen(navController, preferencesRepository)
                        }
                        composable(
                            route = "ocr_screen?filePath={filePath}&imageUri={imageUri}",
                            arguments = listOf(
                                navArgument("filePath") { type = NavType.StringType },
                                navArgument("imageUri") { type = NavType.StringType }
                            )
                        ) {
                            // Pass arguments to the composable
                            OCRScreen("",""
                                //   uploadFilePath1 = it.arguments?.getString("filePath") ?: "",
                                // imageUri1 = it.arguments?.getString("imageUri") ?: ""
                            )
                        }
                        composable("folder") {
                            FolderScreen(navController, modifier = Modifier.padding(innerPadding))
                        }
                        composable("ocrItemScreen/{imageName}/{title}/{description}") { backStackEntry ->
                            // Retrieve the imageName argument
                            val imageName = backStackEntry.arguments?.getString("imageName") ?: "No Image"
                            val title = backStackEntry.arguments?.getString("title") ?: "No title"
                            val description = backStackEntry.arguments?.getString("description") ?: "No title"

                            OcrItemScreen(id="")
                        }
                        composable("recommendationItem/{title}/{desc}") { backStackEntry ->
                            // Retrieve the imageName argument
                            val desc = backStackEntry.arguments?.getString("desc") ?: "No desc"
                            val title = backStackEntry.arguments?.getString("title") ?: "No title"

                            RecommandationScreen(title = title, description = desc, image = "image")
                        }

                        composable("homeRadiologue") {
                            RadiologueNavController(navController)
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
