package com.example.nav.ui.Main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav.databinding.FragmentThirdBinding
import com.example.nav.ui.SettingsViewModel
import com.example.nav.ui.Start.Start
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

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

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        initUI()
        observeDarkMode()
    }

    private fun initUI() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
        }
        binding.backStart.setOnClickListener { goStart() }

        binding.help.setOnClickListener { openHelpUrl() }
    }

    private fun goStart() {
        val intent = Intent(requireActivity(), Start::class.java)
        startActivity(intent)
    }

    private fun observeDarkMode() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isDarkMode.collect { isDarkMode ->
                Log.d("DarkMode", "Is Dark Mode: $isDarkMode")
                binding.switchDarkMode.isChecked = isDarkMode
                applyDarkMode(isDarkMode)
            }
        }
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        val currentMode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(currentMode)
    }

    private fun openHelpUrl() {
        val url = "https://concrete-binder-4b1.notion.site/Wiki-de-la-aplicaci-n-Terranaturale-52bb58bf15e84261ad2a7efaca3c19e1"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
