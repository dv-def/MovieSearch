package ru.dvn.moviesearch.view.contacts

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentContactsBinding
import ru.dvn.moviesearch.model.contacts.Contact
import ru.dvn.moviesearch.model.contacts.ContactAdapter
import ru.dvn.moviesearch.service.ContactsService
import java.util.ArrayList

class ContactsFragment : Fragment() {
    companion object {
        private const val REQUEST_CONTACT_ACCESS = 101

        const val EXTRA_CONTACTS_KEY = "EXTRA_CONTACTS_KEY"
        const val BROADCAST_INTENT_ACTION = "ru.dvn.moviesearch.view.contacts.GET_CONTACTS"

        const val EXTRA_STATE_KEY = "EXTRA_STATE_KEY"
        const val STATE_SUCCESS = "success"
        const val STATE_ERROR = "error"

        fun newInstance() = ContactsFragment()
    }

    interface OnItemClickListener {
        fun doCall(phoneNumber: String)
    }

    private val onItemClickListener = object : OnItemClickListener {
        override fun doCall(phoneNumber: String) {
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle(it.getString(R.string.agree_call))
                    .setMessage("${it.getString(R.string.will_call_to_number)}$phoneNumber")
                    .setNegativeButton(it.getString(R.string.cancel)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(it.getString(R.string.do_call)) { dialog, _ ->
                        dialog.dismiss()
                        startActivity(
                            Intent(Intent.ACTION_CALL).apply {
                                data = Uri.parse("tel:$phoneNumber")
                            }
                        )
                    }
                    .create()
                    .show()
            }
        }
    }

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val adapter: ContactAdapter by lazy {
        ContactAdapter(onItemClickListener)
    }

    private val contactsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.let { extras ->
                val state = extras.getString(EXTRA_STATE_KEY, STATE_ERROR)
                if (state == STATE_SUCCESS) {
                    val contacts =
                        extras.getParcelableArrayList<Contact>(EXTRA_CONTACTS_KEY) as ArrayList<Contact>

                    if (contacts.isNotEmpty()) {
                        binding.loadingLayout.root.visibility = View.GONE
                        binding.rvContacts.visibility = View.VISIBLE

                        adapter.setData(contacts)
                    } else {
                        Toast.makeText(context, R.string.contacts_error, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, R.string.contacts_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(contactsReceiver, IntentFilter(BROADCAST_INTENT_ACTION))

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContacts.adapter = adapter
        checkPermissions()
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(contactsReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.item_contacts).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CONTACT_ACCESS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                } else {
                    activity?.let {
                        AlertDialog.Builder(it)
                            .setTitle(it.getString(R.string.permission_needed))
                            .setMessage(it.getString(R.string.contacts_fragment_will_be_close))
                            .setNegativeButton(it.getString(R.string.close)) { dialog, _ ->
                                dialog.cancel()
                                it.supportFragmentManager.popBackStack()
                            }
                            .create()
                            .show()
                    }
                }
            }
        }
    }

    private fun checkPermissions() {
        context?.let {
            when {
                ContextCompat
                    .checkSelfPermission(it, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle(it.getString(R.string.permission_needed))
                        .setMessage(it.getString(R.string.contacts_needed_explanation))
                        .setNegativeButton(it.getString(R.string.cancel)) { dialog, _ ->
                            dialog.cancel()
                        }
                        .setPositiveButton(it.getString(R.string.get_access)) { dialog, _ ->
                            dialog.dismiss()
                            this.requestPermissions()
                        }
                        .create()
                        .show()
                }

                else -> {
                    this.requestPermissions()
                }
            }
        }
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
            ), REQUEST_CONTACT_ACCESS
        )
    }

    private fun getContacts() {
        activity?.let {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.rvContacts.visibility = View.GONE
            it.startService(Intent(it, ContactsService::class.java))
        }
        
        
    }
}