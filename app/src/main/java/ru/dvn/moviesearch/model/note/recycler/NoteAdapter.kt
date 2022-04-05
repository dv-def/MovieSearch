package ru.dvn.moviesearch.model.note.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.model.note.local.NoteEntity

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private val notes: ArrayList<NoteEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bindNote(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateNotes(newNotes: List<NoteEntity>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    fun deleteNote(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun getNote(position: Int): NoteEntity {
        return notes[position]
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindNote(note: NoteEntity) {
            with(itemView) {
                findViewById<TextView>(R.id.tv_note_item_title).text = note.title
                findViewById<TextView>(R.id.tv_note_item_text).text = note.text
                findViewById<TextView>(R.id.tv_note_item_created_at).text = note.date
            }
        }
    }
}