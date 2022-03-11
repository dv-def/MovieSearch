package ru.dvn.moviesearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

//    override fun onMovieClick(movieId: Int) {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(binding.fragmentHost.id, DetailFragment.newInstance(movieId = movieId))
//            .addToBackStack(null)
//            .commit()
//    }

    private fun createHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentHost.id, HomeFragment.newInstance())
            .commit()
    }
}