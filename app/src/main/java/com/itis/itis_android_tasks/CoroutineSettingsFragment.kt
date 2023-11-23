package com.itis.itis_android_tasks

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.databinding.FragmentCoroutineSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class CoroutineSettingsFragment : Fragment(R.layout.fragment_coroutine_settings) {
    private val viewBinding: FragmentCoroutineSettingsBinding by viewBinding(FragmentCoroutineSettingsBinding::bind)

    private val notificationHandler = NotificationHandler()

    private var mainJob: Job? = null

    private var coroutinesFinished = 0

    private var numberOfCoroutines = 0

    private var numberOfActiveCoroutines = 0

    private var isStopOnBackgroundEnabled = false

    private lateinit var receiver: AirplaneModeReceiver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with(viewBinding) {
            val activity = requireActivity()

            receiver = AirplaneModeReceiver(activity as AppCompatActivity)

            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
                activity.registerReceiver(receiver, it)
            }

            if (Settings.Global.getInt(activity.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
                launchBtn.isEnabled = false
            }

            launchBtn.setOnClickListener {
                numberOfCoroutines = numberOfCoroutinesSb.progress
                launchCoroutines()
            }
        }
    }

    private fun launchCoroutines() {

        coroutinesFinished = 0
        numberOfActiveCoroutines = 0

        with(viewBinding) {

            isStopOnBackgroundEnabled = stopOnBackgroundCb.isChecked

            mainJob = lifecycleScope.launch{
                var counter = 0
                if (!asyncCb.isChecked) {
                    repeat(numberOfCoroutines) {
                        val asyncSample = async(Dispatchers.IO){
                            printCoroutineNumber(++counter)
                        }
                        asyncSample.await()
                    }
                } else {
                    repeat(numberOfCoroutines) {
                        launch (Dispatchers.IO) {
                            printCoroutineNumber(++counter)
                        }
                    }
                }

            }
        }
    }

    private fun updateNumberOfActiveCoroutines() {
        numberOfActiveCoroutines = mainJob?.children?.count { job ->
            job.isActive
        }!!
    }

    private suspend fun printCoroutineNumber(number: Int) {
        delay(Random.nextInt(1, 3) * 1000L)

        if (isStopOnBackgroundEnabled) {
            updateNumberOfActiveCoroutines()
        }

        println(number)


        if (++coroutinesFinished == numberOfCoroutines) {
            makeNotification()
        }
    }

    private fun makeNotification() {
        notificationHandler.createNotification(
            requireContext(),
            1001,
            NotificationManager.IMPORTANCE_HIGH,
            Notification.VISIBILITY_PUBLIC,
            isSeeMoreFeatureEnabled = false,
            areAppLaunchingFeaturesEnabled = false,
            title = getString(R.string.coroutines_title),
            text = getString(R.string.all_done_text)
        )
    }

    override fun onStop() {
        super.onStop()

        if (isStopOnBackgroundEnabled) {
            mainJob?.cancel()
            println(getString(R.string.coroutines_killed, numberOfActiveCoroutines))
        }

    }

}
