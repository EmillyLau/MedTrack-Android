package com.emilly.s35678658.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    // During CSV seeding, if same patientId is inserted twice, replace it
    @Insert
    suspend fun insertPatient(patient: PatientEntity)

    @Insert
    suspend fun insertAllPatients(patients: List<PatientEntity>)

    // Session restore: look up a patient by ID only (used when app restarts with a saved session)
    @Query("SELECT * FROM patients WHERE patientId = :patientId LIMIT 1")
    suspend fun getPatientById(patientId: String): PatientEntity?

    // Login: find patient by ID and password (after account is claimed)
    @Query("SELECT * FROM patients WHERE patientId = :patientId AND password = :password LIMIT 1")
    suspend fun getPatientByIdAndPassword(patientId: String, password: String): PatientEntity?

    // Account claiming: verify PatientID + phone number match before allowing password set
    @Query("SELECT * FROM patients WHERE patientId = :patientId AND phoneNumber = :phone LIMIT 1")
    suspend fun getPatientByIdAndPhone(patientId: String, phone: String): PatientEntity?

    // After account claiming, save the chosen password
    @Query("UPDATE patients SET password = :password WHERE patientId = :patientId")
    suspend fun setPassword(patientId: String, password: String)

    // For clinician dashboard: total patient count
    @Query("SELECT COUNT(*) FROM patients")
    suspend fun getPatientCount(): Int

    // Expose all patients as a Flow for reactive UI (used in clinician dashboard)
    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT patientId FROM patients")
    suspend fun getAllPatientIds(): List<String>
}
