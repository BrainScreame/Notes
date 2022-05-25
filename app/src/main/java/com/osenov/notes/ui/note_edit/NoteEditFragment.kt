package com.osenov.notes.ui.note_edit

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.osenov.notes.R
import com.osenov.notes.data.model.Note
import com.osenov.notes.databinding.FragmentNoteEditBinding
import com.osenov.notes.utils.closeKeyBoard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteEditFragment : Fragment() {

    companion object {
        const val NOTE = "NOTE"

        fun makeArgs(note: Note): Bundle {
            return Bundle(1).apply {
                putParcelable(NOTE, note)
            }
        }
    }

    private var note: Note? = null

    private val viewModel: NoteEditViewModel by viewModels()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentNoteEditBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
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

        note = arguments?.getParcelable(NOTE)

        setupToolbar()
        setupUIComponent()
    }

    override fun onPause() {
        super.onPause()
        this.closeKeyBoard()
        if (note != null) {
            if (binding.textNoteName.text.toString() != "" || binding.textNoteDescription.text.toString() != "") {
                if (binding.textNoteName.text.toString() != note?.title &&
                    binding.textNoteDescription.text.toString() != note?.description
                ) {
                    updateNote()
                }
            } else {
                viewModel.deleteNote(note!!)
            }
        } else {
            if (binding.textNoteName.text.toString() != "" || binding.textNoteDescription.text.toString() != "") {
                addNote()
            }
        }
    }


    private fun setupToolbar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(binding.noteEditToolbar)
        appCompatActivity.title = ""
        setHasOptionsMenu(true)
    }

    private fun setupUIComponent() {
        binding.noteEditToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.textNoteName.setText(note?.title)
        binding.textNoteDescription.setText(note?.description)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                note?.let { viewModel.deleteNote(it) }
                requireActivity().onBackPressed()
            }
            R.id.action_save -> {
                if (note != null) {
                    updateNote()
                } else {
                    addNote()
                }
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateNote() {
        note = note?.copy(
            id = note?.id ?: 0,
            title = binding.textNoteName.text.toString(),
            description = binding.textNoteDescription.text.toString(),
            dateCreate = System.currentTimeMillis()
        )
        note?.let { viewModel.updateNote(it) }
    }

    private fun addNote() {
        note = Note(
            0,
            binding.textNoteName.text.toString(),
            binding.textNoteDescription.text.toString(),
            System.currentTimeMillis()
        )
        note?.let { viewModel.addNote(it) }
    }


}