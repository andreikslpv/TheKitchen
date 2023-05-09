package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentFavoritesBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.models.RecipePreviewType
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipePreviewLoadStateAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipePreviewPagingAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.utils.makeToast
import com.andreikslpv.thekitchen.presentation.utils.visible
import com.andreikslpv.thekitchen.presentation.vm.FavoritesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private lateinit var recipePreviewAdapter: RecipePreviewPagingAdapter

    private val viewModel by viewModels<FavoritesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecipeListRecycler()
        initCollectRecipe()
        setupSwipeToRefresh()
        initShareButton()
        initClearListButton()
    }

    private fun initRecipeListRecycler() {
        binding.favoritesRecyclerRecipe.apply {
            recipePreviewAdapter = RecipePreviewPagingAdapter(object : RecipeItemClickListener {
                override fun click(recipePreview: RecipePreview) {
                    goToRecipeFragment(recipePreview)
                }
            }, object : ItemClickListener {
                override fun click(id: String) {
                    val result = viewModel.tryToRemoveFromFavorites(id)
                    if (!result) Snackbar.make(
                        binding.root, R.string.home_snackbar_text, Snackbar.LENGTH_LONG
                    ).setAction(R.string.home_snackbar_action) { goToAuthFragment() }.show()
                }
            },
                RecipePreviewType.FAVORITES
            )
            layoutManager = LinearLayoutManager(requireContext())

            adapter = recipePreviewAdapter.withLoadStateHeaderAndFooter(
                header = RecipePreviewLoadStateAdapter { recipePreviewAdapter.retry() },
                footer = RecipePreviewLoadStateAdapter { recipePreviewAdapter.retry() }
            )
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(
                paddingBottomInDp = 16,
            )
            addItemDecoration(decorator)
        }

        initLoadStateListening()
        initEmptyListening()
    }

    private fun initLoadStateListening() {
        this.lifecycleScope.launch {
            recipePreviewAdapter.loadStateFlow.collect {
                if (it.source.prepend is LoadState.NotLoading) {
                    binding.catalogProgressBar.visible(true)
                }
                if (it.source.prepend is LoadState.Error) {
                    (it.source.prepend as LoadState.Error).error.message?.makeToast(
                        requireContext()
                    )
                }
                if (it.source.append is LoadState.Error) {
                    (it.source.append as LoadState.Error).error.message?.makeToast(
                        requireContext()
                    )
                }
                if (it.source.refresh is LoadState.NotLoading) {
                    binding.catalogProgressBar.visible(false)
                }
                if (it.source.refresh is LoadState.Error) {
                    binding.catalogProgressBar.visible(false)
                    (it.source.refresh as LoadState.Error).error.message?.makeToast(
                        requireContext()
                    )
                }
            }
        }
    }

    private fun initEmptyListening() {
        this.lifecycleScope.launch {
            recipePreviewAdapter.loadStateFlow.collect {
                if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached) {
                    val temp = recipePreviewAdapter.itemCount < 1
                    binding.favoritesEmptyView.visible(temp)
                    binding.favoritesRecyclerRecipe.visible(!temp)
                } else {
                    binding.favoritesEmptyView.visible(false)
                    binding.favoritesRecyclerRecipe.visible(true)
                }
            }
        }
    }

    private fun initCollectRecipe() {
        this.lifecycleScope.launch {
            viewModel.recipes.collectLatest { pagedData ->
                recipePreviewAdapter.submitData(pagedData)
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.favoritesSwipeRefreshLayout.setOnRefreshListener {
            this.lifecycleScope.launch {
                recipePreviewAdapter.retry()
                binding.favoritesSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initShareButton() {
        binding.favoritesToolbar.menu.findItem(R.id.favoritesShare).setOnMenuItemClickListener {
            //Формируем текст сообщения
            var recipesNames = ""
            var i = 1
            recipePreviewAdapter.snapshot().items.forEach {
                recipesNames += "$i. ${it.name};\n"
                i++
            }
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.favorites_share_message, recipesNames)
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(
                Intent.createChooser(
                    intent,
                    resources.getString(R.string.favorites_share_title)
                )
            )
            true
        }
    }

    private fun initClearListButton() {
        binding.favoritesToolbar.menu.findItem(R.id.favoritesClearList).setOnMenuItemClickListener {
            val result = viewModel.tryToRemoveAllFromFavorites()
            if (!result) Snackbar.make(
                binding.root, R.string.home_snackbar_text, Snackbar.LENGTH_LONG
            ).setAction(R.string.home_snackbar_action) { goToAuthFragment() }.show()
            true
        }
    }

    private fun goToAuthFragment() {
        findTopNavController().navigate(R.id.authFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }

    private fun goToRecipeFragment(recipePreview: RecipePreview) {
        val direction = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment3(
            recipePreview
        )
        findNavController().navigate(direction)
    }
}