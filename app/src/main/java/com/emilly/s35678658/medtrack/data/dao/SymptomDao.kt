package com.emilly.s35678658.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emilly.s35678658.medtrack.data.entities.SymptomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomDao {
    @Insert
    suspend fun insertSymptom(symptom: SymptomEntity)

    @Insert
    suspend fun insertAllSymptoms(symptoms: List<SymptomEntity>)

    // Flow so Symptoms screen reacts to new entries being saved
    @Query("SELECT * FROM symptoms WHERE patientId = :patientId ORDER BY dateTime DESC")
    fun getSymptomsForPatient(patientId: String): Flow<List<SymptomEntity>>

    // For clinician dashboard (Band C)
    @Query("SELECT category FROM symptoms GROUP BY category ORDER BY COUNT(*) DESC LIMIT 1")
    suspend fun getMostCommonSymptomCategory(): String?

    @Query("SELECT AVG(severity) FROM symptoms")
    suspend fun getAverageSymptomSeverity(): Float?

}