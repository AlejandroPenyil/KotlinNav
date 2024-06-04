package com.example.nav.ui.guest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav.R
import com.example.nav.databinding.FragmentFirstBinding
import com.example.nav.databinding.FragmentGuestHomeBinding
import com.example.nav.ui.Start.UserDataHolder
import com.example.nav.ui.UserAdapter
import com.example.prueba.APIService
import com.example.prueba.RetrofitClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [GuestHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class GuestHomeFragment : Fragment() {
    private var _binding: FragmentGuestHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuestHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        getData()
    }

    private fun initRecyclerView() {
        adapter = UserAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getClient().create(APIService::class.java).getImagesGuest()

            if (response.isSuccessful) {
                val images = response.body()?.map { it.ubicacion } ?: emptyList()

                requireActivity().runOnUiThread {
                    adapter = UserAdapter(images)
                    binding.recyclerView.adapter = adapter
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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_guest_home, container, false)
//    }

}