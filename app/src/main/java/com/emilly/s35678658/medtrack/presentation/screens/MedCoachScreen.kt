package com.emilly.s35678658.medtrack.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.emilly.s35678658.medtrack.data.entities.MedCoachTipEntity
import com.emilly.s35678658.medtrack.presentation.viewmodels.GeminiViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.MedCoachViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.MedicationViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.OpenFdaViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.SymptomViewModel
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.ui.theme.MedLightBlue
import com.emilly.s35678658.medtrack.utils.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedCoachScreen(navController: NavHostController) {

    val context = LocalContext.current

    val medicationViewModel: MedicationViewModel = viewModel(
        factory = MedicationViewModel.MedicationViewModelFactory(context)
    )

    val symptomViewModel: SymptomViewModel = viewModel(
        factory = SymptomViewModel.SymptomViewModelFactory(context)
    )

    val medCoachViewModel: MedCoachViewModel = viewModel(
        factory = MedCoachViewModel.MedCoachViewModelFactory(context)
    )

    val openFdaViewModel: OpenFdaViewModel = viewModel()

    val geminiViewModel: GeminiViewModel = viewModel()

    val patientId = AuthManager.getPatientId() ?: ""

    val tips by medCoachViewModel
        .getTipsForPatient(patientId)
        .collectAsState(initial = emptyList())

    val medications by medicationViewModel
        .getMedicationsForPatient(patientId)
        .collectAsState(initial = emptyList())

    val symptoms by symptomViewModel
        .getSymptomsForPatient(patientId)
        .collectAsState(initial = emptyList())

    val medicationNames =
        medications.map { it.medicationName }.distinct()

    val drugInfo by openFdaViewModel.drugInfo.collectAsState()

    val isLoading by openFdaViewModel.isLoading.collectAsState()

    val errorMessage by openFdaViewModel.errorMessage.collectAsState()

    val generatedTip by geminiViewModel.generatedResponse.collectAsState()

    val isGeneratingTip by geminiViewModel.isGenerating.collectAsState()

    var showTipsDialog by remember { mutableStateOf(false) }

    var drugName by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MedLightBlue,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MedCoach",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                windowInsets = WindowInsets(0, 0, 0, 0),

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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = drugName,
                onValueChange = { drugName = it },
                label = { Text("Medication Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {

                OutlinedTextField(
                    value = drugName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Your Medications") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    medicationNames.forEach { medication ->

                        DropdownMenuItem(
                            text = {
                                Text(medication)
                            },
                            onClick = {
                                drugName = medication
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    openFdaViewModel.searchDrug(
                        drugName.trim()
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = MedBlue,
                    contentColor = Color.White
                ),

                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Search Drug")
            }

            if (isLoading) {
                CircularProgressIndicator()
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            drugInfo?.let { drug ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = "Drug Information",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        InfoSection(
                            title = "Purpose",
                            content = drug.purpose?.firstOrNull()
                                ?: "Not available"
                        )

                        InfoSection(
                            title = "Warnings",
                            content = drug.warnings?.firstOrNull()
                                ?: "Not available"
                        )

                        InfoSection(
                            title = "Dosage",
                            content = drug.dosage_and_administration?.firstOrNull()
                                ?: "Not available"
                        )
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "GenAI Medication Tips",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Personalised tips based on your medications and symptom history",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {

                    geminiViewModel.generateMedicationTip(
                        medications = medications,
                        symptoms = symptoms
                    ) { tip ->

                        medCoachViewModel.insertTip(
                            MedCoachTipEntity(
                                patientId = patientId,
                                tipText = tip,
                                timestamp =
                                    java.time.LocalDateTime.now()
                                        .format(
                                            java.time.format.DateTimeFormatter.ofPattern(
                                                "yyyy-MM-dd HH:mm"
                                            )
                                        )
                            )
                        )
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = MedBlue,
                    contentColor = Color.White
                ),

                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Generate Tip")
            }

            if (isGeneratingTip) {
                CircularProgressIndicator()
            }

            if (generatedTip.isNotEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        Text(
                            text = "Your Personalised Tip",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = generatedTip,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    showTipsDialog = true
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Show All Tips")
            }
        }
    }

    if (showTipsDialog) {

        AlertDialog(
            onDismissRequest = {
                showTipsDialog = false
            },

            title = {
                Text("Tip History")
            },

            text = {

                if (tips.isEmpty()) {

                    Text("No tips generated yet.")

                } else {

                    Column(
                        modifier = Modifier.verticalScroll(
                            rememberScrollState()
                        ),

                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        tips.forEach { tip ->

                            Text(
                                text = "${tip.timestamp}\n${tip.tipText}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            HorizontalDivider()
                        }
                    }
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        showTipsDialog = false
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: String
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}