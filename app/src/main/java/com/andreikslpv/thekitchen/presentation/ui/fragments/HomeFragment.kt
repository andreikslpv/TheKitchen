package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentHomeBinding
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.CategoryRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeNewRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.utils.makeToast
import com.andreikslpv.thekitchen.presentation.vm.HomeViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.

 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var categoryDishAdapter: CategoryRecyclerAdapter
    private lateinit var categoryTimeAdapter: CategoryRecyclerAdapter
    private lateinit var recipeNewAdapter: RecipeNewRecyclerAdapter

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        initCollect()
        initProfileButton()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCollect() {
        viewModel.getAllCategories().observe(viewLifecycleOwner) { response ->
            categoryDishAdapter.changeItems(
                response.filter {
                    it.type == CategoryType.DISH.value
                }
            )
            categoryDishAdapter.notifyDataSetChanged()

            categoryTimeAdapter.changeItems(
                response.filter {
                    it.type == CategoryType.TIME.value
                }
            )
            categoryTimeAdapter.notifyDataSetChanged()
        }

        viewModel.getRecipeNew().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    recipeNewAdapter.changeItems(response.data)
                    recipeNewAdapter.notifyDataSetChanged()
                    binding.progressBar.hide()
                }

                is Response.Failure -> {
                    response.errorMessage.makeToast(requireContext())
                    binding.progressBar.hide()
                }
            }
        }

        viewModel.currentUserFromDb.observe(viewLifecycleOwner) { response ->
            binding.progressBar.show()
            recipeNewAdapter.changeFavorites(response.favorites.toList())
            recipeNewAdapter.notifyDataSetChanged()
            binding.progressBar.hide()
        }
    }

    private fun initRecyclers() {
        binding.homeRecyclerRecipe.apply {
            recipeNewAdapter = RecipeNewRecyclerAdapter(
                object : RecipeItemClickListener {
                    override fun click(recipePreview: RecipePreview) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                    }
                },
                object : ItemClickListener {
                    override fun click(id: String) {
                        if (viewModel.currentUserFromAuth != null)
                            viewModel.changeFavoritesStatus(id)
                        else Snackbar.make(
                            binding.root,
                            R.string.home_snackbar_text,
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.home_snackbar_action) { goToAuthFragment() }
                            .show()
                    }
                }
            )
            adapter = recipeNewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(4)
            addItemDecoration(decorator)
        }

        binding.homeRecyclerCategory.apply {
            categoryDishAdapter = CategoryRecyclerAdapter(
                object : ItemClickListener {
                    override fun click(id: String) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                    }
                },
                CategoryType.DISH
            )
            adapter = categoryDishAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(4)
            addItemDecoration(decorator)
        }

        binding.homeRecyclerCategoryTime.apply {
            categoryTimeAdapter = CategoryRecyclerAdapter(
                object : ItemClickListener {
                    override fun click(id: String) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                    }
                },
                CategoryType.TIME
            )
            adapter = categoryTimeAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(4)
            addItemDecoration(decorator)
        }
    }

    private fun initProfileButton() {
        if (viewModel.currentUserFromAuth != null)
            binding.homeToolbar.menu.findItem(R.id.profileButton).setOnMenuItemClickListener {
                // для запуска экранов верхнего уровня (graph_main) используем extension
                findTopNavController().navigate(R.id.profileFragment)
                true
            }
        else goToAuthFragment()
    }

    private fun goToAuthFragment() {
        findTopNavController().navigate(R.id.authFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }

    private fun goToCatalogFragment() {
//        val direction = FiltersFragmentDirections.actionFiltersFragmentToCatalogFragment2(
//            viewModel.filters.value?.getCategoriesArray()
//        )
//        findNavController().navigate(direction)
    }
}