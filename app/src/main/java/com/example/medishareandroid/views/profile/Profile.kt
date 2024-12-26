    package com.example.medishareandroid.views.profile


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Brightness2

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import com.example.medishareandroid.views.components.ProfileOptionCard
@Composable
fun ProfileScreen(navController: NavController, userPreferences: PreferencesRepository, navController2: NavController) {
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    var isDarkMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

        .background(Color(0xFFF9F9F9)) // Background for the screen
    ) {
        BlueHeader1()

        // ProfileInfo now overlaps with BlueHeader1
        ProfileInfo(prefs)


        // Add user-friendly informational text here
        Text(
            text = "You can update your profile here.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .padding(vertical = 4.dp) // Reduces space above and below the text
        )

        // Settings Card for options
        Box(
            modifier = Modifier
                .fillMaxWidth() // Take full width to center
                .padding(30.dp)
                .clip(RoundedCornerShape(20.dp)) // Apply rounded corners to the outer card
                .background(Color.White) // Ensure outer card background is white
        ) {
            // Column for the options
            Column(modifier = Modifier.padding(10.dp)) {
                // Dark Mode Option
                Box(modifier = Modifier.padding(vertical = 8.dp)) {
                    ProfileOptionCard(
                        icon = Icons.Outlined.Brightness2,
                        label = "Dark mode",
                        trailingContent = {
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { isDarkMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFFF9F9F9), // Blue thumb when ON
                                    uncheckedThumbColor = Color.Gray,     // Gray thumb when OFF
                                    checkedTrackColor = Color(0xC10A46F3), // Blue track when ON
                                    uncheckedTrackColor = Color.LightGray // Light gray track when OFF
                                )
                            )
                        }
                    )
                }


                // Edit Profile Option
                Box(modifier = Modifier.padding(vertical = 8.dp)) {
                    ProfileOptionCard(
                        icon = Icons.Default.Person,
                        label = "Edit Profile",
                        onClick = { navController.navigate("editProfileScreen") }
                    )
                }

                // Change Password Option
                Box(modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    ProfileOptionCard(
                        icon = Icons.Default.Lock,
                        label = "Change Password",
                        onClick = { navController.navigate("changePassword") }
                    )
                }

                // Logout Option
                Box(modifier = Modifier.padding(vertical = 8.dp)) {
                    ProfileOptionCard(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        label = "Logout",
                        onClick = {
                            userPreferences.clearLoginData() // Logout logic
                            navController2.navigate("login") // Navigate to login screen
                            Toast.makeText(
                                navController2.context,
                                "Logged out successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }

    }
}



/*
@Composable
fun BlueHeader1() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(Color(0xC10A46F3)) // Blue header background

            .padding(15.dp)
    )


}*/
@Composable
fun BlueHeader1() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp) // Adjust the height if needed
    ) {
        // Image as background
        Image(
            painter = painterResource(id = R.drawable.backgroundprof), // Replace with your image resource
            contentDescription = "Header Image",
            modifier = Modifier
                .fillMaxSize(), // Make the image fill the Box size
            contentScale = ContentScale.Crop // Crop to fit the image
        )
    }
}

@Composable
fun ProfileInfo(prefs: PreferencesRepository) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Take full width to center
            .height(90.dp)
            .padding(start = 30.dp, end = 30.dp)
            .offset(y = (-35).dp) // Adjust vertical position to overlap with BlueHeader1
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), // Ensure Row fills the Box width
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Align content to the start (left)
        ) {
            Image(
                painter = painterResource(R.drawable.hadh),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape) // Circular image
                    .border(2.dp, Color.White, CircleShape), // White border around the image
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Use a Column with alignment set to the start for both Texts
            Column(
                modifier = Modifier.weight(1f), // Let the column take the available space
                horizontalAlignment = Alignment.Start // Align text to the left
            ) {
                // Ensure both texts start at the same X position by using Modifier.fillMaxWidth()
                Text(
                    text = prefs.getName() ?: "Guest",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth() // Align the name to the left
                )
                Text(
                    text = prefs.getEmail() ?: "Guest@esprit.tn",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth() // Ensure the email starts from the same X position
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    MediSHareAndroidTheme {
        ProfileScreen(
            navController = navController,
            userPreferences = PreferencesRepository(context),
            navController2 = rememberNavController()
        )
    }
}