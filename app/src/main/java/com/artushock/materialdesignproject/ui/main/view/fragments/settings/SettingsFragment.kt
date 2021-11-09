package com.artushock.materialdesignproject.ui.main.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.databinding.FragmentSettingsBinding
import com.artushock.materialdesignproject.ui.main.view.activities.isSystemDarkMode
import com.artushock.materialdesignproject.ui.main.view.theme.AppTheme
import com.artushock.materialdesignproject.ui.main.view.theme.ThemeMode
import com.artushock.materialdesignproject.ui.main.view.theme.ThemePreferencesUtil
import com.google.android.material.chip.Chip

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SettingsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themePreferencesUtil = ThemePreferencesUtil(requireContext())
        setThemeChipsState(themePreferencesUtil)

        binding.appThemeChipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let { chip ->
                changeTheme(chip, themePreferencesUtil)
            }
        }

        binding.themeModeChipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let { chip ->
                changeThemeMode(chip, themePreferencesUtil)
            }
        }
    }

    private fun changeThemeMode(chip: Chip, themePreferencesUtil: ThemePreferencesUtil) {
        when (chip.id) {
            R.id.chip_light_mode -> {
                if (isSystemDarkMode) {
                    Toast.makeText(
                        context,
                        "System dark mode turned on! \nUI can be incorrect",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                themePreferencesUtil.changeThemeMode(ThemeMode.LIGHT)
                requireActivity().recreate()
            }
            R.id.chip_dark_mode -> {
                themePreferencesUtil.changeThemeMode(ThemeMode.DARK)
                requireActivity().recreate()
            }
            R.id.chip_system_mode -> {
                themePreferencesUtil.changeThemeMode(ThemeMode.SYSTEM_MODE)
                requireActivity().recreate()
            }
        }
    }

    private fun changeTheme(chip: Chip, themePreferencesUtil: ThemePreferencesUtil) {
        when (chip.id) {
            R.id.chip_blue_green_theme -> {
                themePreferencesUtil.changeTheme(AppTheme.REFLECTION)
                requireActivity().recreate()
            }
            R.id.chip_red_orange_theme -> {
                themePreferencesUtil.changeTheme(AppTheme.SUNSET)
                requireActivity().recreate()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setThemeChipsState(themePreferencesUtil: ThemePreferencesUtil) {
        when (themePreferencesUtil.getThemeFromPreferences()) {
            AppTheme.DEFAULT.theme -> binding.chipBlueGreenTheme.isChecked = true
            AppTheme.REFLECTION.theme -> binding.chipBlueGreenTheme.isChecked = true
            AppTheme.SUNSET.theme -> binding.chipRedOrangeTheme.isChecked = true
        }

        when (themePreferencesUtil.getThemeModeFromPreferences()) {
            ThemeMode.DARK.mode -> binding.chipDarkMode.isChecked = true
            ThemeMode.LIGHT.mode -> binding.chipLightMode.isChecked = true
            ThemeMode.SYSTEM_MODE.mode -> binding.chipSystemMode.isChecked = true
        }
    }
}