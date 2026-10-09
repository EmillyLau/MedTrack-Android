package com.emilly.s35678658.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert
    suspend fun insertMedication(medication: MedicationEntity)

    @Insert
    suspend fun insertAllMedications(medications: List<MedicationEntity>)

    // Returns a Flow so the Home screen updates automatically when meds change
    @Query("SELECT * FROM medications WHERE patientId = :patientId ORDER BY scheduledTime")
    fun getMedicationsForPatient(patientId: String): Flow<List<MedicationEntity>>

    // For clinician dashboard average meds per patient
    @Query("SELECT AVG(medCount) FROM (SELECT COUNT(*) as medCount FROM medications GROUP BY patientId)")
    suspend fun getAverageMedicationsPerPatient(): Float?

}
