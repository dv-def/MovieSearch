package ru.dvn.moviesearch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentDetailBinding
import ru.dvn.moviesearch.model.movie.Movie

class DetailFragment : Fragment() {
    companion object {
        fun newInstance(movie: Movie): DetailFragment {
            val fragment = DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Movie::class.java.simpleName, movie)
                }
            }

            return fragment
        }
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Movie>(Movie::class.java.simpleName)?.let {
            binding.name.text = it.name
            binding.genre.text = it.genre

            binding.favorite.setImageResource(
                if (it.isFavorite) {
                    R.drawable.ic_favorite_for_user
                } else {
                    R.drawable.ic_favorite_border
                }
            )

            val length = "${getString(R.string.movie_length)} ${it.filmLength} min"
            binding.length.text = length

            val rating = "${getString(R.string.rating)} ${it.rating}"
            binding.rating.text = rating

            val budget = "${getString(R.string.budget)} ${it.budget}"
            binding.budget.text = budget

            val revenue = "${getString(R.string.revenue)} ${it.revenue}"
            binding.revenue.text = revenue

            val releaseDate = "${getString(R.string.release_date)} ${it.releaseDate}"
            binding.releaseDate.text = releaseDate

            binding.description.text = it.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}