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

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.remote.Message
import com.example.medishareandroid.remote.ResetPasswordDto
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun NewPassword(navController: NavHostController, modifier: Modifier = Modifier, resetToken: String?) {
    Log.d("test1","_____________________________________")
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val password = remember { mutableStateOf("") }
    val confirmpassword = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf("") }
    fun isValidPassword(password: String): Boolean {
        return password.length >= 4 &&
                password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(com.example.medishareandroid.R.color.passback))

    )
    Column(
        modifier.imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,


        ) {
        Spacer(modifier = Modifier.weight(0.8f))

        Image(
            painter = painterResource(R.drawable.lock),
            contentDescription = "background",
            modifier = Modifier.padding(10.dp).padding(top = 10.dp).size(100.dp)
        )
        Spacer(modifier = Modifier.weight(0.2f))

        Text(
            text = "Your new password must be different from the previously used password.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color =  colorResource(R.color.sign),
            textAlign = TextAlign.Center,
            modifier = Modifier .padding(10.dp)
                .padding(end = 25.dp)
                .padding(start = 25.dp)

        )
        PasswordField(
            password = password.value,
            onPasswordChange = { newPassword ->
                password.value = newPassword
                passwordError.value = if (isValidPassword(newPassword)) {
                    "" // Clear error if valid
                } else {
                    "Password must be at least 4 characters, contain a digit, an uppercase letter, and a lowercase letter."
                }
            },
            label = "New Password",
            isError = passwordError.value.isNotEmpty() // Set isError based on password error
        )

        if (passwordError.value.isNotEmpty()) {
            Text(
                text = passwordError.value,
                color = colorResource(R.color.sign),
                modifier = Modifier.padding(start = 25.dp, end = 25.dp)
            )
        }

// Confirm Password Field
        PasswordField(
            password = confirmpassword.value,
            onPasswordChange = { confirmPasswordInput ->
                confirmpassword.value = confirmPasswordInput
                confirmPasswordError.value = if (confirmPasswordInput == password.value) {
                    "" // Clear error if passwords match
                } else {
                    "Passwords do not match."
                }
            },
            label = "Confirm Password",
            isError = confirmPasswordError.value.isNotEmpty() // Set isError based on confirm password error
        )

        if (confirmPasswordError.value.isNotEmpty()) {
            Text(
                text = confirmPasswordError.value,
                color = colorResource(R.color.sign),
                modifier = Modifier.padding(start = 25.dp, end = 25.dp)
            )
        }
        Button(
            onClick = {
               ///clear focus of fields
                focusManager.clearFocus()
                //keyboard hiding
                keyboardController?.hide()
               // handleForgetPasswordMail(context, fEmail)
                /*if (isLoginEnabled) {
                    // Perform login action
                }

                 */
                if (password.value != confirmpassword.value)
                    Toast.makeText(context, "The passwords do not match. Try again.", Toast.LENGTH_SHORT).show()

                else
                    handleNewPassword(navController,context, password, resetToken.toString())


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
            Text("Change password", fontSize = 20.sp, color = colorResource(R.color.passback))
        }
        Spacer(modifier = Modifier.weight(1f))


    }
}

fun handleNewPassword(navController: NavHostController, context: Context, password: MutableState<String>, resetToken: String) {

    RetrofitInstance.getRetrofit().create(UserAPI::class.java)
        .resetPassword(ResetPasswordDto( resetToken = resetToken, newPassword = password.value ))
        .enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    // Get the message from the response body
                    val message = response.body()?.message ?: "Unexpected response"

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
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
fun PreviewNewPassword() {
    val navController = rememberNavController()


    NewPassword(navController, resetToken = "")
}