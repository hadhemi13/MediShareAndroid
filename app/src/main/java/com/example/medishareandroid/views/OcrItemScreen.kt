package com.example.medishareandroid.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.medishareandroid.remote.BASE_URL

@Composable
fun OcrItemScreen(imageName: String, title: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),


            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Optional padding
                contentAlignment = Alignment.Center // Align children to the center
            ) {
        AsyncImage(
            model = BASE_URL +"upload/"+imageName,
            contentDescription = "Network Image",
            modifier = Modifier
                .padding(8.dp)


        )}}
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),

            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Optional padding
                contentAlignment = Alignment.Center // Align children to the center
            ) {
                // Image name
                Text(
                    text =title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
        }}

    }

}