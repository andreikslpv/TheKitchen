package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentFiltersBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.vm.FiltersViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel


/**
 * A simple [Fragment] subclass.
 */
class FiltersFragment : BaseFragment<FragmentFiltersBinding>(FragmentFiltersBinding::inflate) {

    private val viewModel by viewModels<FiltersViewModel>()

    private val args: FiltersFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filtersToolbar.setupWithNavController(findNavController())

        initCollectCategoriesList()
        initCollectChoosenFilter()
        viewModel.addFilters(args.filters)
        initEraseButton()
        initApplayButton()
    }

    private fun initCollectCategoriesList() {
        viewModel.getAllCategories().observe(viewLifecycleOwner) { response ->
            binding.filtersTime.removeAllViews()
            binding.filtersDish.removeAllViews()
            binding.filtersExclude.removeAllViews()
            response.filter {
                it.type == CategoryType.TIME.value
            }
                .forEach {
                    binding.filtersTime.addView(
                        getChipsWithAttributes(
                            Chip(binding.filtersTime.context),
                            it
                        )
                    )
                }

            response.filter {
                it.type == CategoryType.DISH.value
            }
                .forEach {
                    binding.filtersDish.addView(
                        getChipsWithAttributes(
                            Chip(binding.filtersDish.context),
                            it
                        )
                    )
                }

            response.filter {
                it.type == CategoryType.EXCLUDE.value
            }
                .forEach {
                    binding.filtersExclude.addView(
                        getChipsWithAttributes(
                            Chip(binding.filtersExclude.context),
                            it
                        )
                    )
                }
            viewModel.refreshFilters()
        }
    }

    private fun getChipsWithAttributes(chip: Chip, category: Category): Chip {
        chip.text = category.name
        chip.tag = category.id
        chip.isCheckable = true
        chip.shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
            .setAllCornerSizes(resources.getDimension(R.dimen.chip_corner_radius))
            .build()
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
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            val chipItem = buttonView as Chip
            if (isChecked) viewModel.addFilter(chipItem.tag as String)
            else viewModel.removeFilter(chipItem.tag as String)
        }
        return chip
    }

    private fun initCollectChoosenFilter() {
        viewModel.filters.observe(viewLifecycleOwner) { filters ->
            binding.filtersTime.children.forEach { child ->
                val chip = child as Chip
                chip.isChecked = filters.getCategoriesList().contains(chip.tag)
            }
            binding.filtersDish.children.forEach { child ->
                val chip = child as Chip
                chip.isChecked = filters.getCategoriesList().contains(chip.tag)
            }
            binding.filtersExclude.children.forEach { child ->
                val chip = child as Chip
                chip.isChecked = filters.getCategoriesList().contains(chip.tag)
            }
        }
    }

    private fun initEraseButton() {
        binding.filtersToolbar.menu.findItem(R.id.eraseButton).setOnMenuItemClickListener {
            binding.filtersTime.clearCheck()
            binding.filtersDish.clearCheck()
            binding.filtersExclude.clearCheck()
            true
        }
    }

    private fun initApplayButton() {
        binding.filtersApplayButton.setOnClickListener {
            // возвращаемся в Catalog и передаем в него список установленных фильтров
            val direction = FiltersFragmentDirections.actionFiltersFragmentToCatalogFragment(
                viewModel.filters.value?.getCategoriesArray()
            )
            findNavController().navigate(direction)
        }
    }
}