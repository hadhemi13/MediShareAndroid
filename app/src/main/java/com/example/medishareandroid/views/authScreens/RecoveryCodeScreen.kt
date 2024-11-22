
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import com.example.medishareandroid.R

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

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
import com.example.medishareandroid.remote.ResetToken
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import com.example.medishareandroid.remote.VerifyOtpDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun RecoveryCodeSceen(navController: NavHostController, modifier: Modifier = Modifier) {
    val fEmail = remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val codeDigit1 = remember { mutableStateOf("") }
    val codeDigit2 = remember { mutableStateOf("") }
    val codeDigit3 = remember { mutableStateOf("") }
    val codeDigit4 = remember { mutableStateOf("") }
    val codeDigit5 = remember { mutableStateOf("") }

    val codeDigit6 = remember { mutableStateOf("") }


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
            painter = painterResource(R.drawable.mail),
            contentDescription = "background",
            modifier = Modifier
                .padding(10.dp)
                .padding(top = 10.dp)
                .size(100.dp)
        )
        Spacer(modifier = Modifier.weight(0.2f))

        Text(
            text = "Please enter the 4 digit code to reset your password.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color =  colorResource(R.color.sign),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .padding(end = 25.dp)
                .padding(start = 25.dp)

        )
        Spacer(modifier = Modifier.weight(0.2f))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Create 4 OutlinedTextFields for each digit
            val textFieldModifier = Modifier
                .size(50.dp) // Set a size for each text field
                .padding(0.dp) // Padding around each text field

            OutlinedTextField(
                value = codeDigit1.value,
                onValueChange = {
                    if (it.length <= 1) codeDigit1.value = it // Limit to 1 character
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),    colors = TextFieldDefaults.colors(
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
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp), // Ajustez ici la taille de la police

                singleLine = true
            )
            OutlinedTextField(
                value = codeDigit2.value,
                onValueChange = {
                    if (it.length <= 1) codeDigit2.value = it // Limit to 1 character
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),    colors = TextFieldDefaults.colors(
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
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp), // Ajustez ici la taille de la police

                singleLine = true
            )
            OutlinedTextField(
                value = codeDigit3.value,
                onValueChange = {
                    if (it.length <= 1) codeDigit3.value = it // Limit to 1 character
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),    colors = TextFieldDefaults.colors(
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
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp), // Ajustez ici la taille de la police

                singleLine = true
            )
            OutlinedTextField(
                value = codeDigit4.value,
                onValueChange = {
                    if (it.length <= 1) codeDigit4.value = it // Limit to 1 character
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp), // Ajustez ici la taille de la police

                singleLine = true
            )
            OutlinedTextField(
                value = codeDigit5.value,
                onValueChange = {
                    if (it.length <= 1) codeDigit5.value = it // Limit to 1 character
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp), // Ajustez ici la taille de la police

                singleLine = true
            )
            OutlinedTextField(
                value = codeDigit6.value,
                onValueChange = {
                    if (it.length <= 1) codeDigit6.value = it // Limit to 1 character
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp), // Ajustez ici la taille de la police

                singleLine = true
            )
        }
        Spacer(modifier = Modifier.weight(0.2f))

        Button(
            onClick = {
                //clear focus of fields
                focusManager.clearFocus()
                //keyboard hiding
                keyboardController?.hide()
                handleForgetPasswordMail(context, fEmail)
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
                    color = colorResource(R.color.signupdegg),
                    shape = MaterialTheme.shapes.small

                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            //enabled = isLoginEnabled,
            contentPadding = PaddingValues()
        ) {
            Text("Resend code", fontSize = 20.sp, color = colorResource(R.color.sign))
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            onClick = {
                /*
                //clear focus of fields
                focusManager.clearFocus()
                //keyboard hiding
                keyboardController?.hide()
                handleForgetPasswordMail(context, fEmail)
                /*if (isLoginEnabled) {
                    // Perform login action
                }

                 */*/

                val recoveryCode = codeDigit1.value + codeDigit2.value + codeDigit3.value +
                        codeDigit4.value + codeDigit5.value + codeDigit6.value

                handleVerifyRecoveryCode(context, recoveryCode, navController)




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
            Text("Reset your password", fontSize = 20.sp, color = colorResource(R.color.passback))
        }
        Spacer(modifier = Modifier.weight(0.7f))


    }
}

fun handleForgetPasswordMail(context: Context, fEmail: MutableState<String>) {

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
fun handleVerifyRecoveryCode(context: Context, recoveryCode1:String, navController: NavHostController){


    RetrofitInstance.getRetrofit().create(UserAPI::class.java)
        .verifyOtp(
            VerifyOtpDto(recoveryCode = recoveryCode1))
        .enqueue(object : Callback<ResetToken> {
            override fun onResponse(call: Call<ResetToken>, response: Response<ResetToken>) {
                if (response.isSuccessful) {
                    //navController.navigate("news")
                    Toast.makeText(context, "Recovery Code Verified", Toast.LENGTH_SHORT).show()
                    navController.navigate("newPassword/${response.body()!!.resetToken}")
                }
                else
                //Toast.makeText(navController.context, response.errorBody()!!.string(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, response.errorBody()!!.string(), Toast.LENGTH_SHORT)
                        .show()

            }

            override fun onFailure(call: Call<ResetToken>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()

            }

        })
}

@Preview(showBackground = true)
@Composable
fun PreviewRecoveryCodeSceen() {
    val navController = rememberNavController()


    RecoveryCodeSceen(navController)
}