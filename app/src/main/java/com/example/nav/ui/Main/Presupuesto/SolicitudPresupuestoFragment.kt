package com.example.nav.ui.Main.Presupuesto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.nav.R
import com.example.nav.databinding.FragmentSecondBinding
import com.example.nav.databinding.FragmentSolicitudPresupuestoBinding

class SolicitudPresupuestoFragment : Fragment() {
    private var _binding: FragmentSolicitudPresupuestoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolicitudPresupuestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {
        binding.backBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_solicitudPresupuestoFragment_to_person,null))
    }
}