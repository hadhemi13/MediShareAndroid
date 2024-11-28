package com.example.medishareandroid.views

import android.icu.text.CaseMap.Title
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


@Composable
fun RecommandationScreen(title: String, description:String){
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
                Text(
                    text =title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
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
                    text = description
                )

            }
        }

    }

}