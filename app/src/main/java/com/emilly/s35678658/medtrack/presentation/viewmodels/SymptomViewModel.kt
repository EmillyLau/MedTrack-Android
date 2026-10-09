package com.emilly.s35678658.medtrack.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emilly.s35678658.medtrack.data.entities.SymptomEntity
import com.emilly.s35678658.medtrack.data.repositories.SymptomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SymptomViewModel(context: Context) : ViewModel() {

    private val symptomRepository = SymptomRepository(context)

    fun insertSymptom(symptom: SymptomEntity) {
        viewModelScope.launch {
            symptomRepository.insertSymptom(symptom)
        }
    }

    fun insertAllSymptoms(symptoms: List<SymptomEntity>) {
        viewModelScope.launch {
            symptomRepository.insertAllSymptoms(symptoms)
        }
    }

    fun getSymptomsForPatient(patientId: String): Flow<List<SymptomEntity>> {
        return symptomRepository.getSymptomsForPatient(patientId)
    }

    suspend fun getMostCommonSymptomCategory(): String? {
        return symptomRepository.getMostCommonSymptomCategory()
    }

    suspend fun getAverageSymptomSeverity(): Float? {
        return symptomRepository.getAverageSymptomSeverity()
    }

    class SymptomViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SymptomViewModel(appContext) as T
        }
    }
}