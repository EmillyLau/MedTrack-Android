package com.emilly.s35678658.medtrack.models

data class Medication(
    val patientId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val scheduledTime: String,
    val medicationType: String,
    val notes: String
)
