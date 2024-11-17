package com.example.medishareandroid.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.medishareandroid.models.Visit

@Composable
fun VisitItem(visit: Visit) {
    Image(
        painter = painterResource(id = visit.imageRes),
        contentDescription = "Visit Image",
        modifier = Modifier
            .size(120.dp)
            .clickable { /* TODO: Handle Click on Visit Item */ }
    )
}
