package com.example.medishareandroid.views.authScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.models.SignupRequest
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.remote.UserAPI
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Signup(navController: NavController,modifier: Modifier = Modifier ) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmpassword = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val useremail = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val usernameError = remember { mutableStateOf("") }
    val useremailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf("") }

    val mediFontFamily = FontFamily(
        Font(R.font.chewyregular), // Remplacez par le nom de votre fichier de police
    )

    fun isValidPassword(password: String): Boolean {
        return password.length >= 4 &&
                password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() }
    }

    fun validateInputs() {
        usernameError.value = if (username.value.isEmpty()) "Username cannot be empty" else ""
        useremailError.value = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(useremail.value).matches()) "Invalid email address" else ""

        // Validate password
        passwordError.value = when {
            password.value.isEmpty() -> "Password cannot be empty"
            !isValidPassword(password.value) -> "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
            else -> ""
        }

        // Confirm password validation
        confirmPasswordError.value = if (confirmpassword.value != password.value) "Passwords do not match" else ""
    }
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.passback),
                        colorResource(R.color.passback)
                    ), // Dégradé du blanc au bleu
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()

            .verticalScroll(scrollState)
            .imePadding(),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "background",
            modifier = Modifier
                .padding(10.dp)
                .padding(top = 20.dp)
                .size(140.dp)
        )
        Text(
            text = "Welcome to",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color =  colorResource(R.color.sign),


            )
        Text(
            text = "MediShare",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.signup),
            fontFamily = mediFontFamily
        )

        Column {
            OutlinedTextField(
                value = username.value,
                onValueChange = {
                    username.value = it

                    // Vérifiez ici si l'entrée est valide et mettez à jour le message d'erreur
                    if (it.isNotEmpty()) {
                        usernameError.value = "" // Efface l'erreur si le champ n'est pas vide
                    } else {
                        usernameError.value = "Username cannot be empty" // Mettez à jour le message d'erreur si vide
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(start = 25.dp)
                    .padding(end = 25.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = colorResource(R.color.sign),
                    fontSize = 16.sp,
                ),
                label = {
                    Text(
                        "Username",
                        color = if (usernameError.value.isNotEmpty()) Color.Red else colorResource(R.color.sign) // Change la couleur ici
                    )
                },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = "Username Icon")
                },
                trailingIcon = {
                    if (usernameError.value.isNotEmpty()) {
                        Icon(
                            Icons.Filled.Error, // Remplacez par l'icône d'erreur souhaitée
                            contentDescription = "Error Icon",
                            tint = Color.Red // Change la couleur de l'icône d'erreur
                        )
                    }
                },
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

            // Affichage du message d'erreur
            if (usernameError.value.isNotEmpty()) {
                Text(
                    text = usernameError.value, // Assurez-vous que cette valeur contient le message d'erreur
                    color = colorResource(R.color.sign),
                    textAlign = TextAlign.Center,

                    modifier = Modifier.padding(start = 80.dp, end = 25.dp) // Ajustez le padding selon vos besoins
                )
            }
        }

        OutlinedTextField(
            value = useremail.value,
            onValueChange = {
                useremail.value = it

                // Vérifiez ici si l'entrée est valide et mettez à jour le message d'erreur
                if (isValidEmail(it)) {
                    useremailError.value = "" // Efface l'erreur si le format est valide
                } else if (it.isEmpty()) {
                    useremailError.value = "Email cannot be empty" // Mettez à jour le message d'erreur si vide
                } else {
                    useremailError.value = "Invalid email format" // Mettez à jour le message d'erreur si format invalide
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 25.dp)
                .padding(end = 25.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = colorResource(R.color.sign),
                fontSize = 16.sp,
            ),
            label = {
                Text(
                    "Email",
                    color = if (useremailError.value.isNotEmpty()) Color.Red else colorResource(R.color.sign) // Change la couleur ici
                )
            },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Email Icon")
            },
            trailingIcon = {
                if (useremailError.value.isNotEmpty()) {
                    Icon(
                        Icons.Filled.Error, // Remplacez par l'icône d'erreur souhaitée
                        contentDescription = "Error Icon",
                                tint = Color.Red // Change la couleur de l'icône d'erreur
                    )
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = colorResource(R.color.sign),
                unfocusedLabelColor = colorResource(R.color.sign),
                unfocusedLeadingIconColor = colorResource(R.color.sign),
                unfocusedIndicatorColor = colorResource(R.color.sign),
                unfocusedContainerColor = Color.Transparent,
                cursorColor = colorResource(R.color.sign),
                focusedTextColor = colorResource(R.color.sign),
                focusedLabelColor = colorResource(R.color.sign),
                focusedLeadingIconColor = colorResource(R.color.sign),
                focusedIndicatorColor = colorResource(R.color.sign),
                focusedContainerColor = Color.Transparent
            )
        )

// Affichage du message d'erreur
        if (useremailError.value.isNotEmpty()) {
            Text(
                text = useremailError.value, // Assurez-vous que cette valeur contient le message d'erreur
                color = colorResource(R.color.sign),
                textAlign = TextAlign.Center
,
            )
        }

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
            label = "Password",
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp)
                .padding(end = 25.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = { isChecked.value = it },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = colorResource(R.color.sign),
                    checkedColor = Color.Transparent
                )
            )
            Text(
                text = "I accept the terms of use",
                modifier = Modifier
                    .padding(start = 2.dp)
                    .align(Alignment.CenterVertically) // Center the text vertically with the checkbox
                    .weight(1f), // Ensures the text uses the remaining space if needed
                fontSize = 14.sp,
                color = colorResource(R.color.sign),
                maxLines = 1, // Ensure text is a single line
                overflow = TextOverflow.Ellipsis // If text is too long, it will be truncated
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 25.dp)
                .padding(end = 25.dp)
                .background(
                    color = colorResource(R.color.sign),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Button(
                onClick = {
                    if (!isChecked.value) {
                        Toast.makeText(context, "Please accept the terms to sign up", Toast.LENGTH_SHORT).show()
                    }
                    validateInputs()
                    if (usernameError.value.isEmpty() && useremailError.value.isEmpty() &&
                        passwordError.value.isEmpty() && confirmPasswordError.value.isEmpty() && isChecked.value
                    ) {        handleNavigationsign(context, username,useremail, password, navController)

                    } else {
                        Toast.makeText(context, "Please correct the errors", Toast.LENGTH_SHORT).show()
                    }
            },
                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(),

            ) {
                Text(
                    "Sign up",
                    fontSize = 20.sp,
                    color = colorResource(R.color.signupdegg)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
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
                        "Sign up with Google",
                        fontSize = 20.sp,
                        color = colorResource(R.color.sign)
                    )
                }

        }
        Text(
            text = "Already have an account? Login",
            fontSize = 16.sp,
            color = colorResource(R.color.sign), // Utilisez une couleur qui se démarque
            textDecoration = TextDecoration.Underline,

            modifier = Modifier
                .padding(20.dp)
                .padding(bottom = 15.dp)
                .clickable {
                    navController.navigate("login")
                },

            textAlign = TextAlign.Center // Centrez le texte si vous le souhaitez
        )
    }

}
@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(
                label,
                color = if (isError) Color.Red else colorResource(R.color.sign)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
            .padding(end = 25.dp)
            .padding(10.dp),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            Row {
                if (isError) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = "Error Icon",
                        tint = Color.Red
                    )
                } else {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = colorResource(R.color.sign)
                        )
                    }
                }
            }
        },
        leadingIcon = {
            Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = colorResource(R.color.sign),
            unfocusedLabelColor = colorResource(R.color.sign),
            unfocusedLeadingIconColor = colorResource(R.color.sign),
            unfocusedIndicatorColor = colorResource(R.color.sign),
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = colorResource(R.color.sign),
            focusedLabelColor = colorResource(R.color.sign),
            focusedLeadingIconColor = colorResource(R.color.sign),
            focusedIndicatorColor = colorResource(R.color.sign),
            focusedContainerColor = Color.Transparent
        )
    )
}


fun handleNavigationsign(context: Context ,username : MutableState<String>,useremail: MutableState<String>, password: MutableState<String>, navController: NavController) {

    RetrofitInstance.getRetrofit().create(UserAPI::class.java).signupUser(SignupRequest(name = username.value ,email = useremail.value, password = password.value )).enqueue(object :
        Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "connection success", Toast.LENGTH_SHORT).show()
                navController.navigate("login")
            }

            else
            //Toast.makeText(navController.context, response.errorBody()!!.string(), Toast.LENGTH_SHORT).show()
                Toast.makeText(context, response.errorBody()!!.string(), Toast.LENGTH_SHORT).show()

        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()

        }

    })
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MediSHareAndroidTheme {
        Signup(navController = rememberNavController())
    }
}
