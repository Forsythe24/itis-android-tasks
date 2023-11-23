package com.itis.itis_android_tasks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import by.kirich1409.viewbindingdelegate.internal.findRootView
import com.google.android.material.button.MaterialButton

class AirplaneModeReceiver(private val activity: AppCompatActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return

        val rootView = findRootView(activity)
        val warningCardView = rootView.findViewById<CardView>(R.id.no_network_access_warning_cv)
        val notifyButton = rootView.findViewById<MaterialButton>(R.id.notify_btn)
        val launchButton = rootView.findViewById<MaterialButton>(R.id.launch_btn)

        if (isAirplaneModeEnabled) {
            warningCardView.visibility = View.VISIBLE

            notifyButton?.isEnabled = false
            launchButton?.isEnabled = false
        } else {
            warningCardView.visibility = View.GONE

            notifyButton?.isEnabled = true
            launchButton?.isEnabled = true
        }
    }
}
