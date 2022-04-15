package ru.dvn.moviesearch.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentContactsBinding
import ru.dvn.moviesearch.model.contacts.Contact
import ru.dvn.moviesearch.model.contacts.ContactAdapter
import java.util.ArrayList

private const val REQUEST_CONTACT_ACCESS = 101

class ContactsFragment : Fragment() {
    companion object {
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

    private var handlerThread: HandlerThread? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        handlerThread = HandlerThread("Contacts HT")
        handlerThread?.start()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContacts.adapter = adapter
        checkPermissions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handlerThread?.quitSafely()
        handlerThread = null
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

    @SuppressLint("Range")
    private fun getContacts() {
        context?.let { context ->
            val contentResolver = context.contentResolver

            handlerThread?.let { handlerThread ->
                Handler(handlerThread.looper).post {
                    val contactsCursor: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME
                    )

                    contactsCursor?.let { cursor ->
                        val contacts = ArrayList<Contact>()
                        for (i in 0..cursor.count) {
                            if (cursor.moveToPosition(i)) {
                                val name = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                                )
                                val phoneNumber = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                                contacts.add(
                                    Contact(name, phoneNumber)
                                )
                            }
                        }

                        adapter.setData(contacts)
                    }
                }
            }

        }
    }
}