package com.osenov.notes.data.repository.impl

import androidx.paging.PagingSource
import com.osenov.notes.data.local.NoteDao
import com.osenov.notes.data.model.Note
import com.osenov.notes.data.page_source.NotesPageSource
import com.osenov.notes.data.repository.NotesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepositoryImpl @Inject constructor(
    private val notesPageSource: NotesPageSource.Factory,
    private val noteDao: NoteDao
) : NotesRepository {

    override fun queryNotes(query: String): PagingSource<Int, Note> {
        return notesPageSource.create(query)
    }

    override suspend fun addNote(note: Note) = noteDao.insertNote(note.toNoteEntity())

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note.toNoteEntity())

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note.toNoteEntity())

    override suspend fun getNotes(query: String): List<Note> =
        noteDao.getNotes(query).map { it.toNote() }

    override suspend fun deleteSelectedNotes(notes: List<Note>) =
        noteDao.deleteSelectedNotes(notes.map { it.toNoteEntity() })

}