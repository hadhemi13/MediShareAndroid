package com.example.medishareandroid.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.ProfileViewModel
import com.example.medishareandroid.views.components.EditButton
import com.example.medishareandroid.views.components.VisitItem


@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {
    var context = LocalContext.current
    var prefs = PreferencesRepository(context)
    Column(
        modifier = Modifier
            .fillMaxSize()

            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${prefs.getName() ?: "guest"}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp)
                    .clickable {
                        // Your click action here

                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Followers
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "100K", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Followers", fontSize = 12.sp)
                }

                // Profile Image
                Image(
                    painter = painterResource(id = R.drawable.profile_image),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

                // Following
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "23.5K", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Following", fontSize = 12.sp)
                }
            }

            Text(
                text = "${prefs.getName() ?: "guest"} ",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = prefs.getEmail() ?: "guest@gmail.com",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Sharing SwiftUI Tutorials With You.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Row(verticalAlignment = Alignment.CenterVertically) {


                Text(
                    text = "Follow me @${prefs.getName() ?: "guest"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(15.dp))
                Button(
                    onClick = { navController.navigate("editProfileScreen") },
                    modifier = Modifier
                        .height(35.dp)
                        .width(120.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(6.dp), // Rounded edges for Instagram-like style
                    elevation = ButtonDefaults.buttonElevation(0.dp) // Remove button shadow
                ) {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.Black
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder Grid of images
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(9) { // Placeholder for 9 items
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp) // Adjust the height as needed
                            .background(Color.LightGray)
                            .padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfilePage() {
    ProfileScreen( ProfileViewModel(), rememberNavController())
}

/*
@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row: Username & Options Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = viewModel.username,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_options),
                    contentDescription = "Options Icon"
                )
            }
        }

        // Profile Image
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.profile_image),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        // Profile Bio Text
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = viewModel.bio,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Edit Profile Button
        Spacer(modifier = Modifier.height(8.dp))
        EditButton {navController.navigate("editProfileScreen") }

        // Divider Line
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.Gray
        )

        // Visits Section as a Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.visits.size) { index ->
                VisitItem(visit = viewModel.visits[index])
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilScreenPreview() {
    val viewModel = ProfileViewModel()
    val navController = rememberNavController()
    ProfileScreen(viewModel, navController)
}*/