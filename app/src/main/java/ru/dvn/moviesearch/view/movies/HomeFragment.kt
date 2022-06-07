package ru.dvn.moviesearch.view.movies

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.model.movie.remote.list.TopParam

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.movie_list_fragment_host_top_best,
                MovieListFragment.newInstance(
                    context?.getString(R.string.top_best),
                    TopParam.TOP_BEST_FILMS.paramName
                )
            )
            .commit()

        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.movie_list_fragment_host_top_await,
                MovieListFragment.newInstance(
                    context?.getString(R.string.top_await),
                    TopParam.TOP_AWAIT_FILMS.paramName
                )
            )
            .commit()
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}