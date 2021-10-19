package com.artushock.materialdesignproject.ui.main.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.ui.main.view.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}