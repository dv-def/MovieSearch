package ru.dvn.moviesearch.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentDetailBinding
import ru.dvn.moviesearch.model.movie.detail.DetailsState
import ru.dvn.moviesearch.model.movie.detail.GenreDto
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto
import ru.dvn.moviesearch.viewmodel.DetailsViewModel
import java.lang.StringBuilder

class DetailFragment : Fragment() {
    companion object {
        const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newInstance(movieId: Int): DetailFragment {
            val fragment = DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_MOVIE_ID, movieId)
                }
            }

            return fragment
        }
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        requestData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(detailsState: DetailsState) {
        when(detailsState) {
            is DetailsState.Success -> {
                binding.detailsLoadingLayout.visibility = View.GONE
                binding.detailsMainLayout.visibility = View.VISIBLE
                showMovie(detailsState.movie)
            }
            is DetailsState.Error -> {
                binding.detailsLoadingLayout.visibility = View.GONE
                binding.detailsMainLayout.visibility = View.GONE
                Snackbar
                    .make(binding.detailsMainLayout, R.string.detail_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again) {
                        requestData()
                    }
                    .show()
            }
            is DetailsState.Loading -> {
                binding.detailsLoadingLayout.visibility = View.VISIBLE
                binding.detailsMainLayout.visibility = View.GONE
            }
        }
    }

    private fun requestData() {
        arguments?.getInt(EXTRA_MOVIE_ID)?.let {
            viewModel.getDetails(it)
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