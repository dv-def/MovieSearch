package ru.dvn.moviesearch.model.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.view.ContactsFragment
import java.util.ArrayList

class ContactAdapter(
    private val listener: ContactsFragment.OnItemClickListener
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val contacts = ArrayList<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun setData(newContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(newContacts)
        notifyDataSetChanged()
    }

    inner class ContactViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            with(contact) {
                itemView.findViewById<TextView>(R.id.contact_item_name).text = name
                itemView.findViewById<TextView>(R.id.contact_item_number).text = phoneNumber

                itemView.setOnClickListener {
                    listener.doCall(phoneNumber)
                }
            }


        }
    }
}
