package ru.dvn.moviesearch.model.staff.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.view.staff.StaffDetailsFragment

class StaffFilmListAdapter(
    private val onFilmClickListener: StaffDetailsFragment.OnPersonFilmClickListener
) : RecyclerView.Adapter<StaffFilmListAdapter.StaffFilmsViewHolder>() {
    private val filmsList: ArrayList<PersonFilm> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffFilmsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_film_item, parent, false)

        return StaffFilmsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffFilmsViewHolder, position: Int) {
        holder.bind(filmsList[position])
    }

    override fun getItemCount(): Int {
        return filmsList.size
    }

    fun setData(newFilms: List<PersonFilm>) {
        filmsList.clear()
        filmsList.addAll(newFilms)
        notifyDataSetChanged()
    }

    inner class StaffFilmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(film: PersonFilm) {
            with(itemView) {
                val tvName = findViewById<TextView>(R.id.item_person_film_name)
                if (!film.nameRu.isNullOrEmpty()) {
                    tvName.text = film.nameRu
                } else if (!film.nameEn.isNullOrEmpty()) {
                    tvName.text = film.nameEn
                } else {
                    tvName.visibility = View.GONE
                }

                val tvProfession = findViewById<TextView>(R.id.item_person_film_profession)
                film.professionKey?.let {
                    tvProfession.text = it.profession
                } ?: run { tvProfession.visibility = View.GONE }

                val tvRating = findViewById<TextView>(R.id.item_person_film_rating)
                film.rating?.let {
                    val text = "${context.getString(R.string.rating)} $it"
                    tvRating.text = text
                } ?: run { tvRating.visibility = View.GONE }

                film.filmId?.let { id ->
                    setOnClickListener {
                        onFilmClickListener.onClickFilm(id)
                    }

                    findViewById<ImageButton>(R.id.btn_arrow).setOnClickListener {
                        onFilmClickListener.onClickFilm(id)
                    }
                }
            }
        }
    }
}