package com.example.medishareandroid.views


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.HomeViewModel
import com.example.medishareandroid.viewModels.ProfileViewModel
import com.example.medishareandroid.views.items.ClinicCard

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
        }
    }
}

@Composable
fun ScreenContent(text: String) {
    val viewModel= HomeViewModel()
    val clinics = viewModel.clinics // Get data from ViewModel
    val context = LocalContext.current
    val preferencesRepository = PreferencesRepository(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Welcome Message
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

        // Clinics Section
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
            onClick = { /* Handle View Map click */ },
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text(text = "View Map", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recommendations Section
        Text(
            text = "Recommendations",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Loading recommendations...",
            fontSize = 14.sp,
            color = Color.Gray
        )
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
        "profile" -> Icons.Default.Person
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



