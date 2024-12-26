package com.example.medishareandroid.views.patient

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.medishareandroid.remote.RecReq
import com.example.medishareandroid.remote.Recommendation
import com.example.medishareandroid.remote.RecommendationApi
import com.example.medishareandroid.remote.ReqRes
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.patient.HomeViewModel
import com.example.medishareandroid.views.profile.ChangePasswordScreen
import com.example.medishareandroid.views.ChatScreen
import com.example.medishareandroid.views.profile.EditProfileScreen
import com.example.medishareandroid.views.profile.ProfileScreen
import com.example.medishareandroid.views.TopPeopleScreen
import com.example.medishareandroid.views.components.RecommendationCard
import com.example.medishareandroid.views.items.ClinicCard
import retrofit2.Call

@Composable
fun HomeScreen(navController2: NavController) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesRepository = PreferencesRepository(context)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("home") { ExactDesignScreen(navController) }
            composable("editProfileScreen") {
                EditProfileScreen(context, navController)
            }
            composable("profile") {
                ProfileScreen(navController, preferencesRepository, navController2)
            }
            composable("changePassword") {
                ChangePasswordScreen(navController, preferencesRepository)
            }
            composable("chat") {
                TopPeopleScreen(navController)
            }
            composable("newChat/{discussId}") {backStackEntry ->
                // Retrieve the imageName argument
                val discussId = backStackEntry.arguments?.getString("discussId") ?: "No desc"
                ChatScreen(userId = preferencesRepository.getId()!!, discussionId = discussId)
            }
            composable("newChat") {
                // Retrieve the imageName argument
                ChatScreen(userId = preferencesRepository.getId()!!)
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
            composable("folder") {
                FolderScreen(navController, modifier = Modifier.padding(innerPadding))
            }
            composable("ocrItemScreen/{id}") { backStackEntry ->
                // Retrieve the imageName argument
                val id = backStackEntry.arguments?.getString("id") ?: "No id"

                OcrItemScreen(id = id )
            }
            composable("recommendationItem/{title}/{desc}/{image}") { backStackEntry ->
                // Retrieve the imageName argument
                val desc = backStackEntry.arguments?.getString("desc") ?: "No desc"
                val title = backStackEntry.arguments?.getString("title") ?: "No title"
                val image = backStackEntry.arguments?.getString("image") ?: "No image"


                RecommandationScreen(title = title, description = desc, image = image)
            }

        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", "home", Icons.Filled.Home),
        BottomNavItem("Folder", "folder", Icons.Outlined.Folder),
        BottomNavItem("Add", "add", Icons.Outlined.Add),
        BottomNavItem("Chat", "chat", Icons.AutoMirrored.Outlined.Chat),
        BottomNavItem("Profile", "profile", Icons.Outlined.Person)
        //navController.navigate("profile")

    )

    NavigationBar(containerColor = Color.White) {

        val context = LocalContext.current

        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        var uploadFilePath by remember { mutableStateOf(TextFieldValue("")) }
        val imageUri = rememberSaveable { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                imageUri.value = it.toString()

                // Extract the file path from the URI and save it to uploadFilePath
                val path = getPathFromUri(context, it)
                uploadFilePath = TextFieldValue(path) // Update the global path
                val destination =
                    "ocr_screen?filePath=${uploadFilePath.text}&imageUri=${Uri.encode(imageUri.value)}"
                navController.navigate(destination) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true // Ensure the previous screens are popped
                    }
                    launchSingleTop = true // Avoid multiple launches of the same screen
                }

            }
        }
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        getBottomNavIcon(item.route, currentDestination?.route == item.route),
                        contentDescription = item.label
                    )
                },
                //label = { Text(item.label) },
                selected = currentDestination?.route == item.route ,

                onClick = {
                    if (item.route == "add") {
                        launcher.launch("image/*")

                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedTextColor = Color.Gray,
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.Gray

                ),
                alwaysShowLabel = false,
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)


/*data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector = when (route) {
        "home" -> Icons.Outlined.Home or Icons.Filled.Chat
        "chat" -> Icons.AutoMirrored.Outlined.Chat
        "profile" -> Icons.Outlined.Person
        "folder" -> Icons.Outlined.Folder
        "ocr" -> Icons.Outlined.Add
        else -> Icons.AutoMirrored.Filled.Help
    }
)*/
fun getBottomNavIcon(route: String, isSelected: Boolean): ImageVector {
    return when (route) {
        "home" -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
        "chat" -> if (isSelected) Icons.AutoMirrored.Filled.Chat else Icons.AutoMirrored.Outlined.Chat
        "profile" -> if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
        "folder" -> if (isSelected) Icons.Filled.Folder else Icons.Outlined.Folder
        "add" -> if (isSelected) Icons.Filled.Add else Icons.Outlined.Add
        else -> Icons.AutoMirrored.Filled.Help
    }
}


@Composable
fun ScreenContent() {
    val viewModel = HomeViewModel()
    val clinics = viewModel.clinics // Obtenir les données des cliniques
    val context = LocalContext.current
    val preferencesRepository = PreferencesRepository(context)

    // État pour les recommandations
    val recommendations = remember { mutableStateOf<List<Recommendation>>(emptyList()) }

    // Effectuer un appel réseau pour récupérer les recommandations
    LaunchedEffect(Unit) {
        val api = RetrofitInstance.getRetrofit().create(RecommendationApi::class.java)
        api.fetchRecommendations(RecReq(preferencesRepository.getId()!!))
            .enqueue(object : retrofit2.Callback<ReqRes> {
                override fun onResponse(call: Call<ReqRes>, response: retrofit2.Response<ReqRes>) {
                    if (response.isSuccessful) {
                        val recommendationsList = response.body()?.data
                        if (recommendationsList.isNullOrEmpty()) {
                            Toast.makeText(context, "No recommendations found", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            recommendations.value = recommendationsList
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to load recommendations",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ReqRes>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    // Interface utilisateur principale
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Message de bienvenue
        Text(
            text = "Hi, ${preferencesRepository.getName()}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "How can we help you today?",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Section des cliniques
        Text(
            text = "Clinics & Radiologists",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(clinics.size) { index ->
                ClinicCard(clinic = clinics[index])
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Gérer le clic sur "View Map" */ },
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text(text = "View Map", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section des recommandations
        Text(
            text = "Recommendations",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recommendations.value.asReversed()) { recommendation ->
                RecommendationCard(recommendation = recommendation)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}