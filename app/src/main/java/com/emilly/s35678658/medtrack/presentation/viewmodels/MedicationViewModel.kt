package com.emilly.s35678658.medtrack.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import com.emilly.s35678658.medtrack.data.repositories.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MedicationViewModel(context: Context) : ViewModel() {

    private val medicationRepository = MedicationRepository(context)

    fun insertMedication(medication: MedicationEntity) {
        viewModelScope.launch {
            medicationRepository.insertMedication(medication)
        }
    }

    fun insertAllMedications(medications: List<MedicationEntity>) {
        viewModelScope.launch {
            medicationRepository.insertAllMedications(medications)
        }
    }

    fun getMedicationsForPatient(patientId: String): Flow<List<MedicationEntity>> {
        return medicationRepository.getMedicationsForPatient(patientId)
    }

    suspend fun getAverageMedicationsPerPatient(): Float? {
        return medicationRepository.getAverageMedicationsPerPatient()
    }

    class MedicationViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MedicationViewModel(appContext) as T
        }
    }
}