package com.example.nav.ui.Start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nav.ui.Main.MainActivity
import com.example.nav.databinding.LoginActivityBinding

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

    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}