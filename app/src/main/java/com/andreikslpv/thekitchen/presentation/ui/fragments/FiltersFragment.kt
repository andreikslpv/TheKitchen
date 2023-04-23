package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentFiltersBinding
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.vm.FiltersViewModel
import com.google.android.material.chip.Chip


/**
 * A simple [Fragment] subclass.
 */
class FiltersFragment : BaseFragment<FragmentFiltersBinding>(FragmentFiltersBinding::inflate) {

    private val viewModel by viewModels<FiltersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollect()
        initEraseButton()
    }

    private fun initCollect() {
        viewModel.getAllCategories().observe(viewLifecycleOwner) { response ->
            binding.filtersTime.removeAllViews()
            binding.filtersDish.removeAllViews()
            binding.filtersExclude.removeAllViews()
            response.filter {
                it.type == CategoryType.TIME.value
            }
                .forEach {
                    val chip = Chip(binding.filtersTime.context)
                    chip.text = it.name
                    binding.filtersTime.addView(setChipAttributes(chip))
                }

            response.filter {
                it.type == CategoryType.DISH.value
            }
                .forEach {
                    val chip = Chip(binding.filtersDish.context)
                    chip.text = it.name
                    binding.filtersDish.addView(setChipAttributes(chip))
                }

            response.filter {
                it.type == CategoryType.EXCLUDE.value
            }
                .forEach {
                    val chip = Chip(binding.filtersExclude.context)
                    chip.text = it.name
                    binding.filtersExclude.addView(setChipAttributes(chip))
                }
        }
    }

    private fun setChipAttributes(chip: Chip): Chip {
        chip.isCheckable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            chip.setRippleColorResource(R.color.lime)
            chip.setChipStrokeColorResource(R.color.lime)
            chip.setChipStrokeWidthResource(R.dimen.chip_stroke_width)
            chip.chipBackgroundColor = resources.getColorStateList(
                R.color.background_color_chip_state_list,
                requireActivity().theme
            )
            chip.isCheckedIconVisible = false
        }
        return chip
    }

    private fun initEraseButton() {
        binding.filtersToolbar.menu.findItem(R.id.eraseButton).setOnMenuItemClickListener {
            binding.filtersTime.clearCheck()
            binding.filtersDish.clearCheck()
            binding.filtersExclude.clearCheck()
            true
        }
    }

}