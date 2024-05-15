package com.example.nav.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.nav.databinding.FragmentThirdBinding
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDarkMode()
        initUI()
    }

    private fun initUI() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            applyDarkMode(isChecked)
        }
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        val currentMode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(currentMode)

        // Guardar el estado del modo oscuro
        lifecycleScope.launch {
            requireContext().dataStore.edit { preferences ->
                preferences[IS_DARK_MODE_KEY] = isDarkMode
            }
        }
    }

    private fun observeDarkMode() {
        lifecycleScope.launchWhenStarted {
            requireContext().dataStore.data.collect { preferences ->
                val isDarkMode = preferences[IS_DARK_MODE_KEY] ?: false
                binding.switchDarkMode.isChecked = isDarkMode
                applyDarkMode(isDarkMode)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val IS_DARK_MODE_KEY = booleanPreferencesKey(("is_dark_mode"))
    }
}