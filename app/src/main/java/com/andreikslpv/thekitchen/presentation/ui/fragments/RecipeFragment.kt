package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentRecipeBinding
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.IngredientRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.StepRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.vm.RecipeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class RecipeFragment : BaseFragment<FragmentRecipeBinding>(FragmentRecipeBinding::inflate) {

    private val args: RecipeFragmentArgs by navArgs()

    private val viewModel by viewModels<RecipeViewModel>()

    private lateinit var ingredientAdapter: IngredientRecyclerAdapter

    private lateinit var stepAdapter: StepRecyclerAdapter

    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.instance.dagger.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setRecipePreview(args.recipe)
        initCollectPreviewInfo()
        initFavoriteButton()
        initRecyclers()
        initPortionsCalculator()
        initCollectDetailsInfo()
        initIngredientsCollect()
        initKkalCollect()
    }

    private fun initCollectPreviewInfo() {
        viewModel.recipePreview.observe(viewLifecycleOwner) { recipe ->
            binding.also {
                it.recipeToolbar.setupWithNavController(findNavController())
                it.recipeToolbar.title = recipe.name
                Glide.with(it.recipeImage)
                    .load(recipe.imagePreview)
                    .centerCrop()
                    .into(it.recipeImage)
                it.recipeTimerValue.text = getString(R.string.time, recipe.time)
                viewModel.validAndSetPortionsCount(recipe.portions)
                viewModel.setKkal(recipe.caloriesCount)
            }
        }
    }

    private fun initRecyclers() {
        binding.ingredientRecyclerRecipe.apply {
            ingredientAdapter = IngredientRecyclerAdapter()
            adapter = ingredientAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(
                paddingBottomInDp = 8,
            )
            addItemDecoration(decorator)
        }

        binding.stepsPager.apply {
            stepAdapter = StepRecyclerAdapter()
            adapter = stepAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val count = (adapter as StepRecyclerAdapter).itemCount
                    val current = (position + 1).toDouble()
                    val progress = (current / count) * 100
                    binding.stepsProgress.setProgressCompat(progress.roundToInt(), true)
                }
            })
        }
    }

    private fun initPortionsCalculator() {
        viewModel.portions.observe(viewLifecycleOwner) {
            binding.recipeCalcText.text = it.toString()
            viewModel.changeIngredients(it)
            viewModel.changeKkal(it)
        }
        binding.recipeCalcMinus.setOnClickListener {
            viewModel.downPortionsCount()
        }
        binding.recipeCalcPlus.setOnClickListener {
            viewModel.upPortionsCount()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initIngredientsCollect() {
        viewModel.ingredients.observe(viewLifecycleOwner) {
            ingredientAdapter.changeItems(it)
            ingredientAdapter.notifyDataSetChanged()
        }
    }

    private fun initKkalCollect() {
        viewModel.kkal.observe(viewLifecycleOwner) {
            binding.recipeKkalValue.text = getString(R.string.recipe_kkal, it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCollectDetailsInfo() {
        viewModel.recipeDetails.observe(viewLifecycleOwner) { response ->
            binding.recipeDescription.text = response.description
//            binding.recipeLinkText.text = getString(R.string.link)
            binding.recipeLink.text = response.source
            viewModel.setIngredients(response.ingredients)
            stepAdapter.changeItems(response.steps)
            stepAdapter.notifyDataSetChanged()
        }
    }

    private fun initFavoriteButton() {
        this.lifecycleScope.launch {
            // Suspend the coroutine until the lifecycle is DESTROYED.
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // Safely collect from source when the lifecycle is STARTED
                // and stop collecting when the lifecycle is STOPPED
                viewLifecycleOwner.lifecycleScope.launch {
                    userRepository.getFavorites().collect { list ->
                        if (list.contains(viewModel.recipePreview.value?.id)) {
                            binding.recipeButtonFavorites.setImageResource(R.drawable.ic_favorites_fill)
                        } else {
                            binding.recipeButtonFavorites.setImageResource(R.drawable.ic_favorites)
                        }
                    }
                }
            }
            // Note: at this point, the lifecycle is DESTROYED!
        }

        binding.recipeButtonFavorites.setOnClickListener {
            val result = viewModel.tryToChangeFavoritesStatus()
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