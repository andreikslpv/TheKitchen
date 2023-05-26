package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentShoppingListBinding
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ShoppingItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ShoppingRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.utils.visible
import com.andreikslpv.thekitchen.presentation.vm.ShoppingListViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class ShoppingListFragment :
    BaseFragment<FragmentShoppingListBinding>(FragmentShoppingListBinding::inflate) {

    private lateinit var shoppingAdapter: ShoppingRecyclerAdapter

    private val viewModel by viewModels<ShoppingListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeToRefresh()
        initRecyclers()
        initProductCollect()
        initSelectAllButton()
        initClearButton()
    }

    private fun setupSwipeToRefresh() {
//        binding.shoppingSwipeRefreshLayout.setOnRefreshListener {
//            this.lifecycleScope.launch {
//                //recipePreviewAdapter.retry()
//                binding.shoppingSwipeRefreshLayout.isRefreshing = false
//            }
//        }
    }

    private fun initRecyclers() {
        binding.shoppingRecyclerRecipe.apply {
            shoppingAdapter = ShoppingRecyclerAdapter(
                object : ShoppingItemClickListener {
                    override fun click(shoppingItem: ShoppingItem) {
//                        goToRecipeFragment(recipePreview)
                    }
                },
                object : ShoppingItemClickListener {
                    override fun click(shoppingItem: ShoppingItem) {
                        viewModel.changeSelectedStatus(shoppingItem)
                    }
                },
                viewModel.selectedShoppingItem
            )
            adapter = shoppingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(
                paddingBottomInDp = 4,
                paddingLeftInDp = 16,
                paddingRightInDp = 16,
            )
            addItemDecoration(decorator)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initProductCollect() {
        viewModel.shoppingList.observe(viewLifecycleOwner) {
            shoppingAdapter.changeItems(it)
            shoppingAdapter.notifyDataSetChanged()

            if (it.isEmpty()) {
                binding.shoppingEmptyView.visible(true)
                binding.shoppingNested.visible(false)
            } else {
                binding.shoppingEmptyView.visible(false)
                binding.shoppingNested.visible(true)
            }
        }
    }

    private fun initSelectAllButton() {
        binding.shoppingSelectAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked)
                viewModel.unSelectAll()
            else
                viewModel.selectAll()
        }
    }

    private fun initClearButton() {
        binding.shoppingClearButton.setOnClickListener {
            if (!viewModel.removeSelectedFromShoppingList())
                Snackbar.make(
                    binding.root, R.string.home_snackbar_text,
                    Snackbar.LENGTH_LONG
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