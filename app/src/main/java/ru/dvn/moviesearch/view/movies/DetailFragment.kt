package ru.dvn.moviesearch.view.movies

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentDetailBinding
import ru.dvn.moviesearch.model.history.local.HistoryEntity
import ru.dvn.moviesearch.model.movie.remote.detail.DetailsLoadState
import ru.dvn.moviesearch.model.movie.remote.detail.GenreDto
import ru.dvn.moviesearch.model.movie.remote.detail.MovieDetailDto
import ru.dvn.moviesearch.utils.getCurrentDate
import ru.dvn.moviesearch.view.notes.NoteEditFragment
import ru.dvn.moviesearch.view.notes.NotesListFragment
import ru.dvn.moviesearch.view.settings.SettingsFragment.Companion.NOTES_SETTINGS_KEY
import ru.dvn.moviesearch.view.staff.StaffFragment
import ru.dvn.moviesearch.viewmodel.movies.DetailsViewModel
import ru.dvn.moviesearch.viewmodel.history.HistoryViewModel
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

    private val historyViewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private var kinopoiskFilmId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kinopoiskFilmId = arguments?.getLong(EXTRA_MOVIE_ID)

        detailViewModel.liveData.observe(viewLifecycleOwner) {
            renderDetails(it)
        }

        requestDetails()
        if (activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean(NOTES_SETTINGS_KEY, false) == true) {
            binding.noteFragmentHost.visibility = View.VISIBLE
            kinopoiskFilmId?.let { filmId ->
                childFragmentManager.beginTransaction()
                    .replace(R.id.note_fragment_host, NotesListFragment.newInstance(filmId))
                    .commit()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                        ?.replace(R.id.fragment_host, NoteEditFragment.newInstance(
                            args.getLong(
                                EXTRA_MOVIE_ID
                            )
                        )
                        )?.addToBackStack(null)?.commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderDetails(state: DetailsLoadState) {
        when (state) {
            is DetailsLoadState.Success -> {
                binding.detailsLoadingLayout.visibility = View.GONE
                binding.detailsMainLayout.visibility = View.VISIBLE
                showMovie(state.movie)
                saveHistory(state.movie)
            }
            is DetailsLoadState.Error -> {
                binding.detailsLoadingLayout.visibility = View.GONE
                binding.detailsMainLayout.visibility = View.GONE
                Snackbar
                    .make(binding.detailsMainLayout, R.string.detail_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again) {
                        requestDetails()
                    }
                    .show()
            }
            is DetailsLoadState.Loading -> {
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

            initShowStaffButton()
        }
    }

    private fun saveHistory(movieDetailDto: MovieDetailDto) {
        val history = HistoryEntity(
            id = 0,
            kinopoiskFilmId = movieDetailDto.kinopoiskId,
            movieName = movieDetailDto.nameRu,
            moviePoster = movieDetailDto.posterUrlPreview,
            date = getCurrentDate()
        )

        historyViewModel.save(history)
    }

    private fun initShowStaffButton() {
        arguments?.getLong(EXTRA_MOVIE_ID)?.let { filmId ->
            binding.btnShowStaff.setOnClickListener {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragment_host, StaffFragment.newInstance(filmId))
                    ?.addToBackStack(null)
                    ?.commit()
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