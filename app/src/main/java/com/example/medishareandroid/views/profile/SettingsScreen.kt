package com.example.medishareandroid.views.profile


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Brightness2
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import com.example.medishareandroid.viewModels.SettingsViewModel
import com.example.medishareandroid.views.components.ProfileOptionCard


@Composable
fun SettingsScreen(navController: NavController, userPreferences: PreferencesRepository,navController2: NavController) {
    val context= LocalContext.current
    val prefs = PreferencesRepository(context)
    var isDarkMode by remember { mutableStateOf(false) }
    val viewModel = SettingsViewModel(prefs)
    //var showChangePasswordPopup by remember { mutableStateOf(false) }

    // Observe QR Code bitmap from ViewModel
    //val qrCodeBitmap by viewModel.qrCodeBitmap.collectAsState()
    LaunchedEffect (prefs.getId().toString()) {
        viewModel.fetchQrCode(prefs.getId().toString(),context)
    }
    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF130160), Color(0xFF36353C))
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(

                        text = "${prefs.getName() ?: ""} ${prefs.getName() ?: "Guest"}",

                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = prefs.getEmail() ?: "",
                        fontSize = 14.sp,
                        color = Color.White
                    )

                }
            }
        }

       /* Spacer(modifier = Modifier.height(10.dp))

        //qrCode
        Box(modifier = Modifier.size(300.dp).padding(16.dp), contentAlignment = Alignment.Center) {
            qrCodeBitmap?.let {

                Image( bitmap = it.asImageBitmap(),
                contentDescription = "User Medical Files QR Code",
                modifier = Modifier.size(200.dp)
                )

            } ?:
                CircularProgressIndicator()

        }
*/

        Spacer(modifier = Modifier.height(10.dp))

        ProfileOptionCard(
            icon = Icons.Outlined.Brightness2,
            label = "Dark mode",
            trailingContent = {
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it }
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        ProfileOptionCard(
            icon = Icons.Default.Person,
            label = "Edit Profile",
            onClick = { navController.navigate("editProfileScreen") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        ProfileOptionCard(
            icon = Icons.Default.Lock,
            label = "Password",
            onClick = { navController.navigate("changePassword") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProfileOptionCard(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            label = "Logout",
            onClick = {
                userPreferences.clearLoginData() // Logout logic
                navController2.navigate("login") // Navigate to login screen
                Toast.makeText(
                    navController2.context,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        Spacer(modifier = Modifier.weight(1f))


    }



}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    MediSHareAndroidTheme {
        SettingsScreen(navController = navController, userPreferences = PreferencesRepository(context),
            rememberNavController()
        )
    }

}
