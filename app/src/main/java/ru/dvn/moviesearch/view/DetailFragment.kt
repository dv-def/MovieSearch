package ru.dvn.moviesearch.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentDetailBinding
import ru.dvn.moviesearch.model.movie.detail.GenreDto
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.detail.MovieDetailLoader
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

    private val loaderListener = object: MovieDetailLoader.MovieDetailsLoaderListener {
        override fun onLoaded(movieDetailDto: MovieDetailDto) {
            binding.detailsLoadingLayout.visibility = View.GONE
            binding.detailsMainLayout.visibility = View.VISIBLE
            showMovie(movieDetailDto)
        }

        override fun onFailed(throwable: Throwable) {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private val loader = MovieDetailLoader(listener = loaderListener)

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
        arguments?.getInt(EXTRA_MOVIE_ID)?.let {
            loader.loadMovie(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMovie(movieDetailDto: MovieDetailDto) {
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