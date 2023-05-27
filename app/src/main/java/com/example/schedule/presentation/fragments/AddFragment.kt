package com.example.schedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.schedule.R
import com.example.schedule.data.model.Note
import com.example.schedule.databinding.FragmentAddBinding
import com.example.schedule.presentation.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteTitleEdt: EditText
    private lateinit var noteEdt: EditText
    private lateinit var saveBtn: Button

    private lateinit var viewModel: ScheduleViewModel
    private var noteID = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root

        val tmpViewModel by viewModels<ScheduleViewModel>()
        viewModel = tmpViewModel

        noteTitleEdt = binding.idEdtNoteName
        noteEdt = binding.idEdtNoteDesc
        saveBtn = binding.idBtn

        val noteType = arguments?.getString("noteType")
        val noteTitle = arguments?.getString("noteTitle")
        val noteDescription = arguments?.getString("noteDescription")
        noteID = arguments?.getInt("noteId", -1) ?: -1

        if (noteType == "Edit") {
            saveBtn.text = "Update Note"
            noteTitleEdt.setText(noteTitle)
            noteEdt.setText(noteDescription)
        } else {
            saveBtn.text = "Save Note"
        }

        saveBtn.setOnClickListener {
            val title = noteTitleEdt.text.toString()
            val description = noteEdt.text.toString()

            if (noteType == "Edit") {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                    val currentDateAndTime: String = sdf.format(Date())
                    val updatedNote = Note(title, description, currentDateAndTime)
                    updatedNote.id = noteID
                    viewModel.updateNote(updatedNote)
                }
            } else {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                    val currentDateAndTime: String = sdf.format(Date())
                    val newNote = Note(title, description, currentDateAndTime)
                    viewModel.addNote(newNote)
                }
            }

            val fragment = NotesFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
