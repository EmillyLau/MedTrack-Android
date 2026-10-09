package com.emilly.s35678658.medtrack.data.repositories

import com.emilly.s35678658.medtrack.network.DrugLabelResult
import com.emilly.s35678658.medtrack.network.RetrofitClient

class OpenFdaRepository {

    suspend fun searchDrug(drugName: String): DrugLabelResult? {
        val brandResult = try {
            RetrofitClient.openFdaApi
                .searchDrug("""openfda.brand_name:"$drugName"""")
                .results
                ?.firstOrNull()
        } catch (e: Exception) {
            null
        }

        if (brandResult != null) {
            return brandResult
        }

        return try {
            RetrofitClient.openFdaApi
                .searchDrug("""openfda.generic_name:"$drugName"""")
                .results
                ?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}