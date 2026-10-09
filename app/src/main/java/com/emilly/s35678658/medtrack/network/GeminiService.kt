package com.emilly.s35678658.medtrack.network

import com.emilly.s35678658.medtrack.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

object GeminiService {

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.MEDTRACK_GEMINI_API_KEY
    )

    suspend fun generateTip(prompt: String): String {

        val response = model.generateContent(prompt)

        return response.text
            ?: "Unable to generate a tip right now."
    }
}