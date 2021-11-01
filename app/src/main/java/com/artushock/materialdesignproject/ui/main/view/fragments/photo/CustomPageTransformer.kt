package com.artushock.materialdesignproject.ui.main.view.fragments.photo

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max

private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.85f

class CustomPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position <= -1 -> {
                    alpha = 0f
                }
                position <= 1 -> {
                    val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    alpha =
                        (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }
}