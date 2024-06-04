package com.example.nav.ui.Main.Presupuesto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.nav.R
import com.example.nav.databinding.FragmentSolicitudPresupuestoBinding
import com.example.nav.databinding.FragmentViewPresupuestosBinding


class ViewPresupuestosFragment : Fragment() {
    private var _binding: FragmentViewPresupuestosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPresupuestosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {
        binding.BackBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_viewPresupuestosFragment_to_person,null))
    }
}