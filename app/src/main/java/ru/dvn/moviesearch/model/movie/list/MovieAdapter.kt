package ru.dvn.moviesearch.model.movie.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.MovieItemBinding
import ru.dvn.moviesearch.model.movie.list.remote.FilmDTO
import ru.dvn.moviesearch.view.movies.HomeFragment
import java.util.ArrayList

class MovieAdapter(
    private var onMovieClickListener: HomeFragment.OnMovieClickListener?
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable {
    private var movies: ArrayList<FilmDTO> = ArrayList()
    private var moviesFull: ArrayList<FilmDTO> = ArrayList()

    private val filter = object : Filter() {
        override fun performFiltering(text: CharSequence?): FilterResults {
            val result = ArrayList<FilmDTO>()
            if (text.isNullOrEmpty()) {
                result.addAll(moviesFull)
            } else {
                val search = text.toString().lowercase().trim()
                result.addAll(moviesFull.filter {
                    it.nameRu?.contains(search) == true
                })
            }

            return FilterResults().apply {
                values = result
            }
        }

        override fun publishResults(text: CharSequence?, result: FilterResults?) {
            movies.clear()
            movies.addAll(result?.values as ArrayList<FilmDTO>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieViewHolder(binding = binding, onMovieClickListener = onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setMovies(movieList: List<FilmDTO>) {
        movies.clear()
        movies.addAll(movieList)
        moviesFull.addAll(movieList)
        notifyDataSetChanged()
    }

    fun deleteMovieClickListener() {
        onMovieClickListener = null
    }

    class MovieViewHolder(
        val binding: MovieItemBinding,
        val onMovieClickListener: HomeFragment.OnMovieClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(filmDTO: FilmDTO) {
            with(binding) {
                itemName.text = filmDTO.nameRu
                itemYear.text = filmDTO.year.toString()
                itemRating.text = filmDTO.rating

                Picasso.get()
                    .load(filmDTO.posterUrlPreview)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.default_poster)
                    .into(binding.itemPoster)

                root.setOnClickListener {
                    filmDTO.filmId?.let { filmId ->
                        onMovieClickListener?.onMovieClick(filmId)
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return filter
    }
}