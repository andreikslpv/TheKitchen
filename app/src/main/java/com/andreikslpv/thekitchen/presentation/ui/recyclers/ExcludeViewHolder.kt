package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.databinding.ItemExcludeBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.usecases.GetDefaultExcludeUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeExcludeStatusUseCase
import com.google.android.material.materialswitch.MaterialSwitch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExcludeViewHolder(val binding: ItemExcludeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @Inject
    lateinit var getDefaultExcludeUseCase: GetDefaultExcludeUseCase

    @Inject
    lateinit var tryToChangeExcludeStatusUseCase: TryToChangeExcludeStatusUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun bind(category: Category) {
        binding.itemExcludeName.text = category.name
        CoroutineScope(Dispatchers.IO).launch {
            getDefaultExcludeUseCase.execute().collect {
                withContext(Dispatchers.Main) {
                    binding.itemExcludeButton.isChecked = it.contains(category.id)
                    binding.itemExcludeButton.tag = category.id
                }
            }
        }
        binding.itemExcludeButton.setOnClickListener {
            val switch = (it as MaterialSwitch)
            switch.tag?.let {
                tryToChangeExcludeStatusUseCase.execute(switch.tag as String)
            }
        }
    }
}