package com.example.noteapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.MainActivity
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentUpdateNoteBinding
import com.example.noteapp.model.Note
import com.example.noteapp.toast
import com.example.noteapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import android.app.AlertDialog


class UpdateNoteFragment : Fragment() {
    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var nView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel

        currentNote = args.note!!
        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)
        binding.etNoteBodyUpdate.setText(currentNote.noteBody)
        nView = view
    }

    private fun saveNote(view: View) {
        val noteTitle = binding.etNoteTitleUpdate.text.toString().trim()
        val noteBody = binding.etNoteBodyUpdate.text.toString().trim()

        if (noteBody.isNotEmpty()) {
            val note = Note(currentNote.id, noteTitle, noteBody)
            noteViewModel.updateNote(note)
            Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
            activity?.toast("Note updated")
            view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)

        } else {
            activity?.toast("Note can not be empty!")
        }
    }

    private fun deleteNote(){
        AlertDialog.Builder(activity).apply{
            setTitle("Delte Note")
            setMessage("Are you sure you want to delete this note?")
            setPositiveButton("Delete"){_,_ ->
                noteViewModel.deleteNote(currentNote)
                view?.findNavController()?.navigate(
                    R.id.action_updateNoteFragment_to_homeFragment
                )
            }
            setNegativeButton("Cancel",null)
        }.create().show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save_update -> {
                saveNote(nView)
            }
            R.id.delete_note->{
                deleteNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}