package ru.dvn.moviesearch.model.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.MovieNowPlayingItemBinding
import ru.dvn.moviesearch.databinding.MovieUpcomingItemBinding
import ru.dvn.moviesearch.view.HomeFragment

class MovieAdapter(
    private val mode: AdapterMode,
    private var onMovieClickListener: HomeFragment.OnMovieClickListener?
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var movies: List<Movie> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = if (mode == AdapterMode.MODE_NOW_PLAYING) {
            MovieNowPlayingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            MovieUpcomingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }

        return MovieViewHolder(binding = binding, onMovieClickListener = onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setMovies(movieList: List<Movie>) {
        movies = movieList
        notifyDataSetChanged()
    }

    fun deleteMovieClickListener() {
        onMovieClickListener = null
    }

    class MovieViewHolder(
        val binding: ViewBinding,
        val onMovieClickListener: HomeFragment.OnMovieClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.root.setOnClickListener {
                onMovieClickListener?.onMovieClick(movie = movie)
            }

            when (binding) {
                is MovieNowPlayingItemBinding -> {
                    with(movie) {
                        binding.itemName.text = name
                        binding.itemYear.text = year.toString()
                        binding.itemRating.text = rating.toString()
                        binding.itemFavorite.setImageResource(
                            if (isFavorite) {
                                R.drawable.ic_favorite_for_user
                            } else {
                                R.drawable.ic_favorite_border
                            }
                        )

                        binding.itemFavorite.setOnClickListener {
                            val imageView = it as ImageView
                            imageView.setImageResource(
                                if (isFavorite) {
                                   R.drawable.ic_favorite_border
                                } else {
                                    R.drawable.ic_favorite_for_user
                                }
                            )
                            isFavorite = !isFavorite
                        }
                    }

                }
                is MovieUpcomingItemBinding -> {
                    with(movie) {
                        binding.itemName.text = name
                        binding.itemDate.text = releaseDate
                    }
                }
            }
        }
    }
}