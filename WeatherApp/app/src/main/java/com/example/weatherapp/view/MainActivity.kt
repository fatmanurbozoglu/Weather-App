package com.example.weatherapp.view

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.weatherapp.databinding.ActivityMainBinding
// gradle.properties kısmına android.enableJetifier=true eklendi
// build.gradle.app kısmında VERSION_17 olarak guncelledik
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animation()
    }
    fun animation(){
        val animationView = binding.weatherAnimationView
        animationView.addAnimatorListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                // Animasyon başladığında yapılacak işlemler
                Handler().postDelayed({
                    val intent = Intent(this@MainActivity, WeatherMainActivity::class.java)
                    startActivity(intent)
                    finish()
                },800)
            }

            override fun onAnimationEnd(animation: Animator) {
                // animasyon bitince yapılacaklar

            }
            override fun onAnimationCancel(animation: Animator) {
                // Animasyon iptal edildiğinde yapılacak işlemler
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Animasyon tekrarlandığında yapılacak işlemler
            }
        })
        animationView.playAnimation()
    }
}