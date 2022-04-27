package ru.dvn.moviesearch.view.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentMovieListBinding
import ru.dvn.moviesearch.model.movie.MovieListDataState
import ru.dvn.moviesearch.model.movie.list.MovieAdapter
import ru.dvn.moviesearch.model.movie.list.remote.TopParam
import ru.dvn.moviesearch.viewmodel.MovieListViewModel

private const val EXTRA_TITLE = "EXTRA_TITLE"
private const val EXTRA_TOP_PARAM = "EXTRA_TOP_PARAM"

class MovieListFragment : Fragment() {
    companion object {
        fun newInstance(title: String?, topParam: String): MovieListFragment {
            val args = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_TOP_PARAM, topParam)
            }

            return MovieListFragment().apply {
                arguments = args
            }
        }
    }

    interface OnMovieClickListener {
        fun onMovieClick(filmId: Long)
    }

    private val onMovieClickListener = object : OnMovieClickListener {
        override fun onMovieClick(filmId: Long) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.fragment_host, DetailFragment.newInstance(movieId = filmId))
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
    }

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val movieAdapter = MovieAdapter(onMovieClickListener)
    private val viewModel: MovieListViewModel by lazy {
        ViewModelProvider(this).get(MovieListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.search.queryHint = arguments?.getString(EXTRA_TITLE)
        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieAdapter.filter.filter(newText)
                return true
            }
        })

        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        val topParam = if (arguments?.getString(EXTRA_TOP_PARAM) == TopParam.TOP_AWAIT_FILMS.paramName) {
            TopParam.TOP_AWAIT_FILMS
        } else {
            TopParam.TOP_BEST_FILMS
        }

        viewModel.getLiveData(topParam).observe(viewLifecycleOwner) {
            renderData(it, topParam)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        movieAdapter.deleteMovieClickListener()
        _binding = null
    }

    private fun renderData(dataState: MovieListDataState, topParam: TopParam) {
        when(dataState) {
            is MovieListDataState.Loading -> {
                binding.mainLayout.visibility = View.GONE
                binding.loading.root.visibility = View.VISIBLE
            }
            is MovieListDataState.Error -> {
                binding.loading.root.visibility = View.GONE
                binding.mainLayout.visibility = View.GONE

                dataState.error.message?.let {message ->
                    Snackbar.make(
                        binding.root,
                        message,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(R.string.try_again) {
                        viewModel.getMovieList(topParam)
                    }.show()
                }
            }
            is MovieListDataState.Success -> {
                binding.loading.root.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE

                dataState.movies.films?.let { movieAdapter.setMovies(it) }
            }
        }
    }
}
