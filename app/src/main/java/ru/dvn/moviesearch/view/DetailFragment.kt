package ru.dvn.moviesearch.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentDetailBinding
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.history.HistoryEntity
import ru.dvn.moviesearch.model.movie.detail.remote.GenreDto
import ru.dvn.moviesearch.model.movie.detail.remote.MovieDetailDto
import ru.dvn.moviesearch.model.note.recycler.NoteAdapter
import ru.dvn.moviesearch.utils.getCurrentDate
import ru.dvn.moviesearch.viewmodel.DetailsViewModel
import ru.dvn.moviesearch.viewmodel.HistoryViewModel
import ru.dvn.moviesearch.viewmodel.NotesViewModel
import java.lang.StringBuilder

class DetailFragment : Fragment() {
    companion object {
        const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newInstance(movieId: Long): DetailFragment {
            val fragment = DetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_MOVIE_ID, movieId)
                }
            }

            return fragment
        }
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private val notesViewModel: NotesViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    private val historyViewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private var handlerThread: HandlerThread? = null

    private val noteAdapter = NoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlerThread = HandlerThread("Database HT")
        handlerThread?.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderDetails(it)
        }

        binding.rvNotes.adapter = noteAdapter
        setupItemTouchHelper()

        notesViewModel.getLiveData().observe(viewLifecycleOwner) {
            checkNotesStatus(it)
        }

        requestDetails()
        if (activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean(NOTES_SETTINGS_KEY, false) == true) {
            binding.rvNotes.visibility = View.VISIBLE
            requestNotes()
        } else {
            binding.rvNotes.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handlerThread?.quitSafely()
        handlerThread = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu)
        activity?.let {
            val noteItem = menu.findItem(R.id.menu_item_note_add)
            noteItem.setVisible(
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(NOTES_SETTINGS_KEY, false)
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_note_add -> {
                arguments?.let { args ->
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_host, NoteEditFragment.newInstance(args.getLong(
                            EXTRA_MOVIE_ID)))?.addToBackStack(null)?.commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
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
                handlerThread?.let {
                    Handler(it.looper).post {
                        notesViewModel.delete(noteAdapter.getNote(position))
                        binding.rvNotes.post {
                            noteAdapter.deleteNote(position)
                        }
                    }
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvNotes)
    }

    private fun renderDetails(appState: AppState) {
        when (appState) {
            is AppState.SuccessDetails -> {
                binding.detailsLoadingLayout.visibility = View.GONE
                binding.detailsMainLayout.visibility = View.VISIBLE
                showMovie(appState.movie)
                saveHistory(appState.movie)
            }
            is AppState.Error -> {
                binding.detailsLoadingLayout.visibility = View.GONE
                binding.detailsMainLayout.visibility = View.GONE
                Snackbar
                    .make(binding.detailsMainLayout, R.string.detail_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again) {
                        requestDetails()
                    }
                    .show()
            }
            is AppState.Loading -> {
                binding.detailsLoadingLayout.visibility = View.VISIBLE
                binding.detailsMainLayout.visibility = View.GONE
            }
        }
    }

    private fun requestDetails() {
        arguments?.getLong(EXTRA_MOVIE_ID)?.let {
            detailViewModel.getDetails(it)
        }
    }

    private fun requestNotes() {
        arguments?.getLong(EXTRA_MOVIE_ID)?.let { id ->
            handlerThread?.let {
                Handler(it.looper).post() {
                    notesViewModel.getAllByKinopoiskId(id)
                }
            }
        }
    }

    private fun showMovie(movieDetailDto: MovieDetailDto) {
        binding.detailsLoadingLayout.visibility = View.GONE
        binding.detailsMainLayout.visibility = View.VISIBLE

        with (movieDetailDto) {
            nameRu?.let {
                binding.name.text = it
            } ?: run {
                binding.name.visibility = View.GONE
            }

            slogan?.let {
                binding.slogan.text = it
            } ?: run {
                binding.slogan.visibility = View.GONE
            }

            genres?.let { genresList ->
               binding.genre.text = genresList.str()
            } ?: run {
                binding.genre.visibility = View.GONE
            }

            filmLength?.let {
                val value = "${getString(R.string.movie_length)} $it"
                binding.length.text = value
            } ?: run {
                binding.length.visibility = View.GONE
            }

            ratingKinopoisk?.let {
                val value = "${getString(R.string.rating)} $it"
                binding.rating.text = value
            } ?: run {
                ratingAwait?.let {
                    val value = "${getString(R.string.rating_await)} $it"
                    binding.rating.text = value
                } ?: run {
                    binding.rating.text = getString(R.string.description_is_empty)
                }
            }

            year?.let {
                val value = "${getString(R.string.year)} $it"
                binding.year.text = value
            }?: run {
                binding.year.visibility = View.GONE
            }

            description?.let {
                binding.description.text = it
            } ?: run {
                binding.description.visibility = View.GONE
            }

            posterUrl?.let {
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.default_poster)
                    .into(binding.poster)
            }
        }
    }

    private fun saveHistory(movieDetailDto: MovieDetailDto) {
        handlerThread?.let {
            val handler = Handler(it.looper)
            handler.post {

                val history = HistoryEntity(
                    id = 0,
                    kinopoiskFilmId = movieDetailDto.kinopoiskId,
                    movieName = movieDetailDto.nameRu,
                    moviePoster = movieDetailDto.posterUrlPreview,
                    date = getCurrentDate()
                )

                historyViewModel.save(history)

            }
        }
    }

    private fun checkNotesStatus(appState: AppState) {
        when (appState) {
            is AppState.SuccessNotes -> {
                noteAdapter.updateNotes(appState.notes)
            }
            is AppState.Error -> {
                Toast.makeText(context, appState.error.message, Toast.LENGTH_SHORT).show()
            }
            is AppState.SuccessDML -> {
                Toast.makeText(context, appState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun List<GenreDto>.str(): String {
        val sb = StringBuilder()

        this.forEach {
            sb.append("${it.genre}, ")
        }

        sb.deleteCharAt(sb.lastIndexOf(","))

        return sb.toString().trim()
    }
}