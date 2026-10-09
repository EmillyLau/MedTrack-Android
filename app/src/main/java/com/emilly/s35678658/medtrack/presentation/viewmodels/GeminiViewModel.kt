package com.emilly.s35678658.medtrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import com.emilly.s35678658.medtrack.data.entities.SymptomEntity
import com.emilly.s35678658.medtrack.data.repositories.GeminiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeminiViewModel : ViewModel() {

    private val geminiRepository = GeminiRepository()

    private val _generatedResponse = MutableStateFlow("")
    val generatedResponse: StateFlow<String> = _generatedResponse

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    private val _interactionWarning = MutableStateFlow<String?>(null)
    val interactionWarning: StateFlow<String?> = _interactionWarning

    private val _isCheckingInteractions = MutableStateFlow(false)
    val isCheckingInteractions: StateFlow<Boolean> = _isCheckingInteractions

    fun generateMedicationTip(
        medications: List<MedicationEntity>,
        symptoms: List<SymptomEntity>,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _isGenerating.value = true

                val medicationText =
                    if (medications.isEmpty()) {
                        "No medications recorded."
                    } else {
                        medications.joinToString {
                            "${it.medicationName} (${it.dosage}, ${it.frequency})"
                        }
                    }

                val symptomText =
                    if (symptoms.isEmpty()) {
                        "No symptoms recorded."
                    } else {
                        symptoms.take(3).joinToString {
                            "${it.category}, severity ${it.severity}"
                        }
                    }

                val prompt = """
                    Generate a short, encouraging medication adherence tip for this patient.

                    Patient medication list:
                    $medicationText

                    Recent symptom history:
                    $symptomText

                    Keep it supportive, practical, and under 60 words.
                    Make the tip specific to the listed medication and symptoms.
                """.trimIndent()

                val response = geminiRepository.generateTip(prompt)

                _generatedResponse.value = response
                onResult(response)

            } catch (e: Exception) {
                val fallback =
                    "Remember to take your medication on time and monitor your symptoms regularly."

                _generatedResponse.value = fallback
                onResult(fallback)

            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun checkInteractions(
        existingMedicationNames: List<String>,
        newMedicationName: String,
        onResult: (hasWarning: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _isCheckingInteractions.value = true
                _interactionWarning.value = null

                if (existingMedicationNames.isEmpty()) {
                    onResult(false)
                    return@launch
                }

                val existingText = existingMedicationNames.joinToString(", ")

                val prompt = """
                    A patient is currently taking: $existingText.
                    They want to add: $newMedicationName.

                    Are there any known drug interactions between $newMedicationName
                    and any of these medications: $existingText?

                    Reply in this exact format:
                    - If there ARE interactions: start with "WARNING:" then briefly explain the interaction in 1-2 sentences.
                    - If there are NO interactions: reply only with "SAFE: No known interactions found."

                    Be concise. Do not give medical advice beyond stating the interaction.
                """.trimIndent()

                val response = geminiRepository.generateTip(prompt)

                if (response.startsWith("WARNING:", ignoreCase = true)) {
                    _interactionWarning.value = response
                    onResult(true)
                } else {
                    _interactionWarning.value = null
                    onResult(false)
                }

            } catch (e: Exception) {
                _interactionWarning.value = null
                onResult(false)
            } finally {
                _isCheckingInteractions.value = false
            }
        }
    }

    fun generateClinicianInsights(
        patientCount: Int,
        avgMedications: Float,
        commonSymptom: String,
        avgSeverity: Float,
        onResult: (List<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _isGenerating.value = true

                val prompt = """
                    Based on this MedTrack clinician dashboard data, generate 3 short interesting patterns or observations.

                    Total patients: $patientCount
                    Average medications per patient: ${String.format("%.1f", avgMedications)}
                    Most common symptom category: $commonSymptom
                    Average symptom severity: ${String.format("%.1f", avgSeverity)}

                    Rules:
                    - Do not write an introduction.
                    - Do not write a conclusion.
                    - Return only 3 numbered observations.
                """.trimIndent()

                val response = geminiRepository.generateTip(prompt)

                val insights = response
                    .lines()
                    .map {
                        it.trim()
                            .removePrefix("-")
                            .removePrefix("*")
                            .replace(Regex("^\\d+\\.\\s*"), "")
                            .trim()
                    }
                    .filter { line ->
                        line.isNotBlank() &&
                                !line.contains("here are", ignoreCase = true) &&
                                !line.contains("observations", ignoreCase = true) &&
                                !line.contains("patterns", ignoreCase = true)
                    }
                    .take(3)

                onResult(insights)

            } catch (e: Exception) {
                onResult(
                    listOf(
                        "Patients report $commonSymptom most often, suggesting this symptom may need closer monitoring.",
                        "The average medication load is ${String.format("%.1f", avgMedications)} per patient.",
                        "The average symptom severity is ${String.format("%.1f", avgSeverity)}, indicating overall symptom burden."
                    )
                )
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun clearInteractionWarning() {
        _interactionWarning.value = null
    }
}