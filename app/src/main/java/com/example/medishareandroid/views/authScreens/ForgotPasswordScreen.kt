package com.example.medishareandroid.views.authScreens


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import com.example.medishareandroid.R

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.passback))

    )
    Column(
        modifier.imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,


    ) {
        Spacer(modifier = Modifier.weight(0.8f))

        Image(
            painter = painterResource(R.drawable.mdp),
            contentDescription = "background",
            modifier = Modifier.padding(10.dp).padding(end = 25.dp).padding(start = 30.dp).padding(top = 10.dp).size(140.dp)
        )
        Spacer(modifier = Modifier.weight(0.2f))

        Text(
            text = "Please enter your Email adress to recieve a verification code.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color =  colorResource(R.color.sign),
            textAlign = TextAlign.Center,
            modifier = Modifier .padding(10.dp)
                .padding(end = 25.dp)
                .padding(start = 25.dp)

            )
        OutlinedTextField(
            value = fEmail.value,
            onValueChange = { fEmail.value = it },

            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(top = 40.dp)
                .padding(end = 25.dp)
                .padding(start = 25.dp)
                .padding(bottom = 30.dp)
                .focusRequester(usernameFocusRequester),
            /*textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),*/
            label = {
                Text(
                    "Email",
                    color = colorResource(R.color.sign) // Change la couleur ici
                )
            },

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
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = colorResource(R.color.sign),
                unfocusedLabelColor = Color.Transparent,
                unfocusedLeadingIconColor = colorResource(R.color.sign),
                unfocusedIndicatorColor = colorResource(R.color.sign),
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = colorResource(R.color.sign),
                focusedLabelColor = Color.Transparent,
                focusedLeadingIconColor = colorResource(R.color.sign),
                focusedIndicatorColor = colorResource(R.color.sign),
                focusedContainerColor = Color.Transparent
            ),
            singleLine = true

        )

        Button(
            onClick = {



                //clear focus of fields
                focusManager.clearFocus()
                //keyboard hiding
                keyboardController?.hide()
                handleForgetPassword(context, fEmail, navController)
                /*if (isLoginEnabled) {
                    // Perform login action
                }

                 */





            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(end = 25.dp)
                .padding(start = 25.dp)
                .background(
                  color = colorResource(R.color.signupdeg),
                          shape = MaterialTheme.shapes.small

                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            //enabled = isLoginEnabled,
            contentPadding = PaddingValues()
        ) {
            Text("Send mail", fontSize = 20.sp, color = colorResource(R.color.passback))
        }
        Spacer(modifier = Modifier.weight(1f))


    }
}


fun handleForgetPassword(context: Context, fEmail: MutableState<String>, navController:NavHostController) {

    RetrofitInstance.getRetrofit().create(UserAPI::class.java)
        .forgetPassword(ForgotPasswordDto( email = fEmail.value))
        .enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    // Get the message from the response body
                    val message = response.body()?.message ?: "Unexpected response"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    navController.navigate("forgotPasswordMail")
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