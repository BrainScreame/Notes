package com.osenov.notes.ui.notes_list.paging

import androidx.recyclerview.widget.RecyclerView
import com.osenov.notes.data.model.Note
import com.osenov.notes.databinding.ItemNoteBinding

class NoteViewHolder(private val viewBinding: ItemNoteBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(note: Note?) {
        with(viewBinding) {
            textNoteName.text = note?.title
            textNoteDescription.text = note?.description
        }
    }
}
