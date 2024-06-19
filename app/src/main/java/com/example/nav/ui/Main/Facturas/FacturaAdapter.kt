package com.example.nav.ui.Main.Facturas

import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.R
import com.example.nav.databinding.FragmentFourthBinding
import com.example.nav.dto.FacturaDTO
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

class FacturaAdapter(private val facturas: List<FacturaDTO>, private val fragment: FourthFragment) :
    RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    private val TAG = "FacturaAdapter"

    inner class FacturaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val facturaLogo: ImageView = itemView.findViewById(R.id.presupuestoLogo)
        val facturaDate: TextView = itemView.findViewById(R.id.presupuestoDate)
        val status: TextView = itemView.findViewById(R.id.presupuestoStatus)
        val btnDownload: ImageButton = itemView.findViewById(R.id.downloadButton)

        fun bind(factura: FacturaDTO) {
            facturaLogo.setImageResource(R.drawable.ic_document_logo)
            facturaDate.text = factura.fecha
            status.text = ""
            btnDownload.setOnClickListener {
                factura.id?.let { it1 -> onDownloadButtonClicked(it1.toLong()) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val factura = facturas[position]
        holder.bind(factura)
    }

    private fun onDownloadButtonClicked(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            downloadFile(id)
        }
    }

    private suspend fun downloadFile(id: Long) {
        try {
            val response = RetrofitClient.getClient().create(APIService::class.java).downloadFactura(id)
            var code: Long = id * 2745
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "factura_${code}.pdf")
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

    override fun getItemCount(): Int = facturas.size
}

