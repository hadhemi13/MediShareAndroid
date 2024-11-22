package com.example.medishareandroid.views.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppBottomNavigation() {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        BottomNavigationItem(
            selected = true,
            onClick = { /* Navigate to Home */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Gray) },
            label = { Text("Home") }
        )
        BottomNavigationItem(
            selected = true,
            onClick = { /* Navigate to Home */ },
            icon = { Icon(Icons.Default.Folder, contentDescription = "Folder") },
            label = { Text("Folder") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { /* Navigate to Search */ },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { /* Navigate to Profile */ },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun AppBottemNAvigationPreview(){
    AppBottomNavigation();
}