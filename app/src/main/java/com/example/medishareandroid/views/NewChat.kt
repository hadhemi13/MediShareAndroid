package com.example.medishareandroid.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medishareandroid.R
import com.example.medishareandroid.viewModels.ChatViewModel
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = viewModel(),
    userId: String,
    discussionId: String? = null // Optional parameter to check if there's an ongoing discussion
) {
    val context = LocalContext.current
    val chatMessages by chatViewModel.chatMessages.observeAsState(emptyList())
    val currentDiscussionId by chatViewModel.discussionId.observeAsState()

    // Launch effect to load messages if there's a discussionId or start new conversation
    LaunchedEffect(Unit) {
        Log.d("launched new chat", "is launched true")
        if(!discussionId.isNullOrBlank()) {
            chatViewModel.getDiscussion(discussionId!!, context, userId)
        }
    }

    val messageText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopAppBar(
            backgroundColor = Color(0xC10A46F3),
            navigationIcon = {
                IconButton(onClick = { /* Handle back action */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            title = { Text("New Discussion", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* Handle history action */ }) {
                    Icon(Icons.Default.History, contentDescription = "Discussion History")
                }
            }
        )

        // Chat Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            items(chatMessages) { message ->
                ChatBubble(message = message.content, isUser = message.role=="user", userImage = if (message.role=="user") R.drawable.profile_image else R.drawable.chatgpt)
            }
        }


        // Message Input
        BottomAppBar(
            backgroundColor = Color(0xC10A46F3),
            content = {
                TextField(
                    value = messageText.value,
                    onValueChange = { messageText.value = it },
                    placeholder = { Text("Type something...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = {
                    if (messageText.value.isNotEmpty()) {
                        // Send message with the current discussionId
                        if (!currentDiscussionId.isNullOrBlank()) {
                            Log.d("launched new chat", "is send messege true")

                            chatViewModel.sendMsg(
                                messageText.value,
                                currentDiscussionId!!,
                                context,
                                userId
                            )
                        } else {
                            Log.d("launched new chat", "is create chat true")

                            chatViewModel.createChat(messageText.value, context, userId)
                            Log.d("launched new chat", "is create chat true finish")

                        }
                        messageText.value = ""  // Clear input after sending
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        )
    }
}

@Composable
fun ChatBubble(message: String, isUser: Boolean, userImage: Int) {
    Row(
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
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
            backgroundColor = if (isUser) Color(0xFFEDEDED) else Color(0x2F0A46F3),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(message, modifier = Modifier.padding(8.dp))
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = userImage),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(userId = "12345")  // Preview with a mock userId
}

