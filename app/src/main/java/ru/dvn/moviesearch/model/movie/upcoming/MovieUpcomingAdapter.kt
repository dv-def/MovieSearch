package ru.dvn.moviesearch.model.movie.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.MovieUpcomingItemBinding

class MovieUpcomingAdapter : RecyclerView.Adapter<MovieUpcomingAdapter.ViewHolder>() {
    private var upcomingMovieList: List<UpcomingMovie> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieUpcomingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(upcomingMovieList[position])
    }

    override fun getItemCount(): Int {
        return upcomingMovieList.size
    }

    fun setMovies(movieList: List<UpcomingMovie>) {
        upcomingMovieList = movieList
    }

    class ViewHolder(private val binding: MovieUpcomingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: UpcomingMovie) {
            binding.itemFavorite.setImageResource(
                if (movie.isFavorite) {
                    R.drawable.ic_favorite_for_user
                } else {
                    R.drawable.ic_favorite_border
                }
            )

            binding.itemFavorite.setOnClickListener {
                val imageView = it as ImageView
                if (!movie.isFavorite) {
                    imageView.setImageResource(R.drawable.ic_favorite_for_user)
                    movie.isFavorite = true
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border)
                    movie.isFavorite = false
                }
            }

            binding.itemName.text = movie.name
            binding.itemDate.text = movie.date
        }
    }

}