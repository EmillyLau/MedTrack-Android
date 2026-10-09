package com.emilly.s35678658.medtrack.utils

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import com.emilly.s35678658.medtrack.data.repositories.PatientRepository

object AuthManager {

    val currentPatientId: MutableState<String?> = mutableStateOf(null)

    /**
     * Call once in MainActivity.onCreate().
     * Restores saved session from SharedPreferences so app skips login on restart.
     */
    fun initialise(context: Context) {
        val prefs = context.getSharedPreferences("MedTrackPrefs", Context.MODE_PRIVATE)
        currentPatientId.value = prefs.getString("logged_in_patient_id", null)
    }

    fun isLoggedIn(): Boolean = currentPatientId.value != null

    fun getPatientId(): String? = currentPatientId.value

    fun saveSession(context: Context, patientId: String) {
        val prefs = context.getSharedPreferences("MedTrackPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("logged_in_patient_id", patientId).apply()
        currentPatientId.value = patientId
    }

    /**
     * Clears both in-memory state and SharedPreferences.
     * Navigation is handled in the composable
     * to satisfy "Back does not return to Home" requirement.
     */
    fun logout(context: Context) {
        val prefs = context.getSharedPreferences("MedTrackPrefs", Context.MODE_PRIVATE)
        prefs.edit().remove("logged_in_patient_id").apply()
        currentPatientId.value = null
    }
}
