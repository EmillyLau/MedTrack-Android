package com.emilly.s35678658.medtrack.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emilly.s35678658.medtrack.data.dao.MedicationDao
import com.emilly.s35678658.medtrack.data.dao.PatientDao
import com.emilly.s35678658.medtrack.data.dao.SymptomDao
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import com.emilly.s35678658.medtrack.data.entities.SymptomEntity
import com.emilly.s35678658.medtrack.data.dao.MedCoachTipDao
import com.emilly.s35678658.medtrack.data.dao.TakenStatusDao
import com.emilly.s35678658.medtrack.data.entities.MedCoachTipEntity
import com.emilly.s35678658.medtrack.data.entities.TakenStatusEntity

@Database(
    entities = [PatientEntity::class, MedicationEntity::class, SymptomEntity::class, MedCoachTipEntity::class, TakenStatusEntity::class],
    version = 3,
    exportSchema = false
)
abstract class MedTrackDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun medicationDao(): MedicationDao
    abstract fun symptomDao(): SymptomDao
    abstract fun medCoachTipDao(): MedCoachTipDao
    abstract fun takenStatusDao(): TakenStatusDao

    companion object {
        @Volatile
        private var INSTANCE: MedTrackDatabase? = null

        fun getDatabase(context: Context): MedTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedTrackDatabase::class.java,
                    "medtrack_database"
                )
                    .fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}
