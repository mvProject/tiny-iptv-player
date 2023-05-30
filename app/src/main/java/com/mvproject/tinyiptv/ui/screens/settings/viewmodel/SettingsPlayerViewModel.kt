/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:32
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.ui.screens.settings.actions.SettingsPlayerAction
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlayerViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _playerSettingsState = MutableStateFlow(PlayerSettingsState())
    val playerSettingsState = _playerSettingsState.asStateFlow()

    init {
        viewModelScope.launch {
            _playerSettingsState.update {
                it.copy(
                    isFullscreenEnabled = preferenceRepository.getDefaultFullscreenMode(),
                    resizeMode = preferenceRepository.getDefaultResizeMode()
                )
            }
        }
    }

    fun processAction(action: SettingsPlayerAction) {
        when (action) {
            is SettingsPlayerAction.ChangeFullScreenMode -> {
                val fullScreenState = action.state
                Napier.w("testing fullScreenState:$fullScreenState")
                viewModelScope.launch {
                    _playerSettingsState.update {
                        it.copy(isFullscreenEnabled = fullScreenState)
                    }
                    preferenceRepository.setDefaultFullscreenMode(state = fullScreenState)
                }
            }

            is SettingsPlayerAction.ChangeResizeModePeriod -> {
                val resizeMode = action.mode
                Napier.w("testing resizeMode:$resizeMode")
                viewModelScope.launch {
                    _playerSettingsState.update {
                        it.copy(resizeMode = resizeMode)
                    }
                    preferenceRepository.setDefaultResizeMode(mode = resizeMode)
                }
            }

            SettingsPlayerAction.NavigateBack -> {}
        }

    }

    data class PlayerSettingsState(
        val resizeMode: Int = ResizeMode.Fill.value,
        val isFullscreenEnabled: Boolean = true,
    )
}