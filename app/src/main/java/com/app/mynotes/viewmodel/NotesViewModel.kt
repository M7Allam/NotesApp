package com.app.mynotes.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mynotes.data.Note
import com.app.mynotes.database.NotesDatabase
import com.app.mynotes.repo.NotesRepo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object NotesViewModel : ViewModel() {

    private val repo = NotesRepo
    val notesLiveData = MutableLiveData<List<Note>>()

    fun init(context: Context) {
        NotesRepo.init(context)
    }

    fun getNotes() = viewModelScope.launch(Dispatchers.IO) {
        notesLiveData.postValue(repo.getNotes())
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repo.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateNote(note)
    }

    fun deleteNote(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteNote(id)

    }

}