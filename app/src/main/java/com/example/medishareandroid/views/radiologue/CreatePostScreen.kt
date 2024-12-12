package com.example.medishareandroid.views.radiologue

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.medishareandroid.remote.BASE_URL
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.viewModels.radiologue.CreatePostViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FileDetailsPage(
    navController: NavController, imageId :String, imageName:String, title:String,
    postViewModel: CreatePostViewModel = viewModel()
) {
    val description = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                backgroundColor = Color.White
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Image display
                AsyncImage(
                    model = BASE_URL + imageName,
                    contentDescription = "Network Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(60.dp)
                        .height(250.dp)
                        .clickable {
                            //  navController.navigate("fullscreen/${image.imageName}")
                        },
                    contentScale = ContentScale.Crop

                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Date: 21/02/2024",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Title input field
                OutlinedTextField(
                    value = title,
                    onValueChange = {},
                    label = { Text("Title du post") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Description input field
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description du post") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(50.dp))

                // Submit button
                Button(
                    onClick = {
                        /*coroutineScope.launch {
                            val newPost = PostRequest(
                                idImage = image.id,
                                image = image.imageName,
                                description = description.value,
                                title = "Auto title"
                            )
                            val userId = getUserIdFromPreferences() // Mocked function
                            postViewModel.createPost(
                                mapOf(
                                    "title" to newPost.title,
                                    "imageId" to newPost.idImage,
                                    "content" to newPost.description,
                                    "userid" to userId,
                                    "subreddit" to "dfsdfds"
                                )
                            )
                            navController.popBackStack()
                        }*/
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x9990CAF9),
                                        Color(0xFF90CAF9)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ajouter le Post",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CreatePostPreview() {
    FileDetailsPage(rememberNavController(), "", "title image", "image title")
}