//user/LoginScreen.kt
package com.example.medishareandroid.views.authScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.asPaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.ClickableText
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
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.focus.focusModifier
//import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.models.LoginRequest
import com.example.medishareandroid.models.User
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.auth.AuthViewModel
import com.example.medishareandroid.viewModels.auth.AuthViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable

fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit
) {
    //tates for Username and Password
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    /*val myFontFamily = FontFamily(
        Font(R.font.itimregular), // Remplacez par le nom de votre fichier de police
    )*/
    val mediFontFamily = FontFamily(
        Font(R.font.chewyregular), // Remplacez par le nom de votre fichier de police
    )
//context
    val context = LocalContext.current
//Focus Management for making navigation between text fields smoother
    val focusManager = LocalFocusManager.current
    val usernameFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()

//keyboardController allow manually show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
//val isLoginEnabled = username.value.isNotBlank() && password.value.isNotBlank()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(R.color.passback)
            )
    )
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
                .padding(start = 25.dp)
                .padding(end = 25.dp)

                .size(160.dp)
        )
        Text(
            text = "Welcome to",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.sign),


            )
        Text(
            text = "MediShare",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.signup),
            fontFamily = mediFontFamily
        )
        Spacer(modifier = Modifier.weight(0.5f))

        //OutlinedTextField's Username to accept input //like text field
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(top = 14.dp)
                .padding(start = 25.dp)
                .padding(end = 25.dp)
                .focusRequester(usernameFocusRequester),
            /*textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),*/
            label = {
                Text(
                    "Username",
                    color = colorResource(R.color.sign)
                )
            },
            //keyboardOptions with ImeAction.Next for navigate to next field
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),

            //keyboardActions lets focusManager move focus downward
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
            singleLine = true,
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
            )

        )


        // password field
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(bottom = 14.dp)
                .padding(start = 25.dp)
                .padding(end = 25.dp)
                .focusRequester(passwordFocusRequester),
            /*textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp
            ),*/

            label = {
                Text(
                    "Password",
                    color = colorResource(R.color.sign)
                )
            },
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
            singleLine = true,
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
            )
        )




        Row {


            // forgot password's ClickableText
            ClickableText(

                text = buildAnnotatedString {
                    modifier
                        .padding(10.dp)
                        .padding(bottom = 14.dp)
                        .padding(top = 14.dp)
                        .padding(start = 25.dp)
                        .padding(end = 25.dp)

                    append("Forgot your password?")
                    pushStringAnnotation(tag = "RESET_PASSWORD", annotation = "reset_password")

                    pop()
                },
                onClick = {
                },
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(R.color.sign)

                )

            )
            ClickableText(
                text = buildAnnotatedString {
                    append("Reset it here.")
                    pushStringAnnotation(tag = "RESET_PASSWORD", annotation = "reset_password")


                    pop()
                },
                onClick = {
                    navController.navigate("forgotPassword")
                },
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(R.color.sign),
                    textDecoration = TextDecoration.Underline,

                    )

            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(top = 15.dp)
                .padding(start = 25.dp)
                .padding(end = 25.dp)
                .background(
                    color = colorResource(R.color.sign),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            // login button
            Button(
                onClick = {
                    //clear focus of fields
                    focusManager.clearFocus()
                    //keyboard hiding
                    keyboardController?.hide()
                    handleNavigation(context, email, password, navController) { onLoginClick() }

                    /*if (isLoginEnabled) {
                        // Perform login action
                    }

                     */
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                //enabled = isLoginEnabled,
                contentPadding = PaddingValues()
            ) {
                Text("Sign in", fontSize = 20.sp, color = Color.White)
            }
        }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(top = 12.dp)
                .padding(start = 25.dp)
                .padding(end = 25.dp)
                .background(
                    color = colorResource(R.color.signupdegg),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(),

                ) {


                // Ajout de l'icône Google
                Image(
                    painter = painterResource(id = R.drawable.google), // Remplacez par votre ressource d'image
                    contentDescription = "Google logo",
                    modifier = Modifier.size(24.dp) // Ajustez la taille selon vos besoins
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espace entre l'icône et le texte
                Text(
                    "Sign in with Google",
                    fontSize = 20.sp,
                    color = colorResource(R.color.sign)
                )
            }

        }
        Spacer(modifier = Modifier.weight(1f))

        //spacer
        Spacer(modifier = Modifier.weight(1f))
        //signup route
        Row {

            Text(

                text = "Don't have an account? ",
                color = colorResource(R.color.sign)
            )
            Text(
                text = "Create One",
                fontSize = 16.sp,
                color = colorResource(R.color.sign), // Utilisez une couleur qui se démarque
                textDecoration = TextDecoration.Underline,
                modifier = Modifier

                    .clickable {
                        navController.navigate("signup")
                    },
                textAlign = TextAlign.Center // Centrez le texte si vous le souhaitez
            )

            //signup route's ClickableText

        }
        Spacer(modifier = Modifier.weight(1f))


    }
}

fun handleNavigation(
    context: Context,
    email: MutableState<String>,
    password: MutableState<String>,
    navController: NavHostController,
    onLoginClick: () -> Unit
) {
    val userAPI = RetrofitInstance.getRetrofit().create(UserAPI::class.java)
    val loginRequest = LoginRequest(email = email.value, password = password.value)

    userAPI.login(loginRequest).enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!

                // Instanciation de PreferencesRepository pour stocker les informations de l'utilisateur
                val preferencesRepository = PreferencesRepository(context)
                preferencesRepository.setName(user.userName)
                preferencesRepository.setEmail(user.userEmail)
                preferencesRepository.setId(user.userId)
                preferencesRepository.setToken(user.accessToken)
                preferencesRepository.setRole(user.userRole)

                Toast.makeText(context, "Connection successful", Toast.LENGTH_SHORT).show()

                // Appel de la fonction onLoginClick après succès de connexion
                onLoginClick()
                if (user.userRole == "patient") {
                    // Navigation vers la page d'accueil
                    navController.navigate("homePage") {
                        popUpTo("loginPage") {
                            inclusive = true
                        } // Retirer la page de login de la pile
                    }
                } else {
                    navController.navigate("homeRadiologue") {
                        popUpTo("launchScreen") {
                            inclusive = true
                        } // Retirer la page de login de la pile
                        popUpTo("launchScreen") {
                            inclusive = true
                        }
                    }

                }

            } else {
                val errorMessage = response.errorBody()?.string() ?: "Login failed"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}


@Preview(showBackground = true)
@Composable
fun LoginPreviaw() {

    val context = LocalContext.current

    val preferencesRepository = PreferencesRepository(context)
    val navController = rememberNavController()
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(preferencesRepository)
    )
    LoginScreen(navController) { viewModel.loginUser() }


}