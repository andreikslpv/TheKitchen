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
import com.andreikslpv.thekitchen.domain.models.Ingredient
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ShoppingItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ShoppingRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.utils.ingredientCountToString
import com.andreikslpv.thekitchen.presentation.utils.visible
import com.andreikslpv.thekitchen.presentation.vm.ShoppingListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView

/**
 * A simple [Fragment] subclass.
 */
class ShoppingListFragment :
    BaseFragment<FragmentShoppingListBinding>(FragmentShoppingListBinding::inflate) {

    private val dialogAnimDuration = 500L
    private val dialogAnimAlfa = 1f

    private lateinit var shoppingAdapter: ShoppingRecyclerAdapter

    private val viewModel by viewModels<ShoppingListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeToRefresh()
        initRecyclers()
        initProductCollect()
        initSelectAllButton()
        initClearButton()
        initAddButton()
        initShoppingDialog()
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
                        showDialogEdit(shoppingItem)
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
                authRequiered()
            binding.shoppingSelectAllCheckBox.isChecked = false
        }
    }

    private fun authRequiered() {
        Snackbar.make(
            binding.root, R.string.home_snackbar_text,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.home_snackbar_action) { goToAuthFragment() }.show()
    }

    private fun goToAuthFragment() {
        findTopNavController().navigate(R.id.authFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }

    private fun initAddButton() {
        binding.shoppingAddButton.setOnClickListener {
            showDialogAdd()
        }
    }

    private fun initShoppingDialog() {
        viewModel.unitList.observe(viewLifecycleOwner) {
            (binding.shoppingDialog.unitText as? MaterialAutoCompleteTextView)?.setSimpleItems(
                viewModel.getUnitsNameList().toTypedArray()
            )
        }
    }

    private fun showDialogEdit(shoppingItem: ShoppingItem) {
        binding.shoppingDialog.apply {
            //Делаем видимой
            this.visible(true)
            //Анимируем появление
            animate()
                .setDuration(dialogAnimDuration)
                .alpha(dialogAnimAlfa)
                .start()

            deleteButton.visible(true)
            dialogTitle.text = getString(R.string.shopping_dialog_title_edit)
            actionButton.text = getString(R.string.shopping_dialog_action_edit)
            nameText.setText(shoppingItem.showingName)
            nameText.tag = shoppingItem.ingredient.product
            countText.setText(shoppingItem.ingredient.count.ingredientCountToString())

            (unitText as? MaterialAutoCompleteTextView)?.setText(
                viewModel.getUnitsNameById(shoppingItem.ingredient.unit),
                false
            )

            actionButton.setOnClickListener {
                val newShoppingItem = getShoppingItemFromDialog()
                newShoppingItem?.let {
                    if (!viewModel.editShoppingItem(it)) authRequiered()
                    else this.visible(false)
                }
            }

            deleteButton.setOnClickListener {
                if (!viewModel.removeFromShoppingList(listOf(shoppingItem))) authRequiered()
                else this.visible(false)
            }
        }
    }

    private fun showDialogAdd() {
        binding.shoppingDialog.apply {
            this.visible(true)
            animate()
                .setDuration(dialogAnimDuration)
                .alpha(dialogAnimAlfa)
                .start()

            deleteButton.visible(false)
            dialogTitle.text = getString(R.string.shopping_dialog_title_add)
            actionButton.text = getString(R.string.shopping_dialog_action_add)
            nameText.setText("")
            nameText.tag = ""
            countText.setText("")
            (unitText as? MaterialAutoCompleteTextView)?.setText("", false)

            actionButton.setOnClickListener {
                val newShoppingItem = getShoppingItemFromDialog()
                newShoppingItem?.let {
                    if (!viewModel.addToShoppingList(it)) authRequiered()
                    else this.visible(false)
                }
            }
        }
    }

    private fun getShoppingItemFromDialog(): ShoppingItem? {
        binding.shoppingDialog.apply {
            return if (nameText.text.isNullOrBlank() && nameText.tag.toString().isBlank()) {
                nameText.error = getString(R.string.shopping_dialog_field_error)
                null
            } else {
                val product = nameText.tag.toString()
                val unit = viewModel.getUnitsIdByName(unitText.text.toString())
                val count = countText.text.toString().toDoubleOrNull()
                val showingName =
                    if (nameText.text.isNullOrBlank()) "" else nameText.text.toString()
                ShoppingItem(
                    showingName,
                    Ingredient(product, unit, count ?: 0.0)
                )
            }
        }
    }

}