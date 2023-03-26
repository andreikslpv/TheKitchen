package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.data.db.DbConstants
import com.andreikslpv.thekitchen.databinding.FragmentHomeBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.vm.AuthViewModel
import com.andreikslpv.thekitchen.presentation.vm.HomeViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.snapshots
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.

 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

//    @Inject
//    lateinit var database: FirebaseDatabase
//
//    init {
//        App.instance.dagger.inject(this)
//    }

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileButton.setOnClickListener {
            // для запуска экранов верхнего уровня (graph_main) используем extension
            findTopNavController().navigate(R.id.profileFragment)
        }

        initCollect()
    }

    private fun initCollect() {
        viewModel.getAllCategories().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    println("I/o ${response.data}")
                    binding.progressBar.hide()
                }
                is Response.Failure -> {
                    print(response.errorMessage)
                    binding.progressBar.hide()
                }
            }
        }
    }


//        val refCategory = database.getReference(DbConstants.PATH_CATEGORY)
//        var temp = ""
//        var subRef = refCategory.child("ca00001")
//        subRef.setValue(
//            Category(
//                id = "ca00001",
//                name = "до 60 мин",
//                type = CategoryType(
//                    id = "ct00001",
//                    name = "time"
//                )
//            )
//        )
//        subRef = refCategory.child("ca00002")
//        subRef.setValue(
//            Category(
//                id = "ca00003",
//                name = "до 45 мин",
//                type = CategoryType(
//                    id = "ct00001",
//                    name = "time"
//                )
//            )
//        )
//        subRef = refCategory.child("ca00003")
//        subRef.setValue(
//            Category(
//                id = "ca00003",
//                name = "до 30 мин",
//                type = CategoryType(
//                    id = "ct00001",
//                    name = "time"
//                )
//            )
//        )
//
//        temp = "ca00004"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Супы",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//
//        temp = "ca00005"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Салаты",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//        temp = "ca00006"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Выпечка",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//
//        temp = "ca00007"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Горячее",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//        temp = "ca00008"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Десерты",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//        temp = "ca00009"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Завтраки",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//        temp = "ca00010"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Запеканки",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//        temp = "ca00011"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Заготовки",
//                type = CategoryType(
//                    id = "ct00002",
//                    name = "dish"
//                )
//            )
//        )
//
//
//        temp = "ca00012"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Без мяса",
//                type = CategoryType(
//                    id = "ct00003",
//                    name = "exclude"
//                )
//            )
//        )
//        temp = "ca00013"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Без сахара",
//                type = CategoryType(
//                    id = "ct00003",
//                    name = "exclude"
//                )
//            )
//        )
//        temp = "ca00014"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Без лактозы",
//                type = CategoryType(
//                    id = "ct00003",
//                    name = "exclude"
//                )
//            )
//        )
//        temp = "ca00015"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Без глютена",
//                type = CategoryType(
//                    id = "ct00003",
//                    name = "exclude"
//                )
//            )
//        )
//        temp = "ca00016"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Без лука",
//                type = CategoryType(
//                    id = "ct00003",
//                    name = "exclude"
//                )
//            )
//        )
//        temp = "ca00017"
//        subRef = refCategory.child(temp)
//        subRef.setValue(
//            Category(
//                id = temp,
//                name = "Без свинины",
//                type = CategoryType(
//                    id = "ct00003",
//                    name = "exclude"
//                )
//            )
//        )





        //val refCategoryType = database.getReference(DbConstants.PATH_CATEGORY_TYPE)
//        var subRef = refCategoryType.child("ct00001")
//        subRef.setValue(
//            CategoryType(
//                id = "ct00001",
//                name = "time"
//            )
//        )
//        subRef = refCategoryType.child("ct00002")
//        subRef.setValue(
//            CategoryType(
//                id = "ct00002",
//                name = "dish"
//            )
//        )
//        subRef = refCategoryType.child("ct00003")
//        subRef.setValue(
//            CategoryType(
//                id = "ct00003",
//                name = "exclude"
//            )
//        )
        //refCategoryType.

}