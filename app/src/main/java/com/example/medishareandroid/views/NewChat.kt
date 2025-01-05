package com.example.medishareandroid.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomAppBar
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medishareandroid.R
import com.example.medishareandroid.models.chat.MsgDouble
import com.example.medishareandroid.viewModels.patient.ChatViewModel

@Composable
fun ChatScreen(
    userId: String,
    discussionId: String? = null,
    chatViewModel: ChatViewModel = viewModel()
) {
    val context = LocalContext.current
    val chatMessages by chatViewModel.chatMessages.observeAsState(emptyList())
    val currentDiscussionId by chatViewModel.discussionId.observeAsState()
    val isLoading by chatViewModel.loading.observeAsState(false)
    val messageText = remember { mutableStateOf("") }
    val messageAwaiting = remember { mutableStateOf("") }


    // Handle the message sending
    val sendMessage = {
        if (messageText.value.isNotEmpty()) {
            // Create a new message object
            val newMessage = MsgDouble(content = messageText.value, role = "user")
            messageAwaiting.value = messageText.value
            // Add the message to chatMessages immediately
            chatMessages.toMutableList().apply {
                add(0, newMessage) // Add the message at the top (newest at the bottom)
            }

            // Send the message to the server
            if (!currentDiscussionId.isNullOrBlank()) {
                chatViewModel.sendMsg(
                    messageText.value,
                    currentDiscussionId!!,
                    context,
                    userId
                )
            } else {
                chatViewModel.createChat(messageText.value, context, userId)
            }

            // Clear the input field
            messageText.value = ""
        }
    }

    // Fetch discussion if discussionId is provided
    LaunchedEffect(discussionId) {
        if (!discussionId.isNullOrBlank()) {
            chatViewModel.getDiscussion(discussionId, context, userId)
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.White,
                content = {
                    TextField(
                        value = messageText.value,
                        onValueChange = { messageText.value = it },
                        placeholder = { Text("Type something...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(onClick = sendMessage) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Adjust content to avoid overlap with BottomAppBar
        ) {
            // Top App Bar
            TopAppBar(
                backgroundColor = Color.White,
                title = { Text("Discussion", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* Handle history */ }) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )

            // Chat Messages List
            LazyColumn(
                modifier = Modifier.weight(1f), // Take up remaining space after TopAppBar
                reverseLayout = true
            ) {
                items(chatMessages.asReversed()) { message ->
                    ChatBubble(
                        message = message.content,
                        isUser = message.role == "user",
                        userImage = if (message.role == "user") R.drawable.profile_image else R.drawable.chatgpt
                    )
                }

                // Typing Indicator
                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            TypingIndicator()
                        }
                    }
                }
            }
            if (isLoading) {
                ChatBubble(
                    isUser = true,
                    message = messageAwaiting.value,
                    userImage = R.drawable.profile_image
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp), // Adjust the padding if necessary
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        //horizontalArrangement = Arrangement.Center // Ensure the indicator is centered
                    ) {
                        TypingIndicator() // This will be at the bottom
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, isUser: Boolean, userImage: Int) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Row(
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        if (!isUser) {
            Image(
                painter = painterResource(id = userImage),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            backgroundColor = if (isUser) Color(0xFF0078FF) else Color(0xFFEDEDED),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(4.dp) // Adjust the padding if needed
                .widthIn(max = (screenWidth * 0.7f)) // Set maximum width as 70% of the screen width
        ) {
            Text(message, modifier = Modifier.padding(8.dp),
                color = if (isUser) Color.White else Color.Black)
        }

        /*if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = userImage),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }*/
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    //ChatScreen(userId = "12345")  // Preview with a mock userId
    ChatBubble(isUser = false, message = "this is ia chat", userImage = R.drawable.chatgpt)
    ChatBubble(isUser = true, message = "this is human chat", userImage = R.drawable.profile_image)
}

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    val dotCount = 3
    val dotStates = remember { Array(dotCount) { mutableStateOf(false) } }

    // Launch animation
    LaunchedEffect(Unit) {
        while (true) {
            for (i in 0 until dotCount) {
                dotStates[i].value = true
                kotlinx.coroutines.delay(300L) // Delay between dots
                dotStates[i].value = false
            }
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.chatgpt),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        dotStates.forEach { isVisible ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isVisible.value) Color.Gray else Color.LightGray)
            )
        }
    }
}
