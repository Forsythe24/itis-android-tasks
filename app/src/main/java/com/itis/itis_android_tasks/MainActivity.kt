package com.itis.itis_android_tasks

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.itis_android_tasks.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController

    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    private lateinit var receiver: AirplaneModeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        navController = (supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment).navController
        findViewById<BottomNavigationView>(R.id.main_bottom_navigation).apply {
            setupWithNavController(navController)
        }

        checkIntents()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(POST_NOTIFICATIONS),
                    POST_NOTIFICATIONS_REQUEST_CODE
                )
            }
        }

        if (Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
            viewBinding.noNetworkAccessWarningCv.visibility = View.VISIBLE
        }

        receiver = AirplaneModeReceiver(this)

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }
    }

    private fun checkIntents() {
        if (intent.getBooleanExtra(ParamsKey.MAKE_A_TOAST_KEY, false)) {
            Toast.makeText(this, "Careful! Hot from the oven!", Toast.LENGTH_SHORT).show()
        }

        if (intent.getBooleanExtra(ParamsKey.GO_TO_NOTIFICATION_SETTINGS_KEY, false)) {
            navController.navigate(R.id.notificationSettingsFragment)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var granted = false
        when (requestCode) {
            POST_NOTIFICATIONS_REQUEST_CODE -> {
                granted = grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED
            }
        }
        if (!granted) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, POST_NOTIFICATIONS)) {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.permission_denined_desc))
                    .setPositiveButton(
                        getString(R.string.app_settings)
                    ) { _, _ ->
                        openAppSettings()
                    }
                    .show()
            }
        }
    }

    private fun openAppSettings() {
        intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null);
        intent.data = uri
        startActivity(intent)
    }


    companion object {
        private const val POST_NOTIFICATIONS_REQUEST_CODE = 1000
    }
}
