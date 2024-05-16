package com.example.nav.ui

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nav.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    companion object {
        val DARK_MODE_PREF_KEY = booleanPreferencesKey("dark_mode")
    }

    val isDarkMode: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_PREF_KEY] ?: false
        }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[DARK_MODE_PREF_KEY] = isDarkMode
            }
        }
    }
}