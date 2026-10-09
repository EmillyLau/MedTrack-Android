package com.emilly.s35678658.medtrack.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "med_coach_tips",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["patientId"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["patientId"])]
)
data class MedCoachTipEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: String,
    val tipText: String,
    val timestamp: String
)