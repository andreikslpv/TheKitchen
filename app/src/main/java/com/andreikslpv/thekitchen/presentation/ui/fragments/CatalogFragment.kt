package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentCatalogBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipePreviewLoadStateAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipePreviewPagingAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.makeToast
import com.andreikslpv.thekitchen.presentation.utils.simpleScan
import com.andreikslpv.thekitchen.presentation.utils.visible
import com.andreikslpv.thekitchen.presentation.vm.CatalogViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class CatalogFragment : BaseFragment<FragmentCatalogBinding>(FragmentCatalogBinding::inflate) {

    private lateinit var recipePreviewAdapter: RecipePreviewPagingAdapter

    private val viewModel by viewModels<CatalogViewModel>()

    private val args: CatalogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.categories.observe(viewLifecycleOwner) {}
        initRecipeListRecycler()
        initCollectRecipe()
        viewModel.addFilters(args.filters)
        initCollectFilter()
        setupSwipeToRefresh()
        initFiltersButton()
    }

        private fun initCollectFilter() {
        viewModel.filters.observe(viewLifecycleOwner) { filters ->
            binding.catalogFilters.removeAllViews()
            filters.getCategoriesList().forEach { item ->
                val category = viewModel.getCategoryById(item)
                category?.let {
                    binding.catalogFilters.addView(
                        getChipsWithAttributes(
                            Chip(binding.catalogFilters.context),
                            it
                        )
                    )
                }
            }
            if (filters.getCategoriesList().size > 0) binding.catalogFilters.visible(true)
            else binding.catalogFilters.visible(false)
        }
    }

    private fun getChipsWithAttributes(chip: Chip, category: Category): Chip {
        chip.text = category.name
        chip.tag = category.id
        chip.isCheckable = true
        chip.shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
            .setAllCornerSizes(resources.getDimension(R.dimen.chip_corner_radius))
            .build()
        chip.setChipBackgroundColorResource(R.color.lime)
        chip.isCheckedIconVisible = false

        chip.setOnCheckedChangeListener { buttonView, _ ->
            val chipItem = buttonView as Chip
            viewModel.removeFilter(chipItem.tag as String)
        }
        return chip
    }

    private fun initRecipeListRecycler() {
        binding.catalogRecyclerRecipe.apply {
            recipePreviewAdapter = RecipePreviewPagingAdapter(
                object : RecipeItemClickListener {
                    override fun click(recipePreview: RecipePreview) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                    }
                },
                object : ItemClickListener {
                    override fun click(id: String) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                    }
                }
            )
            layoutManager = LinearLayoutManager(requireContext())
            //binding.selectionsRecycler.setHasFixedSize(true)

            adapter = recipePreviewAdapter.withLoadStateHeaderAndFooter(
                header = RecipePreviewLoadStateAdapter { recipePreviewAdapter.retry() },
                footer = RecipePreviewLoadStateAdapter { recipePreviewAdapter.retry() }
            )
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(4)
            addItemDecoration(decorator)
        }

        initLoadStateListening()
        handleScrollingToTopWhenSearching()
    }

    private fun initCollectRecipe() {
        this.lifecycleScope.launch {
            viewModel.recipes.collectLatest { pagedData ->
                recipePreviewAdapter.submitData(pagedData)
            }
        }
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

    // Когда пользователь меняет поисковой запрос, то отслеживаем этот момент и прокручиваем в начало списка
    private fun handleScrollingToTopWhenSearching() =
        this.lifecycleScope.launch {
            getRefreshLoadStateFlow()
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.catalogRecyclerRecipe.scrollToPosition(0)
                    }
                }
        }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return recipePreviewAdapter.loadStateFlow
            .map { it.refresh }
    }

    private fun setupSwipeToRefresh() {
        binding.catalogSwipeRefreshLayout.setOnRefreshListener {
            this.lifecycleScope.launch {
                recipePreviewAdapter.submitData(PagingData.empty())
                viewModel.refresh()
                binding.catalogSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initFiltersButton() {
        binding.catalogToolbar.menu.findItem(R.id.fitersButton).setOnMenuItemClickListener {
            // запускаем Filters и передаем в него список уже установленных фильтров
//            val direction = CatalogFragmentDirections.actionCatalogFragment2ToFiltersFragment2(
//                viewModel.filters.value?.getCategoriesArray()
//            )
//            findNavController().navigate(direction)
            true
        }
    }
}