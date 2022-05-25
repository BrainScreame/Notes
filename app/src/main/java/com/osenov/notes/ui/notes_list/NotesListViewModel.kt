package com.osenov.notes.ui.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osenov.notes.data.model.Note
import com.osenov.notes.domain.GetQueryNotesUseCase
import com.osenov.notes.domain.NotesDeleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getQueryNotesUseCase: Provider<GetQueryNotesUseCase>,
    private val notesDeleteUseCase: NotesDeleteUseCase
) : ViewModel() {
    companion object {
        private const val DEFAULT_QUERY = ""
    }

    private val _query = MutableStateFlow(DEFAULT_QUERY)
    val query: StateFlow<String> = _query.asStateFlow()

    private val _notes: MutableStateFlow<List<Note>> =
        MutableStateFlow(emptyList())
    val notes = _notes.asStateFlow()

    init {
        viewModelScope.launch {
            query.map(::getNotes).collect {
                _notes.value = it
            }
        }
    }

    fun setQuery(query: String) {
        _query.tryEmit(query)
    }

    private suspend fun getNotes(query: String): List<Note> {
        return getQueryNotesUseCase.get().getNotes(query)
    }

    fun deleteNotes(notes: List<Note>) {
        viewModelScope.launch {
            notesDeleteUseCase.deleteNotes(notes)
        }
    }

    fun updateNotes() {
        viewModelScope.launch {
            _notes.value = getQueryNotesUseCase.get().getNotes(query.value)
        }
    }


}