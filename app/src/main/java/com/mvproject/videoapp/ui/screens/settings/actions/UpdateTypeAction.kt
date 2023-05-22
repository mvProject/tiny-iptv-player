/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 13:14
 *
 */

package com.mvproject.videoapp.ui.screens.settings.actions

sealed class UpdateTypeAction {
    data class UpdateInfoPeriod(val type: Int) : UpdateTypeAction()
    data class UpdateMainEpgPeriod(val type: Int) : UpdateTypeAction()
    data class UpdateAlterEpgPeriod(val type: Int) : UpdateTypeAction()
}