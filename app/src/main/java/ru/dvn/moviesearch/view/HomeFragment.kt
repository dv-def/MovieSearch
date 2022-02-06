package ru.dvn.moviesearch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHomeBinding
import ru.dvn.moviesearch.model.movie.nowplaying.NowPlayingAppState
import ru.dvn.moviesearch.model.movie.nowplaying.MovieNowPlayingAdapter
import ru.dvn.moviesearch.model.movie.upcoming.MovieUpcomingAdapter
import ru.dvn.moviesearch.model.movie.upcoming.UpcomingAppState
import ru.dvn.moviesearch.viewmodel.MovieViewModel

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MovieViewModel

    private lateinit var nowPlayingAdapter: MovieNowPlayingAdapter
    private lateinit var upcomingAdapter: MovieUpcomingAdapter

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

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        initNowPlayingBlock()
        initUpcomingBlock()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initNowPlayingBlock() {
        nowPlayingAdapter = MovieNowPlayingAdapter()

        val nowPlayingRecycler = binding.nowPlayingRecyclerView
        nowPlayingRecycler.adapter = nowPlayingAdapter
        nowPlayingRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val observer = Observer<NowPlayingAppState> {
            renderNowPlaying(it)
        }

        viewModel.getNowPlayingLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getNowPlayingMoviesFromLocalStorage()

    }

    private fun renderNowPlaying(data: NowPlayingAppState) {
        when(data) {
            is NowPlayingAppState.Success -> {
                binding.nowPlayingLoading.visibility = View.GONE
                nowPlayingAdapter.setMovies(data.nowPlayingMovieList)
            }
            is NowPlayingAppState.Error -> {
                binding.nowPlayingLoading.visibility = View.GONE
                Snackbar.make(binding.main, String.format(getString(R.string.formatted_error), data.error), Snackbar.LENGTH_INDEFINITE).show()
            }
            is NowPlayingAppState.Loading -> {
                binding.nowPlayingLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun initUpcomingBlock() {
        upcomingAdapter = MovieUpcomingAdapter()

        val upcomingRecyclerView = binding.upcomingRecyclerView
        upcomingRecyclerView.adapter = upcomingAdapter
        upcomingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val observer = Observer<UpcomingAppState> {
            renderUpcomingData(it)
        }

        viewModel.getUpcomingLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getUpcomingFromLocalStorage()
    }

    private fun renderUpcomingData(data: UpcomingAppState) {
        when(data) {
            is UpcomingAppState.Success -> {
                binding.upcomingLoading.visibility = View.GONE
                upcomingAdapter.setMovies(data.movieList)
            }
            is UpcomingAppState.Error -> {
                binding.upcomingLoading.visibility = View.GONE
                Snackbar.make(binding.main, String.format(getString(R.string.formatted_error), data.error), Snackbar.LENGTH_INDEFINITE).show()
            }
            is UpcomingAppState.Loading -> {
                binding.upcomingLoading.visibility = View.VISIBLE
            }
        }
    }
}