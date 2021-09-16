package com.l1xly.notes.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.l1xly.notes.R
import com.l1xly.notes.databinding.FragmentUpdateBinding
import com.l1xly.notes.models.Note
import com.l1xly.notes.ui.viewmodels.NoteViewModel
import com.l1xly.notes.utils.getCurrentTime
import com.l1xly.notes.utils.getDateFormatFull
import com.l1xly.notes.utils.hideKeyboard

class UpdateFragment : Fragment() {

    private val viewModel: NoteViewModel by activityViewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        // Update note by pressing back button
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                updateNote()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindNote(args.note)
    }

    private fun bindNote(note: Note) {
        binding.apply {
            titleEditText.setText(note.title)
            noteEditText.setText(note.description)
            lastEditDateTextView.text = getDateFormatFull(note.lastDateEdit)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_update, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                updateNote()
                hideKeyboard()
                true
            }

            R.id.delete -> {
                showConfirmationDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateNote() {
        val note = Note(
            args.note.id,
            binding.titleEditText.text.toString(),
            binding.noteEditText.text.toString(),
            getCurrentTime()
        )
        if (viewModel.noteWasChanged(args.note, note)) {
            if (viewModel.isValidNote(note)) {
                viewModel.updateNote(note)
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.note_is_empty),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            findNavController().popBackStack()
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteNote()
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    private fun deleteNote() {
        viewModel.deleteNote(args.note)
        Toast.makeText(context, getString(R.string.note_is_deleted), Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}