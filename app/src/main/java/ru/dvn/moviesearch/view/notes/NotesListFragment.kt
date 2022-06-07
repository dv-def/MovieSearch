package ru.dvn.moviesearch.view.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.databinding.FragmentNotesListBinding
import ru.dvn.moviesearch.model.note.local.NotesListState
import ru.dvn.moviesearch.model.note.recycler.NoteAdapter
import ru.dvn.moviesearch.viewmodel.notes.NotesListViewModel

class NotesListFragment : Fragment() {
    companion object {
        private const val EXTRA_FILM_ID = "EXTRA_FILM_ID"

        fun newInstance(filmId: Long): NotesListFragment {
            val args = Bundle().apply {
                putLong(EXTRA_FILM_ID, filmId)
            }

            return NotesListFragment().apply {
                arguments = args
            }
        }
    }

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val notesListViewModel: NotesListViewModel by lazy {
        ViewModelProvider(this).get(NotesListViewModel::class.java)
    }

    private var noteAdapter = NoteAdapter()
    private var kinopoiskFilmId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kinopoiskFilmId = arguments?.getLong(EXTRA_FILM_ID)

        binding.rvNotes.adapter = noteAdapter
        setupItemTouchHelper()

        kinopoiskFilmId?.let { filmId ->
            notesListViewModel.liveData.observe(viewLifecycleOwner) { state ->
                renderNotes(state)
            }
            notesListViewModel.getAllByKinopoiskId(filmId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderNotes(state: NotesListState) {
        when(state) {
            is NotesListState.SuccessGetAll -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.rvNotes.visibility = View.VISIBLE
                noteAdapter.updateNotes(state.notes)
            }
            is NotesListState.SuccessDelete -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.rvNotes.visibility = View.VISIBLE
                noteAdapter.deleteNote(state.position)
            }
            is NotesListState.Error -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.rvNotes.visibility = View.GONE
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            is NotesListState.Loading -> {
                binding.loadingLayout.root.visibility = View.VISIBLE
                binding.rvNotes.visibility = View.GONE
            }
        }
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.START or (ItemTouchHelper.END))
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val entity = noteAdapter.getNote(position)
                notesListViewModel.delete(entity, position)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvNotes)
    }
}