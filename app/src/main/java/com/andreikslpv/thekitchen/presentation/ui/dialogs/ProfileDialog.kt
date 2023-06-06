package com.andreikslpv.thekitchen.presentation.ui.dialogs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.andreikslpv.thekitchen.databinding.MergeProfileDialogBinding
import com.andreikslpv.thekitchen.presentation.utils.visible

class ProfileDialog(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    val binding = MergeProfileDialogBinding.inflate(LayoutInflater.from(context), this)
    val actionButton = binding.dialogActionButton

    init {
        binding.dialogCancelButton.setOnClickListener {
            this.visible(false)
        }
    }

}