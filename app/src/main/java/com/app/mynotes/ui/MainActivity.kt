package com.app.mynotes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.mynotes.R
import com.app.mynotes.viewmodel.NotesViewModel

class MainActivity : AppCompatActivity() {

    private val notesViewModel: NotesViewModel by lazy {
        NotesViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notesViewModel.init(this)

        getNotes()
    }

    private fun getNotes() {
        notesViewModel.getNotes()
    }
}