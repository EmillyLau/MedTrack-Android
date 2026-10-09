package com.emilly.s35678658.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emilly.s35678658.medtrack.data.entities.MedCoachTipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedCoachTipDao {

    @Insert
    suspend fun insertTip(tip: MedCoachTipEntity)

    @Query("SELECT * FROM med_coach_tips WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getTipsForPatient(patientId: String): Flow<List<MedCoachTipEntity>>
}