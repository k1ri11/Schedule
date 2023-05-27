package com.example.schedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.data.model.Note
import com.example.schedule.databinding.FragmentNotesBinding
import com.example.schedule.presentation.adapters.NoteClickDeleteInterface
import com.example.schedule.presentation.adapters.NoteClickInterface
import com.example.schedule.presentation.adapters.NoteRVAdapter
import com.example.schedule.presentation.viewmodels.ScheduleViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(), NoteClickInterface, NoteClickDeleteInterface {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var notesRV: RecyclerView
    private lateinit var addFAB: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        notesRV = binding.notesRV
        addFAB = binding.idFAB

        notesRV.layoutManager = LinearLayoutManager(requireContext())

        val noteRVAdapter = NoteRVAdapter(requireContext(), this, this)
        notesRV.adapter = noteRVAdapter

        val tmpViewModel by viewModels<ScheduleViewModel>()
        viewModel = tmpViewModel
        viewModel.getNotes()

        viewModel.notes.observe(viewLifecycleOwner) { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        }

        addFAB.setOnClickListener {
            val addFragment = AddFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, addFragment)
                .commit()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNoteClick(note: Note) {
        val addFragment = AddFragment()
        val args = Bundle()
        args.putString("noteType", "Edit")
        args.putString("noteTitle", note.noteTitle)
        args.putString("noteDescription", note.noteDescription)
        args.putInt("noteId", note.id)
        addFragment.arguments = args

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, addFragment)
            .commit()
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
    }
}
