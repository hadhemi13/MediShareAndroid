package com.example.medishareandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medishareandroid.R
import com.example.medishareandroid.viewModels.ProfileViewModel
import com.example.medishareandroid.views.components.EditButton
import com.example.medishareandroid.views.components.VisitItem

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