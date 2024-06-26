package com.example.nav.ui.Main.Presupuesto

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
import com.example.nav.databinding.FragmentViewPresupuestosBinding
import com.example.nav.ui.Start.UserDataHolder.usuarioDTO
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import com.squareup.picasso.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File



@AndroidEntryPoint
class ViewPresupuestosFragment : Fragment() {
    private var _binding: FragmentViewPresupuestosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PresupuestoAdapter

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
        _binding = FragmentViewPresupuestosBinding.inflate(inflater, container, false)
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
                val response = RetrofitClient.getClient().create(APIService::class.java).findByUsuario(usuarioDTO)

                if (response.isSuccessful) {
                    val presupuestos = response.body() ?: emptyList()
                    Log.d("ViewPresupuestosFragment", "Presupuestos loaded: ${presupuestos.size}")

                    requireActivity().runOnUiThread {
                        adapter = PresupuestoAdapter(presupuestos,this@ViewPresupuestosFragment)
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
        adapter = PresupuestoAdapter(emptyList(),this)
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