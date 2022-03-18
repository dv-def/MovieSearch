package ru.dvn.moviesearch.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHomeBinding
import ru.dvn.moviesearch.model.movie.MovieAdapter
import ru.dvn.moviesearch.model.movie.list.*

const val EXTRA_TOP_FILMS_STATUS = "EXTRA_TOP_FILMS_STATUS"
const val EXTRA_TOP_FILMS_STATUS_SUCCESS = "EXTRA_TOP_FILMS_STATUS_SUCCESS"
const val EXTRA_TOP_FILMS_STATUS_ERROR = "EXTRA_MOVIE_LIST_ERROR"

const val EXTRA_LOAD_MODE = "EXTRA_LOAD_MODE"
const val EXTRA_MOVIE_LIST = "EXTRA_MOVIE_LIST"

const val MOVIE_LIST_RECEIVER_ACTION = "MOVIE_LIST_RECEIVER_ACTION"

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var topBestMoviesAdapter: MovieAdapter
    private lateinit var topAwaitMoviesAdapter: MovieAdapter

    private val receiver = object: BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(EXTRA_TOP_FILMS_STATUS)) {
                EXTRA_TOP_FILMS_STATUS_SUCCESS -> {
                    intent.getParcelableExtra<MovieList>(EXTRA_MOVIE_LIST)?.let {
                        when (intent.getStringExtra(EXTRA_LOAD_MODE)) {
                            MoviesLoadMode.TOP_BEST_FILMS.getMode() -> {
                                showTopBestMovies(it)
                            }
                            MoviesLoadMode.TOP_AWAIT_FILMS.getMode() -> {
                                showTopAwaitMovies(it)
                            }
                        }
                    }
                }
                EXTRA_TOP_FILMS_STATUS_ERROR -> {
                    when (intent.getStringExtra(EXTRA_LOAD_MODE)) {
                        MoviesLoadMode.TOP_BEST_FILMS.getMode() -> {
                            showTopBestMoviesError()
                        }
                        MoviesLoadMode.TOP_AWAIT_FILMS.getMode() -> {
                            showTopAwaitMoviesError()
                        }
                    }
                }
            }
        }
    }

    private val onMovieClickListener = object : OnMovieClickListener {
        override fun onMovieClick(movieId: Int) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.fragment_host, DetailFragment.newInstance(movieId = movieId))
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                receiver,
                IntentFilter(MOVIE_LIST_RECEIVER_ACTION)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTopBestMoviesBlock()
        initTopAwaitMoviesBlock()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        topBestMoviesAdapter.deleteMovieClickListener()
        topAwaitMoviesAdapter.deleteMovieClickListener()
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver)
        }
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initTopBestMoviesBlock() {
        topBestMoviesAdapter = MovieAdapter(onMovieClickListener = onMovieClickListener)
        binding.topBestRecyclerView.apply {
            adapter = topBestMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.topBestError.setOnClickListener {
            requestTopBest()
        }

        requestTopBest()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initTopAwaitMoviesBlock() {
        topAwaitMoviesAdapter = MovieAdapter(onMovieClickListener = onMovieClickListener)
        binding.topAwaitRecyclerView.apply {
            adapter = topAwaitMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.topAwaitError.setOnClickListener {
            requestTopAwait()
        }

        requestTopAwait()

    }

    private fun requestTopBest() {
        with(binding) {
            topBestError.visibility = View.GONE
            topBestLoading.root.visibility = View.VISIBLE
        }
        context?.let {
            it.startService(
                Intent(it, MovieListService::class.java).apply {
                    putExtra(EXTRA_MOVIE_LOAD_MODE, MoviesLoadMode.TOP_BEST_FILMS.getMode())
                }
            )
        }
    }

    private fun requestTopAwait() {
        with(binding) {
            topAwaitError.visibility = View.GONE
            topBestLoading.root.visibility = View.VISIBLE
        }

        context?.let {
            it.startService(
                Intent(it, MovieListService::class.java).apply {
                    putExtra(EXTRA_MOVIE_LOAD_MODE, MoviesLoadMode.TOP_AWAIT_FILMS.getMode())
                }
            )
        }
    }

    private fun showTopBestMovies(movieList: MovieList) {
        movieList.films?.let {
            topBestMoviesAdapter.setMovies(it)
            with(binding) {
                topBestLoading.root.visibility = View.GONE
                topBestMainLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun showTopBestMoviesError() {
        with(binding) {
            topBestLoading.root.visibility = View.GONE
            topBestError.visibility = View.VISIBLE
        }
    }

    private fun showTopAwaitMovies(movieList: MovieList) {
        movieList.films?.let {
            topAwaitMoviesAdapter.setMovies(it)
            with(binding) {
                topAwaitLoading.root.visibility = View.GONE
                topAwaitMainLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun showTopAwaitMoviesError() {
        with(binding) {
            topAwaitLoading.root.visibility = View.GONE
            topAwaitError.visibility = View.VISIBLE
        }
    }

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Int)
    }

}