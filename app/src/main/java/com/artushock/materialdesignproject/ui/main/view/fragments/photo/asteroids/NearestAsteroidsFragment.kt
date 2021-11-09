package com.artushock.materialdesignproject.ui.main.view.fragments.photo.asteroids

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.databinding.FragmentNearestAsteroidsStartBinding

class NearestAsteroidsFragment : Fragment() {

    private var isAsteroidsFound = false

    private var _binding: FragmentNearestAsteroidsStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNearestAsteroidsStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nearestAsteroidAsteroidsImageView.setOnClickListener {
            initAnimation()
        }
    }

    private fun initAnimation() {
        if (isAsteroidsFound) hideTitle() else showTitle()
    }

    private fun showTitle() {
        isAsteroidsFound = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_nearest_asteroids_end)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 2000

        val container = binding.nearestAsteroidAsteroidsContainer
        TransitionManager.beginDelayedTransition(container, transition)
        constraintSet.applyTo(container)
    }

    private fun hideTitle() {
        isAsteroidsFound = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_nearest_asteroids_start)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 2000

        val container = binding.nearestAsteroidAsteroidsContainer
        TransitionManager.beginDelayedTransition(container, transition)
        constraintSet.applyTo(container)
    }
}