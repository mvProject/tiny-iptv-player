/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.05.23, 15:40
 *
 */

package com.mvproject.videoapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.videoapp.utils.AppConstants.LONG_NO_VALUE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun setCurrentPlaylistId(playlistId: Long) {
        dataStore.edit { settings ->
            settings[SELECTED_PLAYLIST] = playlistId
        }
    }

    suspend fun loadCurrentPlaylistId() = dataStore.data.map { preferences ->
        preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
    }.first()

    val currentPlaylistId
        get() = dataStore.data.map { preferences ->
            preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
        }

    suspend fun setMainInfoExist(state: Boolean) {
        dataStore.edit { settings ->
            settings[MAIN_INFO_EXIST] = state
        }
    }

    suspend fun getMainInfoExist() = dataStore.data.map { preferences ->
        preferences[MAIN_INFO_EXIST] ?: false
    }.first()

    suspend fun setMainInfoUse(state: Boolean) {
        dataStore.edit { settings ->
            settings[MAIN_INFO_USE] = state
        }
    }

    suspend fun getMainInfoUse() = dataStore.data.map { preferences ->
        preferences[MAIN_INFO_USE] ?: false
    }.first()


    suspend fun setAlterInfoExist(state: Boolean) {
        dataStore.edit { settings ->
            settings[ALTER_INFO_EXIST] = state
        }
    }

    suspend fun getAlterInfoExist() = dataStore.data.map { preferences ->
        preferences[ALTER_INFO_EXIST] ?: false
    }.first()

    suspend fun setAlterInfoUse(state: Boolean) {
        dataStore.edit { settings ->
            settings[ALTER_INFO_USE] = state
        }
    }

    suspend fun getAlterInfoUse() = dataStore.data.map { preferences ->
        preferences[ALTER_INFO_USE] ?: false
    }.first()

    suspend fun setChannelsViewType(type: String) {
        dataStore.edit { settings ->
            settings[CHANNELS_VIEW_TYPE] = type
        }
    }

    suspend fun getChannelsViewType() = dataStore.data.map { preferences ->
        preferences[CHANNELS_VIEW_TYPE]
    }.first()


    suspend fun setEpgInfoLastUpdate(timestamp: Long) {
        dataStore.edit { settings ->
            settings[EPG_INFO_LAST_UPDATE] = timestamp
        }
    }

    suspend fun getEpgInfoLastUpdate() = dataStore.data.map { preferences ->
        preferences[EPG_INFO_LAST_UPDATE] ?: LONG_NO_VALUE
    }.first()

    suspend fun setPlaylistLastUpdate(playlistId: Long, timestamp: Long) {
        dataStore.edit { settings ->
            val playlistLastUpdateKey =
                longPreferencesKey(PLAYLIST_LAST_UPDATE + playlistId.toString())
            settings[playlistLastUpdateKey] = timestamp
        }
    }

    suspend fun getPlaylistLastUpdate(playlistId: Long) = dataStore.data.map { preferences ->
        val playlistLastUpdateKey =
            longPreferencesKey(PLAYLIST_LAST_UPDATE + playlistId.toString())
        preferences[playlistLastUpdateKey] ?: LONG_NO_VALUE
    }.first()

    suspend fun setMainEpgLastUpdate(timestamp: Long) {
        dataStore.edit { settings ->
            settings[EPG_MAIN_LAST_UPDATE] = timestamp
        }
    }

    suspend fun getMainEpgLastUpdate() = dataStore.data.map { preferences ->
        preferences[EPG_MAIN_LAST_UPDATE] ?: LONG_NO_VALUE
    }.first()

    suspend fun setAlterEpgLastUpdate(timestamp: Long) {
        dataStore.edit { settings ->
            settings[EPG_ALTER_LAST_UPDATE] = timestamp
        }
    }

    suspend fun getAlterEpgLastUpdate() = dataStore.data.map { preferences ->
        preferences[EPG_ALTER_LAST_UPDATE]
    }.first()


    suspend fun setEpgInfoUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_INFO_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getEpgInfoUpdatePeriod() = dataStore.data.map { preferences ->
        preferences[EPG_INFO_LAST_UPDATE_PERIOD] ?: INT_VALUE_ZERO
    }.first()


    suspend fun setMainEpgUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_MAIN_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getMainEpgUpdatePeriod() = dataStore.data.map { preferences ->
        preferences[EPG_MAIN_LAST_UPDATE_PERIOD] ?: INT_VALUE_ZERO
    }.first()

    suspend fun setAlterEpgUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_ALTER_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getAlterEpgUpdatePeriod() = dataStore.data.map { preferences ->
        preferences[EPG_ALTER_LAST_UPDATE_PERIOD] ?: INT_VALUE_ZERO
    }.first()


    private companion object {
        val SELECTED_PLAYLIST = longPreferencesKey("SelectedPlaylist")
        val MAIN_INFO_EXIST = booleanPreferencesKey("MainInfoExist")
        val MAIN_INFO_USE = booleanPreferencesKey("AlterInfoExist")
        val ALTER_INFO_EXIST = booleanPreferencesKey("MainInfoUsing")
        val ALTER_INFO_USE = booleanPreferencesKey("AlterInfoUsing")
        val CHANNELS_VIEW_TYPE = stringPreferencesKey("ChannelsViewType")

        val EPG_INFO_LAST_UPDATE = longPreferencesKey("EpgInfoLastUpdate")
        val EPG_MAIN_LAST_UPDATE = longPreferencesKey("EpgMainLastUpdate")
        val EPG_ALTER_LAST_UPDATE = longPreferencesKey("EpgAlterLastUpdate")

        val EPG_INFO_LAST_UPDATE_PERIOD = intPreferencesKey("EpgInfoLastUpdatePeriod")
        val EPG_MAIN_LAST_UPDATE_PERIOD = intPreferencesKey("EpgMainLastUpdatePeriod")
        val EPG_ALTER_LAST_UPDATE_PERIOD = intPreferencesKey("EpgAlterLastUpdatePeriod")

        const val PLAYLIST_LAST_UPDATE = "PlaylistLastUpdate"
    }
}