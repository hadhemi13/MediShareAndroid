package com.example.medishareandroid.views.radiologue

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.io.File
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.medishareandroid.R
import com.example.medishareandroid.models.radiologue.Patient
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
    var patientsList by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var filteredPatientsList by remember { mutableStateOf(listOf<Patient>()) }
    var patientEmail by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedPatient by remember { mutableStateOf<Patient?>(null) }

    val imageUri = rememberSaveable { mutableStateOf(imageUri1) }
    val coroutineScope = rememberCoroutineScope()
    val painter = rememberAsyncImagePainter(
        model = imageUri.value.ifEmpty { R.drawable.profile_image }
    )

    LaunchedEffect(Unit) {
        viewModel.getPatientsByRadiologist(userId!!, context) { patients ->
            patientsList = patients // Update the patients list with the result
            filteredPatientsList = patients // Initially show all patients
            patients.forEach { patient ->
                Log.d("PatientData", "Patient ID: ${patient._id}, Name: ${patient.name}")
            }
        }
    }

    fun filterPatients(query: String) {
        filteredPatientsList = patientsList.filter {
            it.name.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF9F9F9)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

        ) {
        Text("Add new image", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = patientEmail,
            onValueChange = {
                patientEmail = it
                filterPatients(it) // Filter patients whenever the text changes
            },
            label = { Text("Search Patient (Name or Email)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 2.dp) // Reduce space below the TextField
                .padding(top = 20.dp)


        )
// Display the filtered list of patients
        if (filteredPatientsList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()

                    .height(150.dp) // Constrain the height of LazyColumn
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredPatientsList) { patient ->
                        DropdownMenuItem(onClick = {
                            patientEmail = patient.email // Select the patient's email
                            filteredPatientsList = listOf(patient) // Keep only the selected patient
                            selectedPatient = patient // Update selected patient

                            expanded = false // Close the menu after selection
                            println("Selected Patient ID: ${patient._id}")  // This will print the ID to the console

                        }) {
                            Text("${patient.name} - ${patient.email}")
                        }
                    }
                }

                // Add the animated arrow at the bottom-right
                val infiniteTransition = rememberInfiniteTransition()
                val offsetY by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 10f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 4.dp, bottom = 30.dp) // Position the arrow at the bottom-right
                        .offset(y = offsetY.dp), // Apply vertical floating animation
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ArrowDownward, // Replace with your arrow drawable resource
                        contentDescription = "Scroll Indicator",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Gray // Set the color of the arrow
                    )
                }
            }
        } else {
            Text("No patients found", color = Color.Gray)
        }
        // Image picker section
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(200.dp, 150.dp) // Adjust size for a rectangular shape
                .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                .background(colorResource(R.color.box))
        ) {
            Image(
                painter = painter,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

        }





        BasicTextField(
            value = imageName,
            onValueChange = { imageName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp) // Reduced space below the TextField

                .padding(8.dp),
            decorationBox = { innerTextField -> innerTextField() },
            singleLine = true
        )



        // Bottom buttons section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F9F9)),

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
                    if (selectedPatient != null) {
                        coroutineScope.launch {
                            viewModel.uploadFileImage(
                                uploadFilePath.text,
                                imgTitle,
                                userId!!,
                                selectedPatient!!._id,
                                context
                            )
                        }
                        navController.popBackStack("folderRadiologue", inclusive = true)
                    } else {
                        Log.e("UploadImage", "No patient selected")
                    }
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