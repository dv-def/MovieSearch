package ru.dvn.moviesearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentHost.id, HomeFragment.newInstance())
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_item_home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragmentHost.id, HomeFragment.newInstance())
                        .commit()
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
    }
}