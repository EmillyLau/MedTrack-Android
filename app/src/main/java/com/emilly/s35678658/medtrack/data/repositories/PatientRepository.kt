package com.emilly.s35678658.medtrack.data.repositories

import android.content.Context
import com.emilly.s35678658.medtrack.data.database.MedTrackDatabase
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import kotlinx.coroutines.flow.Flow

class PatientRepository(context: Context) {

    private val patientDao = MedTrackDatabase.getDatabase(context).patientDao()

    suspend fun insertPatient(patient: PatientEntity) {
        patientDao.insertPatient(patient)
    }

    suspend fun insertAllPatients(patients: List<PatientEntity>) {
        patientDao.insertAllPatients(patients)
    }

    suspend fun getPatientById(patientId: String): PatientEntity? {
        return patientDao.getPatientById(patientId)
    }

    suspend fun getPatientByIdAndPassword(
        patientId: String,
        password: String
    ): PatientEntity? {
        return patientDao.getPatientByIdAndPassword(patientId, password)
    }

    suspend fun getPatientByIdAndPhone(
        patientId: String,
        phone: String
    ): PatientEntity? {
        return patientDao.getPatientByIdAndPhone(patientId, phone)
    }

    suspend fun setPassword(patientId: String, password: String) {
        patientDao.setPassword(patientId, password)
    }

    suspend fun getPatientCount(): Int {
        return patientDao.getPatientCount()
    }

    fun getAllPatients(): Flow<List<PatientEntity>> {
        return patientDao.getAllPatients()
    }

    suspend fun getAllPatientIds(): List<String> {
        return patientDao.getAllPatientIds()
    }
}