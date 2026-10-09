package com.emilly.s35678658.medtrack.utils

import android.content.Context
import android.content.SharedPreferences
import com.emilly.s35678658.medtrack.data.database.MedTrackDatabase
import com.emilly.s35678658.medtrack.data.entities.MedicationEntity
import com.emilly.s35678658.medtrack.data.entities.PatientEntity
import com.emilly.s35678658.medtrack.data.entities.SymptomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CsvSeeder(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("MedTrackPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    suspend fun seedDatabaseIfNeeded(database: MedTrackDatabase) {
        if (prefs.getBoolean("db_seeded", false)) return

        withContext(Dispatchers.IO) {
            val patientDao = database.patientDao()
            val medicationDao = database.medicationDao()
            val symptomDao = database.symptomDao()

            // Load data from CSV files and Shared Preferences
            val patients = loadPatients() + loadPatientsFromPrefs()
            val medications = loadMedications() + loadMedicationsFromPrefs()
            val symptoms = loadSymptoms()

            // Insert data into the database
            patientDao.insertAllPatients(patients)
            medicationDao.insertAllMedications(medications)
            symptomDao.insertAllSymptoms(symptoms)

            // Mark as seeded
            prefs.edit().putBoolean("db_seeded", true).apply()

        }
    }

    // ---------- CSV LOADERS ----------

    private fun loadPatients(): List<PatientEntity> {
        val list = mutableListOf<PatientEntity>()

        val input = context.assets.open("patients.csv")
        val reader = BufferedReader(InputStreamReader(input))

        reader.readLine() // skip header

        reader.forEachLine { line ->
            val tokens = parseCsvLine(line)

            if (tokens.size >= 3) {
                list.add(
                    PatientEntity(
                        patientId = tokens[0].trim(),
                        phoneNumber = tokens[1].trim(),
                        name = tokens[2].trim(),
                        password = null // IMPORTANT: no password initially
                    )
                )
            }
        }

        reader.close()
        return list
    }

    private fun loadPatientsFromPrefs(): List<PatientEntity> {
        val json = prefs.getString("users", null) ?: return emptyList()

        val type = object : TypeToken<List<com.emilly.s35678658.medtrack.models.Patient>>() {}.type
        val oldUsers: List<com.emilly.s35678658.medtrack.models.Patient> = gson.fromJson(json, type)

        return oldUsers.map {
            PatientEntity(
                patientId = it.patientId,
                phoneNumber = it.phoneNumber,
                name = it.name,
                password = it.password
            )
        }
    }

    private fun loadMedications(): List<MedicationEntity> {
        val list = mutableListOf<MedicationEntity>()

        val input = context.assets.open("medications.csv")
        val reader = BufferedReader(InputStreamReader(input))

        reader.readLine()

        reader.forEachLine { line ->
            val tokens = parseCsvLine(line)

            if (tokens.size >= 7) {
                list.add(
                    MedicationEntity(
                        patientId = tokens[0].trim(),
                        medicationName = tokens[1].trim(),
                        dosage = tokens[2].trim(),
                        frequency = tokens[3].trim(),
                        scheduledTime = tokens[4].trim(),
                        medicationType = tokens[5].trim(),
                        notes = tokens[6].trim()
                    )
                )
            }
        }

        reader.close()
        return list
    }

    private fun loadMedicationsFromPrefs(): List<MedicationEntity> {
        val json = prefs.getString("medications", null) ?: return emptyList()
        val type =
            object : TypeToken<List<com.emilly.s35678658.medtrack.models.Medication>>() {}.type
        val oldMeds: List<com.emilly.s35678658.medtrack.models.Medication> =
            gson.fromJson(json, type)

        return oldMeds.map {
            MedicationEntity(
                patientId = it.patientId,
                medicationName = it.medicationName,
                dosage = it.dosage,
                frequency = it.frequency,
                scheduledTime = it.scheduledTime,
                medicationType = it.medicationType,
                notes = it.notes
            )
        }
    }

    private fun loadSymptoms(): List<SymptomEntity> {
        val list = mutableListOf<SymptomEntity>()

        val input = context.assets.open("symptoms.csv")
        val reader = BufferedReader(InputStreamReader(input))

        reader.readLine()

        reader.forEachLine { line ->
            val tokens = parseCsvLine(line)

            if (tokens.size >= 5) {

                val patientId = tokens[0].trim()
                val category = tokens[1].trim()
                val severity = tokens[2].trim().toInt()
                val dateTime = tokens.last().trim()

                val notes = tokens
                    .subList(3, tokens.lastIndex)
                    .joinToString(", ") { it.trim() }

                list.add(
                    SymptomEntity(
                        patientId = patientId,
                        category = category,
                        severity = severity,
                        notes = notes,
                        dateTime = dateTime
                    )
                )
            }
        }

        reader.close()
        return list
    }
}

private fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    val current = StringBuilder()
    var insideQuotes = false

    for (char in line) {
        when {
            char == '"' -> {
                insideQuotes = !insideQuotes
            }

            char == ',' && !insideQuotes -> {
                result.add(current.toString().trim())
                current.clear()
            }

            else -> {
                current.append(char)
            }
        }
    }

    result.add(current.toString().trim())

    return result
}

