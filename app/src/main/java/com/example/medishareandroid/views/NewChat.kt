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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomAppBar
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.History
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
    //val chatResponse by chatViewModel.chatResponse.observeAsState()

    // Charger la discussion existent ou démarrer une nouvelle
    LaunchedEffect(Unit) {
        if (!discussionId.isNullOrBlank()) {
            chatViewModel.getDiscussion(discussionId, context, userId)
        }
    }

    val messageText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barre Supérieure
        TopAppBar(
            backgroundColor = Color.White,

            title = { Text("Discussion", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* Gérer l'historique */ }) {
                    Icon(Icons.Default.History, contentDescription = "History")
                }
            }
        )

        // Afficher les messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                //.padding(horizontal = 8.dp)
                .padding(bottom = 100.dp)
        ) {
            items(chatMessages) { message ->
                Log.d("mfdgfd",message.content)
                ChatBubble(
                    message = message.content,
                    isUser = message.role == "user",
                    userImage = if (message.role == "user") R.drawable.profile_image else R.drawable.chatgpt
                )
            }
        }

        // Entrée de Message
        BottomAppBar(
            backgroundColor = Color.White,
            content = {
                TextField(
                    value = messageText.value,
                    onValueChange = { messageText.value = it },
                    placeholder = { Text("Tapez quelque chose...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = {
                    if (messageText.value.isNotEmpty()) {
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
                        messageText.value = "" // Réinitialiser le champ
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Envoyer")
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
            modifier = Modifier.padding()
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
    ChatBubble(isUser = false, message = "this is ia chat", userImage = R.drawable.chatgpt)
    ChatBubble(isUser = true, message = "this is human chat", userImage = R.drawable.profile_image)
}

