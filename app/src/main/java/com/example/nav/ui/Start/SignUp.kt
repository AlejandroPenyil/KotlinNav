package com.example.nav.ui.Start

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nav.databinding.LoginActivityBinding
import com.example.nav.databinding.SignUpActivityBinding
import com.example.nav.dto.UsuarioDTO
import com.example.nav.ui.Main.MainActivity
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    private lateinit var binding: SignUpActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.signUp.setOnClickListener { goToMain() }
        binding.btBack.setOnClickListener { goBack() }
    }

    private fun goBack() {
        val intent = Intent(this, Start::class.java)
        startActivity(intent)
    }

    private fun goToMain() {
        val nombre = binding.editTextName.text.toString()
        val contraseña = binding.editTextTextPassword2.text.toString()
        val correo = binding.editTextTextEmailAddress.text.toString()
        val telefono = binding.editTextPhone.text.toString()
        val userName = binding.editTextUserName.text.toString()
        val confContraseña = binding.editTextTextPasswordConfirmation.text.toString()
        val dni = binding.editTextDni.text.toString()
        val direccion = binding.editTextDireccion.text.toString()

        if (nombre.isEmpty() || contraseña.isEmpty() || correo.isEmpty() || telefono.isEmpty()
            || userName.isEmpty() || confContraseña.isEmpty() || direccion.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this@SignUp, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Validar que las contraseñas coincidan
        if (confContraseña != contraseña) {
            Toast.makeText(this@SignUp, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }
        val newUsuario = UsuarioDTO(
            0,
            nombre,
            contraseña,
            correo,
            dni,
            "CLIENT",
            telefono,
            direccion,
            userName
        )

        val retrofit = RetrofitClient.getClient()

        // Creamos una instancia de ApiService utilizando Retrofit
        val apiService = retrofit.create(APIService::class.java)

        apiService.register(newUsuario).enqueue(object : Callback<UsuarioDTO> {
            override fun onResponse(call: Call<UsuarioDTO>, response: Response<UsuarioDTO>) {
                if (response.isSuccessful) {
                    UserDataHolder.usuarioDTO = response.body()
                    val intent = Intent(this@SignUp, Login::class.java)
                    startActivity(intent)
                } else {
                    val errorMessage =
                        response.errorBody()?.string() ?: "Error de autenticación"
                    runOnUiThread {
                        Toast.makeText(this@SignUp, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<UsuarioDTO>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@SignUp, "Error de red", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}