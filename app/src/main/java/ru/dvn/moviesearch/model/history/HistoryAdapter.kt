package ru.dvn.moviesearch.model.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.dvn.moviesearch.R

class HistoryAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val history: ArrayList<HistoryEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(history[position])
    }

    override fun getItemCount(): Int {
        return history.size
    }

    fun setHistory(newHistory: List<HistoryEntity>) {
        history.clear()
        history.addAll(newHistory)
        for (index in history.indices) {
            notifyItemInserted(index)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(kinopoiskId: Long)
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(historyEntity: HistoryEntity) {
            with(itemView) {
                val poster = findViewById<ImageView>(R.id.iv_movie_poster)
                Picasso.get()
                    .load(historyEntity.moviePoster)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.default_poster)
                    .into(poster)

                findViewById<TextView>(R.id.tv_history_movie_name).text = historyEntity.movieName
                findViewById<TextView>(R.id.tv_history_date).text = historyEntity.date

                setOnClickListener {
                    onItemClickListener.onItemClick(historyEntity.kinopoiskFilmId!!)
                }
            }
        }
    }

}