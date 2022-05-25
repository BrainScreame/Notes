package com.osenov.notes.domain

import com.osenov.notes.data.model.Note
import com.osenov.notes.data.repository.NotesRepository
import javax.inject.Inject

class NotesDeleteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun deleteNotes(notes: List<Note>) {
        repository.deleteSelectedNotes(notes)
    }
}