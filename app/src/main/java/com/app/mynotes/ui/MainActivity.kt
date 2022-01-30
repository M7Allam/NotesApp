package com.app.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.mynotes.R
import com.app.mynotes.viewmodel.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(){

    private val notesViewModel = NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notesViewModel.init(this)

        getNotes()
    }

    private fun getNotes(){
        notesViewModel.getNotes()
    }
}