package com.osenov.notes.data.local

import androidx.room.*
import com.osenov.notes.data.model.NoteEntity

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Delete
    suspend fun deleteSelectedNotes(notes: List<NoteEntity>)

    @Query("SELECT * FROM notes WHERE title LIKE :query or description LIKE :query ORDER BY dateCreate DESC")
    suspend fun getNotes(query: String): List<NoteEntity>

}