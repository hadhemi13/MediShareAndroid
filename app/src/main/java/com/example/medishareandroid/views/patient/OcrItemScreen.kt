package com.example.medishareandroid.views.patient

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.viewModels.patient.OCRViewModel

@Composable
fun OcrItemScreen(id: String, viewModel: OCRViewModel = viewModel()) {
    val ocrByIdResponse by viewModel.ocrByIdResponse.observeAsState()
    val error by viewModel.errorMessage1.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)

    LaunchedEffect(Unit) {
        Log.d("launch getOcrById",id)
        viewModel.getOcrById(id)
        Log.d("launch getOcrById",id)

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Show loading indicator or error message
        when {
            isLoading -> Text("Loading...", fontSize = 16.sp, color = Color.Gray)
            error.isNotEmpty() -> Text(error, fontSize = 16.sp, color = Color.Red)
            ocrByIdResponse != null -> {
                val data = ocrByIdResponse!!

                // Display Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FileOpen,
                        contentDescription = "Title icon",
                        tint = Color.Blue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = data!!["title"]?.toString() ?: "No Title",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Card for Image and Attributes
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Display Image
                        AsyncImage(
                            model = "${BASE_URL}upload/${data!!["image_name"]}",
                            contentDescription = "OCR Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        // Render Dynamic Attributes
                        data.filterKeys { it !in listOf("title", "image_name", "_id", "userId", "__v") }.forEach { (key, value) ->
                            AttributeRow(key.toString(), value ?: "null")
                            Divider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttributeRow(key: String, value: Any) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "$key icon",
            tint = Color.Blue
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = key.replaceFirstChar { it.uppercase() }, // Capitalize key
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}
