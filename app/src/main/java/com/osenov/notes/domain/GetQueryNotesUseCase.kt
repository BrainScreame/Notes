package com.osenov.notes.domain

import androidx.paging.PagingSource
import com.osenov.notes.data.model.Note
import com.osenov.notes.data.repository.NotesRepository
import javax.inject.Inject

class GetQueryNotesUseCase @Inject constructor(private val repository: NotesRepository) {
    operator fun invoke(query: String) : PagingSource<Int, Note> {
        return repository.queryNotes(query)
    }

    suspend fun getNotes(query: String) : List<Note> = repository.getNotes("%$query%")
}