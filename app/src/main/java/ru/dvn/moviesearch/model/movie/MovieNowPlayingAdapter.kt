package ru.dvn.moviesearch.model.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R

class MovieNowPlayingAdapter : RecyclerView.Adapter<MovieNowPlayingAdapter.ViewHolder>() {
    private var movieList: List<Movie> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_now_playing_item, parent, false)
        return ViewHolder(itemView = view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList?.size
    }

    fun setMovies(movieList: List<Movie>) {
        this.movieList = movieList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            val tvName = itemView.findViewById<TextView>(R.id.item_name)
            tvName.text = movie.name

            val tvYear = itemView.findViewById<TextView>(R.id.item_year)
            tvYear.text = movie.year

            val tvRating = itemView.findViewById<TextView>(R.id.item_rating)
            tvRating.text = movie.rating.toString()
        }
    }
}