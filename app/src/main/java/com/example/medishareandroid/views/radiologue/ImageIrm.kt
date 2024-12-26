package com.example.medishareandroid.views.radiologue

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.viewModels.radiologue.TumorDetectionViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImageIrm(
    navController: NavController,
    imageId: String,
    imageName: String,
    title: String,
    viewModel: TumorDetectionViewModel = viewModel()
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    Log.d("currentroutr", currentRoute!!)
    val filters = if ("noid" != imageId) {
        mutableListOf("Detect", "Post", "Rotate", "Zoom In", "Zoom Out")
    } else {
        mutableListOf("Detect", "Rotate", "Zoom In", "Zoom Out")
    }

// State to show the dialog
    val alertMessage by viewModel.alertMessage.observeAsState()
    val showDialog = remember { mutableStateOf(false) }
    val rotation = remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val contrast = remember { mutableStateOf(1f) }
    val showContrastSlider = remember { mutableStateOf(false) }
    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = title, color = Color.White) },
                    backgroundColor = Color.Black
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->

                        Button(
                            onClick = {

                                    if (filter == "Post") {
                                        navController.navigate("detailsImage/${imageId}/${imageName}/${title}")

                                    }

                                if (filter == "Detect") {
                                    viewModel.performTumorDetection("./$imageName")
                                    showDialog.value = true // Show dialog on detection
                                }
                                if (filter == "Rotate") {
                                    rotation.value += 90f
                                }
                                if (filter == "Zoom In") {
                                    scale += 0.1f
                                }
                                if (filter == "Zoom Out") {
                                    if (scale > 0.1f) scale -= 0.1f
                                }
                                if (filter == "Adjust Contrast") {
                                    showContrastSlider.value = !showContrastSlider.value
                                }


                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color(0xFF008080),
                                backgroundColor = Color.Black
                            ),
                        ) {
                            Text(filter)
                        }
                    }
                }
            }
        },
        content = {

            if (showDialog.value) {
                alertMessage?.let {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text("Tumor Detection Result") },
                        text = { Text(it) },
                        confirmButton = {
                            Button(
                                onClick = { showDialog.value = false }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
            // Image display
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = BASE_URL + imageName,
                    contentDescription = "Editable Image",
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            rotationZ = rotation.value,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, rotationChange ->
                                scale *= zoom
                                rotation.value += rotationChange

                                // Adjust movement based on rotation
                                val rad = Math.toRadians(rotation.value.toDouble())
                                val sin = kotlin.math
                                    .sin(rad)
                                    .toFloat()
                                val cos = kotlin.math
                                    .cos(rad)
                                    .toFloat()

                                val adjustedX = pan.x * cos - pan.y * sin
                                val adjustedY = pan.x * sin + pan.y * cos

                                offsetX += adjustedX
                                offsetY += adjustedY
                            }
                        }
                        .graphicsLayer {
                            val colorMatrix = ColorMatrix().apply {
                                setToScale(contrast.value, contrast.value, contrast.value, 1f)
                            }
                            // Use the colorFilter inside graphicsLayer for contrast adjustment
                            //     this.colorFilter = ColorMatrixColorFilter(colorMatrix)
                        },
                    contentScale = ContentScale.Crop
                )
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun ImageIrmPreview() {
    ImageIrm(
        imageId = "fdgdhg",
        imageName = "imageName",
        title = "title",
        navController = rememberNavController()
    )
}