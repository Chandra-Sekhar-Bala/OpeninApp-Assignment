package com.openinapp.task.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.openinapp.task.R
import com.openinapp.task.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        // setting up bottom nav clicks
        binding.bottomNavView.setupWithNavController(findNavController(R.id.navHost_fragment))
        binding.fab.setOnClickListener {
            Toast.makeText(this, "It's an assignment sprint 🕊️", Toast.LENGTH_SHORT).show()
        }
    }
}