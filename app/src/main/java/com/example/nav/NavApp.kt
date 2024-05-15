package com.example.nav

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore(name = "settings")
@HiltAndroidApp
class NavApp:Application(){

    override fun onCreate() {
        super.onCreate()

        // Carga las preferencias al iniciar la aplicación
        loadDarkModePreference()
    }

    private fun loadDarkModePreference() {
        GlobalScope.launch(Dispatchers.IO) {
            val preferences = applicationContext.dataStore.data.first()
            val isDarkMode = preferences[IS_DARK_MODE_KEY] ?: false
            val currentMode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            // Establece el modo de noche según las preferencias cargadas
            withContext(Dispatchers.Main) {
                AppCompatDelegate.setDefaultNightMode(currentMode)
            }
        }
    }

    companion object {
        private val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    }
}