package ru.dvn.moviesearch.view.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dvn.moviesearch.databinding.FragmentNoteEditBinding
import ru.dvn.moviesearch.model.note.local.NoteEditState
import ru.dvn.moviesearch.model.note.local.NoteEntity
import ru.dvn.moviesearch.utils.getCurrentDate
import ru.dvn.moviesearch.viewmodel.notes.NoteEditViewModel

class NoteEditFragment : Fragment() {
    companion object {
        const val EXTRA_KINOPOISK_FILM_ID = "EXTRA_KINOPOISK_FILM_ID"

        fun newInstance(filmId: Long): NoteEditFragment {
            val args = Bundle().apply {
                putLong(EXTRA_KINOPOISK_FILM_ID, filmId)
            }

            val fragment = NoteEditFragment().apply {
                arguments = args
            }

            return fragment
        }
    }

    private var _binding:FragmentNoteEditBinding? = null
    private val binding get() = _binding!!

    private var kinopoiskFilmId: Long? = null

    private val noteEditViewModel: NoteEditViewModel by lazy {
        ViewModelProvider(this).get(NoteEditViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kinopoiskFilmId = arguments?.getLong(EXTRA_KINOPOISK_FILM_ID)

        noteEditViewModel.liveData.observe(viewLifecycleOwner) {
            checkState(it)
        }

        kinopoiskFilmId?.let { filmId ->
            with(binding) {
                btnNoteSave.setOnClickListener {
                    val title = tilNoteTitle.editText?.text.toString()
                    val text = tilNoteText.editText?.text.toString()

                    val note = NoteEntity(
                        id = 0,
                        kinopoiskFilmId = filmId,
                        title = title,
                        text = text,
                        date = getCurrentDate()
                    )

                    noteEditViewModel.save(note)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkState(state: NoteEditState) {
        when(state) {
            is NoteEditState.SuccessSave -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE
                showMessage(state.message)
                closeFragment()
            }
            is NoteEditState.Error -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE
                showMessage(state.message)
                closeFragment()
            }
            is NoteEditState.Loading -> {
                binding.loadingLayout.root.visibility = View.VISIBLE
                binding.mainLayout.visibility = View.GONE
            }
        }
    }

    private fun closeFragment() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
