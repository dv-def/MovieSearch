package ru.dvn.moviesearch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHomeBinding
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.movie.MovieNowPlayingAdapter
import ru.dvn.moviesearch.viewmodel.MovieViewModel

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MovieViewModel

    private lateinit var nowPlayingAdapter: MovieNowPlayingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nowPlayingRecycler = binding.nowPlayingRecyclerView
        nowPlayingAdapter = MovieNowPlayingAdapter()
        nowPlayingRecycler.adapter = nowPlayingAdapter
        nowPlayingRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val observer = Observer<AppState> {
            renderData(it)
        }

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getMoviesFromLocalStorage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(data: AppState) {
        when(data) {
            is AppState.Success -> {
                nowPlayingAdapter.setMovies(data.movieList)
            }
            is AppState.Error -> {
                Snackbar.make(binding.main, String.format(getString(R.string.formatted_error), data.error), Snackbar.LENGTH_INDEFINITE).show()
            }
            is AppState.Loading -> {
                Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
            }
        }
    }
}