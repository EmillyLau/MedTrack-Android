package com.emilly.s35678658.medtrack.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emilly.s35678658.medtrack.R
import com.emilly.s35678658.medtrack.presentation.viewmodels.PatientViewModel
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.ui.theme.MedLightBlue
import com.emilly.s35678658.medtrack.ui.theme.MedTextSoft
import com.emilly.s35678658.medtrack.utils.AuthManager
import com.emilly.s35678658.medtrack.presentation.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val patientViewModel: PatientViewModel = viewModel(
        factory = PatientViewModel.PatientViewModelFactory(context)
    )
    val loginResult by patientViewModel.loginResult.collectAsState()

    var patientId by remember { mutableStateOf("") }
    var patientIdError by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var generalError by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginResult) {

        when (loginResult) {

            "success" -> {
                AuthManager.saveSession(context, patientId)

                snackbarHostState.showSnackbar("Login successful")

                patientViewModel.clearLoginResult()

                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Login) { inclusive = true }
                    launchSingleTop = true
                }
            }

            "not_found" -> {
                snackbarHostState.showSnackbar("Patient ID not found")
                patientViewModel.clearLoginResult()
            }

            "not_claimed" -> {
                snackbarHostState.showSnackbar("Please claim your account first")
                patientViewModel.clearLoginResult()
            }

            "wrong_password" -> {
                snackbarHostState.showSnackbar("Incorrect password")
                patientViewModel.clearLoginResult()
            }
        }
    }

    Scaffold(
        containerColor = MedLightBlue,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MedLightBlue
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                // AI acknowledgement: The logo used in this application was generated using ChatGPT for demonstration purposes only.
                painter = painterResource(id = R.drawable.medtrack_logo),
                contentDescription = "MedTrack Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Log in to continue managing your health",
                color = MedTextSoft,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = patientId,
                        onValueChange = {
                            patientId = it
                            patientIdError = ""
                            generalError = ""
                        },
                        label = { Text("Patient ID") },
                        singleLine = true,
                        isError = patientIdError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (patientIdError.isNotEmpty()) {
                        Text(
                            text = patientIdError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = ""
                            generalError = ""
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (passwordError.isNotEmpty()) {
                        Text(
                            text = passwordError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }

                    if (generalError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = generalError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            patientIdError = ""
                            passwordError = ""
                            generalError = ""

                            when {
                                patientId.isBlank() -> {
                                    patientIdError = "patient ID is required"
                                }

                                password.isBlank() -> {
                                    passwordError = "Password is required"
                                }

                                else -> {
                                    patientViewModel.login(patientId, password)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MedBlue,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("Login")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // For CSV-seeded patients who have never logged in
            Text(
                text = "First time? Claim your account",
                color = MedBlue,
                fontSize = 14.sp,
                modifier = Modifier.clickable { navController.navigate(Routes.ClaimAccount) }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Don't have an account? Create one",
                color = MedBlue,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { navController.navigate(Routes.SignUp) }
            )
        }
    }
}