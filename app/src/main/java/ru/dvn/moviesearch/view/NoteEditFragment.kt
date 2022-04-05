package ru.dvn.moviesearch.view

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentNoteEditBinding
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.note.local.NoteEntity
import ru.dvn.moviesearch.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.*

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

    private var handlerThread: HandlerThread? = null

    private val noteViewModel: NotesViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlerThread = HandlerThread("Edit Note HT")
        handlerThread?.start()
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

        noteViewModel.getLiveData().observe(viewLifecycleOwner) {
            when(it) {
                is AppState.SuccessDML -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is AppState.Error -> {
                    Toast.makeText(context, it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        arguments?.let { args ->
            with(binding) {
                btnNoteSave.setOnClickListener {
                    val title = tilNoteTitle.editText!!.text.toString()
                    val text = tilNoteText.editText!!.text.toString()
                    val kinopoiskId = args.getLong(EXTRA_KINOPOISK_FILM_ID)

                    val note = NoteEntity(
                        id = 0,
                        kinopoiskFilmId = kinopoiskId,
                        title = title,
                        text = text,
                        date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            .format(Date().time)
                    )

                    handlerThread?.let {
                        Handler(it.looper).post {
                            noteViewModel.save(note)
                        }
                    }
                    activity
                        ?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragment_host, DetailFragment.newInstance(kinopoiskId))
                        ?.commit()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread?.quitSafely()
        handlerThread = null
        print("NOTE EDIT ON DESTROY")
    }
}