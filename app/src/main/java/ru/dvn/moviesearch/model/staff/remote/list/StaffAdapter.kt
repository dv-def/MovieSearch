package ru.dvn.moviesearch.model.staff.remote.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.view.staff.StaffFragment

class StaffAdapter(
    private val onPersonClickListener: StaffFragment.OnPersonClickListener
) : RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {
    private val staff: ArrayList<StaffDto> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.staff_item, parent, false)

        return StaffViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        holder.bind(staff[position])
    }

    override fun getItemCount(): Int {
        return staff.size
    }

    fun setData(newStaff: List<StaffDto>) {
        staff.clear()
        staff.addAll(newStaff)
        notifyDataSetChanged()
    }

    inner class StaffViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(staff: StaffDto) {
            with(itemView) {
                findViewById<TextView>(R.id.tv_item_staff_name).text = staff.nameRu
                findViewById<TextView>(R.id.tv_item_staff_profession)
                    .text = staff.professionKey?.profession

                staff.posterUrl?.let { posterUrl ->
                    Picasso.get()
                        .load(posterUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.default_poster)
                        .into(findViewById<ImageView>(R.id.iv_item_staff))
                }

                setOnClickListener {
                    if (staff.staffId != null && staff.staffId != 0L) {
                        onPersonClickListener.onClick(staff.staffId)
                    }
                }
            }
        }
    }

}