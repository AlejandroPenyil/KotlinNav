package com.example.nav.ui.Main.Presupuesto

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.R
import com.example.nav.dto.PresupuestoDTO
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class PresupuestoAdapter(private val presupuestos: List<PresupuestoDTO>) :
    RecyclerView.Adapter<PresupuestoAdapter.PresupuestoViewHolder>() {

    private val CHANNEL_ID = "mi_canal_notificaciones"
    private val TAG = "PresupuestoAdapter"

    inner class PresupuestoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val presupuestoLogo: ImageView = itemView.findViewById(R.id.presupuestoLogo)
        val presupuestoStatus: TextView = itemView.findViewById(R.id.presupuestoStatus)
        val presupuestoDate: TextView = itemView.findViewById(R.id.presupuestoDate)
        val btnDownload: ImageButton = itemView.findViewById(R.id.downloadButton)
        val btnAccept: ImageButton = itemView.findViewById(R.id.btnAccept)
        val btnReject: ImageButton = itemView.findViewById(R.id.btnReject)

        fun bind(presupuesto: PresupuestoDTO) {
            presupuestoLogo.setImageResource(R.drawable.ic_document_logo)
            presupuestoStatus.text = presupuesto.estado
            presupuestoDate.text = presupuesto.fechaEnvio

            if (presupuesto.estado == "ESPERA") {
                btnAccept.visibility = View.VISIBLE
                btnReject.visibility = View.VISIBLE
            } else {
                btnAccept.visibility = View.GONE
                btnReject.visibility = View.GONE
            }

            btnDownload.setOnClickListener {
                onDownloadButtonClicked(presupuesto.id.toLong())
            }

            btnAccept.setOnClickListener {
                // Acción de aceptación
            }

            btnReject.setOnClickListener {
                // Acción de rechazo
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresupuestoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return PresupuestoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresupuestoViewHolder, position: Int) {
        val presupuesto = presupuestos[position]
        holder.bind(presupuesto)
    }

    override fun getItemCount(): Int = presupuestos.size

    private fun onDownloadButtonClicked(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            downloadFile(id)
        }
    }

    private suspend fun downloadFile(id: Long) {
        try {
            val response = RetrofitClient.getClient().create(APIService::class.java).downloadFile(id)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "presupuesto_${id}.pdf")
                    saveFile(body.byteStream(), file)

                    withContext(Dispatchers.Main) {
                        showDownloadNotification(file)
                    }
                }
            } else {
                Log.e(TAG, "Failed to download file: ${response.code()}")
                withContext(Dispatchers.Main) {
//                    Toast.makeText(presupuestos[0].estado.con, "Failed to download file", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during file download", e)
            withContext(Dispatchers.Main) {
//                Toast.makeText(presupuestos[0].presupuestoStatus.context, "Error downloading file", Toast.LENGTH_SHORT).show()
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

    private fun showDownloadNotification(file: File) {
        val notificationBuilder = NotificationCompat.Builder(presupuestos[0].presupuestoStatus.context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Descarga completada")
            .setContentText("Archivo descargado: ${file.name}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getOpenFileIntent(file))

        val notificationManager = NotificationManagerCompat.from(presupuestos[0].presupuestoStatus.context)
        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun getOpenFileIntent(file: File): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(presupuestos[0].presupuestoStatus.context, "com.example.myapp.fileprovider", file)
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return PendingIntent.getActivity(presupuestos[0].presupuestoStatus.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}


//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Descargas",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    private fun showDownloadCompleteNotification(file: File) {
//        val intent = Intent(Intent.ACTION_VIEW)
//        val uri = FileProvider.getUriForFile(
//            requireContext(),
//            "${requireContext().packageName}.provider",
//            file
//        )
//        intent.setDataAndType(uri, "application/pdf")
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//        val pendingIntent = PendingIntent.getActivity(
//            requireContext(),
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
//            .setContentTitle("Archivo descargado")
//            .setContentText("Presiona para abrir")
//            .setSmallIcon(R.drawable.ic_document_logo)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        val notificationManager = NotificationManagerCompat.from(requireContext())
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        val NOTIFICATION_ID = 1
//        notificationManager.notify(NOTIFICATION_ID, builder.build())
//    }

    override fun getItemCount(): Int = presupuestos.size
}

