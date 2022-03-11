package ru.dvn.moviesearch.view

import android.os.Build
import android.os.Bundle
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
import ru.dvn.moviesearch.model.movie.MovieList
import ru.dvn.moviesearch.model.movie.MovieListLoader
import ru.dvn.moviesearch.model.movie.MoviesLoadMode

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var topMoviesAdapter: MovieAdapter
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

    private val topMoviesLoaderListener = object: MovieListLoader.MovieListLoaderListener {
        override fun onLoaded(movieList: MovieList) {
            movieList.films?.let {
                binding.topBestLoading.root.visibility = View.GONE
                topMoviesAdapter.setMovies(it)
            }
        }

        override fun onFailed(throwable: Throwable) {
            binding.topBestLoading.root.visibility = View.GONE
            binding.topBestError.visibility = View.VISIBLE
            Toast.makeText(context, "Can not load TOP_BEST movies", Toast.LENGTH_SHORT).show()
        }
    }
    private val topBestMoviesLoader = MovieListLoader(topMoviesLoaderListener, MoviesLoadMode.TOP_BEST_FILMS)

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        topMoviesAdapter.deleteMovieClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initTopBestMoviesBlock() {
        topMoviesAdapter = MovieAdapter(onMovieClickListener = onMovieClickListener)
        binding.topBestRecyclerView.apply {
            adapter = topMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        topBestMoviesLoader.loadMovieList()

    }

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Int)
    }

}