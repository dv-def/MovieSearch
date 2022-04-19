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
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.staff.list.StaffAdapter
import ru.dvn.moviesearch.viewmodel.StaffViewModel

private const val GRID_LAYOUT_COLUMN_COUNT = 2

private const val EXTRA_FILM_ID = "EXTRA_FILM_ID"

class StaffFragment : Fragment() {
    companion object {
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

    val onPersonClickListener = object : OnPersonClickListener {
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

    private val viewModel: StaffViewModel by lazy {
        ViewModelProvider(this).get(StaffViewModel::class.java)
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

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        arguments?.getLong(EXTRA_FILM_ID)?.let { filmId ->
            viewModel.getStaff(filmId)
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

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessStaff -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.rvStaff.visibility = View.VISIBLE
                staffAdapter.setData(appState.staff)
            }
            is AppState.Loading -> {
                binding.rvStaff.visibility = View.GONE
                binding.loadingLayout.root.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.rvStaff.visibility = View.GONE
                binding.loadingLayout.root.visibility = View.GONE

                Snackbar.make(
                    requireContext(),
                    binding.mainLayout,
                    appState.error.message.toString(),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    R.string.back
                ) { activity?.supportFragmentManager?.popBackStack() }
                .show()
            }
        }
    }
}