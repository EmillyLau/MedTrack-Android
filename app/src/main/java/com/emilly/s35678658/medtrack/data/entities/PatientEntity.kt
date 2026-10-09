package com.emilly.s35678658.medtrack.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity (
    @PrimaryKey
    val patientId: String,
    val phoneNumber: String,
    val name: String,
    val password: String? = null
)
