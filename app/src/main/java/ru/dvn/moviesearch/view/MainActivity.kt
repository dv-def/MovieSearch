package ru.dvn.moviesearch.view

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.ActivityMainBinding
import ru.dvn.moviesearch.model.ConnectivityReceiver

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var receiver = ConnectivityReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            createHomeFragment()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_item_home -> {
                    createHomeFragment()
                    true
                }
                R.id.bottom_item_favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragmentHost.id, FavoritesFragment.newInstance())
                        .commit()
                    true
                }
                else -> {
                    false
                }
            }
        }

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun createHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentHost.id, HomeFragment.newInstance())
            .commit()
    }
}