package ru.dvn.moviesearch.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHomeBinding
import ru.dvn.moviesearch.model.movie.AdapterMode
import ru.dvn.moviesearch.model.movie.AppState
import ru.dvn.moviesearch.model.movie.Movie
import ru.dvn.moviesearch.model.movie.MovieAdapter
import ru.dvn.moviesearch.viewmodel.MovieViewModel

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }

    private val nowPlayingAdapter: MovieAdapter by lazy {
        MovieAdapter(
            mode = AdapterMode.MODE_NOW_PLAYING,
            onMovieClickListener = onMovieClickListener
        )
    }
    private val upcomingAdapter: MovieAdapter by lazy {
        MovieAdapter(
            mode = AdapterMode.MODE_UPCOMING,
            onMovieClickListener = onMovieClickListener
        )
    }

    private lateinit var onMovieClickListener: OnMovieClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMovieClickListener) {
            onMovieClickListener = context
        }

        onMovieClickListener = when (context) {
            is OnMovieClickListener -> context
            else -> object : OnMovieClickListener {
                override fun onMovieClick(movie: Movie) {
                    Toast.makeText(context, R.string.can_not_open_movie, Toast.LENGTH_SHORT).show()
                }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNowPlayingBlock()
        initUpcomingBlock()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        nowPlayingAdapter.deleteMovieClickListener()
        upcomingAdapter.deleteMovieClickListener()
    }

    private fun initNowPlayingBlock() {
        binding.nowPlayingError.setOnClickListener {
            viewModel.getNowPlayingMoviesFromLocalStorage()
        }

        binding.nowPlayingRecyclerView.apply {
            adapter = nowPlayingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        val observer = Observer<AppState> {
            renderNowPlaying(it)
        }

        viewModel.getNowPlayingLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getNowPlayingMoviesFromLocalStorage()

    }

    private fun renderNowPlaying(data: AppState) {
        when (data) {
            is AppState.Success -> {
                binding.nowPlayingLoading.root.visibility = View.GONE
                binding.nowPlayingError.visibility = View.GONE
                nowPlayingAdapter.setMovies(data.movies)
            }
            is AppState.Error -> {
                binding.nowPlayingLoading.root.visibility = View.GONE
                binding.nowPlayingError.visibility = View.VISIBLE
            }
            is AppState.Loading -> {
                binding.nowPlayingLoading.root.visibility = View.VISIBLE
                binding.nowPlayingError.visibility = View.GONE
            }
        }
    }

    private fun initUpcomingBlock() {
        binding.upcomingError.setOnClickListener {
            viewModel.getUpcomingFromLocalStorage()
        }

        binding.upcomingRecyclerView.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        val observer = Observer<AppState> {
            renderUpcomingData(it)
        }

        viewModel.getUpcomingLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getUpcomingFromLocalStorage()
    }

    private fun renderUpcomingData(data: AppState) {
        when (data) {
            is AppState.Success -> {
                binding.upcomingLoading.root.visibility = View.GONE
                binding.upcomingError.visibility = View.GONE
                upcomingAdapter.setMovies(data.movies)
            }
            is AppState.Error -> {
                binding.upcomingLoading.root.visibility = View.GONE
                binding.upcomingError.visibility = View.VISIBLE
            }
            is AppState.Loading -> {
                binding.upcomingLoading.root.visibility = View.VISIBLE
                binding.upcomingError.visibility = View.GONE
            }
        }
    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }
}