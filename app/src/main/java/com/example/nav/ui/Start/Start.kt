package com.example.nav.ui.Start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nav.databinding.StartActivityBinding
import com.example.nav.ui.Main.MainActivity
import com.example.nav.ui.guest.MainGuest

class Start : AppCompatActivity() {
    private lateinit var binding: StartActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.btLoggin.setOnClickListener { goToMain() }
        binding.btSignUp.setOnClickListener { goToSignUp() }
        binding.btGuest.setOnClickListener { goToGuest() }
    }

    private fun goToGuest() {
        val intent = Intent(this, MainGuest::class.java)
        startActivity(intent)
    }

    private fun goToSignUp() {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

    private fun goToMain(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}