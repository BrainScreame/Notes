package com.osenov.notes.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Long,
    var title: String,
    var description: String,
    var dateCreate: Long,
): Parcelable {
    fun toNoteEntity() = NoteEntity(
        id, title, description, dateCreate
    )
}