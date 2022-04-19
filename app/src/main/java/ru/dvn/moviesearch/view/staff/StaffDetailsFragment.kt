package ru.dvn.moviesearch.view.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.databinding.FragmentStaffDetailsBinding
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.staff.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.details.StaffFilmListAdapter
import ru.dvn.moviesearch.view.movies.DetailFragment
import ru.dvn.moviesearch.viewmodel.StaffViewModel

private const val EXTRA_PERSON_ID = "EXTRA_PERSON_ID"

class StaffDetailsFragment : Fragment() {
    companion object {
        fun newInstance(personId: Long): StaffDetailsFragment {
            return StaffDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_PERSON_ID, personId)
                }
            }
        }
    }

    interface OnPersonFilmClickListener {
        fun onClickFilm(filmId: Long)
    }

    private var _binding: FragmentStaffDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StaffViewModel by lazy {
        ViewModelProvider(this).get(StaffViewModel::class.java)
    }

    private val onFilmClickListener = object : OnPersonFilmClickListener {
        override fun onClickFilm(filmId: Long) {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragment_host, DetailFragment.newInstance(filmId))
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private val filmsAdapter: StaffFilmListAdapter by lazy {
        StaffFilmListAdapter(onFilmClickListener)
    }

    private var isFilmsShowed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentStaffDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        arguments?.getLong(EXTRA_PERSON_ID)?.let { personId ->
            viewModel.getStaffDetails(personId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessStaffDetails -> {
                binding.loadingLayout.root.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE
                showInfo(appState.staffDetails)
            }
            is AppState.Loading -> {
                binding.mainLayout.visibility = View.GONE
                binding.loadingLayout.root.visibility = View.VISIBLE
            }
        }
    }

    private fun showInfo(staffDetails: StaffDetailsDto) {
        Picasso.get()
            .load(staffDetails.posterUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.default_poster)
            .into(binding.fragmentStaffDetailsIvPoster)

        with(binding) {
            fragmentStaffDetailsTvName.text = if (!staffDetails.nameRu.isNullOrEmpty()) {
                staffDetails.nameRu
            } else {
                staffDetails.nameEn
            }

            staffDetails.profession?.let {
                fragmentStaffDetailsTvProfessions.text = it
            } ?: run { fragmentStaffDetailsTvProfessions.visibility = View.GONE }

            var dates = ""
            staffDetails.birthday?.let { birthday ->
                dates += "${requireContext().getString(R.string.birthday)}: $birthday"
                staffDetails.death?.let { death ->
                    dates += "\n${requireContext().getString(R.string.deathday)}: $death"
                }
            }

            if (dates.isNotEmpty()) {
                fragmentStaffDetailsTvDates.text = dates
            } else {
                fragmentStaffDetailsTvDates.visibility = View.GONE
            }

            staffDetails.birthplace?.let {
                val text = "${requireContext().getString(R.string.birthplace)}: $it"
                fragmentStaffDetailsTvBirthplace.text = text
            } ?: run { fragmentStaffDetailsTvBirthplace.visibility = View.GONE }

            staffDetails.deathplace?.let {
                val text = "${requireContext().getString(R.string.deathplace)}: $it"
                fragmentStaffDetailsTvDeathplace.text = text
            } ?: run { fragmentStaffDetailsTvDeathplace.visibility = View.GONE }

            staffDetails.facts?.let {
                val facts = it.joinToString("\n")
                if (facts.isNotEmpty()) {
                    fragmentStaffDetailsTvFacts.text = facts
                }
            } ?: run { fragmentStaffDetailsTvFacts.visibility = View.GONE }

            if (!staffDetails.films.isNullOrEmpty()) {
                filmsAdapter.setData(staffDetails.films.toMutableSet().distinctBy { it.nameRu })

                fragmentStaffDetailsRvFilms.apply {
                    adapter = filmsAdapter
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                }

                btnFilms.visibility = View.VISIBLE

                btnFilms.setOnClickListener {
                    btnFilms.iconGravity = MaterialButton.ICON_GRAVITY_END
                    if (!isFilmsShowed) {
                        fragmentStaffDetailsRvFilms.visibility = View.VISIBLE
                        btnFilms.setIconResource(R.drawable.ic_baseline_arrow_drop_up)
                    } else {
                        fragmentStaffDetailsRvFilms.visibility = View.GONE
                        btnFilms.setIconResource(R.drawable.ic_baseline_arrow_drop_down)
                    }

                    isFilmsShowed = !isFilmsShowed
                }
            }
        }
    }
}