package com.example.medishareandroid.views.radiologue

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.views.ChangePasswordScreen
import com.example.medishareandroid.views.EditProfileScreen
import com.example.medishareandroid.views.FolderScreen
import com.example.medishareandroid.views.OCRScreen
import com.example.medishareandroid.views.OcrItemScreen
import com.example.medishareandroid.views.ProfileScreen

@Composable
fun RadiologueNavController(navControllerMain: NavController) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val userId = prefs.getId()!!
    Log.d("tteste1", "RadiologueNavController$userId")

    Scaffold(
        bottomBar = { BottomNavigationBarRadiologue(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "homeRadiologue",
            modifier = Modifier.padding(innerPadding)
        ) {


            composable("homeRadiologue") { HomePage(userId = userId )}


            composable("folderRadiologue") {
                FilesPage(userId = userId, navController = navController)
            }
            composable("detailsImage/{imageId}/{imageName}/{title}") {backStackEntry ->
                // Retrieve the imageName argument
                val imageId = backStackEntry.arguments?.getString("imageId") ?: "No id"
                val imageName = backStackEntry.arguments?.getString("imageName") ?: "No id"
                val title = backStackEntry.arguments?.getString("title") ?: "No id"

                FileDetailsPage(navController, imageId, imageName, title)
            }
            composable("imageIrm/{imageId}/{imageName}/{title}") {backStackEntry ->
                // Retrieve the imageName argument
                val imageId = backStackEntry.arguments?.getString("imageId") ?: "No id"
                val imageName = backStackEntry.arguments?.getString("imageName") ?: "No id"
                val title = backStackEntry.arguments?.getString("title") ?: "No id"

                ImageIrm(navController, imageId, imageName, title)
            }
            composable(
                route = "ocr_screen?filePath={filePath}&imageUri={imageUri}",
                arguments = listOf(
                    navArgument("filePath") { type = NavType.StringType },
                    navArgument("imageUri") { type = NavType.StringType }
                )
            ) {
                // Pass arguments to the composable
                OCRScreen(
                    uploadFilePath1 = it.arguments?.getString("filePath") ?: "",
                    imageUri1 = it.arguments?.getString("imageUri") ?: ""
                )
            }

            composable("ocrItemScreen/{id}") { backStackEntry ->
                // Retrieve the imageName argument
                val id = backStackEntry.arguments?.getString("id") ?: "No id"

                OcrItemScreen(id = id )
            }
            composable("editProfileScreen") {
                EditProfileScreen(context, navController)
            }
            composable("profile") {
                ProfileScreen(navController, prefs, navControllerMain)
            }
            composable("changePassword") {
                ChangePasswordScreen(navController, prefs)
            }

        }
    }
}