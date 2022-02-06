package ru.dvn.moviesearch.model.movie.upcoming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R

class MovieUpcomingAdapter : RecyclerView.Adapter<MovieUpcomingAdapter.ViewHolder>() {
    private var upcomingMovieList: List<UpcomingMovie> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_upcoming_item, parent, false)
        return ViewHolder(itemView = view)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: UpcomingMovie) {
            val imgFavorite = itemView.findViewById<ImageView>(R.id.item_favorite)
            imgFavorite.setImageResource(
                if (movie.isFavorite) {
                    R.drawable.ic_favorite_for_user
                } else {
                    R.drawable.ic_favorite_border
                }
            )

            imgFavorite.setOnClickListener {
                val imageView = it as ImageView
                if (!movie.isFavorite) {
                    imageView.setImageResource(R.drawable.ic_favorite_for_user)
                    movie.isFavorite = true
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border)
                    movie.isFavorite = false
                }
            }

            val tvName = itemView.findViewById<TextView>(R.id.item_name)
            tvName.text = movie.name

            val tvDate = itemView.findViewById<TextView>(R.id.item_date)
            tvDate.text = movie.date
        }
    }

}