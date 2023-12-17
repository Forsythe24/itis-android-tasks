package com.itis.itis_android_tasks.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = (supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment).navController
        controller.navigate(R.id.authorizationFragment)
    }
}
