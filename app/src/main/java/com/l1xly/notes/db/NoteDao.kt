package com.l1xly.notes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.l1xly.notes.models.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note WHERE title LIKE :query OR description LIKE :query ORDER BY id DESC")
    fun searchNote(query: String): LiveData<List<Note>>
}