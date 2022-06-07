package ru.dvn.moviesearch.view

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHomeBinding
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.movie.list.MovieAdapter
import ru.dvn.moviesearch.model.movie.list.remote.MoviesLoadMode
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
        override fun onMovieClick(movieId: Long) {
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
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        (menu.findItem(R.id.item_search).actionView as SearchView).apply {
            queryHint = activity?.getString(R.string.search)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    topBestMoviesAdapter.filter.filter(newText)
                    topAwaitMoviesAdapter.filter.filter(newText)

                    return true
                }
            })
        }
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

    private fun renderTopBestData(appState: AppState) {
        when (appState) {
            is AppState.SuccessList -> {
                binding.topBestLoading.root.visibility = View.GONE
                binding.topBestMainLayout.visibility = View.VISIBLE
                appState.movies.films?.let {
                    topBestMoviesAdapter.setMovies(it)
                }
            }
            is AppState.Error -> {
                binding.topBestLoading.root.visibility = View.GONE
                binding.topBestError.visibility = View.VISIBLE
            }
            is AppState.Loading -> {
                binding.topBestLoading.root.visibility = View.VISIBLE
                binding.topBestError.visibility = View.GONE
                binding.topBestMainLayout.visibility = View.GONE
            }
        }
    }

    private fun renderTopAwait(appState: AppState) {
        when (appState) {
            is AppState.SuccessList -> {
                binding.topAwaitLoading.root.visibility = View.GONE
                binding.topAwaitMainLayout.visibility = View.VISIBLE
                appState.movies.films?.let {
                    topAwaitMoviesAdapter.setMovies(it)
                }
            }
            is AppState.Error -> {
                binding.topAwaitLoading.root.visibility = View.GONE
                binding.topAwaitError.visibility = View.VISIBLE
            }
            is AppState.Loading -> {
                binding.topAwaitLoading.root.visibility = View.VISIBLE
                binding.topAwaitError.visibility = View.GONE
                binding.topAwaitMainLayout.visibility = View.GONE
            }
        }
    }

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Long)
    }

}