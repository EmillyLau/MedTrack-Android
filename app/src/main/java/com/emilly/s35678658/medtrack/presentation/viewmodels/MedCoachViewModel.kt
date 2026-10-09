package com.emilly.s35678658.medtrack.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emilly.s35678658.medtrack.data.entities.MedCoachTipEntity
import com.emilly.s35678658.medtrack.data.repositories.MedCoachTipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MedCoachViewModel(context: Context) : ViewModel() {

    private val repository = MedCoachTipRepository(context)

    fun insertTip(tip: MedCoachTipEntity) {
        viewModelScope.launch {
            repository.insertTip(tip)
        }
    }

    fun getTipsForPatient(patientId: String): Flow<List<MedCoachTipEntity>> {
        return repository.getTipsForPatient(patientId)
    }

    class MedCoachViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MedCoachViewModel(appContext) as T
        }
    }
}