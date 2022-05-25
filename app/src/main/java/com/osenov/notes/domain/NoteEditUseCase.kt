package com.osenov.notes.domain

import com.osenov.notes.data.model.Note
import com.osenov.notes.data.repository.NotesRepository
import javax.inject.Inject

class NoteEditUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun addNote(note: Note) = repository.addNote(note)

    suspend fun updateNote(note: Note) = repository.updateNote(note)

    suspend fun deleteNote(note: Note) = repository.deleteNote(note)
}