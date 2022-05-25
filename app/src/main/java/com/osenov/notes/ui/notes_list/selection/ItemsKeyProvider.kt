package com.osenov.notes.ui.notes_list.selection

import androidx.recyclerview.selection.ItemKeyProvider

class ItemsKeyProvider(private val adapter: NotesRecyclerAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long = adapter.notes[position].id

    override fun getPosition(key: Long): Int = adapter.notes.indexOfFirst { it.id == key }
}