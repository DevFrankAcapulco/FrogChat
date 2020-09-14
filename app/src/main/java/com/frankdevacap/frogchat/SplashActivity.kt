package com.frankdevacap.frogchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

lateinit var handler: Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_splash)
        imageViewSplash.startAnimation(animation)
        textViewSplash.startAnimation(animation)

        handler = Handler()

        var handler = Handler().postDelayed({
            startActivity(Intent(this, MainEmptyActivity::class.java))
            finish()
        }, 2000)

    }
}