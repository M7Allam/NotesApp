package com.mallam.mynotes.repo

import android.content.Context
import com.mallam.mynotes.data.Note
import com.mallam.mynotes.database.NotesDatabase

object NotesRepo {

    private lateinit var notesDatabase : NotesDatabase
    fun init(context: Context){
        notesDatabase = NotesDatabase.getInstance(context)
    }

    fun getNotes() = notesDatabase.noteDao().notesList

    suspend fun insertNote(note: Note) = notesDatabase.noteDao().insertNote(note)

    suspend fun updateNote(note: Note) = notesDatabase.noteDao().updateNote(note)

    suspend fun deleteNote(id: Int) = notesDatabase.noteDao().deleteNote(id)

}