package com.example.medishareandroid.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.remote.DiscussionRes
import com.example.medishareandroid.viewModels.patient.FetchDiscussionViewModel


import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.medishareandroid.repositories.PreferencesRepository

@Composable
fun TopPeopleScreen(
    navController: NavController,
    fetchDiscussionViewModel: FetchDiscussionViewModel = viewModel()
) {
    val context = LocalContext.current
    // Observe the LiveData from the ViewModel
    val discussions by fetchDiscussionViewModel.discussions.observeAsState(emptyList())
    val errorMessage by fetchDiscussionViewModel.errorMessage.observeAsState("")
    val prefs = PreferencesRepository(context)
    // Trigger fetching discussions (replace "123" with the actual userId)
    LaunchedEffect(Unit) {
        fetchDiscussionViewModel.fetchDiscussions(prefs.getId()!!)
    }

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
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // Button to start a new discussion
        Text(
            text = "New discussion\n",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
                .clickable { navController.navigate("newChat") }
        )

        Spacer(modifier = Modifier.height(16.dp)) // Add space before the list of people

        // Display the list or an error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn  {
                items(discussions.asReversed()) { discussion ->
                    PersonCard(discussion, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

fun DiscussionRes.toPerson(): Person {
    return Person(
        name = this.title,
        title = "Discussion ID: ${this._id}",
        image = R.drawable.chatgpt
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonCard(person: DiscussionRes, navController: NavController) {
    Card(
        onClick = {
            navController.navigate("newChat/${person._id}")

        },
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),


        ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.chatgpt),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(text = person.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)

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
