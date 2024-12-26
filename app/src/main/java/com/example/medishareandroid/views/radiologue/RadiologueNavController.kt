package com.example.medishareandroid.views.radiologue

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.views.ChatScreen
import com.example.medishareandroid.views.TopPeopleScreen
import com.example.medishareandroid.views.profile.ChangePasswordScreen
import com.example.medishareandroid.views.profile.EditProfileScreen
import com.example.medishareandroid.views.patient.OcrItemScreen
import com.example.medishareandroid.views.profile.ProfileScreen

@Composable
fun RadiologueNavController(navControllerMain: NavController) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val userId = prefs.getId()!!
    Log.d("tteste1", "RadiologueNavController$userId")
    val showBottomBar = remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = { if (showBottomBar.value) {
            BottomNavigationBarRadiologue(navController)
        } }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "homeRadiologue",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("chat") {
                TopPeopleScreen(navController)
            }
            composable("newChat/{discussId}") {backStackEntry ->
                // Retrieve the imageName argument
                val discussId = backStackEntry.arguments?.getString("discussId") ?: "No desc"
                ChatScreen(userId = userId, discussionId = discussId)
            }
            composable("newChat") {backStackEntry ->
                // Retrieve the imageName argument

                ChatScreen(userId = userId)
            }
            composable("homeRadiologue") { HomePage(userId = userId , navController = navController)}


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
                route = "uploadImage?filePath={filePath}&imageUri={imageUri}",
                arguments = listOf(
                    navArgument("filePath") { type = NavType.StringType },
                    navArgument("imageUri") { type = NavType.StringType }
                )
            ) {
                // Pass arguments to the composable
                UploadImage(
                    uploadFilePath1 = it.arguments?.getString("filePath") ?: "",
                    imageUri1 = it.arguments?.getString("imageUri") ?: "",
                    navController
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