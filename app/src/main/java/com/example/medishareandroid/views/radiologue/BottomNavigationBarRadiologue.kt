package com.example.medishareandroid.views.radiologue

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.models.radiologue.BottomNavItemR

@Composable
fun BottomNavigationBarRadiologue(navController: NavController) {
    val items = listOf(
        BottomNavItemR("Home", "homeRadiologue", Icons.Filled.Home),
        BottomNavItemR("Folder", "folderRadiologue", Icons.Outlined.Folder),
        BottomNavItemR("Chat", "chat", Icons.Outlined.Add),
        BottomNavItemR("Profile", "profile", Icons.Outlined.Person)

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
                val path =getPathFromUri(context, it)
                uploadFilePath = TextFieldValue(path) // Update the global path
                val destination = "uploadImage?filePath=${uploadFilePath.text}&imageUri=${Uri.encode(imageUri.value)}"
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
                        getBottomNavIconR(item.route, currentDestination?.route == item.route),
                        contentDescription = item.label
                    )
                },
                //label = { Text(item.label) },
                selected = currentDestination?.route == item.route ,

                onClick = {
                    /*  if (item.route == "upload") {
                        launcher.launch("image/*")

                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }*/*/
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
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
fun getBottomNavIconR(route: String, isSelected: Boolean): ImageVector {
    return when (route) {
        "homeRadiologue" -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
        "profile" -> if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
        "folderRadiologue" -> if (isSelected) Icons.Filled.Folder else Icons.Outlined.Folder
        "chat" -> if (isSelected) Icons.Filled.Chat else Icons.Outlined.Chat
        else -> Icons.AutoMirrored.Filled.Help
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarRadiologuePreview(){
    BottomNavigationBarRadiologue(rememberNavController())
}