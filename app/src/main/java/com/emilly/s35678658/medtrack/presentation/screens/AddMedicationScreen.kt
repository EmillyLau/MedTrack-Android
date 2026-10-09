package com.emilly.s35678658.medtrack.presentation.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.ui.theme.MedLightBlue
import com.emilly.s35678658.medtrack.ui.theme.MedTextSoft
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.MedicationViewModel
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import com.emilly.s35678658.medtrack.presentation.viewmodels.GeminiViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.MedCoachViewModel
import com.emilly.s35678658.medtrack.utils.AuthManager
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val patientId = AuthManager.getPatientId()

    val medicationViewModel: MedicationViewModel = viewModel(
        factory = MedicationViewModel.MedicationViewModelFactory(context)
    )

    val existingMedications by medicationViewModel
        .getMedicationsForPatient(patientId ?: "")
        .collectAsState(initial = emptyList())

    val geminiViewModel: GeminiViewModel = viewModel()


    val interactionWarning by geminiViewModel.interactionWarning.collectAsState()
    val isCheckingInteractions by geminiViewModel.isCheckingInteractions.collectAsState()

    var pendingMedication by remember { mutableStateOf<MedicationEntity?>(null) }
    var showInteractionDialog by remember { mutableStateOf(false) }

    var medicationName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var scheduledTime by remember { mutableStateOf("") }
    var medicationType by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var medicationNameError by remember { mutableStateOf("") }
    var dosageError by remember { mutableStateOf("") }
    var frequencyError by remember { mutableStateOf("") }
    var scheduledTimeError by remember { mutableStateOf("") }
    var medicationTypeError by remember { mutableStateOf("") }

    val frequencyOptions = listOf(
        "Once daily",
        "Twice daily",
        "Three times daily",
        "As needed"
    )

    val medicationTypeOptions = listOf(
        "Tablet",
        "Capsule",
        "Liquid",
        "Injection",
        "Topical",
        "Other"
    )

    val calendar = Calendar.getInstance()

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                scheduledTime = String.format("%02d:%02d", hour, minute)
                scheduledTimeError = ""
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
    }

    fun clearForm() {
        medicationName = ""
        dosage = ""
        frequency = ""
        scheduledTime = ""
        medicationType = ""
        notes = ""

        medicationNameError = ""
        dosageError = ""
        frequencyError = ""
        scheduledTimeError = ""
        medicationTypeError = ""
    }

    fun saveMedication(medication: MedicationEntity) {
        medicationViewModel.insertMedication(medication)
        scope.launch {
            snackbarHostState.showSnackbar("Medication saved successfully")
        }
        geminiViewModel.clearInteractionWarning()
        clearForm()
        navController.popBackStack()
    }

    if (showInteractionDialog && interactionWarning != null) {
        AlertDialog(
            onDismissRequest = {
                showInteractionDialog = false
                pendingMedication = null
                geminiViewModel.clearInteractionWarning()
            },

            title = {
                Text(
                    text = "⚠️ Drug Interaction Warning",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            },

            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = interactionWarning ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Please consult your doctor or pharmacist before adding this medication.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },

            confirmButton = {
                Button(
                    onClick = {
                        pendingMedication?.let {
                            saveMedication(it)
                        }

                        showInteractionDialog = false
                        pendingMedication = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Save Anyway")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showInteractionDialog = false
                        pendingMedication = null
                        geminiViewModel.clearInteractionWarning()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        containerColor = MedLightBlue,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Medications",
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Add a new medication to your daily schedule",
                color = MedTextSoft,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = medicationName,
                        onValueChange = { medicationName = it; medicationNameError = "" },
                        label = { Text("Medication Name *") },
                        isError = medicationNameError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (medicationNameError.isNotEmpty()) {
                        Text(text = medicationNameError, color = MaterialTheme.colorScheme.error)
                    }

                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it; dosageError = "" },
                        label = { Text("Dosage (e.g., 500mg, 10ml) *") },
                        isError = dosageError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (dosageError.isNotEmpty()) {
                        Text(text = dosageError, color = MaterialTheme.colorScheme.error)
                    }


                    // Frequency Dropdown
                    var freqExpanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedTextField(
                            value = frequency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Frequency *") },
                            isError = frequencyError.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    if (freqExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    "Dropdown",
                                    Modifier.clickable { freqExpanded = !freqExpanded }
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = freqExpanded,
                            onDismissRequest = { freqExpanded = false }) {
                            frequencyOptions.forEach { freq ->
                                DropdownMenuItem(text = { Text(freq) }, onClick = {
                                    frequency = freq
                                    freqExpanded = false
                                })
                            }
                        }
                    }
                    if (frequencyError.isNotEmpty()) {
                        Text(text = frequencyError, color = MaterialTheme.colorScheme.error)
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { timePickerDialog.show() }
                    ) {
                        OutlinedTextField(
                            value = scheduledTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Scheduled Time *") },
                            isError = scheduledTimeError.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { timePickerDialog.show() }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Select time"
                                    )
                                }
                            }
                        )
                    }
                    if (scheduledTimeError.isNotEmpty()) {
                        Text(
                            text = scheduledTimeError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Medication Type Dropdown
                    var typeExpanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedTextField(
                            value = medicationType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Medication Type *") },
                            isError = medicationTypeError.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    if (typeExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    "Dropdown",
                                    Modifier.clickable { typeExpanded = !typeExpanded }
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }) {
                            medicationTypeOptions.forEach { type ->
                                DropdownMenuItem(text = { Text(type) }, onClick = {
                                    medicationType = type
                                    typeExpanded = false
                                })
                            }
                        }
                    }
                    if (medicationTypeError.isNotEmpty()) {
                        Text(text = medicationTypeError, color = MaterialTheme.colorScheme.error)
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                val dosageRegex = Regex("""^\d+(\.\d+)?(mg|ml|g)$""")

                                medicationNameError =
                                    if (medicationName.isBlank())
                                        "Medication name is required"
                                    else ""

                                dosageError =
                                    if (dosage.isBlank())
                                        "Dosage is required"
                                    else if (!dosage.matches(dosageRegex))
                                        "Invalid format (e.g. 500mg)"
                                    else ""

                                frequencyError =
                                    if (frequency.isBlank())
                                        "Frequency is required"
                                    else ""

                                scheduledTimeError =
                                    if (scheduledTime.isBlank())
                                        "Time is required"
                                    else ""

                                medicationTypeError =
                                    if (medicationType.isBlank())
                                        "Type is required"
                                    else ""

                                val hasError =
                                    medicationNameError.isNotEmpty() ||
                                            dosageError.isNotEmpty() ||
                                            frequencyError.isNotEmpty() ||
                                            scheduledTimeError.isNotEmpty() ||
                                            medicationTypeError.isNotEmpty()

                                if (!hasError) {

                                    if (patientId == null) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "No logged-in user found"
                                            )
                                        }
                                        return@Button
                                    }

                                    val newMedication = MedicationEntity(
                                        patientId = patientId,
                                        medicationName = medicationName,
                                        dosage = dosage,
                                        frequency = frequency,
                                        scheduledTime = scheduledTime,
                                        medicationType = medicationType,
                                        notes = notes
                                    )

                                    pendingMedication = newMedication

                                    geminiViewModel.checkInteractions(
                                        existingMedicationNames = existingMedications.map { it.medicationName },
                                        newMedicationName = medicationName
                                    ) { hasWarning ->

                                        if (hasWarning) {
                                            showInteractionDialog = true
                                        } else {
                                            saveMedication(newMedication)
                                        }
                                    }
                                }
                            },

                            enabled = !isCheckingInteractions,

                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),

                            shape = RoundedCornerShape(16.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = MedBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                if (isCheckingInteractions)
                                    "Checking..."
                                else
                                    "Save"
                            )
                        }

                        OutlinedButton(
                            onClick = { clearForm() },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Clear")
                        }
                    }
                }
            }
        }
    }
}