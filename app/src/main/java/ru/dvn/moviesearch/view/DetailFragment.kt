package ru.dvn.moviesearch.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        Log.d(
            "TAG",
            "Detail: ${this.id}"
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}