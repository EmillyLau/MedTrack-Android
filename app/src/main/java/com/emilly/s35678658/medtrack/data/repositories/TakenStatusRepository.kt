package com.emilly.s35678658.medtrack.data.repositories

import android.content.Context
import com.emilly.s35678658.medtrack.data.database.MedTrackDatabase
import com.emilly.s35678658.medtrack.data.entities.TakenStatusEntity
import kotlinx.coroutines.flow.Flow

class TakenStatusRepository(context: Context) {

    private val takenStatusDao =
        MedTrackDatabase.getDatabase(context).takenStatusDao()

    suspend fun insertTakenStatus(status: TakenStatusEntity) {
        takenStatusDao.insertTakenStatus(status)
    }

    fun getTakenStatusForDate(
        patientId: String, date: String): Flow<List<TakenStatusEntity>> {
        return takenStatusDao.getTakenStatusForDate(patientId, date)
    }

    suspend fun deleteTakenStatus(patientId: String, medicationName: String, scheduledTime: String, date: String
    ) {
        takenStatusDao.deleteTakenStatus(patientId, medicationName, scheduledTime, date)
    }
}