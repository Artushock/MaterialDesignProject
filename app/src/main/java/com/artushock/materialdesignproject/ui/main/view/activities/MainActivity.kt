package com.artushock.materialdesignproject.ui.main.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.databinding.MainActivityBinding
import com.artushock.materialdesignproject.ui.main.view.fragments.search.SearchFragment
import com.artushock.materialdesignproject.ui.main.view.fragments.settings.SettingsFragment
import com.artushock.materialdesignproject.ui.main.view.fragments.photo.PhotoFragment
import com.artushock.materialdesignproject.ui.main.view.theme.ThemePreferencesUtil

var isSystemDarkMode = true

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemePreferencesUtil(this).setAppTheme()

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            replaceFragment(PhotoFragment.newInstance())
        }
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        binding.appBottomNavigation.setOnItemSelectedListener() { it: MenuItem ->
            when (it.itemId) {
                R.id.photos_menu_item -> {
                    replaceFragment(PhotoFragment.newInstance())
                    true
                }
                R.id.search_menu_item -> {
                    replaceFragment(SearchFragment.newInstance())
                    true
                }
                R.id.settings_menu_item -> {
                    replaceFragment(SettingsFragment.newInstance())
                    true
                }
                else -> {
                    replaceFragment(PhotoFragment.newInstance())
                    true
                }
            }
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

}