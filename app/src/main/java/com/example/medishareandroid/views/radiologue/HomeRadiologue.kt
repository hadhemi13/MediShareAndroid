package com.example.medishareandroid.views.radiologue

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.medishareandroid.R
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.remote.Post
import com.example.medishareandroid.viewModels.radiologue.FetchPostViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage(viewModel: FetchPostViewModel= viewModel(), userId: String) {

    val posts by  viewModel.postsResponse.observeAsState()

    // Fetch posts from the ViewModel when the composable is first launched
    LaunchedEffect(userId) {
        viewModel.fetchPosts(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MediShare") },
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        }
    ) {
        Column {
            val filters = listOf("Recent", "Trending", "Best", "Risking")
            Log.d("Homeradiologue", "column1----------------")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF008080), backgroundColor = Color.White),
                    ) {
                        Text(filter)
                    }
                }
            }
            Log.d("Homeradiologue", "column2----------------")

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                posts?.let {
                    items(it) { post ->
                        PostCard(post = post)
                    }
                } ?: item {
                    Text("No posts available.", modifier = Modifier.padding(16.dp))
                }
            }
            Log.d("Homeradiologue", "column3----------------")


        }
    }
}
@Composable
fun PostCard(post: Post) {
    var showComments by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Log.d("postss","${post.toString()}")
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.profile_image) ,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(post.author, fontWeight = FontWeight.Bold)
                    Text(post.timeAgo, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(post.Content, style = MaterialTheme.typography.body1)

            Spacer(modifier = Modifier.height(8.dp))
           /* Image(
                painter = painterResource(R.drawable.backgroundprof),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        // Navigate to fullscreen image
                    }
            )*/
            AsyncImage(
                model = BASE_URL +post.image,
                contentDescription = "Network Image",
                modifier = Modifier
                    .height(200.dp).fillMaxWidth().padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spacer
                Spacer(modifier = Modifier.width(2.dp))

                // Arrow Up and Count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Upvote",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = post.upvotes.toString(),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                // Spacer
                Spacer(modifier = Modifier.width(2.dp))

                // Comment Icon
                IconButton(
                    onClick = { showComments = !showComments }
                ) {
                    Icon(
                        imageVector = if (showComments) Icons.Default.CommentBank else Icons.Default.Comment,
                        contentDescription = "Toggle Comments",
                        tint = Color(0xFF113155)
                    )
                }

                // Spacer
                Spacer(modifier = Modifier.width(2.dp))

                // Share Icon
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.Gray
                )

                // Spacer
                Spacer(modifier = Modifier.width(2.dp))
            }
            if (showComments) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Example comments
                    val comments = listOf("Nice post!", "Interesting content")
                    comments.forEach { comment ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(comment, style = MaterialTheme.typography.body2)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text("Add a comment...") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            if (commentText.isNotEmpty()) {
                                // Save comment
                                commentText = ""
                            }
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Send Comment")
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun HomePagePreview() {

    val dummyPosts = listOf(
        Post(id = "1", "",1,Content = "Sample post content", timeAgo = "dsdsf", subreddit = "",
            author = "", profileImage = "", image="", statePost = false
        ),
        Post(id = "1", "",1,Content = "Sample post content", timeAgo = "dsdsf", subreddit = "",
            author = "", profileImage = "", image="", statePost = false
        ),    )
    val dummyViewModel = FetchPostViewModel().apply {
        _postsResponse.value = dummyPosts
    }
    HomePage(userId = "55", viewModel = dummyViewModel)}
