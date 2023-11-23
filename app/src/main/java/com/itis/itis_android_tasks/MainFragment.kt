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
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private var importance = NotificationManager.IMPORTANCE_LOW

    private var privacy = Notification.VISIBILITY_SECRET

    private var isSeeMoreFeatureEnabled = false

    private var areAppLaunchingFeaturesEnabled = false

    private val notificationHandler = NotificationHandler()

    private lateinit var receiver: AirplaneModeReceiver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val activity = requireActivity()

        receiver = AirplaneModeReceiver(activity as AppCompatActivity)

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            activity.registerReceiver(receiver, it)
        }

        if (Settings.Global.getInt(activity.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
            viewBinding.notifyBtn.isEnabled = false
        }

        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle

        savedStateHandle?.getLiveData<Int>(ParamsKey.IMPORTANCE_KEY)
            ?.observe(viewLifecycleOwner) { importance ->
                importance?.let {
                    this.importance = importance
                }
            }

        savedStateHandle?.getLiveData<Int>(ParamsKey.PRIVACY_KEY)
            ?.observe(viewLifecycleOwner) { privacy ->
                privacy?.let {
                    this.privacy = privacy
                }
            }

        savedStateHandle?.getLiveData<Boolean>(ParamsKey.SEE_MORE_FEATURE_KEY)
            ?.observe(viewLifecycleOwner) { isEnabled ->
                isEnabled?.let {
                    this.isSeeMoreFeatureEnabled = isEnabled
                }
            }

        savedStateHandle?.getLiveData<Boolean>(ParamsKey.APP_LAUNCHING_FEATURES_KEY)
            ?.observe(viewLifecycleOwner) { isEnabled ->
                isEnabled?.let {
                    this.areAppLaunchingFeaturesEnabled = isEnabled
                }
            }


        with (viewBinding) {

            notifyBtn.setOnClickListener {
                notificationHandler.createNotification(
                    requireContext(),
                    1000,
                    importance,
                    privacy,
                    isSeeMoreFeatureEnabled,
                    areAppLaunchingFeaturesEnabled,
                    titleEt.text.toString(),
                    textEt.text.toString()
                )
            }
        }

    }
}
