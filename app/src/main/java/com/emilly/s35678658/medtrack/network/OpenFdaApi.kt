package com.emilly.s35678658.medtrack.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFdaApi {

    @GET("drug/label.json")
    suspend fun searchDrug(
        @Query("search") search: String,
        @Query("limit") limit: Int = 1
    ): OpenFdaResponse
}