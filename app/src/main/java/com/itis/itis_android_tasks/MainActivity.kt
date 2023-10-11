package com.itis.itis_android_tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itis.itis_android_tasks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportFragmentManager.findFragmentByTag(DoubleFragment.DOUBLE_FRAGMENT_TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    binding.fragmentContainer.id,
                    DoubleFragment.newInstance(),
                    DoubleFragment.DOUBLE_FRAGMENT_TAG
                )
                .commit()
        }
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig);
//
//        val newOrientation = newConfig.orientation;
//        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(binding.fragmentContainer.id, DoubleFragment.newInstance())
//                .commit()
//        } else  {
//            findViewById<FrameLayout>(R.id.placeholder_2).visibility = View.GONE
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
