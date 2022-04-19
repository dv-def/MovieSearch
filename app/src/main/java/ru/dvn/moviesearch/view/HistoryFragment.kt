package ru.dvn.moviesearch.view

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHistoryBinding
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.history.HistoryAdapter
import ru.dvn.moviesearch.view.movies.DetailFragment
import ru.dvn.moviesearch.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private var handlerThread: HandlerThread? = null

    private val historyViewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private val onItemClickListener = object : HistoryAdapter.OnItemClickListener {
        override fun onItemClick(kinopoiskId: Long) {
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_host, DetailFragment.newInstance(kinopoiskId))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private val historyAdapter = HistoryAdapter(onItemClickListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        handlerThread = HandlerThread("History HT")
        handlerThread?.start()

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistories.adapter = historyAdapter

        historyViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderHistory(it)
        }

        requestHistory()
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handlerThread?.quitSafely()
        handlerThread = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        (menu.findItem(R.id.item_search).actionView as SearchView).apply {
            queryHint = activity?.getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    historyAdapter.filter.filter(newText)
                    return true
                }
            })
        }
    }

    private fun requestHistory() {
        handlerThread?.let {
            Handler(it.looper).post {
                historyViewModel.getAll()
            }
        }
    }

    private fun renderHistory(appState: AppState) {
        if (appState is AppState.SuccessHistory) {
            historyAdapter.setHistory(appState.history)
        }
    }
}