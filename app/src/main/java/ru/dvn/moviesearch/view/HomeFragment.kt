package ru.dvn.moviesearch.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHomeBinding
import ru.dvn.moviesearch.model.movie.MovieAdapter
import ru.dvn.moviesearch.model.movie.list.MovieList
import ru.dvn.moviesearch.model.movie.list.MovieListLoader
import ru.dvn.moviesearch.model.movie.list.MoviesLoadMode

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var topBestMoviesAdapter: MovieAdapter
    private lateinit var topAwaitMoviesAdapter: MovieAdapter

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

    private val topBestMoviesLoaderListener = object: MovieListLoader.MovieListLoaderListener {
        override fun onLoaded(movieList: MovieList) {
            movieList.films?.let {
                binding.topBestLoading.root.visibility = View.GONE
                topBestMoviesAdapter.setMovies(it)
            }
        }

        override fun onFailed(throwable: Throwable) {
            binding.topBestLoading.root.visibility = View.GONE
            binding.topBestError.visibility = View.VISIBLE
            Toast.makeText(context, "Can not load TOP_BEST movies", Toast.LENGTH_SHORT).show()
        }
    }
    private val topBestMoviesLoader = MovieListLoader(topBestMoviesLoaderListener, MoviesLoadMode.TOP_BEST_FILMS)

    private val topAwaitMoviesLoaderListener = object: MovieListLoader.MovieListLoaderListener {
        override fun onLoaded(movieList: MovieList) {
            movieList.films?.let {
                binding.topAwaitLoading.root.visibility = View.GONE
                topAwaitMoviesAdapter.setMovies(it)
            }
        }

        override fun onFailed(throwable: Throwable) {
            binding.topAwaitLoading.root.visibility = View.GONE
            binding.topAwaitError.visibility = View.VISIBLE
            Toast.makeText(context, "Can not load TOP_AWAIT movies", Toast.LENGTH_SHORT).show()
            Log.d("LOADER", throwable.message.toString())
        }
    }

    private val topAwaitMoviesLoader = MovieListLoader(topAwaitMoviesLoaderListener, MoviesLoadMode.TOP_AWAIT_FILMS)

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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initTopBestMoviesBlock() {
        topBestMoviesAdapter = MovieAdapter(onMovieClickListener = onMovieClickListener)
        binding.topBestRecyclerView.apply {
            adapter = topBestMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        topBestMoviesLoader.loadMovieList()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initTopAwaitMoviesBlock() {
        topAwaitMoviesAdapter = MovieAdapter(onMovieClickListener = onMovieClickListener)
        binding.topAwaitRecyclerView.apply {
            adapter = topAwaitMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        topAwaitMoviesLoader.loadMovieList()
    }

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Int)
    }

}