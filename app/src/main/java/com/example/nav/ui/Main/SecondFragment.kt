package com.example.nav.ui.Main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.nav.R
import com.example.nav.databinding.FragmentFirstBinding
import com.example.nav.databinding.FragmentSecondBinding
import com.example.nav.databinding.FragmentSolicitudPresupuestoBinding
import com.example.nav.ui.Main.Presupuesto.SolicitudPresupuestoFragment
import com.example.nav.ui.Start.Start

class SecondFragment:Fragment(R.layout.fragment_second) {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {
        binding.SolPreBTN.setOnClickListener(Navigation.createNavigateOnClickListener(R.id
            .action_person_to_solicitudPresupuestoFragment,null))
        binding.ViewPresBTN.setOnClickListener(Navigation.createNavigateOnClickListener(R.id
            .action_person_to_viewPresupuestosFragment))
    }

//    https://umhandroid.momrach.es/android-navigation-java/
}
