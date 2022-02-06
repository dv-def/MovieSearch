package ru.dvn.moviesearch.model.movie.nowplaying

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R

class MovieNowPlayingAdapter : RecyclerView.Adapter<MovieNowPlayingAdapter.ViewHolder>() {
    private var nowPlayingMovieList: List<NowPlayingMovie> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_now_playing_item, parent, false)
        return ViewHolder(itemView = view)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(nowPlayingMovie: NowPlayingMovie) {
            val tvName = itemView.findViewById<TextView>(R.id.item_name)
            tvName.text = nowPlayingMovie.name

            val tvYear = itemView.findViewById<TextView>(R.id.item_year)
            tvYear.text = nowPlayingMovie.year

            val tvRating = itemView.findViewById<TextView>(R.id.item_rating)
            tvRating.text = nowPlayingMovie.rating.toString()

            val imgFavorite = itemView.findViewById<ImageView>(R.id.item_favorite)
            imgFavorite.setImageResource(
                if (nowPlayingMovie.isFavorite) {
                    R.drawable.ic_favorite_for_user
                } else {
                    R.drawable.ic_favorite_border
                }
            )

            imgFavorite.setOnClickListener {
                val imageView = it as ImageView
                if (!nowPlayingMovie.isFavorite) {
                    imageView.setImageResource(R.drawable.ic_favorite_for_user)
                    nowPlayingMovie.isFavorite = true
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border)
                    nowPlayingMovie.isFavorite = false
                }
            }
        }
    }
}