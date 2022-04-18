package ru.dvn.moviesearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_contacts -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_host, ContactsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentHost.id, HomeFragment.newInstance())
            .commit()
    }
}