package com.osenov.notes.ui.notes_list.selection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.osenov.notes.data.model.Note
import com.osenov.notes.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NotesRecyclerAdapter(
    private val onItemClicked: (Note) -> Unit
) : RecyclerView.Adapter<NotesRecyclerAdapter.NoteRecyclerViewHolder>() {

    var notes: MutableList<Note> = mutableListOf()
        private set

    var tracker: SelectionTracker<Long>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteRecyclerViewHolder {
        return NoteRecyclerViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteRecyclerViewHolder, position: Int) {
        holder.bind(notes[position])
        holder.itemView.setOnClickListener { onItemClicked(notes[position]) }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListNotes(list: List<Note>) {
        notes = list.toMutableList()
        notifyDataSetChanged()
    }

    fun removeListNotes(list: List<Note>) {
        list.forEach {
            val indexRemoveNote = notes.indexOf(it)
            if(indexRemoveNote != -1) {
                notes.removeAt(indexRemoveNote)
                notifyItemRemoved(indexRemoveNote)
            }
        }
    }

    inner class NoteRecyclerViewHolder(private val viewBinding: ItemNoteBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        private val calendar = Calendar.getInstance()

        @SuppressLint("SimpleDateFormat")
        private val dateFormat = SimpleDateFormat("d MMM, HH:mm")

        fun bind(note: Note) {

            calendar.timeInMillis = note.dateCreate
            viewBinding.textNoteName.text = note.title
            viewBinding.textNoteDescription.text = note.description
            viewBinding.textTimeCreate.text = dateFormat.format(calendar.time)

            tracker?.let {
                if (it.isSelected(note.id)) {
                    viewBinding.viewSelected.visibility = View.VISIBLE
                } else {
                    viewBinding.viewSelected.visibility = View.GONE
                }
            }
        }


        fun getItem(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long = notes[bindingAdapterPosition].id
            }
    }

}
