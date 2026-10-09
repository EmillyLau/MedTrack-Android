package com.emilly.s35678658.medtrack.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.emilly.s35678658.medtrack.data.entities.SymptomEntity
import com.emilly.s35678658.medtrack.presentation.viewmodels.SymptomViewModel
import com.emilly.s35678658.medtrack.utils.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val symptomViewModel: SymptomViewModel = viewModel(
        factory = SymptomViewModel.SymptomViewModelFactory(context)
    )

    val patientId = AuthManager.getPatientId()

    val symptomsHistory by symptomViewModel
        .getSymptomsForPatient(patientId ?: "")
        .collectAsState(initial = emptyList())


    var category by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf(5f) }
    var notes by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }

    var categoryError by remember { mutableStateOf("") }
    var dateTimeError by remember { mutableStateOf("") }
    var notesError by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }


    val symptomCategories = listOf(
        "Pain",
        "Nausea",
        "Dizziness",
        "Fatigue",
        "Headache",
        "Skin Reaction",
        "Other"
    )

    val calendar = Calendar.getInstance()
    val selectedSeverity = severity.toInt()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val pickedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        dateTime = "$pickedDate ${String.format("%02d:%02d", hour, minute)}"
                        dateTimeError = ""
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun clearForm() {
        category = ""
        severity = 5f
        notes = ""
        dateTime = ""
        categoryError = ""
        dateTimeError = ""
        notesError = ""
    }

    Scaffold(
        containerColor = MedLightBlue,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Symptoms",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Log New Symptom",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
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
                        Box {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Symptom Category *") },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Icon(
                                        if (categoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        "Dropdown",
                                        Modifier.clickable { categoryExpanded = !categoryExpanded }
                                    )
                                }
                            )

                            DropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                symptomCategories.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            category = item
                                            categoryError = ""
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        if (categoryError.isNotEmpty()) {
                            Text(
                                text = categoryError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Severity: $selectedSeverity (${severityLabel(selectedSeverity)})",
                            style = MaterialTheme.typography.bodyLarge,
                            color = severityColor(selectedSeverity)
                        )

                        Slider(
                            value = severity,
                            onValueChange = { severity = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = severityColor(selectedSeverity),
                                activeTrackColor = severityColor(selectedSeverity)
                            )
                        )

                        Text(
                            text = when (selectedSeverity) {
                                in 1..3 -> "Low discomfort"
                                in 4..6 -> "Moderate discomfort"
                                else -> "High discomfort"
                            },
                            color = severityColor(selectedSeverity),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        OutlinedTextField(
                            value = notes,
                            onValueChange = {
                                notes = it
                                notesError =
                                    if (it.length > 200) "Notes must be 200 characters or less"
                                    else "" },
                            label = { Text("Additional Notes (Optional)") },
                            isError = notesError.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            supportingText = {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Error (top line)
                                    if (notesError.isNotEmpty()) {
                                        Text(
                                            text = notesError,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    // Counter (bottom right)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text("${notes.length}/200")
                                    }
                                }
                            }
                        )

                        OutlinedTextField(
                            value = dateTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("When did this symptom occur? *") },
                            isError = dateTimeError.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = { Text("Tap to select date & time") },
                            trailingIcon = {
                                IconButton(onClick = { datePickerDialog.show() }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Select time"
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val formatter =
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                                dateTime = LocalDateTime.now().format(formatter)
                                dateTimeError = "" },
                            modifier = Modifier.height(48.dp)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MedBlue,
                                contentColor = Color.White)
                        ) {
                            Text("Use Current Date & Time")
                        }

                        if (dateTimeError.isNotEmpty()) {
                            Text(
                                text = dateTimeError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Button(
                            onClick = {
                                categoryError =
                                    if (category.isBlank()) "Please select a symptom category" else ""

                                dateTimeError =
                                    if (dateTime.isBlank()) "Please select date and time" else ""

                                notesError =
                                    if (notes.length > 200) "Notes must be 200 characters or less" else ""

                                val hasError =
                                    categoryError.isNotEmpty() ||
                                            dateTimeError.isNotEmpty() ||
                                            notesError.isNotEmpty()

                                if (!hasError) {
                                    scope.launch {
                                        if (patientId == null) {
                                            snackbarHostState.showSnackbar("No logged-in user found")
                                            return@launch
                                        }

                                        val newSymptom = SymptomEntity(
                                            patientId = patientId,
                                            category = category,
                                            severity = severity.toInt(),
                                            notes = notes,
                                            dateTime = dateTime
                                        )

                                        symptomViewModel.insertSymptom(newSymptom)

                                        snackbarHostState.showSnackbar("Symptom logged successfully")
                                        clearForm()
                                        navController.popBackStack()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MedBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Save Symptom")
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Text(
                    text = "Symptom History",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (symptomsHistory.isEmpty()) {
                item {
                    Text("No symptoms logged yet.")
                }
            } else {
                items(symptomsHistory) { symptom ->
                    SymptomCard(symptom)
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun SymptomCard(symptom: SymptomEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = symptom.category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Date/Time: ${symptom.dateTime}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val label = when (symptom.severity) {
                in 1..3 -> "Mild"
                in 4..6 -> "Moderate"
                else -> "Severe"
            }

            val color = when (symptom.severity) {
                in 1..3 -> Color(0xFF2E7D32)
                in 4..6 -> Color(0xFFF9A825)
                else -> Color(0xFFC62828)
            }

            Text(
                text = "Severity: ${symptom.severity} ($label)",
                color = color,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Notes: ${if (symptom.notes.isBlank()) "-" else symptom.notes}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun severityLabel(severity: Int): String {
    return when (severity) {
        in 1..3 -> "Mild"
        in 4..6 -> "Moderate"
        else -> "Severe"
    }
}

fun severityColor(severity: Int): Color {
    return when (severity) {
        in 1..3 -> Color(0xFF2E7D32)   // green
        in 4..6 -> Color(0xFFF9A825)   // amber
        else -> Color(0xFFC62828)      // red
    }
}