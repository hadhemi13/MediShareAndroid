package com.example.medishareandroid.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MediShareScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2F4F9A))
            .padding(16.dp)
    ) {
        // Top Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome to MediShare",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Senior Project Manager",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "Tokopedia - Jakarta, ID",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                // Profile Icon Placeholder
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search a radiologist or clinic",
                color = Color.Gray,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = Color(0xFFDFDFDF)
            ) {
                // Filter Icon Placeholder
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabItem("Home", true)
            TabItem("Clinics & radiologists", false)
            TabItem("Recommendations", false)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MainButton("Clinics and Radiologists list", Color(0xFFBDEFFF))
            MainButton("Recommendations", Color(0xFFF9E5FF))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MainButton("Chat", Color(0xFFFFE5CD))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Clinics and Radiologists Section
        SectionTitle("Clinics and Radiologists")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem("Graphic Designer", "Bukalapak - Jakarta, ID", "$50 - $75 / Mo", Color(0xFFFFF1F3))
            CardItem("UX Writer", "Gojek - Jakarta, ID", "$50 - $75 / Mo", Color(0xFFF0F9F0))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recommendations Section
        SectionTitle("Recommendations")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem("FE Developer", "Google - Jakarta, ID", "$50 - $75 / Mo", Color(0xFFF3F3F3))
            CardItem("FE Developer", "Twitter - Jakarta, ID", "$50 - $75 / Mo", Color(0xFFF3F3F3))
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean) {
    Text(
        text = text,
        color = if (isSelected) Color.Black else Color.Gray,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(
                if (isSelected) Color.White else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}

@Composable
fun MainButton(text: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(150.dp, 80.dp)
            .background(backgroundColor, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CardItem(title: String, location: String, price: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(150.dp, 200.dp)
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = location, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = price, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MediShareScreenPreview() {
    MediShareScreen()
}
