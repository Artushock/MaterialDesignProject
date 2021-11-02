package com.artushock.materialdesignproject.ui.main.view.fragments.photo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos.MarsRoverPhotosFragment
import com.artushock.materialdesignproject.ui.main.view.fragments.photo.photoofday.PhotoOfTheDayFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val fragments =
        arrayOf(PhotoOfTheDayFragment(), MarsRoverPhotosFragment(), DayBeforeYesterdayPhotoFragment())


    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> fragments[0]
        1 -> fragments[1]
        2 -> fragments[2]
        else -> fragments[0]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position){
            0 -> "Photo of the Day"
            1 -> "Rovers on Mars"
            2 -> "Nearest asteroids"
            else -> "Today"
        }
    }
}