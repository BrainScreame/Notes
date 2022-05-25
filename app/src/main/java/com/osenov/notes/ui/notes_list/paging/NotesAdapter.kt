package com.osenov.notes.ui.notes_list.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.osenov.notes.data.model.Note
import com.osenov.notes.databinding.ItemNoteBinding

class NotesAdapter(private val onItemClicked: (Note?) -> Unit) :
    PagingDataAdapter<Note, NoteViewHolder>(CharacterDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onItemClicked(getItem(position)) }
    }

}

private object CharacterDiffItemCallback : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.description == newItem.description
    }
}