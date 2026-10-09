package com.emilly.s35678658.medtrack.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import com.emilly.s35678658.medtrack.data.repositories.PatientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch

class PatientViewModel(context: Context) : ViewModel() {

    private val patientRepository = PatientRepository(context)

    private val _selectedPatient = MutableStateFlow<PatientEntity?>(null)
    val selectedPatient: StateFlow<PatientEntity?> = _selectedPatient

    private val _loginResult = MutableStateFlow("")
    val loginResult: StateFlow<String> = _loginResult

    fun clearLoginResult() {
        _loginResult.value = ""
    }

    private val _claimResult = MutableStateFlow("")
    val claimResult: StateFlow<String> = _claimResult

    fun clearClaimResult() {
        _claimResult.value = ""
    }

    val allPatients: Flow<List<PatientEntity>> = patientRepository.getAllPatients()

    fun insertPatient(patient: PatientEntity) {
        viewModelScope.launch { patientRepository.insertPatient(patient) }
    }

    fun loadPatientById(patientId: String) {
        viewModelScope.launch {
            _selectedPatient.value = patientRepository.getPatientById(patientId)
        }
    }

    fun login(patientId: String, password: String) {
        viewModelScope.launch {

            val patient = patientRepository.getPatientById(patientId)

            _loginResult.value = when {
                patient == null ->
                    "not_found"

                patient.password.isNullOrEmpty() ->
                    "not_claimed"

                patient.password != password ->
                    "wrong_password"

                else ->
                    "success"
            }
        }
    }

    fun claimAccount(patientId: String, phoneNumber: String, newPassword: String) {
        viewModelScope.launch {
            val patient = patientRepository.getPatientByIdAndPhone(patientId, phoneNumber)

            _claimResult.value = when {
                patient == null -> "not_found"
                !patient.password.isNullOrEmpty() -> "already_claimed"
                else -> {
                    patientRepository.setPassword(patientId, newPassword)
                    "success"
                }
            }
        }
    }
    suspend fun generateNextPatientId(): String {
        val ids = patientRepository.getAllPatientIds()

        val maxId = ids
            .mapNotNull { it.removePrefix("P").toIntOrNull() }
            .maxOrNull() ?: 1000

        return "P${maxId + 1}"
    }

    suspend fun getPatientById(patientId: String): PatientEntity? =
        patientRepository.getPatientById(patientId)

    suspend fun getAllPatientIds(): List<String> {
        return patientRepository.getAllPatientIds()
    }

    suspend fun getPatientCount(): Int {
        return patientRepository.getPatientCount()
    }

    class PatientViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PatientViewModel(appContext) as T
        }
    }
}


