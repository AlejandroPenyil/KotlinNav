package com.example.prueba

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.databinding.ItemSuperheroBinding
import com.squareup.picasso.Picasso

class PokemonViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemSuperheroBinding.bind(view)

    fun bind(user: User, onItemSelected: (String) -> Unit) {
        binding.tvSuperheroName.text = user.name
        Picasso.get().load(user.image.url).into(binding.ivSuperhero)
        binding.root.setOnClickListener { onItemSelected(user.order.toString()) }
    }

}
