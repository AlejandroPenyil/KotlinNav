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
import com.example.nav.ui.Start.UserDataHolder.usuarioDTO
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import com.example.prueba.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        getData()
    }

    private fun initRecyclerView() {
        adapter = UserAdapter(emptyList())
        binding.rvPokemon.layoutManager = LinearLayoutManager(activity)
        binding.rvPokemon.adapter = adapter
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getClient().create(APIService::class.java).getFiles(usuarioDTO)

            if (response.isSuccessful) {
                val images = response.body()?.map { it.ubicacion } ?: emptyList()

                requireActivity().runOnUiThread {
                    adapter = ImageAdapter(images)
                    binding.rvPokemon.adapter = adapter
                }
            } else {
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Failed to load images", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}