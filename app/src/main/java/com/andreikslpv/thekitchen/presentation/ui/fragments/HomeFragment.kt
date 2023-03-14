package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentHomeBinding
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController

/**
 * A simple [Fragment] subclass.

 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileButton.setOnClickListener {
            // для запуска экранов верхнего уровня (graph_main) используем extension
            findTopNavController().navigate(R.id.profileFragment)
        }
    }
}