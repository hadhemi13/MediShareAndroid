package com.example.medishareandroid.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.views.patient.ExactDesignScreen
import com.example.medishareandroid.views.patient.FolderScreen
import com.example.medishareandroid.views.patient.OCRScreen
import com.example.medishareandroid.views.patient.getPathFromUri
import com.example.medishareandroid.views.profile.SettingsScreen
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun BottomNavScreen(navController: NavController, navController2: NavController) {
    val items = listOf("Home", "Folder", "Plus", "Chat", "Profile")

    // Remember pager state
    val pagerState = rememberPagerState(pageCount = { items.size })

    // Remember a coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()
    //val navController = rememberNavController()  // Ensure you have a NavController for navigating
    //val navController2 = rememberNavController() // Another NavController for ProfileScreen's actions
    val prefs =
        PreferencesRepository(LocalContext.current) // Pass PreferencesRepository to ProfileScreen


    // Scaffold Layout for fixed BottomNavigation
    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.height(60.dp),
                backgroundColor = Color.White // Set background color to white
            ) {
                items.forEachIndexed { index, title ->
                    BottomNavigationItem(
                        icon = {
                            when (title) {
                                "Home" -> Icon(
                                    imageVector = if (pagerState.currentPage == index) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home",
                                    tint = if (pagerState.currentPage == index) Color.Black else Color.Black // All icons will be black
                                )

                                "Folder" -> Icon(
                                    imageVector = if (pagerState.currentPage == index) Icons.Filled.Folder else Icons.Outlined.Folder,
                                    contentDescription = "Folder",
                                    tint = if (pagerState.currentPage == index) Color.Black else Color.Black // All icons will be black
                                )

                                "Plus" -> {
                                    // Show the icon with a smaller background when selected
                                    if (pagerState.currentPage == index) {
                                        Box(
                                            modifier = Modifier
                                                .padding(4.dp) // Smaller padding for the background
                                                .background(
                                                    Color.Black,
                                                    shape = RoundedCornerShape(12.dp)
                                                ) // Rounded corners
                                                .size(40.dp) // Control the size of the background (smaller than before)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Add,
                                                contentDescription = "Plus",
                                                modifier = Modifier.align(Alignment.Center),
                                                tint = Color.White // White color for the icon when selected
                                            )
                                        }
                                    } else {
                                        // Default icon without background
                                        Icon(
                                            imageVector = Icons.Outlined.Add,
                                            contentDescription = "Plus",
                                            tint = Color.Black // Black for unselected
                                        )
                                    }
                                }

                                "Chat" -> Icon(
                                    imageVector = if (pagerState.currentPage == index) Icons.Filled.Chat else Icons.Outlined.Chat,
                                    contentDescription = "Chat",
                                    tint = if (pagerState.currentPage == index) Color.Black else Color.Black // All icons will be black
                                )

                                "Profile" -> Icon(
                                    imageVector = if (pagerState.currentPage == index) Icons.Filled.Person else Icons.Outlined.Person,
                                    contentDescription = "Profile",
                                    tint = if (pagerState.currentPage == index) Color.Black else Color.Black // All icons will be black
                                )
                            }
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        alwaysShowLabel = false, // Disable label visibility
                        interactionSource = remember { MutableInteractionSource() }, // Disable ripple effect (shadow)
                        modifier = Modifier.shadow(0.dp) // Remove any shadow
                    )
                }
            }
        }
    ) { innerPadding ->
        // Pager for swiping between screens
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 0, // Avoid preloading adjacent pages
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Add padding to avoid overlap with bottom navigation
        ) { page ->
            when (page) {
                0 -> ExactDesignScreen(navController2)
                1 -> FolderScreen(navController2, Modifier)
                2 -> {
                    val context = LocalContext.current
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
                        }
                    }
                    OCRScreen("","") // Launch OCRScreen only if it's the current page

                    // Trigger logic only when the page becomes visible
                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }
                            .distinctUntilChanged() // Only act on actual page changes
                            .collect { currentPage ->
                                if (currentPage == 2) {
                                    launcher.launch("image/*")
                                }
                            }
                    }

                }
                3 -> FolderScreen(navController2, Modifier)
                4 -> SettingsScreen(
                    navController = navController2,
                    userPreferences = prefs,
                    navController2 = navController
                ) // Profile screen with NavController            }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavScreenPreview() {
    val navController = rememberNavController()
    val navController2 = rememberNavController()

    BottomNavScreen(navController, navController2)

}
