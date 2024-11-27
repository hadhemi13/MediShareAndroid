package com.example.medishareandroid.views

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medishareandroid.R
@Composable
fun ChatScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopAppBar(
            backgroundColor = Color(0xC10A46F3),
            navigationIcon = {
                IconButton(onClick = { /* Navigate back action */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("New discussion", fontWeight = FontWeight.Bold)
                    }
                }
            },
            actions = {
                IconButton(onClick = { /* Call action */ }) {
                    Icon(Icons.Default.History, contentDescription = "Historique des discussions")
                }
            }
        )

        // Chat Content
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        ) {
            item {
                ChatBubble(
                    "Hey mate, how’s all going?",
                    isUser = true,
                    userImage = R.drawable.hadh
                )
                ChatBubble(
                    "Yeah, everything good! What’s your project update?",
                    isUser = false,
                    userImage = R.drawable.chatgpt
                )
                ChatBubble(
                    "No trouble! Look at these!!!",
                    isUser = true,
                    userImage = R.drawable.hadh
                )
            }
        }

        // Message Input
        BottomAppBar(
            backgroundColor = Color(0xC10A46F3),

            content = {
                TextField(
                    value = "",
                    onValueChange = { /* Update text */ },
                    placeholder = { Text("Type something...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = { /* Send action */ }) {
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
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    ) {
        if (!isUser) {
            Image(
                painter = painterResource(id = userImage),
                contentDescription = null,
                modifier = Modifier.size(30.dp).clip(RoundedCornerShape(20.dp))
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
                modifier = Modifier.size(30.dp).clip(RoundedCornerShape(20.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen()
}
