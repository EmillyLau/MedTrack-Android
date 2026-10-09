package com.emilly.s35678658.medtrack.data.repositories

import com.emilly.s35678658.medtrack.network.GeminiService

class GeminiRepository {

    suspend fun generateTip(prompt: String): String {
        return GeminiService.generateTip(prompt)
    }
}