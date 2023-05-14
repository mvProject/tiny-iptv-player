/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:32
 *
 */

package com.mvproject.videoapp.ui.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.videoapp.data.repository.PreferenceRepository
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_ZERO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EpgSettingsViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UpdatePeriodState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoUpdatePeriod = preferenceRepository.getEpgInfoUpdatePeriod(),
                    mainUpdatePeriod = preferenceRepository.getMainEpgUpdatePeriod(),
                    alterUpdatePeriod = preferenceRepository.getAlterEpgUpdatePeriod()
                )
            }
        }
    }

    fun processAction(action: UpdateTypeAction) {
        when (action) {
            is UpdateTypeAction.UpdateInfoPeriod -> {
                val newType = action.type
                viewModelScope.launch {
                    _state.update {
                        it.copy(infoUpdatePeriod = newType)
                    }
                    preferenceRepository.setEpgInfoUpdatePeriod(type = newType)
                }
            }

            is UpdateTypeAction.UpdateMainEpgPeriod -> {
                val newType = action.type
                viewModelScope.launch {
                    _state.update {
                        it.copy(mainUpdatePeriod = newType)
                    }
                    preferenceRepository.setMainEpgUpdatePeriod(type = newType)
                }
            }

            is UpdateTypeAction.UpdateAlterEpgPeriod -> {
                val newType = action.type
                viewModelScope.launch {
                    _state.update {
                        it.copy(alterUpdatePeriod = newType)
                    }
                    preferenceRepository.setAlterEpgUpdatePeriod(type = newType)
                }
            }
        }
    }
}

data class UpdatePeriodState(
    val infoUpdatePeriod: Int = INT_VALUE_ZERO,
    val mainUpdatePeriod: Int = INT_VALUE_ZERO,
    val alterUpdatePeriod: Int = INT_VALUE_ZERO
)

sealed class UpdateTypeAction {
    data class UpdateInfoPeriod(val type: Int) : UpdateTypeAction()
    data class UpdateMainEpgPeriod(val type: Int) : UpdateTypeAction()
    data class UpdateAlterEpgPeriod(val type: Int) : UpdateTypeAction()
}