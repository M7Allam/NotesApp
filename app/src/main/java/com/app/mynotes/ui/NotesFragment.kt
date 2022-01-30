package com.app.mynotes.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.mynotes.R
import com.app.mynotes.adapter.NotesAdapter
import com.app.mynotes.data.Note
import com.app.mynotes.database.NotesDatabase
import com.app.mynotes.databinding.FragmentNotesBinding
import com.app.mynotes.utilities.BaseFragment
import com.app.mynotes.utilities.ItemNotesAdapterOnCLick
import com.app.mynotes.viewmodel.NotesViewModel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class NotesFragment : BaseFragment() {

    private lateinit var binding: FragmentNotesBinding
    private val notesViewModel = NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesList: List<Note>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        notesViewModel.init(requireContext())

        searchView()
        onFABClick()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnNotesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        notesViewModel.notesLiveData.removeObservers(viewLifecycleOwner)
    }

    private fun observeOnNotesList(){
        notesViewModel.notesLiveData.observe(viewLifecycleOwner, {
            it?.let {
                notesList = it.sortedByDescending { note ->
                    note.date
                }
                setupRecycler(notesList)
            }
        })
    }

    private fun setupRecycler(notesList: List<Note>) {
        notesAdapter = NotesAdapter(requireActivity(), notesList)
        onItemNoteClick()
        onWebUrlNoteClick()
        binding.recyclerNotes.adapter = notesAdapter
        binding.recyclerNotes.setHasFixedSize(true)
        binding.recyclerNotes.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
    }

    private fun onItemNoteClick() {
        notesAdapter.onItemClickListener = { note, position ->
            Navigation.findNavController(requireView()).run {
                val args = Bundle()
                args.putBoolean("isNewNote", false)
                args.putSerializable("note", note)
                args.putInt("position", position)
                navigate(R.id.action_notesFragment_to_createNoteFragment, args)
            }
        }
    }

    private fun onWebUrlNoteClick(){
        notesAdapter.onWebUrlClickListener = { note ->
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(note.webLink)))
        }
    }


    private fun searchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(p0: String?): Boolean {
                val tempArr = ArrayList<Note>()

                for (note in notesList) {
                    if (note.title!!.lowercase(Locale.getDefault()).contains(p0.toString())) {
                        tempArr.add(note)
                    }
                }
                notesAdapter.setNotesList(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true
            }
        })
    }

    private fun onFABClick() {
        binding.fabCreateNote.setOnClickListener {
            Navigation.findNavController(requireView()).run {
                val args = Bundle()
                args.putBoolean("isNewNote", true)
                navigate(R.id.action_notesFragment_to_createNoteFragment, args)
            }
        }
    }

}