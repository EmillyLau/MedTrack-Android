package com.emilly.s35678658.medtrack.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emilly.s35678658.medtrack.presentation.navigation.Routes
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.ui.theme.MedLightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianLoginScreen(navController: NavController) {
    var accessKey by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MedLightBlue,
        topBar = {
            TopAppBar(
                title = { Text(text = "Clinician Login",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MedLightBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Clinician Access",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = accessKey,
                onValueChange = {
                    accessKey = it
                    errorMessage = ""
                },
                label = { Text("Access Key") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = errorMessage.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    when {

                        accessKey.isBlank() -> {
                            errorMessage = "Please enter the access key"
                        }

                        accessKey != "dollar-entry-apples" -> {
                            errorMessage = "Invalid access key"
                        }

                        else -> {
                            navController.navigate(Routes.ClinicianDashboard)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedBlue)
            ) {
                Text("Login")
            }
        }
    }
}

