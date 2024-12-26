package com.example.medishareandroid.views.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.medishareandroid.remote.BASE_URL


@Composable
fun RecommandationScreen(title: String, description: String, image: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),


            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    // Image Section
                    AsyncImage(
                        model = BASE_URL + "upload/" + image,
                        contentDescription = "Network Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    // Title Section
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Description Section with Vertical Line
                    Row(
                        modifier = Modifier
                            .fillMaxWidth() // Ensure Row fills available width
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight() // Ensure Box fills available height
                                .width(2.dp)
                                .background(color = Color.Black)
                        ){}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(8.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        }


    }

}


@Preview(showBackground = true)
@Composable
fun RecommendationScreenPreview() {
    RecommandationScreen("title", description = "description", image = "image")
}