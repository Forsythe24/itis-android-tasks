package com.itis.itis_android_tasks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.utils.worker.UserDeletionWorker
import com.itis.itis_android_tasks.databinding.ActivityMainBinding
import com.itis.itis_android_tasks.utils.ParamsKey
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var workManager: WorkManager
    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)
        val controller = (supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment).navController
        controller.navigate(R.id.authorizationFragment)
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        val request = OneTimeWorkRequestBuilder<UserDeletionWorker>()
//            .setInputData(ParamsKey.USER_ID_KEY to )
//            .setInitialDelay(5, TimeUnit.SECONDS)
//            .build()
//
//        workManager.enqueue(request)
//    }
}
