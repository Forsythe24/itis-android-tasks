package com.itis.itis_android_tasks

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.databinding.FragmentNotificationSettingsBinding
import java.lang.IllegalArgumentException

class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings) {
    private val viewBinding: FragmentNotificationSettingsBinding by viewBinding(FragmentNotificationSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with (viewBinding) {
            importanceRg.setOnCheckedChangeListener { _, i ->
                val importance =
                    when (i) {
                        firstImportanceRb.id -> NotificationManager.IMPORTANCE_LOW
                        secondImportanceRb.id -> NotificationManager.IMPORTANCE_DEFAULT
                        thirdImportanceRb.id -> NotificationManager.IMPORTANCE_HIGH
                        else -> {
                            throw IllegalArgumentException("No such notification importance type")
                        }
                    }
                findNavController().previousBackStackEntry?.savedStateHandle?.set(ParamsKey.IMPORTANCE_KEY, importance)
            }

            privacyRg.setOnCheckedChangeListener { _, i ->
                val importance =
                    when (i) {
                        firstPrivacyRb.id -> Notification.VISIBILITY_PUBLIC
                        secondPrivacyRb.id -> Notification.VISIBILITY_PRIVATE
                        thirdPrivacyRb.id -> Notification.VISIBILITY_SECRET
                        else -> {
                            throw IllegalArgumentException("No such notification visibility type")
                        }
                    }
                findNavController().previousBackStackEntry?.savedStateHandle?.set(ParamsKey.PRIVACY_KEY, importance)
            }

            seeMoreCb.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(ParamsKey.SEE_MORE_FEATURE_KEY, (it as CheckBox).isChecked)
            }

            appLaunchingFeaturesCb.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(ParamsKey.APP_LAUNCHING_FEATURES_KEY, (it as CheckBox).isChecked)
            }
        }
        
    }
}
