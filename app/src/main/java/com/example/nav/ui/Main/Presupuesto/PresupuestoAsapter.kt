package com.example.nav.ui.Main.Presupuesto

import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
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

class PresupuestoAdapter(private val presupuestos: List<PresupuestoDTO>, private val fragment: ViewPresupuestosFragment) :
    RecyclerView.Adapter<PresupuestoAdapter.PresupuestoViewHolder>() {

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
            presupuestoDate.text = presupuesto.fechalEnvio

            if (presupuesto.estado == "ESPRA") {
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

    private fun onDownloadButtonClicked(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            downloadFile(id)
        }
    }

    private suspend fun downloadFile(id: Long) {
        try {
            val response = RetrofitClient.getClient().create(APIService::class.java).downloadFile(id)
            var code: Long = id * 2745
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "presupuesto_${code}.pdf")
                    saveFile(body.byteStream(), file)

                    withContext(Dispatchers.Main) {
                        fragment.showDownloadNotification(file)
                    }
                }
            } else {
                Log.e(TAG, "Failed to download file: ${response.code()}")
                withContext(Dispatchers.Main) {
                    // Mostrar error al usuario
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during file download", e)
            withContext(Dispatchers.Main) {
                // Mostrar error al usuario
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

    override fun getItemCount(): Int = presupuestos.size
}

