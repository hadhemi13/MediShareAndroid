package com.example.medishareandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R

@Composable
fun TopPeopleScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        // Section with Text and Button
        Text(
            text = "Comment puis-je vous aider ?\n",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // Center the text horizontally
                .padding(bottom = 16.dp) // Add space below the text
        )

        // Button to start a new discussion
        Text(
            text = "New discussion\n",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // Center the text horizontally
                .padding(bottom = 16.dp).clickable { navController.navigate("newChat") }, // Add space below the text

        )
        Spacer(modifier = Modifier.height(16.dp)) // Add space before the list of people

        // List of People
        val people = listOf(
            Person("Elena Ivanova", "Project Head", R.drawable.chatgpt),
            Person("Marie Wondy", "Project Manager", R.drawable.chatgpt),
            Person("Oskar Jansson", "Design Head", R.drawable.chatgpt),
            Person("Nora Jensen", "UX Writer", R.drawable.chatgpt)
        )

        // Displaying each person in a card
        Column {
            people.forEach { person ->
                PersonCard(person)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun PersonCard(person: Person) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = person.image),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = person.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = person.title, color = Color.Gray, fontSize = 14.sp)
            }
            IconButton(onClick = { /* More options action */ }) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "More options")
            }
        }
    }
}

data class Person(val name: String, val title: String, val image: Int)

@Preview(showBackground = true)
@Composable
fun TopPeopleScreenPreview() {
    TopPeopleScreen(rememberNavController())
}
