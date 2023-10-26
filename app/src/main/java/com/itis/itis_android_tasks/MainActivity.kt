package com.itis.itis_android_tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itis.itis_android_tasks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.fragmentContainer.id, StartFragment.newInstance())
                .commit()
        }
    }
}
