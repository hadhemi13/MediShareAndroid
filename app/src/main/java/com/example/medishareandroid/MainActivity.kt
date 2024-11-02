package com.example.medishareandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediSHareAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    logo()
                    Greeting(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun logo() {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "logo",
        modifier = Modifier.padding(end = 20.dp)
    )
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Welcome to",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black


        )



        Text(
            text = "MediShare",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.bleu),
        )

        OutlinedTextField(
            value = username.value,
            onValueChange = {
                username.value = it

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            label = { Text("Username") },
            singleLine = true
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            label = { Text("Password") },
            singleLine = true
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(

                            Color(0xFF4A90E0),
                            Color(0xFF2260FF)
                        )
                    ),
                    shape = MaterialTheme.shapes.small // Adjust shape to match button
                )
        ) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent // Set to transparent to show gradient
                ),
                contentPadding = PaddingValues() // Remove padding for alignment
            ) {
                Text(
                    "Login",
                    fontSize = 20.sp,
                    color = Color.White // Adjust text color as needed
                )
            }
        }

    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MediSHareAndroidTheme {
        Column {
            logo()
            Greeting()
        }
    }
}