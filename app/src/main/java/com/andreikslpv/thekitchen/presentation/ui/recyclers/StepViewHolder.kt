package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemStepBinding
import com.andreikslpv.thekitchen.domain.models.Step

class StepViewHolder(val binding: ItemStepBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(step: Step) {
        binding.itemTitle.text =
            binding.root.context.getString(R.string.step_title, step.number.toString())
        binding.itemText.text = step.text
    }
}