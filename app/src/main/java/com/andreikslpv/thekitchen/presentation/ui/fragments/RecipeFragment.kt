package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentRecipeBinding
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.vm.RecipeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class RecipeFragment : BaseFragment<FragmentRecipeBinding>(FragmentRecipeBinding::inflate) {

    private val args: RecipeFragmentArgs by navArgs()

    private val viewModel by viewModels<RecipeViewModel>()

    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.instance.dagger.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPreviewInfo()
        initFavoriteButton()
        initPortionsCalculator()
        binding.recipeDescription.text = args.recipe.name

    }

    private fun setPreviewInfo() {
        binding.also {
            it.recipeToolbar.setupWithNavController(findNavController())
            it.recipeToolbar.title = args.recipe.name
            Glide.with(it.recipeImage)
                .load(args.recipe.imagePreview)
                .centerCrop()
                .into(it.recipeImage)
            it.recipeTimerValue.text = getString(R.string.time, args.recipe.time)
            it.recipeKkalValue.text = getString(R.string.recipe_kkal, args.recipe.caloriesCount)
            viewModel.validAndSetPortionsCount(args.recipe.portions)
        }
    }

    private fun initPortionsCalculator() {
        viewModel.portions.observe(viewLifecycleOwner) {
            binding.recipeCalcText.text = it.toString()
        }
        binding.recipeCalcMinus.setOnClickListener {
            viewModel.downPortionsCount()
        }
        binding.recipeCalcPlus.setOnClickListener {
            viewModel.upPortionsCount()
        }
    }

    private fun initFavoriteButton() {
        CoroutineScope(Dispatchers.Main).launch {
            userRepository.getFavorites().collect { list ->
                if (list.contains(args.recipe.id)) {
                    println("AAA 1")
                    binding.recipeButtonFavorites.setImageResource(R.drawable.ic_favorites_fill)
                }
                else {
                    println("AAA 2")
                    binding.recipeButtonFavorites.setImageResource(R.drawable.ic_favorites)
                }
            }
        }
        binding.recipeButtonFavorites.setOnClickListener {
            val result = viewModel.tryToChangeFavoritesStatus(args.recipe.id)
            if (!result) Snackbar.make(
                binding.root, R.string.home_snackbar_text, Snackbar.LENGTH_LONG
            ).setAction(R.string.home_snackbar_action) { goToAuthFragment() }.show()
        }
    }

    private fun goToAuthFragment() {
        findTopNavController().navigate(R.id.authFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }


}