package com.example.medishareandroid.user
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medishareandroid.R
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme

@Composable
fun Signup(modifier: Modifier = Modifier) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmpassword = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val useremail = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }
    val context = LocalContext.current  // Get the correct context

    val myFontFamily = FontFamily(
        Font(R.font.itimregular), // Remplacez par le nom de votre fichier de police
    )
    val mediFontFamily = FontFamily(
        Font(R.font.chewyregular), // Remplacez par le nom de votre fichier de police
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.White,colorResource(R.color.white)), // Dégradé du blanc au bleu
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

        Text(
            text = "Welcome to",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color =  colorResource(R.color.sign),
                    fontFamily = myFontFamily,


        )
        Text(
            text = "MediShare",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.signup),
            fontFamily = mediFontFamily
        )

        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            label = { Text("Username") },
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = "Username Icon")
            },  singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = colorResource(R.color.sign), // Couleur du texte en non-focus
                unfocusedLabelColor = colorResource(R.color.sign), // Couleur du label en non-focus
                unfocusedLeadingIconColor = colorResource(R.color.sign), // Couleur de l'icône en non-focus
                unfocusedIndicatorColor = colorResource(R.color.sign), // Couleur du contour en non-focus
                unfocusedContainerColor = Color.Transparent, // Couleur de fond en non-focus

                focusedTextColor = colorResource(R.color.sign), // Couleur du texte en focus
                focusedLabelColor = colorResource(R.color.sign), // Couleur du label en focus
                focusedLeadingIconColor = colorResource(R.color.sign), // Couleur de l'icône en focus
                focusedIndicatorColor = colorResource(R.color.sign), // Couleur du contour en focus
                focusedContainerColor = Color.Transparent // Couleur de fond en focus
            )



                )




        OutlinedTextField(
            value = useremail.value,
            onValueChange = { useremail.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Email Icon")
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = colorResource(R.color.sign),
                unfocusedLabelColor = colorResource(R.color.sign),
                unfocusedLeadingIconColor = colorResource(R.color.sign),
                unfocusedIndicatorColor = colorResource(R.color.sign),
                unfocusedContainerColor = Color.Transparent,
                cursorColor = colorResource(R.color.sign) ,
                focusedTextColor = colorResource(R.color.sign),
                focusedLabelColor = colorResource(R.color.sign),
                focusedLeadingIconColor = colorResource(R.color.sign),
                focusedIndicatorColor = colorResource(R.color.sign),
                focusedContainerColor = Color.Transparent
            )
        )

        PasswordField(
            password = password.value,
            onPasswordChange = { password.value = it },
            label = "Password"
        )

        PasswordField(
            password = confirmpassword.value,
            onPasswordChange = { confirmpassword.value = it },
            label = "Confirm Password"
        )
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
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
                text = "J'accepte les conditions d'utilisation",
                modifier = Modifier
                    .padding(start = 2.dp)
                    .align(Alignment.CenterVertically) // Center the text vertically with the checkbox
                    .weight(1f), // Ensures the text uses the remaining space if needed
                fontSize = 16.sp,
                color = colorResource(R.color.sign),
                maxLines = 1, // Ensure text is a single line
                overflow = TextOverflow.Ellipsis // If text is too long, it will be truncated
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(

                            colorResource(R.color.sign),
                            colorResource(R.color.sign),

                            )
                    ),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Button(
                onClick = {
                    if (!isChecked.value) {
                        Toast.makeText(context, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
                        // Display the toast with the correct context
                    } else {
                        // Proceed to the next step
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
                    color = colorResource(R.color.white)
                )
            }
        }
        Text(
            text = "Already have an account? Login",
            fontSize = 16.sp,
            color = colorResource(R.color.sign), // Utilisez une couleur qui se démarque
            modifier = Modifier
                .padding(30.dp)
                .clickable {
                    // Action à réaliser lors du clic, par exemple, naviguer vers la page de connexion
                    // navigationController.navigate("login") // Assurez-vous d'importer la bibliothèque de navigation
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
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                    tint = colorResource(R.color.sign)
                )
            }
        },
        leadingIcon = {
            Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = colorResource(R.color.sign), // Couleur du texte en non-focus
            unfocusedLabelColor = colorResource(R.color.sign), // Couleur du label en non-focus
            unfocusedLeadingIconColor = colorResource(R.color.sign), // Couleur de l'icône en non-focus
            unfocusedIndicatorColor = colorResource(R.color.sign), // Couleur du contour en non-focus
            unfocusedContainerColor = Color.Transparent, // Couleur de fond en non-focus

            focusedTextColor = colorResource(R.color.sign), // Couleur du texte en focus
            focusedLabelColor = colorResource(R.color.sign), // Couleur du label en focus
            focusedLeadingIconColor = colorResource(R.color.sign), // Couleur de l'icône en focus
            focusedIndicatorColor = colorResource(R.color.sign), // Couleur du contour en focus
            focusedContainerColor = Color.Transparent // Couleur de fond en focus
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MediSHareAndroidTheme {
        Signup()
    }
}
