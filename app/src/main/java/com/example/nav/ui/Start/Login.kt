package com.example.nav.ui.Start

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nav.data.LoginRequest
import com.example.nav.ui.Main.MainActivity
import com.example.nav.databinding.LoginActivityBinding
import com.example.nav.dto.UsuarioDTO
import com.example.prueba.APIService
import com.example.prueba.RequestInterceptor
import com.example.prueba.RetrofitClient
import com.google.gson.Gson
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserDataHolder {
    var usuarioDTO: UsuarioDTO? = null
}

class Login : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.button.setOnClickListener { goToMain() }
        binding.button2.setOnClickListener { goBack() }
    }

    private fun goBack() {
        val intent = Intent(this, Start::class.java)
        startActivity(intent)
    }

    private fun goToMain() {
        val nombreUsuario = binding.editTextText2.text.toString()
        val contraseña = binding.editTextTextPassword.text.toString()

        val requestBody = LoginRequest(nombreUsuario,contraseña)

        val retrofit = RetrofitClient.getClient()

        // Creamos una instancia de ApiService utilizando Retrofit
        val apiService = retrofit.create(APIService::class.java)

        apiService.login(requestBody).enqueue(object : Callback<UsuarioDTO> {
            override fun onResponse(call: Call<UsuarioDTO>, response: Response<UsuarioDTO>) {
                if (response.isSuccessful) {
                    UserDataHolder.usuarioDTO = response.body()
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error de autenticación"
                    runOnUiThread {
                        Toast.makeText(this@Login, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<UsuarioDTO>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@Login, "Error de red", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}