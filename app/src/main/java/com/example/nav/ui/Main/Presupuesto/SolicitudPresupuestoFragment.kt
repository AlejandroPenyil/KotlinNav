package com.example.nav.ui.Main.Presupuesto

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.nav.R
import com.example.nav.databinding.FragmentSecondBinding
import com.example.nav.databinding.FragmentSolicitudPresupuestoBinding
import com.example.nav.dto.SolicitudDTO
import com.example.nav.ui.Start.UserDataHolder.usuarioDTO
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

class SolicitudPresupuestoFragment : Fragment() {
    private var _binding: FragmentSolicitudPresupuestoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolicitudPresupuestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUi() {
        binding.backBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_solicitudPresupuestoFragment_to_person,null))
        binding.btnSend.setOnClickListener { enviar() }
    }

    //var solicitud: SolicitudDTO? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private fun enviar() {
        val descripcion = binding.textInputEditText.text.toString()

        val solicitud = SolicitudDTO(
            id = null,  // Puedes asignar un valor específico si es necesario
            fechaSolicitud = Instant.now().toString(),  // Método para obtener la fecha actual en el formato deseado
            descripcion = descripcion,
            atendida = false,  // Asumiendo que inicialmente no está atendida
            idUsuario = usuarioDTO?.id  // Supongo que tienes un usuarioDTO con el id del usuario actual
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.getClient().create(APIService::class.java).createSolicitud(solicitud)

                if (response.isSuccessful) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(activity, "Solicitud enviada exitosamente", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_solicitudPresupuestoFragment_to_person)
                    }
                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(activity, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}