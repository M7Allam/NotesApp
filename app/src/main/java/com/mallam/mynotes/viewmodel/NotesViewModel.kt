package com.mallam.mynotes.viewmodel

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mallam.mynotes.data.Note
import com.mallam.mynotes.repo.NotesRepo
import com.mallam.mynotes.utilities.log
import com.mallam.mynotes.utilities.makeSuccessToasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object NotesViewModel : ViewModel() {

    private val repo = NotesRepo
    val notesLiveData = MutableLiveData<List<Note>>()

    fun init(context: Context) {
        NotesRepo.init(context)
    }

    fun getNotes() = viewModelScope.launch(Dispatchers.IO) {
        notesLiveData.postValue(repo.getNotes())
    }

    private fun insertNote(note: Note, view:View) = viewModelScope.launch(Dispatchers.IO) {
        repo.insertNote(note)
        withContext(Dispatchers.Main){
            view.makeSuccessToasty("Saved!")
        }
    }

    fun insertNoteAndRefresh(note: Note, view:View){
        viewModelScope.launch(Dispatchers.IO){
            val insertJob = insertNote(note, view)
            insertJob.join()
            getNotes()
        }

    }


    private fun updateNote(note: Note, view:View) = viewModelScope.launch(Dispatchers.IO) {
        repo.updateNote(note)
        withContext(Dispatchers.Main){
            view.makeSuccessToasty("Saved!")
        }
    }

    fun updateNoteAndRefresh(note: Note, view:View){
        viewModelScope.launch(Dispatchers.IO){
            val updateJob = updateNote(note, view)
            updateJob.join()
            getNotes()
        }

    }

    private fun deleteNote(id: Int, activity: FragmentActivity, view:View) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteNote(id)
        withContext(Dispatchers.Main){
            view.makeSuccessToasty("Deleted!")
            activity.onBackPressed()
        }
    }

    fun deleteNoteAndRefresh(id: Int, activity: FragmentActivity, view:View){
        viewModelScope.launch(Dispatchers.IO){
            val deleteJob = deleteNote(id, activity, view)
            deleteJob.join()
            getNotes()
        }

    }

}