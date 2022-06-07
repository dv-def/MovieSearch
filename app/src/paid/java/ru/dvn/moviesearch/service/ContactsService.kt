package ru.dvn.moviesearch.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.database.Cursor
import android.os.IBinder
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.dvn.moviesearch.model.contacts.Contact
import ru.dvn.moviesearch.view.contacts.ContactsFragment
import java.lang.Exception
import java.util.ArrayList

class ContactsService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        getContacts()
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("Range")
    private fun getContacts() {
        Thread {
            try {
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
                    sendBroadcastSuccess(contacts)
                }
            } catch (e: Exception) {
                sendBroadcastError()
            } finally {
                stopSelf()
            }
        }.start()
    }

    private fun sendBroadcastSuccess(contacts: ArrayList<Contact>) {
        val intent = Intent(ContactsFragment.BROADCAST_INTENT_ACTION).apply {
            putExtra(ContactsFragment.EXTRA_STATE_KEY, ContactsFragment.STATE_SUCCESS)
            putParcelableArrayListExtra(ContactsFragment.EXTRA_CONTACTS_KEY, contacts)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun sendBroadcastError() {
        val intent = Intent().apply {
            putExtra(ContactsFragment.EXTRA_STATE_KEY, ContactsFragment.STATE_ERROR)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}