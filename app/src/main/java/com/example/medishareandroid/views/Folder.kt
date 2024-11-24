package com.example.medishareandroid.views

import android.content.Context
import com.example.medishareandroid.viewModels.OCRViewModel


import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.decode.ImageSource
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.remote.OCRResponse
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.repositories.PreferencesRepository


@Composable
fun FolderScreen(navController: NavController, viewModel: OCRViewModel = viewModel()) {}
    // Observe LiveData
   /* val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val ocrResponses by viewModel.ocrResponses.observeAsState(emptyList())
    val error by viewModel.error.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)  // Track loading state
    LaunchedEffect(Unit) {
        val userId =  // Replace with the actual user ID
        viewModel.fetchAllImages(prefs.getId()!!, context)
    }
    // UI elements
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (isLoading) {
            // Show loading indicator
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (error.isNotEmpty()) {
                // Error state
                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
            } else {
                // Content list
                LazyColumn {
                    items(ocrResponses) { ocrResponse ->
                        OCRItem(ocrResponse = ocrResponse, context)
                    }
                }
            }
        }
    }
}

@Composable
fun OCRItem(ocrResponse: OCRResponse,context:Context) {
 /*  //Toast.makeText(context,BASE_URL+"upload/"+ocrResponse.image_name , Toast.LENGTH_LONG).show()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row {
            AsyncImage(
                model = BASE_URL+"upload/"+ocrResponse.image_name,
                contentDescription = "Network Image",
                modifier = Modifier
                    .height(50.dp)
            )
        Column(modifier = Modifier.padding(16.dp)) {
            // Image name
            Text(
                text = "Image Name: ${ocrResponse.image_name}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Error details, if available
            ocrResponse.error?.let {
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
                Text(
                    text = "Details: ${ocrResponse.details ?: "No details available"}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Patient name, if available
            ocrResponse.patient_name?.let {
                Text(text = "Patient: $it", style = MaterialTheme.typography.bodyLarge)
            }

            // Prescription details
            ocrResponse.prescription?.forEach { prescriptionItem ->
                Text(text = "Activity: ${prescriptionItem.activity}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Frequency: ${prescriptionItem.frequency}", style = MaterialTheme.typography.bodyMedium)
                prescriptionItem.location?.let {
                    Text(text = "Location: $it", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
    }*/

}
@Preview(showBackground = true)
@Composable
fun PreviewFolderScreen() {




    // Use a mocked version of the OCRViewModel

    // FolderScreen composable with mocked viewModel
    FolderScreen(navController = rememberNavController(), viewModel = OCRViewModel())
}
*/