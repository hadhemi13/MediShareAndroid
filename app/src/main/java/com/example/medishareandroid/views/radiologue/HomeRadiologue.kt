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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.medishareandroid.R
import com.example.medishareandroid.models.radiologue.DisplayingPosts
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.remote.Post
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.radiologue.CreateCommentViewModel
import com.example.medishareandroid.viewModels.radiologue.FetchPostViewModel
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage(
    viewModel: FetchPostViewModel = viewModel(),
    createCommentViewModel: CreateCommentViewModel = viewModel(),
    userId: String, navController: NavController
) {

    val posts by viewModel.displayingPosts.observeAsState()

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
            /*LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color(0xFF008080),
                            backgroundColor = Color.White
                        ),
                    ) {
                        Text(filter)
                    }
                }
            }*/
            Log.d("Homeradiologue", "column2----------------")

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                posts?.let {
                    items(it.asReversed()) { post ->
                        PostCard(
                            displayingPost = post,
                            createCommentViewModel = createCommentViewModel,
                            viewModel = viewModel, navController= navController
                        )
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
fun PostCard(
    displayingPost: DisplayingPosts,
    createCommentViewModel: CreateCommentViewModel,
    viewModel: FetchPostViewModel,
    navController: NavController
) {
    var showComments by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val result by createCommentViewModel.result.observeAsState() // Ensure it's a StateFlow
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    val userId = prefs.getId()!!
    var upvoteState by remember { mutableStateOf(displayingPost.post.statepost) }
    var nbreVotes by remember { mutableStateOf(displayingPost.post.upvotes) }


    val isLoading by createCommentViewModel.isLoading.observeAsState(false)
    val errorMessage by createCommentViewModel.error.observeAsState()
    LaunchedEffect(result) {
        if (result != null) {
            delay(2000) // 2-second delay
            displayingPost.comments = displayingPost.comments + result!!
        }
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Log.d("postss","${displayingPost.post.toString()}")
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.profile_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(displayingPost.post.author, fontWeight = FontWeight.Bold)
                    Text(displayingPost.post.timeAgo, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(displayingPost.post.Content, style = MaterialTheme.typography.body1)

            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = BASE_URL + displayingPost.post.image,
                contentDescription = "Network Image",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(8.dp).clickable {
                        navController.navigate("imageIrm/noid/${ displayingPost.post.image}/${displayingPost.post.title}")
                    }
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
                    IconButton(
                        onClick = {
                            upvoteState = !upvoteState
                            if(upvoteState){
                                nbreVotes= nbreVotes + 1
                            }else
                            {
                                nbreVotes= nbreVotes - 1
                            }
                            viewModel.setUpvotes(displayingPost.post.id, userId)



                        }
                    ) {
                        Icon(

                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Upvote",
                            tint = if (upvoteState) Color.Blue else Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = nbreVotes.toString(),
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

                    // displayingPost.comments
                    //val comments = listOf("Nice post!", "Interesting content")
                    displayingPost.comments
                        .forEach { comment ->

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(comment.comment, style = MaterialTheme.typography.body2)
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
                                displayingPost.comments
                                // Save comment
                                createCommentViewModel.createComment(
                                    displayingPost.post.id,
                                    prefs.getId()!!,
                                    commentText
                                )
                                //add comment
                                /*if (result == null) {

                                    Toast.makeText(context, "Error: ${errorMessage ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                                }*/
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
        Post(
            id = "1", "", 1, Content = "Sample post content", timeAgo = "dsdsf", subreddit = "",
            author = "", profileImage = "", image = "", statepost = false
        ),
        Post(
            id = "1", "", 1, Content = "Sample post content", timeAgo = "dsdsf", subreddit = "",
            author = "", profileImage = "", image = "", statepost = false
        ),
    )
    val dummyViewModel = FetchPostViewModel().apply {
        _postsResponse.value = dummyPosts
    }
    val navController= rememberNavController()
    HomePage(userId = "55", viewModel = dummyViewModel , navController = navController)
}
