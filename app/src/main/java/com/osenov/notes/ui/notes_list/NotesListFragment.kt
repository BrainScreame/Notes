package com.osenov.notes.ui.notes_list


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.osenov.notes.R
import com.osenov.notes.data.model.Note
import com.osenov.notes.databinding.FragmentNotesListBinding
import com.osenov.notes.ui.note_edit.NoteEditFragment
import com.osenov.notes.ui.notes_list.selection.ItemsDetailsLookup
import com.osenov.notes.ui.notes_list.selection.ItemsKeyProvider
import com.osenov.notes.ui.notes_list.selection.NoteItemDecoration
import com.osenov.notes.ui.notes_list.selection.NotesRecyclerAdapter
import com.osenov.notes.utils.closeKeyBoard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesListFragment : Fragment(), ActionMode.Callback {

    private var searchView: SearchView? = null
    private var actionMode: ActionMode? = null

    private val viewModel: NotesListViewModel by viewModels()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentNotesListBinding.inflate(layoutInflater)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        NotesRecyclerAdapter { note ->
            findNavController().navigate(
                R.id.action_notesListFragment_to_noteEditFragment,
                NoteEditFragment.makeArgs(note)
            )
        }
    }

    private val tracker by lazy(LazyThreadSafetyMode.NONE) {
        SelectionTracker.Builder(
            "selectionItem",
            binding.recyclerNotes,
            ItemsKeyProvider(adapter),
            ItemsDetailsLookup(binding.recyclerNotes),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
    }

    private val notesItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        NoteItemDecoration(resources.getDimensionPixelSize(R.dimen.notes_recycler_offset))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupUIComponents()

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(
                R.id.action_notesListFragment_to_noteEditFragment
            )
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.updateNotes()
    }

    override fun onPause() {
        super.onPause()
        binding.recyclerNotes.removeItemDecoration(notesItemDecoration)
        this.closeKeyBoard()
    }

    private fun setupToolbar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(binding.notesToolbar)
        appCompatActivity.setTitle(R.string.notes_toolbar_title)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notes_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                searchView = item.actionView as SearchView
                addOnQueryTextListenerOnSearchView()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun update(list: List<Note>) {
        adapter.setListNotes(list)
    }

    private fun setupUIComponents() {
        lifecycleScope.launchWhenStarted {
            viewModel.notes.collect {
                update(it)
                if(it.isNotEmpty()) {
                    binding.textEmptyList.visibility = View.GONE
                } else{
                    binding.textEmptyList.visibility = View.VISIBLE
                }

            }
        }

        binding.recyclerNotes.addItemDecoration(notesItemDecoration)
        binding.recyclerNotes.adapter = adapter

        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()

                    if (actionMode == null) {
                        actionMode = requireActivity().startActionMode(this@NotesListFragment)
                    }

                    val notes = tracker.selection.size()
                    if (notes > 0) {
                        actionMode?.title = getString(R.string.selected_text, notes)
                    } else {
                        actionMode?.finish()
                    }
                }
            })

        adapter.tracker = tracker

        binding.swipeToRefreshNotes.setOnRefreshListener {
            viewModel.updateNotes()
            binding.swipeToRefreshNotes.isRefreshing = false
        }
    }

    private fun addOnQueryTextListenerOnSearchView() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText ?: "")
                return false
            }
        })
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.notes_menu_selection, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = true

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_delete -> {
                val selected = adapter.notes.filter {
                    tracker.selection.contains(it.id)
                }.toMutableList()
                viewModel.deleteNotes(selected)
                adapter.removeListNotes(selected)
                actionMode?.finish()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        tracker.clearSelection()
        actionMode = null
    }


}