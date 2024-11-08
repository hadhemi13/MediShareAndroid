//user/LoginScreen.kt
package com.example.medishareandroid.user

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.asPaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.ClickableText
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
//import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.models.User
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController:NavHostController, modifier: Modifier = Modifier) {
    //tates for Username and Password
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }


    //context
    val context = LocalContext.current
    //Focus Management for making navigation between text fields smoother
    val focusManager = LocalFocusManager.current
    val usernameFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()

    //keyboardController allow manually show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    //val isLoginEnabled = username.value.isNotBlank() && password.value.isNotBlank()


    Column(
        /* modifier = Modifier
             .fillMaxSize()
             .padding(WindowInsets.systemBars.asPaddingValues())

             ,*/
        //modifier = Modifier
          //  .fillMaxSize()
            //.padding(padding)
            modifier.imePadding(),
            //.padding(16.dp), // General padding for spacing
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,

        ) {
        // spacer user to used to push content towards the center
        Spacer(modifier = Modifier.weight(1f))

        //logo and welcome to our application's text
        Image(

            painter = painterResource(R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .padding(20.dp)
                .size(140.dp)
        )
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


        //OutlinedTextField's Username to accept input //like text field
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .focusRequester(usernameFocusRequester),
            /*textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),*/
            label = { Text("Username") },
            //keyboardOptions with ImeAction.Next for navigate to next field
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),

            //keyboardActions lets focusManager move focus downward
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
            singleLine = true

        )


        // password field
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .focusRequester(passwordFocusRequester),
            /*textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp
            ),*/
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(

                //KeyboardType.Password hides input for privacy.
                keyboardType = KeyboardType.Password,
                //ImeAction.Done changes the keyboard button to "Done"
                //indicating the final field in the form.
                imeAction = ImeAction.Done
            ),
            //make the password invisible
            visualTransformation = PasswordVisualTransformation(),
            //hide key board on done
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide() // Hide the keyboard when Done is pressed
                    focusManager.clearFocus()  // Clear focus from the text field
                    // Trigger login or other actions here if necessary
                }
            ),
            singleLine = true
        )


        // login button
        Button(
            onClick = {
                //clear focus of fields
                focusManager.clearFocus()
                //keyboard hiding
                keyboardController?.hide()
                handleNavigation(context, email, password)
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
            Text("Login", fontSize = 20.sp, color = Color.White)
        }

        // forgot password's ClickableText
        ClickableText(
            text = buildAnnotatedString {
                append("I forgot my password")
                pushStringAnnotation(tag = "RESET_PASSWORD", annotation = "reset_password")

                pop()
            },
            onClick = {
                navController.navigate("forgotPassword")
            },
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                color = colorResource(R.color.bleu)
            )
        )

        //spacer
        Spacer(modifier = Modifier.weight(1f))
        //signup route
        Row {
            Text(
                text = "Wanna try our services? ",
                color = Color.Gray
            )
            //signup route's ClickableText
            ClickableText(
                text = buildAnnotatedString {
                    append("Sign Up")
                    pushStringAnnotation(tag = "RESET_PASSWORD", annotation = "reset_password")

                    pop()
                },
                onClick = {
                },
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    color = colorResource(R.color.bleu),
                    textDecoration = TextDecoration.Underline
                )

            )
        }

    }
}

fun handleNavigation(context:Context, email: MutableState<String>, password: MutableState<String>) {

    RetrofitInstance.getRetrofit().create(UserAPI::class.java).login(User(email = email.value, password = password.value)).enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful)
                //navController.navigate("news")
            Toast.makeText(context, "connection success", Toast.LENGTH_SHORT).show()

            else
                //Toast.makeText(navController.context, response.errorBody()!!.string(), Toast.LENGTH_SHORT).show()
                Toast.makeText(context, response.errorBody()!!.string(), Toast.LENGTH_SHORT).show()

        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()

        }

    })
}




@Preview(showBackground = true)
@Composable
fun LoginPreviaw() {
    val navController = rememberNavController()

    LoginScreen(navController)

}