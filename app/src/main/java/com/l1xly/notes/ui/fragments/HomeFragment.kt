package com.l1xly.notes.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.l1xly.notes.R
import com.l1xly.notes.databinding.FragmentHomeBinding
import com.l1xly.notes.ui.adapters.NotesAdapter
import com.l1xly.notes.ui.viewmodels.NoteViewModel

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel: NoteViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NotesAdapter

    private var isLinearLayoutManager = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddFragment())
        }

        // Observer for all notes
        viewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        adapter = NotesAdapter {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToUpdateFragment(it)
            )
        }
        chooseLayout()
        swipeToDelete()
        binding.recyclerView.adapter = adapter
    }

    private fun swipeToDelete() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val noteForDeleting = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteNote(noteForDeleting)
                Snackbar.make(
                    binding.rootLayout,
                    getString(R.string.note_is_deleted),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.undo)) {
                    viewModel.insertNote(noteForDeleting)
                }.setAnchorView(R.id.fab_add_note).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun chooseLayout() {
        binding.recyclerView.layoutManager =
            if (isLinearLayoutManager) {
                LinearLayoutManager(context)
            } else {
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
    }

    private fun setIcon(menuItem: MenuItem) {
        if (isLinearLayoutManager) {
            menuItem.setIcon(R.drawable.ic_grid_layout)
        } else {
            menuItem.setIcon(R.drawable.ic_linear_layout)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        setIcon(menu.findItem(R.id.change_layout))
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = getString(R.string.search_notes)
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(item)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            viewModel.searchNote(query = "%$newText%").observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}