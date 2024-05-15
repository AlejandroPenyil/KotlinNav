package com.example.prueba

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.R

class UserAdapter(
    var superheroList: List<User> = emptyList(),
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_superhero, parent, false)
        )
    }

    fun updateList(list: List<User>) {

        if (list != null){
            superheroList = list
            notifyDataSetChanged()

        }
    }

    override fun onBindViewHolder(viewholder: PokemonViewHolder, position: Int) {
        viewholder.bind(superheroList[position], onItemSelected)
    }

    override fun getItemCount() = superheroList.size

}
