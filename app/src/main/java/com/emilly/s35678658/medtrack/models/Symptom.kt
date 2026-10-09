package com.emilly.s35678658.medtrack.models

data class Symptom(
    val patientId: String,
    val category: String,
    val severity: Int,
    val notes: String,
    val dateTime: String
)
