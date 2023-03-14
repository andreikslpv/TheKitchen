package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navOptions
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentProfileBinding
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.vm.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signOutButton.setOnClickListener {
            signOut()
        }

        getAuthState()
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