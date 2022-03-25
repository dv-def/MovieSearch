package ru.dvn.moviesearch.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHomeBinding
import ru.dvn.moviesearch.model.movie.list.MovieListState
import ru.dvn.moviesearch.model.movie.MovieAdapter
import ru.dvn.moviesearch.model.movie.list.MoviesLoadMode
import ru.dvn.moviesearch.viewmodel.MovieListViewModel

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var topBestMoviesAdapter: MovieAdapter
    private lateinit var topAwaitMoviesAdapter: MovieAdapter

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProvider(this).get(MovieListViewModel::class.java)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initTopBestMoviesBlock() {
        topBestMoviesAdapter = MovieAdapter(onMovieClickListener = onMovieClickListener)
        binding.topBestRecyclerView.apply {
            adapter = topBestMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.getTopBestLiveData().observe(viewLifecycleOwner) {
            renderTopBestData(it)
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

        viewModel.getTopAwaitLiveData().observe(viewLifecycleOwner) {
            renderTopAwait(it)
        }

        binding.topAwaitError.setOnClickListener {
            requestTopAwait()
        }

        requestTopAwait()
    }

    private fun requestTopBest() {
        viewModel.getMovieList(MoviesLoadMode.TOP_BEST_FILMS)
    }

    private fun requestTopAwait() {
        viewModel.getMovieList(MoviesLoadMode.TOP_AWAIT_FILMS)
    }

    private fun renderTopBestData(movieListState: MovieListState) {
        when (movieListState) {
            is MovieListState.Success -> {
                binding.topBestLoading.root.visibility = View.GONE
                binding.topBestMainLayout.visibility = View.VISIBLE
                movieListState.movies.films?.let {
                    topBestMoviesAdapter.setMovies(it)
                }
            }
            is MovieListState.Error -> {
                binding.topBestLoading.root.visibility = View.GONE
                binding.topBestError.visibility = View.VISIBLE
            }
            is MovieListState.Loading -> {
                binding.topBestLoading.root.visibility = View.VISIBLE
                binding.topBestError.visibility = View.GONE
                binding.topBestMainLayout.visibility = View.GONE
            }
        }
    }

    private fun renderTopAwait(movieListState: MovieListState) {
        when (movieListState) {
            is MovieListState.Success -> {
                binding.topAwaitLoading.root.visibility = View.GONE
                binding.topAwaitMainLayout.visibility = View.VISIBLE
                movieListState.movies.films?.let {
                    topAwaitMoviesAdapter.setMovies(it)
                }
            }
            is MovieListState.Error -> {
                binding.topAwaitLoading.root.visibility = View.GONE
                binding.topAwaitError.visibility = View.VISIBLE
            }
            is MovieListState.Loading -> {
                binding.topAwaitLoading.root.visibility = View.VISIBLE
                binding.topAwaitError.visibility = View.GONE
                binding.topAwaitMainLayout.visibility = View.GONE
            }
        }
    }

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Int)
    }

}