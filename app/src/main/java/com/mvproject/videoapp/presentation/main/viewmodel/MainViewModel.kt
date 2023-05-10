/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.videoapp.data.helpers.SyncHelper
import com.mvproject.videoapp.data.manager.EpgManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class MainViewModel(
    private val epgManager: EpgManager,
    private val syncHelper: SyncHelper
) : ViewModel() {

    val infoUpdateState = syncHelper.infoUpdateState
    val alterUpdateState = syncHelper.alterUpdateState
    val mainUpdateState = syncHelper.mainUpdateState

    fun checkEpgInfoUpdate() {
        Napier.w("testing checkEpgInfoUpdate")
        viewModelScope.launch {
            syncHelper.checkEpgInfoUpdate()
        }
    }

    fun checkAlterEpgUpdate() {
        Napier.w("testing checkAlterEpgUpdate")
        viewModelScope.launch {
            syncHelper.checkAlterEpgUpdate()
        }
    }

    fun checkMainEpgUpdate() {
        Napier.w("testing checkMainEpgUpdate")
        viewModelScope.launch {
            syncHelper.checkMainEpgUpdate()
        }
    }

    fun loadEpg() {
        viewModelScope.launch {
            epgManager.getMainEpgData()
        }
    }
}

