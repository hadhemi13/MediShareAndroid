import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.example.medishareandroid.R
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.viewModels.auth.AuthViewModel
import kotlinx.coroutines.delay

class LaunchScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchScreenContent(navController = rememberNavController())
        }
    }
}

@Composable
fun AuthScreen(viewModel: AuthViewModel, navController: NavController) {
    // Obtenir l’état de connexion de l’utilisateur
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    val context = LocalContext.current
    val prefs = PreferencesRepository(context)
    if (isUserLoggedIn) {
        val routef: String = if (prefs.getRole() == "patient") {
            "homePage"
        } else {
            "homeRadiologue"
        }
        if (prefs.getRole()=="patient") {
            navController.navigate(routef) {
                popUpTo("launchScreen") { inclusive = true }
            }
        }else{
            navController.navigate(routef) {
                popUpTo("launchScreen") { inclusive = true }
            }
        }
    } else {
        LaunchScreenContent(navController)
    }
}

@Composable
fun LaunchScreenContent(navController: NavController) {
    val myFontFamily = FontFamily(
        Font(R.font.itimregular), // Remplacez par le nom de votre fichier de police
    )
    val mediFontFamily = FontFamily(
        Font(R.font.chewyregular), // Remplacez par le nom de votre fichier de police
    )
    val imageIds = listOf(


        R.drawable.image1, // Remplacez par vos images
        R.drawable.image2,
        R.drawable.image3
    )


    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            currentIndex = (currentIndex + 1) % imageIds.size
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.White, Color(0xFF90CAF9)), // Dégradé du blanc au bleu
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(7f))

            Image(
                painter = painterResource(id = imageIds[currentIndex]),
                contentDescription = null,
                modifier = Modifier
                    .size(400.dp)
                    .padding(start = 15.dp)
                    .padding(end = 10.dp)
                    .padding(top = 70.dp)
                    .padding(bottom = 70.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.box), shape = RoundedCornerShape(30.dp))
                    .height(350.dp),

                contentAlignment = Alignment.Center // Centre le contenu dans le Box
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally // Centre horizontalement tous les éléments de la Column
                ) {
                    Text(text = "Welcome", color = Color.White, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier
                        .height(8.dp)
                        .padding(bottom = 20.dp))
                    Text(
                        text = "Discover MediShare : a new way to securely share and collaboratively analyze medical images.",
                        color = Color.White,
                        textAlign = TextAlign.Center, // Centre le texte lui-même

                    )
                    Button(
                        onClick = {
                            navController.navigate("login") // Navigation vers l'écran de connexion
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.signupdeg)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(70.dp)
                            .padding(top = 30.dp)
                            .padding(end = 60.dp)
                            .padding(start = 60.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Let's Start",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Arrow Icon",
                                tint = Color.White
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLaunchScreen() {
    LaunchScreenContent(navController = rememberNavController())
}
