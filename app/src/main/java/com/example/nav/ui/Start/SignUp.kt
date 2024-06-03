package com.example.nav.ui.Start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nav.databinding.LoginActivityBinding
import com.example.nav.databinding.SignUpActivityBinding
import com.example.nav.ui.Main.MainActivity

class SignUp: AppCompatActivity() {
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

    private fun goToMain(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}