package com.osenov.notes.ui.notes_list.selection

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class NoteItemDecoration (private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val position: Int = parent.getChildAdapterPosition(view)

        with(outRect) {
            if (params.spanIndex % 2 == 0) {
                top = space
                left = space
                right = space / 2
            } else {
                top = space
                left = space / 2
                right = space
            }

            if (state.itemCount % 2 == 0 && (position == state.itemCount - 1 || position == state.itemCount - 2)) {
                bottom = space
            } else if (state.itemCount % 2 == 1 && position == state.itemCount - 1) {
                bottom = space
            }

        }
    }
}