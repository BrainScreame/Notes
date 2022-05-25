package com.osenov.notes.ui.note_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osenov.notes.data.model.Note
import com.osenov.notes.domain.NoteEditUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(private val noteEditUseCase: NoteEditUseCase) :
    ViewModel() {

    fun updateNote(note: Note) {
        GlobalScope.launch {
            noteEditUseCase.updateNote(note)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteEditUseCase.addNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteEditUseCase.deleteNote(note)
        }
    }
}