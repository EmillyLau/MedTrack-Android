package com.emilly.s35678658.medtrack.data.repositories

import android.content.Context
import com.emilly.s35678658.medtrack.data.database.MedTrackDatabase
import com.emilly.s35678658.medtrack.data.entities.MedCoachTipEntity
import kotlinx.coroutines.flow.Flow

class MedCoachTipRepository(context: Context) {

    private val medCoachTipDao =
        MedTrackDatabase.getDatabase(context).medCoachTipDao()

    suspend fun insertTip(tip: MedCoachTipEntity) {
        medCoachTipDao.insertTip(tip)
    }

    fun getTipsForPatient(patientId: String): Flow<List<MedCoachTipEntity>> {
        return medCoachTipDao.getTipsForPatient(patientId)
    }
}