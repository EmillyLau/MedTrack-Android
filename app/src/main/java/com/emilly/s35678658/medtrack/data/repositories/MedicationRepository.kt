package com.emilly.s35678658.medtrack.data.repositories

import android.content.Context
import com.emilly.s35678658.medtrack.data.database.MedTrackDatabase
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import kotlinx.coroutines.flow.Flow

class MedicationRepository(context: Context) {

    private val medicationDao = MedTrackDatabase.getDatabase(context).medicationDao()

    suspend fun insertMedication(medication: MedicationEntity) {
        medicationDao.insertMedication(medication)
    }

    suspend fun insertAllMedications(medications: List<MedicationEntity>) {
        medicationDao.insertAllMedications(medications)
    }

    fun getMedicationsForPatient(patientId: String): Flow<List<MedicationEntity>> {
        return medicationDao.getMedicationsForPatient(patientId)
    }

    suspend fun getAverageMedicationsPerPatient(): Float? {
        return medicationDao.getAverageMedicationsPerPatient()
    }
}