package com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpaceItemDecoration (private val paddingInDp: Int): RecyclerView.ItemDecoration() {
    private val Int.convertPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = paddingInDp.convertPx
        outRect.left = paddingInDp.convertPx
        outRect.bottom = paddingInDp.convertPx * 4

    }
}