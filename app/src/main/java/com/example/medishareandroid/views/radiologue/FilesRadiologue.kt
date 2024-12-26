package com.example.medishareandroid.views.radiologue

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.medishareandroid.models.radiologue.ImageResponse
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.viewModels.radiologue.FilesViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FilesPage(viewModel: FilesViewModel = viewModel(), userId: String, navController:NavController) {


    val images by viewModel.imagesResponse.observeAsState(emptyList())
    val context = LocalContext.current

    var uploadFilePath by remember { mutableStateOf(TextFieldValue("")) }
    val imageUri = rememberSaveable { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult (
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
    LaunchedEffect(userId) {
        viewModel.fetchImages(userId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Files Page") },
                backgroundColor = Color.White,
                actions = {
                    IconButton(onClick = {  launcher.launch("image/*") }) {
                        Icon(Icons.Default.FileUpload, contentDescription = "Upload File")
                    }
                }
            )
        },
        content = {
            when {
                images.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(images!!.asReversed()) { image ->
                            FileCard(image = image, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FileCard(image: ImageResponse, navController: NavController) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth().clickable {
             navController.navigate("imageIrm/${image._id}/${image.imageName}/${image.title}")

        // navController.navigate("detailsImage/${image._id}/${image.imageName}/${image.title}")
        },
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = BASE_URL + image.imageName,
                contentDescription = "Network Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f)
            ) {
                Text(image.title, fontWeight = FontWeight.Bold)
                Text("View Image", color = Color.Gray)
            }
            IconButton(onClick = { /* Navigate to details */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun FilesPageprview(){
    FilesPage(userId = "fdgdhg", navController = rememberNavController())
}