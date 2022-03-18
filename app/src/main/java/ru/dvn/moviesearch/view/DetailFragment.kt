package ru.dvn.moviesearch.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentDetailBinding
import ru.dvn.moviesearch.model.movie.detail.DetailService
import ru.dvn.moviesearch.model.movie.detail.EXTRA_DETAIL_ID
import ru.dvn.moviesearch.model.movie.detail.GenreDto
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto
import java.lang.StringBuilder

const val EXTRA_DETAIL_STATUS = "EXTRA_DETAIL_STATUS"
const val EXTRA_DETAIL_STATUS_SUCCESS = "EXTRA_DETAIL_STATUS_SUCCESS"
const val EXTRA_DETAIL_STATUS_ERROR = "EXTRA_DETAIL_STATUS_ERROR"

const val EXTRA_DETAIL = "EXTRA_DETAIL"

const val EXTRA_DETAIL_ACTION = "EXTRA_DETAIL_ACTION"

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

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.getStringExtra(EXTRA_DETAIL_STATUS)) {
                EXTRA_DETAIL_STATUS_SUCCESS -> {
                    intent.getParcelableExtra<MovieDetailDto>(EXTRA_DETAIL)?.let {
                        showMovie(it)
                    }
                }

                EXTRA_DETAIL_STATUS_ERROR -> {
                    activity?.supportFragmentManager?.popBackStack()
                    Toast.makeText(context, R.string.detail_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(receiver, IntentFilter(EXTRA_DETAIL_ACTION)
            )
        }
        super.onCreate(savedInstanceState)
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

        context?.let { context->
            arguments?.getInt(EXTRA_MOVIE_ID)?.let { id ->
                context.startService(
                    Intent(context, DetailService::class.java).apply {
                        putExtra(EXTRA_DETAIL_ID, id)
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(receiver)
        }
        super.onDestroy()
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