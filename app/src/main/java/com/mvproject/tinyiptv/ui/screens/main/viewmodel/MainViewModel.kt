/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.helpers.SyncHelper
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class MainViewModel(
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
}

