package com.example.medishareandroid.views.profile


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medishareandroid.R
import com.example.medishareandroid.repositories.PreferencesRepository
import com.example.medishareandroid.ui.theme.MediSHareAndroidTheme
import com.example.medishareandroid.viewModels.SettingsViewModel
import com.example.medishareandroid.viewModels.SettingsViewModelFactory


@Composable
fun ChangePasswordScreen(
    navController: NavController,
    preferencesRepository: PreferencesRepository
) {
    val context = LocalContext.current
    val factory = SettingsViewModelFactory(preferencesRepository)
//
    val viewModel =
        ViewModelProvider(context as androidx.lifecycle.ViewModelStoreOwner, factory)[SettingsViewModel::class.java]

    val oldPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val userId = preferencesRepository.getId()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Change Password",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Old Password Field
        OutlinedTextField(
            value = oldPassword.value,
            onValueChange = { oldPassword.value = it },
            label = { Text("Old Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // New Password Field
        OutlinedTextField(
            value = newPassword.value,
            onValueChange = { newPassword.value = it },
            label = { Text("New Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // Save Button
        Button(
            onClick = {
                viewModel.updatePassword(
                    oldPassword.value,
                    newPassword.value,
                    confirmPassword.value,
                    context,
                    userId!!,
                    navController
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.sign)),

            ) {
            Text(text = "Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    MediSHareAndroidTheme {
        ChangePasswordScreen(
            navController = rememberNavController(),
            preferencesRepository = PreferencesRepository(LocalContext.current)
        )
    }
}
