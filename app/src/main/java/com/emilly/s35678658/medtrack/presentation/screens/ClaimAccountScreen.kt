package com.emilly.s35678658.medtrack.presentation.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.emilly.s35678658.medtrack.presentation.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimAccountScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val patientViewModel: PatientViewModel = viewModel(
        factory = PatientViewModel.PatientViewModelFactory(context)
    )
    val claimResult by patientViewModel.claimResult.collectAsState()

    var patientId by remember { mutableStateOf("")  }
    var phoneNumber by remember { mutableStateOf("")  }
    var newPassword by remember { mutableStateOf("")  }
    var newConfirmPassword by remember { mutableStateOf("")  }

    var patientIdError by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf("") }
    var newConfirmPasswordError by remember { mutableStateOf("") }

    LaunchedEffect(claimResult) {

        when (claimResult) {

            "success" -> {
                snackbarHostState.showSnackbar(
                    "Account claimed successfully. Please log in."
                )

                patientViewModel.clearClaimResult()

                navController.navigate(Routes.Login) {
                    popUpTo(Routes.ClaimAccount) { inclusive = true }
                }
            }

            "already_claimed" -> {
                snackbarHostState.showSnackbar(
                    "This account has already been claimed. Please log in."
                )

                patientViewModel.clearClaimResult()
            }

            "not_found" -> {
                snackbarHostState.showSnackbar(
                    "Patient ID and phone number do not match."
                )

                patientViewModel.clearClaimResult()
            }
        }
    }

    Scaffold(
        containerColor = MedLightBlue,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MedLightBlue)
            )
        }
    ) {
        padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                // AI acknowledgement: The logo used in this application was generated using ChatGPT for demonstration purposes only.
                painter = painterResource(id = R.drawable.medtrack_logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "First-time login",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enter your Patient ID and phone number to set a new password.",
                color = MedTextSoft,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = patientId,
                        onValueChange = {
                            patientId = it
                            patientIdError = ""
                        },
                        label = { Text("Patient ID") },
                        singleLine = true,
                        isError = patientIdError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (patientIdError.isNotEmpty()) {
                        Text(
                            patientIdError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it.filter { ch -> ch.isDigit() }.take(10)
                            phoneNumberError = "" },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneNumberError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (phoneNumberError.isNotEmpty()) {
                        Text(
                            phoneNumberError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            newPasswordError = "" },
                        label = { Text("New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = newPasswordError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (newPasswordError.isNotEmpty()) {
                        Text(
                            newPasswordError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newConfirmPassword,
                        onValueChange = {
                            newConfirmPassword = it
                            newConfirmPasswordError = "" },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = newConfirmPasswordError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (newConfirmPasswordError.isNotEmpty()) {
                        Text(
                            newConfirmPasswordError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            var isValid = true

                            if (patientId.isBlank()) {
                                patientIdError = "Patient ID is required"
                                isValid = false
                            }

                            if (phoneNumber.length != 10 || !phoneNumber.startsWith("04")) {
                                phoneNumberError = "Phone must be 10 digits starting with 04"
                                isValid = false
                            }

                            if (
                                newPassword.length < 8 ||
                                !newPassword.any { it.isLetter() } ||
                                !newPassword.any { it.isDigit() }
                            ) {
                                newPasswordError = "Password must be at least 8 characters with letter and number"
                                isValid = false
                            }

                            if (newConfirmPassword.isBlank()) {
                                newConfirmPasswordError = "Please confirm your password"
                                isValid = false
                            } else if (newPassword != newConfirmPassword) {
                                newConfirmPasswordError = "Passwords do not match"
                                isValid = false
                            }

                            if (isValid) {
                                patientViewModel.claimAccount(
                                    patientId = patientId.trim(),
                                    phoneNumber = phoneNumber,
                                    newPassword = newPassword
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MedBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Claim Account")
                    }
                }
            }

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