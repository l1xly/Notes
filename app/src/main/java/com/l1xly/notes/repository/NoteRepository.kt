package com.l1xly.notes.repository

import com.l1xly.notes.db.NoteDao
import com.l1xly.notes.models.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    fun getAllNotes() = noteDao.getAllNotes()

    fun searchNote(query: String) = noteDao.searchNote(query)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}