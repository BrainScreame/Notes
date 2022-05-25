package com.osenov.notes.data.repository

import androidx.paging.PagingSource
import com.osenov.notes.data.model.Note

interface NotesRepository {
    fun queryNotes(query: String): PagingSource<Int, Note>

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun getNotes(query: String) : List<Note>

    suspend fun deleteSelectedNotes(notes : List<Note>)
}