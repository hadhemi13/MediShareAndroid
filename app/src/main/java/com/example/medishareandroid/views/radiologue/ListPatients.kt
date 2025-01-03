package com.example.medishareandroid.views.radiologue

import androidx.compose.runtime.Composable
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medishareandroid.models.radiologue.Patient
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.radiologue.UploadFileViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SearchPatientsPage(
    navController: NavController,
    viewModel: UploadFileViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val userId = prefs.getId() // Immutable user ID
    var patientEmail by remember { mutableStateOf("") }
    var patientsList by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var filteredPatientsList by remember { mutableStateOf(listOf<Patient>()) }

    // Fetch patients on initial load
    LaunchedEffect(Unit) {
        viewModel.getPatientsByRadiologist(userId!!, context) { patients ->
            patientsList = patients // Update the patients list with the result
            filteredPatientsList = patients // Initially show all patients
        }
    }

    // Filter patients when the search query changes
    fun filterPatients(query: String) {
        filteredPatientsList = patientsList.filter {
            it.name.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true)
        }
    }

    // Handle text input change and filtering
    LaunchedEffect(patientEmail) {
        filterPatients(patientEmail) // Filter patients whenever the text changes
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF9F9F9)),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Search Patients", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // Search bar
        OutlinedTextField(
            value = patientEmail,
            onValueChange = {
                patientEmail = it // Update the search query
            },
            label = { Text("Search Patient (Name or Email)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .padding(top = 20.dp)
        )

        // Display the filtered list of patients
        if (filteredPatientsList.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredPatientsList) { patient ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Make the Text clickable
                        Text(
                            "${patient.name} - ${patient.email}",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    // Log the patient's ID when clicked
                                    navController.navigate("filesPage/${userId}/${patient._id}")
                                }
                        )

                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 10.dp) // Add padding here
                        )
                    }
                }
            }
        } else {
            Text("No patients found", color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPatientsPagePreview() {
    SearchPatientsPage(navController = rememberNavController())
}
