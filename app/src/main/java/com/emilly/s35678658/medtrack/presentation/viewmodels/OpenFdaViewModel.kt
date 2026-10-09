package com.emilly.s35678658.medtrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emilly.s35678658.medtrack.data.repositories.OpenFdaRepository
import com.emilly.s35678658.medtrack.network.DrugLabelResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OpenFdaViewModel : ViewModel() {

    private val openFdaRepository = OpenFdaRepository()

    private val _drugInfo = MutableStateFlow<DrugLabelResult?>(null)
    val drugInfo: StateFlow<DrugLabelResult?> = _drugInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun searchDrug(drugName: String) {
        if (drugName.isBlank()) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""
                _drugInfo.value = null

                val result = openFdaRepository.searchDrug(drugName.trim())

                if (result != null) {
                    _drugInfo.value = result
                } else {
                    _errorMessage.value = "Drug not found"
                }

            } catch (e: Exception) {
                _errorMessage.value =
                    "Unable to load drug information. Please check your connection."
            } finally {
                _isLoading.value = false
            }
        }
    }
}