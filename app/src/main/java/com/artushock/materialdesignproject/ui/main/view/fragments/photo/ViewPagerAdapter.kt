package com.artushock.materialdesignproject.ui.main.view.fragments.photo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    private val fragments =
        arrayOf(PhotoOfTheDayFragment(), YesterdayPhotoFragment(), DayBeforeYesterdayPhotoFragment())


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
            1 -> "Yesterday"
            2 -> "Day before yesterday"
            else -> "Today"
        }
    }
}