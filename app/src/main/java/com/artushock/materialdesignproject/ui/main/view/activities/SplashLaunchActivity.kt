package com.artushock.materialdesignproject.ui.main.view.activities

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.BounceInterpolator
import com.artushock.materialdesignproject.databinding.ActivitySplashLaunchBinding

class SplashLaunchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashActivityLogoImageView.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .rotation(20f)
            .setDuration(3500)
            .setInterpolator(BounceInterpolator())
            .setListener(object: Animator.AnimatorListener{
                override fun onAnimationEnd(animator: Animator?) {
                    startActivity(Intent(this@SplashLaunchActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAnimationStart(p0: Animator?) {
                    // do nothing
                }

                override fun onAnimationCancel(p0: Animator?) {
                    // do nothing
                }

                override fun onAnimationRepeat(p0: Animator?) {
                    // do nothing
                }
            })
    }
}