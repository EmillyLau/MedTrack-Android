package com.emilly.s35678658.medtrack.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emilly.s35678658.medtrack.data.entities.TakenStatusEntity
import com.emilly.s35678658.medtrack.data.repositories.TakenStatusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TakenStatusViewModel(context: Context) : ViewModel() {

    private val repository =
        TakenStatusRepository(context)

    fun insertTakenStatus(status: TakenStatusEntity) {

        viewModelScope.launch {
            repository.insertTakenStatus(status)
        }
    }

    fun getTakenStatusForDate(patientId: String, date: String): Flow<List<TakenStatusEntity>> {

        return repository.getTakenStatusForDate(patientId, date
        )
    }

    fun deleteTakenStatus(patientId: String, medicationName: String, scheduledTime: String, date: String
    ) {

        viewModelScope.launch {
            repository.deleteTakenStatus(patientId, medicationName, scheduledTime, date)
        }
    }

    class TakenStatusViewModelFactory(context: Context) : ViewModelProvider.Factory {

        private val appContext = context.applicationContext

        override fun <T : ViewModel> create(
            modelClass: Class<T>): T { return TakenStatusViewModel(appContext) as T
        }
    }
}