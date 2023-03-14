package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentTabsBinding
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class TabsFragment : BaseFragment<FragmentTabsBinding>(FragmentTabsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // соединяем graph_tabs и bottomView
        val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

}