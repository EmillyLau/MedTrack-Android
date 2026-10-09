package com.emilly.s35678658.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emilly.s35678658.medtrack.data.entities.TakenStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TakenStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTakenStatus(status: TakenStatusEntity)

    @Query("""SELECT * FROM taken_status WHERE patientId = :patientId AND date = :date""")
    fun getTakenStatusForDate(patientId: String, date: String): Flow<List<TakenStatusEntity>>

    @Query("""DELETE FROM taken_status WHERE patientId = :patientId AND medicationName = :medicationName AND scheduledTime = :scheduledTime AND date = :date""")
    suspend fun deleteTakenStatus(patientId: String, medicationName: String, scheduledTime: String, date: String)
}