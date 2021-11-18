package com.artushock.materialdesignproject.ui.main.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.databinding.ActivitySplashLaunchBinding

class SplashLaunchActivity : AppCompatActivity() {

    lateinit var handler: Handler
    lateinit var binding: ActivitySplashLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashActivityLogoImageView.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .rotation(20f)
            .duration =3500


        val handlerThread = HandlerThread("LaunchActivityThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        handler.postDelayed({
            startActivity(Intent(this@SplashLaunchActivity, MainActivity::class.java))
            finish()
        }, 2000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}