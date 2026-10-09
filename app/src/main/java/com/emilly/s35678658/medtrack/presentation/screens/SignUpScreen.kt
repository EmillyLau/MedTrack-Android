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
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.ui.theme.MedLightBlue
import com.emilly.s35678658.medtrack.ui.theme.MedTextSoft
import com.emilly.s35678658.medtrack.presentation.viewmodels.PatientViewModel
import com.emilly.s35678658.medtrack.presentation.navigation.Routes
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val patientViewModel: PatientViewModel = viewModel(
        factory = PatientViewModel.PatientViewModelFactory(context)
    )
    var generatedPatientId by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        generatedPatientId =
            patientViewModel.generateNextPatientId()
    }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var fullNameError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

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
                .padding(horizontal = 24.dp)
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
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Join MedTrack to manage your health easily",
                color = MedTextSoft,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 3.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            fullNameError = ""
                        },
                        label = { Text("Full Name *") },
                        isError = fullNameError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (fullNameError.isNotEmpty()) {
                        Text(
                            fullNameError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it.filter { ch -> ch.isDigit() }.take(10)
                            phoneError = ""
                        },
                        label = { Text("Phone Number *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (phoneError.isNotEmpty()) {
                        Text(
                            phoneError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = ""
                        },
                        label = { Text("Password *") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passwordError.isNotEmpty()) {
                        Text(
                            passwordError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = ""
                        },
                        label = { Text("Confirm Password *") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = confirmPasswordError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (confirmPasswordError.isNotEmpty()) {
                        Text(
                            confirmPasswordError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            var isValid = true

                            if (fullName.isBlank()) {
                                fullNameError = "Full Name is required"
                                isValid = false
                            }

                            if (phoneNumber.length != 10 || !phoneNumber.startsWith("04")) {
                                phoneError = "Phone must be 10 digits starting with 04"
                                isValid = false
                            }

                            if (password.length < 8 || !password.any { it.isDigit() } || !password.any { it.isLetter() }) {
                                passwordError =
                                    "Password must be at least 8 chars with letter and number"
                                isValid = false
                            }

                            if (confirmPassword.isBlank()) {
                                confirmPasswordError = "Confirm Password is required"
                                isValid = false
                            } else if (password != confirmPassword) {
                                confirmPasswordError = "Passwords do not match"
                                isValid = false
                            }

                            if (isValid) {
                                scope.launch {

                                    val newPatient = PatientEntity(
                                        patientId = generatedPatientId,
                                        phoneNumber = phoneNumber,
                                        name = fullName.trim(),
                                        password = password
                                    )

                                    patientViewModel.insertPatient(newPatient)

                                    snackbarHostState.showSnackbar(
                                        "Account created! Your Patient ID is $generatedPatientId"
                                    )

                                    navController.navigate(Routes.Login) {
                                        popUpTo(Routes.SignUp) { inclusive = true }
                                    }
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
                        )
                    ) {
                        Text("Sign Up")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already have an account? Login",
                color = MedBlue,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Login)
                },
            )
        }
    }
}
