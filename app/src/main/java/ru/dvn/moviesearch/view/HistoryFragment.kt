package ru.dvn.moviesearch.view

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHistoryBinding
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.history.HistoryAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handlerThread?.quitSafely()
        handlerThread = null
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