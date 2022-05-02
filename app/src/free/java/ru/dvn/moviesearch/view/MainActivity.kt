package ru.dvn.moviesearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.ActivityMainBinding
import ru.dvn.moviesearch.view.history.HistoryFragment
import ru.dvn.moviesearch.view.movies.HomeFragment
import ru.dvn.moviesearch.view.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            createHomeFragment()
            Snackbar
                .make(binding.root, R.string.free_version, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_paid) {
                    Toast.makeText(this, R.string.padi_magic, Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_item_home -> {
                    createHomeFragment()
                    true
                }
                R.id.bottom_item_history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentHost.id, HistoryFragment())
                        .addToBackStack(null)
                        .commit()

                    true
                }
                R.id.bottom_item_settings -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragmentHost.id, SettingsFragment())
                        .addToBackStack(null)
                        .commit()

                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun createHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentHost.id, HomeFragment.newInstance())
            .commit()
    }
}