package com.example.nav.ui.Main.Facturas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav.R
import com.example.nav.databinding.FragmentFourthBinding
import com.example.nav.ui.Start.UserDataHolder.usuarioDTO
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FourthFragment:Fragment(R.layout.fragment_fourth) {
    private var _binding: FragmentFourthBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FacturaAdapter

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted, proceed with showing notifications
                } else {
                    // Permission is denied, show a message to the user
                    Toast.makeText(requireContext(), "Permission denied to show notifications", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFourthBinding.inflate(inflater, container, false)
        createNotificationChannel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Check if the user has previously denied the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.POST_NOTIFICATIONS)) {
                // Show an explanation to the user
                Toast.makeText(requireContext(), "Notification permission is required to show download notifications", Toast.LENGTH_LONG).show()
            }
            // Request the permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
        }

        initUi()
        initRecyclerView()
        getData()
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.getClient().create(APIService::class.java).facturaFindByUsuario(usuarioDTO)

                if (response.isSuccessful) {
                    val facturas = response.body() ?: emptyList()
                    Log.d("ViewFacturasFragment", "Facturas loaded: ${facturas.size}")

                    requireActivity().runOnUiThread {
                        adapter = FacturaAdapter(facturas,this@FourthFragment)
                        binding.recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("ViewFacturasFragment", "Failed to load facturas: ${response.errorBody()?.string()}")
                    requireActivity().runOnUiThread {
                        Toast.makeText(activity, "Failed to load facturas", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewFacturasFragment", "Exception loading facturas", e)
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Error loading facturas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = FacturaAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
    }

    private fun initUi() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showDownloadNotification(file: File) {
        val notificationBuilder = NotificationCompat.Builder(requireContext(), "mi_canal_notificaciones")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Descarga completada")
            .setContentText("Archivo descargado: ${file.name}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val intent = getOpenFileIntent(requireContext(), file)
        val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        notificationBuilder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(requireContext())
        val notificationId = 1

        // Check permission before notifying
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(notificationId, notificationBuilder.build())
        } else {
            Toast.makeText(requireContext(), "Permission denied to show notification", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getOpenFileIntent(context: Context, file: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        Log.d("FileProvider", "File path: ${file.path}")
        Log.d("FileProvider", "File exists: ${file.exists()}")

        try {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            Log.d("FileProvider", "URI: $uri")
            intent.setDataAndType(uri, "application/pdf")
        } catch (e: Exception) {
            Log.e("FileProvider", "Error generating URI", e)
        }
        return intent
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Mi canal de notificaciones"
            val descriptionText = "Notificaciones de descarga de archivos"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("mi_canal_notificaciones", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}