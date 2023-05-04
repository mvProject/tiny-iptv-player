/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mvproject.videoapp.utils.AppConstants.LONG_NO_VALUE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferenceRepository(private val dataStore: DataStore<Preferences>) {


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


    private companion object {
        val SELECTED_PLAYLIST = longPreferencesKey("SelectedPlaylist")
        val MAIN_INFO_EXIST = booleanPreferencesKey("MainInfoExist")
        val MAIN_INFO_USE = booleanPreferencesKey("AlterInfoExist")
        val ALTER_INFO_EXIST = booleanPreferencesKey("MainInfoUsing")
        val ALTER_INFO_USE = booleanPreferencesKey("AlterInfoUsing")
        val CHANNELS_VIEW_TYPE = stringPreferencesKey("ChannelsViewType")
    }
}