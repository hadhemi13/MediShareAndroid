package com.example.medishareandroid.views.radiologue

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.io.File
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.medishareandroid.R
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.radiologue.UploadFileViewModel
import com.example.medishareandroid.views.components.ProfileOptionCard
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.FileOutputStream

@Composable
fun UploadImage(
    uploadFilePath1: String,
    imageUri1: String,
    navController: NavController,
    viewModel: UploadFileViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val userId = prefs.getId() // Immutable user ID
    var imageName by remember { mutableStateOf(TextFieldValue("")) }
    val uploadFilePath by remember { mutableStateOf(TextFieldValue(uploadFilePath1)) }
    var imgTitle by remember { mutableStateOf("") }

    val imageUri = rememberSaveable { mutableStateOf(imageUri1) }
    val coroutineScope = rememberCoroutineScope()
    val painter = rememberAsyncImagePainter(
        model = imageUri.value.ifEmpty { R.drawable.profile_image }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image picker section
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .padding(top = 100.dp)
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Image(
                painter = painter,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Camera Icon",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    //.clickable {
                    //   launcher.launch("image/*")
                    //}
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text("OCR API Demo", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        BasicTextField(
            value = imageName,
            onValueChange = { imageName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField -> innerTextField() },
            singleLine = true
        )

        // Spacer to push the buttons to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Bottom buttons section
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            // Champ de texte pour le nom
            OutlinedTextField(
                value = imgTitle,
                onValueChange = { imgTitle = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            ProfileOptionCard(
                icon = Icons.Default.UploadFile,
                label = "Upload File",
                onClick = {
                    coroutineScope.launch {viewModel.uploadFileImage(uploadFilePath.text, imgTitle, userId!!, context)}
                        navController.popBackStack( "folderRadiologue", inclusive = true)
                }


            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OCRScreenPreview() {
    UploadImage("", "", rememberNavController())
}


// Helper function to get the path from URI (Updated for content:// URIs)
fun getPathFromUri(context: Context, uri: Uri): String {
    var filePath = ""
    if (uri.scheme == "content") {
        // Handle content:// URIs
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    filePath = it.getString(columnIndex)
                }
            }
            it.close()
        }

        // If the file path is still empty, try copying the file
        if (filePath.isEmpty()) {
            filePath = copyFileToAppStorage(context, uri)
        }
    } else {
        // For file:// URIs, just return the path directly
        filePath = uri.path ?: "Unknown path"
    }
    return filePath
}


// Helper function to copy the file from content:// URI to app's internal storage
fun copyFileToAppStorage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)

    try {
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath // Return the new file path in app's storage
    } catch (e: Exception) {
        e.printStackTrace()
        return "Error copying file"
    }
}