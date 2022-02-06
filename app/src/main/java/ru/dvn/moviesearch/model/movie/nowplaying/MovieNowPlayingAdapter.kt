package ru.dvn.moviesearch.model.movie.nowplaying

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.MovieNowPlayingItemBinding

class MovieNowPlayingAdapter : RecyclerView.Adapter<MovieNowPlayingAdapter.ViewHolder>() {
    private var nowPlayingMovieList: List<NowPlayingMovie> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieNowPlayingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nowPlayingMovieList[position])
    }

    override fun getItemCount(): Int {
        return nowPlayingMovieList.size
    }

    fun setMovies(nowPlayingMovieList: List<NowPlayingMovie>) {
        this.nowPlayingMovieList = nowPlayingMovieList
    }

    class ViewHolder(private val binding: MovieNowPlayingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: NowPlayingMovie) {
            binding.itemName.text = movie.name
            binding.itemYear.text = movie.year
            binding.itemRating.text = movie.rating.toString()

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
        }
    }
}