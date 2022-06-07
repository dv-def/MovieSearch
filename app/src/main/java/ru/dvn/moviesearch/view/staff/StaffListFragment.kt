package ru.dvn.moviesearch.view.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentStaffBinding
import ru.dvn.moviesearch.model.staff.remote.StaffListState
import ru.dvn.moviesearch.model.staff.remote.list.StaffAdapter
import ru.dvn.moviesearch.viewmodel.staff.StaffListViewModel

class StaffFragment : Fragment() {
    companion object {
        private const val EXTRA_FILM_ID = "EXTRA_FILM_ID"
        private const val GRID_LAYOUT_COLUMN_COUNT = 2

        fun newInstance(filmId: Long): StaffFragment {
            return StaffFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_FILM_ID, filmId)
                }
            }
        }
    }

    interface OnPersonClickListener {
        fun onClick(personId: Long)
    }

    private var _binding: FragmentStaffBinding? = null
    private val binding get() = _binding!!

    private val onPersonClickListener = object : OnPersonClickListener {
        override fun onClick(personId: Long) {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragment_host, StaffDetailsFragment.newInstance(personId))
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private val staffAdapter: StaffAdapter by lazy {
        StaffAdapter(onPersonClickListener)
    }

    private val listViewModel: StaffListViewModel by lazy {
        ViewModelProvider(this).get(StaffListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStaff.apply {
            adapter = staffAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_LAYOUT_COLUMN_COUNT)
        }

        listViewModel.liveData.observe(viewLifecycleOwner) {
            renderData(it)
        }

        arguments?.getLong(EXTRA_FILM_ID)?.let { filmId ->
            listViewModel.getStaff(filmId)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: StaffListState) {
        when (appState) {
            is StaffListState.Success -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.rvStaff.visibility = View.VISIBLE
                staffAdapter.setData(appState.staff)
            }
            is StaffListState.Loading -> {
                binding.rvStaff.visibility = View.GONE
                binding.loadingLayout.root.visibility = View.VISIBLE
            }
            is StaffListState.Error -> {
                binding.rvStaff.visibility = View.GONE
                binding.loadingLayout.root.visibility = View.GONE

                Snackbar.make(
                    requireContext(),
                    binding.mainLayout,
                    appState.message,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    R.string.back
                ) { activity?.supportFragmentManager?.popBackStack() }
                .show()
            }
        }
    }
}