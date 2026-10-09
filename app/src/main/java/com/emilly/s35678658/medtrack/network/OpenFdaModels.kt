package com.emilly.s35678658.medtrack.network

data class OpenFdaResponse(
    val results: List<DrugLabelResult>?
)

data class DrugLabelResult(
    val purpose: List<String>?,
    val warnings: List<String>?,
    val dosage_and_administration: List<String>?,
    val active_ingredient: List<String>?,
    val indications_and_usage: List<String>?
)