package com.emilly.s35678658.medtrack.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "taken_status",
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
data class TakenStatusEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: String,
    val medicationName: String,
    val scheduledTime: String,
    val date: String,
    val isTaken: Boolean
)