package com.jonathas.petclinic.ui.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jonathas.petclinic.databinding.ItemPetInfoBinding
import com.jonathas.petclinic.models.PetItemModel

class PetsAdapter(
    private val viewModel: MainViewModel
) : ListAdapter<PetItemModel, PetsAdapter.PetsViewHolder>(PetsAdapter) {

    private companion object : DiffUtil.ItemCallback<PetItemModel>() {
        override fun areItemsTheSame(oldItem: PetItemModel, newItem: PetItemModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: PetItemModel, newItem: PetItemModel): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.imageUrl == newItem.imageUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsViewHolder =
        PetsViewHolder(
            ItemPetInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PetsViewHolder, position: Int) =
        holder.bindData(getItem(position))

    inner class PetsViewHolder(private val binding: ItemPetInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(pet: PetItemModel) {
            binding.petItem = pet
            binding.scope = viewModel.viewModelScope
            binding.root.setOnClickListener { viewModel.handlePetSelected(pet) }
            binding.executePendingBindings()
        }
    }

}