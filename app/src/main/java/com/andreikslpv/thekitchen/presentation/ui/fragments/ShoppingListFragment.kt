package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.databinding.FragmentShoppingListBinding
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ShoppingRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.visible
import com.andreikslpv.thekitchen.presentation.vm.ShoppingListViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ShoppingListFragment : BaseFragment<FragmentShoppingListBinding>(FragmentShoppingListBinding::inflate) {

    private lateinit var shoppingAdapter: ShoppingRecyclerAdapter

    private val viewModel by viewModels<ShoppingListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeToRefresh()
        initRecyclers()
        initProductCollect()
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
            shoppingAdapter = ShoppingRecyclerAdapter()
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
        viewModel.getShoppingList().observe(viewLifecycleOwner) {
            shoppingAdapter.changeItems(it)
            shoppingAdapter.notifyDataSetChanged()

//            binding.shoppingEmptyView.visible(true)
//            binding.shoppingNotEmptyView.visible(false)

            if(it.isEmpty()) {
                binding.shoppingEmptyView.visible(true)
                binding.shoppingNested.visible(false)
            } else {
                binding.shoppingEmptyView.visible(false)
                binding.shoppingNested.visible(true)
            }
        }
    }
}