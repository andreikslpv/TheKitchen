package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentProfileBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ExcludeRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeMiniRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.utils.makeToast
import com.andreikslpv.thekitchen.presentation.vm.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    private lateinit var recipeHistoryAdapter: RecipeMiniRecyclerAdapter
    private lateinit var excludeAdapter: ExcludeRecyclerAdapter

    private val decorator = SpaceItemDecoration(
        paddingBottomInDp = 16,
        paddingRightInDp = 4,
        paddingLeftInDp = 4,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileToolbar.setupWithNavController(findNavController())
        initRecipeHistoryCollect()
        initCategoryCollect()
        initRecyclers()
        initCurrentUserCollect()
        iniSignOutButton()
        getAuthState()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecipeHistoryCollect() {
        viewModel.getRecipeHistory().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    recipeHistoryAdapter.changeItems(response.data)
                    recipeHistoryAdapter.notifyDataSetChanged()
                    binding.progressBar.hide()
                }

                is Response.Failure -> {
                    response.errorMessage.makeToast(requireContext())
                    binding.progressBar.hide()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCategoryCollect() {
        viewModel.getCategoryExclude().observe(viewLifecycleOwner) {
            excludeAdapter.changeItems(it)
            excludeAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclers() {
        binding.profileRecyclerRecipe.apply {
            recipeHistoryAdapter = RecipeMiniRecyclerAdapter(
                object : RecipeItemClickListener {
                    override fun click(recipePreview: RecipePreview) {
                        goToRecipeFragment(recipePreview)
                    }
                },
                object : ItemClickListener {
                    override fun click(id: String) {
                        val result = viewModel.tryToChangeFavoritesStatus(id)
                        if (!result) Snackbar.make(
                            binding.root, R.string.home_snackbar_text, Snackbar.LENGTH_LONG
                        ).setAction(R.string.home_snackbar_action) { goToAuthFragment() }.show()
                    }
                }
            )
            adapter = recipeHistoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //Применяем декоратор для отступов
            addItemDecoration(decorator)
        }

        binding.excludeRecyclerRecipe.apply {
            excludeAdapter = ExcludeRecyclerAdapter()
            adapter = excludeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(
                paddingBottomInDp = 16,
            )
            addItemDecoration(decorator)
        }
    }

    private fun goToRecipeFragment(recipePreview: RecipePreview) {
        val direction = ProfileFragmentDirections.actionProfileFragmentToRecipeFragment2(
            recipePreview
        )
        findNavController().navigate(direction)
    }

    private fun goToAuthFragment() {
        findTopNavController().navigate(R.id.authFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }

    private fun initCurrentUserCollect() {
        viewModel.getCurrentUser().observe(viewLifecycleOwner) { user ->
            user?.let {
                Glide.with(binding.profileAvatar)
                    .load(user.photoUrl)
                    .centerCrop()
                    .into(binding.profileAvatar)
                binding.profileName.setText(user.displayName)
                binding.profileEmail.text = user.email
            }
        }
    }

    private fun iniSignOutButton() {
        binding.signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        viewModel.signOut().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> binding.progressBar.hide()
                is Response.Failure -> {
                    print(response.errorMessage)
                    binding.progressBar.hide()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getAuthState() {
        viewModel.getAuthState().observe(viewLifecycleOwner) { isUserSignedOut ->
            if (isUserSignedOut) {
                findTopNavController().navigate(R.id.authFragment, null, navOptions {
                    popUpTo(R.id.tabsFragment) {
                        inclusive = true
                    }
                })
            }
        }
    }

}