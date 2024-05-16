package com.example.nav

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import com.example.nav.ui.SettingsViewModel.Companion.DARK_MODE_PREF_KEY
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


val Context.dataStore by preferencesDataStore(name = "settings")
@HiltAndroidApp
class NavApp:Application(){

    override fun onCreate() {
        super.onCreate()

        // Carga las preferencias al iniciar la aplicación
        loadDarkModePreference()
    }

    private fun loadDarkModePreference() {
        val appScope = CoroutineScope(Dispatchers.Default)
        appScope.launch {
            val preferences = applicationContext.dataStore.data.firstOrNull()
            Log.d("DataStore", "Is Dark Mode: $preferences")
            val isDarkMode = preferences?.get(IS_DARK_MODE_KEY) ?: false
            Log.d("DataStore", "Is Dark Mode: $isDarkMode")
            val currentMode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }


            // Establece el modo de noche según las preferencias cargadas
            runBlocking {
                AppCompatDelegate.setDefaultNightMode(currentMode)
            }
        }
    }

    companion object {
        private val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    }
}