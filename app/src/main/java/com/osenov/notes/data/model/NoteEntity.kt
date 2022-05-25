package com.osenov.notes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String,
    val dateCreate: Long
) {
    fun toNote() = Note(
        id, title, description, dateCreate
    )
}