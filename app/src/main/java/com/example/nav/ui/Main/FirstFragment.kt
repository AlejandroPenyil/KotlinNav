package com.example.nav.ui.Main

import android.content.Context
import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav.R
import com.example.nav.databinding.FragmentFirstBinding
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import com.example.prueba.User
import com.example.prueba.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FirstFragment:Fragment() {
    private lateinit var retrofit: RetrofitClient

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserAdapter

    override fun onViewCreated (view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        retrofit = RetrofitClient

        initUI()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun initUI() {
        initListeners()

    }

    private fun initListeners() {
        binding.Call.setOnClickListener { getData() }

        adapter = UserAdapter { getData()}
        binding.rvPokemon.setHasFixedSize(true)
        binding.rvPokemon.layoutManager = LinearLayoutManager(this.activity)
        binding.rvPokemon.adapter = adapter
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            if(binding.editTextText.text.isNotEmpty()) {
                var name = binding.editTextText.text.toString()

                name = UCharacter.toLowerCase(name)

                Log.d("Api Request Pokemon", "Pokemon Name: $name")

                val response = retrofit.getClient().create(APIService::class.java).getFiles(name)

                R.layout.activity_main
                if (response.isSuccessful) {
                    val user = response.body()!!

                    val list: List<User> = listOf(user)

                    requireActivity().runOnUiThread {
                        adapter.updateList(list)
                        hideKeyboard()
                    }
                } else {
                    requireActivity().runOnUiThread {
                        makeToast("Pokemon not found")
                        hideKeyboard()
                    }
                }

            }else{
                requireActivity().runOnUiThread {
                    makeToast("Write a name")
                }
            }
        }
    }

    private fun makeToast(value: String) {
        Toast.makeText(this.activity, value, Toast.LENGTH_LONG).show()

    }
    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun Fragment.hideKeyboard() {
        val activity = this.activity
        if (activity is AppCompatActivity) {
            activity.hideKeyboard()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
