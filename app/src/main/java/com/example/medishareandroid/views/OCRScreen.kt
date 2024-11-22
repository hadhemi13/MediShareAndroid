package com.example.medishareandroid.views

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image

//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme

//import android.net.Uri
import android.util.Log

import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import com.example.medishareandroid.remote.Message
import com.example.medishareandroid.remote.OCRResponse
import com.example.medishareandroid.remote.OcrAPI
import com.example.medishareandroid.remote.ResetPasswordDto
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import okio.BufferedSink
import okio.source
import java.io.InputStream
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
//import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import coil3.compose.rememberAsyncImagePainter
import com.example.medishareandroid.R
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.OCRViewModel
import java.io.FileOutputStream

@Composable
fun OCRScreen(viewModel: OCRViewModel = viewModel()) {
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val userId = prefs.getId() // Immutable user ID
    var imageName by remember { mutableStateOf(TextFieldValue("")) }
    var uploadFilePath by remember { mutableStateOf(TextFieldValue("")) }
    var result by remember { mutableStateOf("") }

    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        model = imageUri.value.ifEmpty { R.drawable.profile_image }
    )

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
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
                    .clickable {
                        launcher.launch("image/*")
                    }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text("OCR API Demo", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // Display user ID
        Text(
            text = "User ID: $userId",
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        // Image name input
        BasicTextField(
            value = imageName,
            onValueChange = { imageName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField -> innerTextField() },
            singleLine = true
        )

        Button(onClick = {
            viewModel.analyzeImage(imageUri.value) {
                result = it?.toString() ?: "Error analyzing image"
            }
        }) {
            Text("Analyze Image")
        }

        Spacer(Modifier.height(16.dp))

        // File path input
        BasicTextField(
            value = uploadFilePath,
            onValueChange = { uploadFilePath = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField -> innerTextField() },
            singleLine = true
        )

        // Print the selected file path
        Text("Selected File Path: ${uploadFilePath.text}")

        Spacer(Modifier.height(16.dp))

        // Upload button
        Button(onClick = {
            viewModel.uploadFile(uploadFilePath.text, userId!!, context)
        }) {
            Text("Upload File")
        }

        Spacer(Modifier.height(16.dp))

        // Display result
        Text("Result: $result")
    }
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
