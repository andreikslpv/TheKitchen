package com.andreikslpv.thekitchen.presentation.ui.dialogs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.andreikslpv.thekitchen.databinding.MergeShoppingDialogBinding
import com.andreikslpv.thekitchen.presentation.utils.visible

class ShoppingDialog(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    val binding = MergeShoppingDialogBinding.inflate(LayoutInflater.from(context), this)
    val actionButton = binding.dialogActionButton
    val deleteButton = binding.dialogDelete
    val nameText = binding.dialogNameText
    val countText = binding.dialogCountText
    val unitText = binding.dialogUnitText
    val dialogTitle = binding.dialogTitle

    init {
        binding.dialogCancelButton.setOnClickListener {
            this.visible(false)
        }
    }

}