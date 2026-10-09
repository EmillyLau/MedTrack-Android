package com.emilly.s35678658.medtrack.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emilly.s35678658.medtrack.presentation.viewmodels.MedicationViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.PatientViewModel
import com.emilly.s35678658.medtrack.presentation.viewmodels.SymptomViewModel
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.presentation.viewmodels.GeminiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianDashboardScreen(navController: NavController) {

    val context = LocalContext.current

    val patientViewModel: PatientViewModel = viewModel(
        factory = PatientViewModel.PatientViewModelFactory(context)
    )

    val medicationViewModel: MedicationViewModel = viewModel(
        factory = MedicationViewModel.MedicationViewModelFactory(context)
    )

    val symptomViewModel: SymptomViewModel = viewModel(
        factory = SymptomViewModel.SymptomViewModelFactory(context)
    )

    val geminiViewModel: GeminiViewModel = viewModel()

    var patientCount by remember { mutableStateOf(0) }
    var commonSymptom by remember { mutableStateOf("N/A") }
    var avgMedications by remember { mutableStateOf(0f) }
    var avgSeverity by remember { mutableStateOf(0f) }
    var insights by remember { mutableStateOf<List<String>>(emptyList()) }
    var isFindingPatterns by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        patientCount = patientViewModel.getPatientCount()
        avgMedications = medicationViewModel.getAverageMedicationsPerPatient() ?: 0f
        commonSymptom = symptomViewModel.getMostCommonSymptomCategory() ?: "N/A"
        avgSeverity = symptomViewModel.getAverageSymptomSeverity() ?: 0f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Clinician Dashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
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
            StatisticCard(
                title = "Total Patients",
                value = patientCount.toString()
            )

            StatisticCard(
                title = "Average Medications Per Patient",
                value = String.format("%.1f", avgMedications)
            )

            StatisticCard(
                title = "Most Common Symptom",
                value = commonSymptom
            )

            StatisticCard(
                title = "Average Symptom Severity",
                value = String.format("%.1f", avgSeverity)
            )

            Button(
                onClick = {
                    isFindingPatterns = true

                    geminiViewModel.generateClinicianInsights(
                        patientCount = patientCount,
                        avgMedications = avgMedications,
                        commonSymptom = commonSymptom,
                        avgSeverity = avgSeverity
                    ) { result ->
                        insights = result
                        isFindingPatterns = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MedBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Find Patterns")
            }
            if (isFindingPatterns) {
                CircularProgressIndicator()
            }

            if (insights.isNotEmpty()) {
                Text(
                    text = "GenAI Insights",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                insights.forEachIndexed { index, insight ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "${index + 1}. $insight",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun StatisticCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MedBlue
            )
        }
    }
}