package com.l1xly.notes.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l1xly.notes.models.Note
import com.l1xly.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    private val _allNotes = repository.getAllNotes()
    val allNotes: LiveData<List<Note>>
        get() = _allNotes

    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun searchNote(query: String): LiveData<List<Note>> {
        return repository.searchNote(query)
    }

    fun isValidNote(note: Note): Boolean {
        return !(note.title.isBlank() && note.description.isBlank())
    }

    fun noteWasChanged(oldNote: Note, newNote: Note): Boolean {
        return !(oldNote.title == newNote.title && oldNote.description == newNote.description)
    }
}