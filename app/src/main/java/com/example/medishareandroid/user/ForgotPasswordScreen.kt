package com.example.medishareandroid.user


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.models.User
import com.example.medishareandroid.remote.ForgotPasswordDto
import com.example.medishareandroid.remote.Message
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ForgotPasswordScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val fEmail = remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val usernameFocusRequester = FocusRequester()


    Column(
        modifier.imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,

        ) {
        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = fEmail.value,
            onValueChange = { fEmail.value = it },

            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .focusRequester(usernameFocusRequester),
            /*textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),*/
            label = { Text("Email") },

            //keyboardOptions with ImeAction.Next for navigate to next field
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                //keyboardActions lets focusManager move focus downward
                onDone = {
                    keyboardController?.hide() // Hide the keyboard when Done is pressed
                    focusManager.clearFocus()  // Clear focus from the text field
                    // Trigger login or other actions here if necessary
                }),
            singleLine = true

        )

        Button(
            onClick = {
                //clear focus of fields
                focusManager.clearFocus()
                //keyboard hiding
                keyboardController?.hide()
                handleForgetPassword(context, fEmail)
                /*if (isLoginEnabled) {
                    // Perform login action
                }

                 */
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    //for color gradient
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF4A90E0), Color(0xFF2260FF))
                    ),
                    shape = MaterialTheme.shapes.small
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            //enabled = isLoginEnabled,
            contentPadding = PaddingValues()
        ) {
            Text("Reset your password", fontSize = 20.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.weight(1f))


    }
}

fun handleForgetPassword(context: Context, fEmail: MutableState<String>) {

    RetrofitInstance.getRetrofit().create(UserAPI::class.java)
        .forgetPassword(ForgotPasswordDto( email = fEmail.value))
        .enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    // Get the message from the response body
                    val message = response.body()?.message ?: "Unexpected response"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                } else {
                    // Handle error message
                    val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    Log.d("1",errorMsg)
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Toast.makeText(context, t.message ?: "Network failure", Toast.LENGTH_SHORT).show()
            }
        })




}

@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    val navController = rememberNavController()


    ForgotPasswordScreen(navController)
}