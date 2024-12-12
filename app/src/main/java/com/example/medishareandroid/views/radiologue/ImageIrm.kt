package com.example.medishareandroid.views.radiologue

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.medishareandroid.remote.BASE_URL


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImageIrm(navController: NavController, imageId :String, imageName:String, title:String) {
    val filters = listOf("Recent", "Trending", "Post")

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
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->

                        Button(
                            onClick = {if (filter == "Post"){
                                 navController.navigate("detailsImage/${imageId}/${imageName}/${title}")

                            } },
                            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF008080), backgroundColor = Color.White),
                        ) {
                            Text(filter)
                        }
                    }
                }
                // Image display
                AsyncImage(
                    model = BASE_URL + imageName,
                    contentDescription = "Network Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .size(60.dp)
                        .height(250.dp)
                        .clickable {
                            //  navController.navigate("fullscreen/${image.imageName}")
                        },
                    contentScale = ContentScale.Crop

                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ImageIrmPreview(){
    ImageIrm(imageId = "fdgdhg", imageName = "imageName", title = "title", navController = rememberNavController())
}