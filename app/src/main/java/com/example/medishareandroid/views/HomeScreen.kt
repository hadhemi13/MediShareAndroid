package com.example.medishareandroid.views


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medishareandroid.R
import com.example.medishareandroid.remote.RecReq
import com.example.medishareandroid.remote.Recommendation
import com.example.medishareandroid.remote.RecommendationApi
import com.example.medishareandroid.remote.ReqRes
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.HomeViewModel
import com.example.medishareandroid.viewModels.ProfileViewModel
import com.example.medishareandroid.views.components.RecommendationCard
import com.example.medishareandroid.views.items.ClinicCard
import retrofit2.Call
import okhttp3.Callback
import okhttp3.Response

@Composable
fun HomeScreen(navController2: NavController) {
    val navController = rememberNavController()
    val viewModel = ProfileViewModel()
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

            composable("home") { ScreenContent("Home") }
            composable("search") { ScreenContent("Search") }
            composable("profile") { ProfileScreen(viewModel, navController) }
            composable("editProfileScreen") {
                EditProfileScreen(context, navController)
            }
            composable("settings") {
                SettingsScreen(navController, preferencesRepository, navController2)
            }
            composable("changePassword") {
                ChangePasswordScreen(navController, preferencesRepository)
            }
            composable("ocr"){
               // PhotosView()
                OCRScreen()
            }
            composable("folder") {
                FolderScreen(navController)
            }
        }
    }
}
@Composable
fun ScreenContent(text: String) {
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
                            Toast.makeText(context, "No recommendations found", Toast.LENGTH_SHORT).show()
                        } else {
                            recommendations.value = recommendationsList
                        }
                    } else {
                        Toast.makeText(context, "Failed to load recommendations", Toast.LENGTH_SHORT).show()
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
            items(recommendations.value.size) { index ->
                RecommendationCard(recommendation = recommendations.value[index])
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", "home"),
        BottomNavItem("Folder", "folder"),
        BottomNavItem("Add", "ocr"),
        BottomNavItem("Search", "search"),
        BottomNavItem("Profile", "settings")
                //navController.navigate("settings")

    )

    NavigationBar(containerColor = Color.White) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    selectedIndicatorColor = Color.Black,
                    unselectedTextColor = Color.Gray,
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.Gray

              )
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector = when (route) {
        "home" -> Icons.Default.Home
        "search" -> Icons.Default.Search
        "settings" -> Icons.Default.Person
        "folder" -> Icons.Default.Folder
        "ocr" -> Icons.Default.AddCircle
        else -> Icons.AutoMirrored.Filled.Help
    }
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}



