package com.osenov.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.osenov.notes.data.local.NotesDatabase.Companion.DB_VERSION
import com.osenov.notes.data.model.NoteEntity

@Database(entities = [NoteEntity::class], version = DB_VERSION)
abstract class NotesDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "notes.db"
        const val DB_VERSION = 1
    }

    abstract fun noteDao(): NoteDao
}