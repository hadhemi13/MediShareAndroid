package com.example.medishareandroid.views.patient

import android.content.Context
import androidx.compose.foundation.background
import com.example.medishareandroid.viewModels.patient.OCRViewModel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.remote.OCRResponse
import com.example.medishareandroid.repositories.PreferencesRepository


@Composable
fun FolderScreen(navController: NavController,modifier: Modifier, viewModel: OCRViewModel = viewModel()) {
    // Observe LiveData
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val ocrResponses by viewModel.ocrResponses.observeAsState(emptyList())
    val error by viewModel.error.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)  // Track loading state




    LaunchedEffect(Unit) {

        viewModel.fetchAllImages(prefs.getId()!!, context)
    }
    // UI elements
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color.Blue),
            contentAlignment= Alignment.Center
        ) {
            Text(
                text = "Manage all your documents here with ease.",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1


            )
        }
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
                    items(ocrResponses.asReversed()) { ocrResponse ->
                        OCRItem(ocrResponse = ocrResponse, context, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun OCRItem(ocrResponse: OCRResponse,context:Context, navController: NavController) {
  //Toast.makeText(context,BASE_URL+"upload/"+ocrResponse.image_name , Toast.LENGTH_LONG).show()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("ocrItemScreen/${ocrResponse._id}")


            },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically){
            AsyncImage(
                model = BASE_URL+"upload/"+ocrResponse.image_name,
                contentDescription = "Network Image",
                modifier = Modifier
                    .height(100.dp).width(100.dp).padding(8.dp)
            )
        Column(modifier = Modifier.padding(16.dp)) {
            // Image name
            Text(
                text ="${ocrResponse.title}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            /*

            // Error details, if available
            ocrResponse.error?.let {
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
                Text(
                    text = "Details: ${ocrResponse.details ?: "No details available"}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
*/
           /* // Patient name, if available
            ocrResponse.patient_name?.let {
                Text(text = "Patient: $it", style = MaterialTheme.typography.bodyLarge)
            }*/

            // Prescription details
        }
    }
    }

}
@Preview(showBackground = true)
@Composable
fun PreviewFolderScreen() {

   // OCRItem(OCRResponse("","",image_name="file-1732749608704-151419999.jpg", __v = 0), LocalContext.current)


    // Use a mocked version of the OCRViewModel

    // FolderScreen composable with mocked viewModel
   FolderScreen(navController = rememberNavController(), Modifier)
}
