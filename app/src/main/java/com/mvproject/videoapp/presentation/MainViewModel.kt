/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.videoapp.data.helpers.InfoChannelHelper
import com.mvproject.videoapp.data.manager.EpgManager
import kotlinx.coroutines.launch

class MainViewModel(
    private val epgManager: EpgManager,
    private val infoChannelHelper: InfoChannelHelper,

    ) : ViewModel() {

    fun prepareInfo() {
        viewModelScope.launch {
            epgManager.prepareEpgInfo()
        }
    }

    fun loadEpg() {
        viewModelScope.launch {
            epgManager.getMainEpgData()
        }
    }

    fun loadAlterEpg() {
        viewModelScope.launch {
            epgManager.getAlterEpg()
        }
    }

    fun checkAllPlaylistsChannelsInfo() {
        viewModelScope.launch {
            infoChannelHelper.checkAllPlaylistsChannelsInfo()
        }
    }
}

