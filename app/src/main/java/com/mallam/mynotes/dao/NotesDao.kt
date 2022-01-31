package com.mallam.mynotes.dao

import androidx.room.*
import com.mallam.mynotes.data.Note

@Dao
interface NotesDao {

    @get: Query("SELECT * FROM notes_table ORDER BY date DESC")
    val notesList: List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM notes_table WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Update
    suspend fun updateNote(note: Note)


}