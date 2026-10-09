package com.emilly.s35678658.medtrack.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medications",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["patientId"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Index on patientId speeds up queries
    indices = [Index(value = ["patientId"])]
)
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val patientId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val scheduledTime: String,
    val medicationType: String,
    val notes: String
)
