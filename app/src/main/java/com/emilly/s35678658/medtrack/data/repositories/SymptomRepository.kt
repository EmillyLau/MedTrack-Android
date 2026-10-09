package com.emilly.s35678658.medtrack.data.repositories

import android.content.Context
import com.emilly.s35678658.medtrack.data.database.MedTrackDatabase
import com.emilly.s35678658.medtrack.data.entities.SymptomEntity
import kotlinx.coroutines.flow.Flow

class SymptomRepository(context: Context) {

    private val symptomDao = MedTrackDatabase.getDatabase(context).symptomDao()

    suspend fun insertSymptom(symptom: SymptomEntity) {
        symptomDao.insertSymptom(symptom)
    }

    suspend fun insertAllSymptoms(symptoms: List<SymptomEntity>) {
        symptomDao.insertAllSymptoms(symptoms)
    }

    fun getSymptomsForPatient(patientId: String): Flow<List<SymptomEntity>> {
        return symptomDao.getSymptomsForPatient(patientId)
    }

    suspend fun getMostCommonSymptomCategory(): String? {
        return symptomDao.getMostCommonSymptomCategory()
    }

    suspend fun getAverageSymptomSeverity(): Float? {
        return symptomDao.getAverageSymptomSeverity()
    }
}