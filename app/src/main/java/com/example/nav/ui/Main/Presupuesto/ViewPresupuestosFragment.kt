package com.example.nav.ui.Main.Presupuesto

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav.R
import com.example.nav.databinding.FragmentSolicitudPresupuestoBinding
import com.example.nav.databinding.FragmentViewPresupuestosBinding
import com.example.nav.ui.Start.UserDataHolder.usuarioDTO
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@AndroidEntryPoint
class ViewPresupuestosFragment : Fragment() {
    private var _binding: FragmentViewPresupuestosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PresupuestoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPresupuestosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initRecyclerView()
        getData()
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.getClient().create(APIService::class.java).findByUsuario(usuarioDTO)

                if (response.isSuccessful) {
                    val presupuestos = response.body() ?: emptyList()
                    Log.d("ViewPresupuestosFragment", "Presupuestos loaded: ${presupuestos.size}")

                    requireActivity().runOnUiThread {
                        adapter = PresupuestoAdapter(presupuestos)
                        binding.recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("ViewPresupuestosFragment", "Failed to load presupuestos: ${response.errorBody()?.string()}")
                    requireActivity().runOnUiThread {
                        Toast.makeText(activity, "Failed to load presupuestos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewPresupuestosFragment", "Exception loading presupuestos", e)
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Error loading presupuestos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = PresupuestoAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
    }

    private fun initUi() {
        binding.BackBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_viewPresupuestosFragment_to_person,null))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onDownloadButtonClicked(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            downloadFile(id)
        }
    }

    suspend fun downloadFile(id: Long) {
        val apiService = RetrofitClient.getClient().create(APIService::class.java).downloadFile(id)

        if (apiService.isSuccessful) {
            apiService.body()?.let { body ->
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "presupuesto_${id}.pdf")
                saveFile(body.byteStream(), file)
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "File downloaded: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveFile(inputStream: InputStream, file: File) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
        } finally {
            outputStream?.close()
        }
    }
}