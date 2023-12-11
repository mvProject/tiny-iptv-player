/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 15:24
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.ui.screens.settings.player.action.SettingsPlayerAction
import com.mvproject.tinyiptv.ui.screens.settings.player.state.SettingsPlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlayerViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _settingsPlayerState = MutableStateFlow(SettingsPlayerState())
    val settingsPlayerState = _settingsPlayerState.asStateFlow()

    init {
        viewModelScope.launch {
            _settingsPlayerState.update {
                it.copy(
                    isFullscreenEnabled = preferenceRepository.getDefaultFullscreenMode(),
                    resizeMode = preferenceRepository.getDefaultResizeMode()
                )
            }
        }
    }

    fun processAction(action: SettingsPlayerAction) {
        when (action) {
            is SettingsPlayerAction.SetFullScreenMode -> {
                val fullScreenState = action.state
                viewModelScope.launch {
                    _settingsPlayerState.update {
                        it.copy(isFullscreenEnabled = fullScreenState)
                    }
                    preferenceRepository.setDefaultFullscreenMode(state = fullScreenState)
                }
            }

            is SettingsPlayerAction.SetResizeMode -> {
                val resizeMode = action.mode
                viewModelScope.launch {
                    _settingsPlayerState.update {
                        it.copy(resizeMode = resizeMode)
                    }
                    preferenceRepository.setDefaultResizeMode(mode = resizeMode)
                }
            }

            is SettingsPlayerAction.SetRatioMode -> {
                val ratioMode = action.mode
                viewModelScope.launch {
                    _settingsPlayerState.update { current ->
                        current.copy(ratioMode = ratioMode)
                    }
                    preferenceRepository.setDefaultRatioMode(mode = ratioMode)
                }
            }
        }
    }
}