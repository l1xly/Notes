package com.l1xly.notes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.l1xly.notes.R
import com.l1xly.notes.databinding.FragmentAddBinding
import com.l1xly.notes.models.Note
import com.l1xly.notes.ui.viewmodels.NoteViewModel
import com.l1xly.notes.utils.hideKeyboard

class AddFragment : Fragment() {

    private val viewModel: NoteViewModel by activityViewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        // Add note by pressing back button
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveNote()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Add note by pressing up button
            android.R.id.home -> {
                saveNote()
                hideKeyboard()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val note = Note(
            0,
            binding.titleEditText.text.toString(),
            binding.noteEditText.text.toString()
        )
        if (viewModel.isValidNote(note)) {
            viewModel.insertNote(note)
        } else {
            Toast.makeText(context, getString(R.string.note_is_empty), Toast.LENGTH_LONG).show()
        }
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}