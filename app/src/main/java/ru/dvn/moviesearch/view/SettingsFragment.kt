package ru.dvn.moviesearch.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.dvn.moviesearch.databinding.FragmentSettingsBinding

const val NOTES_SETTINGS_KEY = "NOTES_SETTINGS_KEY"

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            binding.fragmentSettingsCheckboxHistory.isChecked =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(NOTES_SETTINGS_KEY, false)
        }

        binding.fragmentSettingsBtnSave.setOnClickListener {
            activity?.let {
                it.getPreferences(Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean(
                        NOTES_SETTINGS_KEY,
                        binding.fragmentSettingsCheckboxHistory.isChecked
                    )
                    .apply()

                it.supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}