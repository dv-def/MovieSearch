package ru.dvn.moviesearch.view.history

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentHistoryBinding
import ru.dvn.moviesearch.model.history.local.HistoryAdapter
import ru.dvn.moviesearch.model.history.local.HistoryState
import ru.dvn.moviesearch.view.movies.DetailFragment
import ru.dvn.moviesearch.viewmodel.history.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private val onItemClickListener = object : HistoryAdapter.OnItemClickListener {
        override fun onItemClick(kinopoiskId: Long) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_host, DetailFragment.newInstance(kinopoiskId))
                ?.addToBackStack(null)?.commit()
        }
    }

    private val historyAdapter = HistoryAdapter(onItemClickListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistories.adapter = historyAdapter

        historyViewModel.liveData.observe(viewLifecycleOwner) {
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
        historyViewModel.getAll()
    }

    private fun renderHistory(state: HistoryState) {
        when(state) {
            is HistoryState.SuccessGetAll -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.rvHistories.visibility = View.VISIBLE

                with(state.history) {
                    if (this.isNotEmpty()) {
                        historyAdapter.setHistory(this)
                    } else {
                        Toast.makeText(requireContext(), R.string.no_histiry, Toast.LENGTH_LONG).show()
                    }
                }
            }
            is HistoryState.Error -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.rvHistories.visibility = View.VISIBLE

                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(requireContext().getString(R.string.try_again)) {
                        requestHistory()
                    }.show()
            }
            is HistoryState.Loading -> {
                binding.loadingLayout.root.visibility = View.VISIBLE
                binding.rvHistories.visibility = View.GONE
            }
        }
    }
}