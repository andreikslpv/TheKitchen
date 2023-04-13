package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentHomeBinding
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.ui.MainActivity
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.CategoryRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.StoriesRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.utils.makeToast
import com.andreikslpv.thekitchen.presentation.vm.HomeViewModel

/**
 * A simple [Fragment] subclass.

 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var storiesAdapter: StoriesRecyclerAdapter
    private lateinit var categoryAdapter: CategoryRecyclerAdapter
    private lateinit var timeAdapter: CategoryRecyclerAdapter

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        initCollect()
        initProfileButton()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCollect() {
        viewModel.getCategoriesDish().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    categoryAdapter.changeItems(response.data)
                    categoryAdapter.notifyDataSetChanged()
                    binding.progressBar.hide()
                }
                is Response.Failure -> {
                    response.errorMessage.makeToast(requireContext())
                    binding.progressBar.hide()
                }
            }
        }

        viewModel.getCategoriesTime().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    timeAdapter.changeItems(response.data)
                    timeAdapter.notifyDataSetChanged()
                    binding.progressBar.hide()
                }
                is Response.Failure -> {
                    response.errorMessage.makeToast(requireContext())
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun initRecyclers() {
        binding.homeRecyclerStories.apply {
            storiesAdapter =
                StoriesRecyclerAdapter(
                    object : ItemClickListener {
                        override fun click(id: String) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                        }
                    }
                )
            adapter = storiesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(4)
            addItemDecoration(decorator)
        }

        binding.homeRecyclerCategory.apply {
            categoryAdapter =
                CategoryRecyclerAdapter(
                    object : ItemClickListener {
                        override fun click(id: String) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                        }
                    }
                )
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(4)
            addItemDecoration(decorator)
        }

        binding.homeRecyclerCategoryTime.apply {
            timeAdapter =
                CategoryRecyclerAdapter(
                    object : ItemClickListener {
                        override fun click(id: String) {
//                            viewLifecycleOwner.lifecycleScope.launch {
//                                removePhotoFromFavoritesUseCase.execute(photo.id)
//                            }
                        }
                    }
                )
            adapter = timeAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(4)
            addItemDecoration(decorator)
        }
    }

    private fun initProfileButton() {
        if ((requireActivity() as MainActivity).isSignedIn())
            binding.homeToolbar.menu.findItem(R.id.profileButton).setOnMenuItemClickListener {
                // для запуска экранов верхнего уровня (graph_main) используем extension
                findTopNavController().navigate(R.id.profileFragment)
                true
            }
        else
            findTopNavController().navigate(R.id.authFragment, null, navOptions {
                popUpTo(R.id.tabsFragment) {
                    inclusive = true
                }
            })
    }
}