package com.itis.itis_android_tasks.ui.fragments

import android.app.Dialog
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.utils.worker.UserDeletionWorker
import com.itis.itis_android_tasks.databinding.FragmentProfileBinding
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.model.User
import com.itis.itis_android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    lateinit var userId: String
    lateinit var user: User

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        userId = requireArguments().getString(ParamsKey.USER_ID_KEY).toString()
        with(viewBinding) {
            lifecycleScope.launch {


                withContext(Dispatchers.IO) {
                    val userEntity = ServiceLocator.getDbInstance().userDao.getUserById(userId)!!

                    userEntity.let {
                        user = User(it.id, it.name, it.phoneNumber, it.email, it.password, null)
                    }
                }

                user.let {
                    nameTv.text = it.name
                    emailTv.text = it.email
                    phoneNumberTv.text = it.phoneNumber
                }

            }

            logoutBtn.setOnClickListener {
                unauthorize()

                findNavController().navigate(R.id.action_profileFragment_to_authorizationFragment)
            }

            phoneNumberTv.setOnClickListener {
                showPhoneNumberSettingDialog()
            }
            
            changePasswordBtn.setOnClickListener {
                showCurrentPasswordInputDialog()
            }

            deleteAccountBtn.setOnClickListener {
                showDeletingAccountDialog()
            }
        }

    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDeletingAccountDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(true)

        dialog.setContentView(R.layout.two_option_dialog)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val tvHeader = dialog.findViewById<TextView>(R.id.header_tv)
        val btnDeleteAccount = dialog.findViewById<MaterialButton>(R.id.first_option_btn)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.second_option_btn)

        tvHeader.text = getString(R.string.delete_account_question)

        btnDeleteAccount.text = getString(R.string.delete_btn)

        btnCancel.text = getString(R.string.cancel_btn)

        btnDeleteAccount.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {

                ServiceLocator.getDbInstance().apply {
                    userDao.updateUserDeleteDate(userId, LocalDate.now().toString())

                    val deletionRequest = OneTimeWorkRequestBuilder<UserDeletionWorker>()
                        .setInputData(
                            workDataOf(ParamsKey.USER_ID_KEY to userId)
                        )
                        .setInitialDelay(7, TimeUnit.DAYS)
                        .build()

                    WorkManager.getInstance(requireContext()).enqueueUniqueWork(
                        getString(R.string.delayed_user_deletion),
                        ExistingWorkPolicy.KEEP,
                        deletionRequest)

                    unauthorize()
                }

                withContext(Dispatchers.Main) {
                    dialog.hide()
                    findNavController().navigate(R.id.action_profileFragment_to_authorizationFragment)
                }
            }
        }

        btnCancel.setOnClickListener {
            dialog.hide()
        }
    }

    private fun showCurrentPasswordInputDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(true)

        dialog.setContentView(R.layout.user_info_setting_dialog)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val btnSetNewPassword = dialog.findViewById<MaterialButton>(R.id.take_user_info_btn)
        val etPassword = dialog.findViewById<EditText>(R.id.user_info_et)
        val tvHeader = dialog.findViewById<TextView>(R.id.header_tv)
        
        btnSetNewPassword.text = getString(R.string.submit)
        tvHeader.text = getString(R.string.current_password_request)

        var newPassword = ""

        etPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                newPassword = p0.toString()
            }

        })


        btnSetNewPassword.setOnClickListener {

            if (user.password == newPassword) {
                dialog.hide()
                showPasswordSettingDialog()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.wrong_password_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showPasswordSettingDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(true)

        dialog.setContentView(R.layout.user_info_setting_dialog)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val btnSetNewPassword = dialog.findViewById<MaterialButton>(R.id.take_user_info_btn)
        val etPassword = dialog.findViewById<EditText>(R.id.user_info_et)
        val tvHeader = dialog.findViewById<TextView>(R.id.header_tv)

        tvHeader.text = getString(R.string.new_password_request)

        var newPassword = ""

        etPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                newPassword = p0.toString()
            }

        })


        btnSetNewPassword.setOnClickListener {

            if (newPassword.isNotEmpty()) {

                lifecycleScope.launch (Dispatchers.IO) {

                    val userDao = ServiceLocator.getDbInstance().userDao

                    userDao.updateUserPassword(userId, newPassword)
                    user.password = newPassword

                    withContext(Dispatchers.Main) {

                        dialog.hide()

                        Toast.makeText(
                            context,
                            getString(R.string.password_set_success),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            } else {
                Toast.makeText(context, getString(R.string.empty_password_warning), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showPhoneNumberSettingDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(true)

        dialog.setContentView(R.layout.user_info_setting_dialog)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val btnSetNewPassword = dialog.findViewById<MaterialButton>(R.id.take_user_info_btn)
        val etPassword = dialog.findViewById<EditText>(R.id.user_info_et)
        
        var newPassword: String?

        viewBinding.apply {
            etPassword.setText(phoneNumberTv.text)

            newPassword = phoneNumberTv.text as String
        }

        etPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                newPassword = p0.toString()
            }

        })


        btnSetNewPassword.setOnClickListener {

            if (newPassword!!.isNotEmpty()) {
                lifecycleScope.launch (Dispatchers.IO) {
                    try {
                        ServiceLocator.getDbInstance().userDao.updateUserPhoneNumber(userId,
                            newPassword!!
                        )
                        withContext(Dispatchers.Main) {

                            dialog.hide()

                            Toast.makeText(
                                context,
                                getString(R.string.phone_number_setting_success),
                                Toast.LENGTH_LONG
                            ).show()

                            viewBinding.phoneNumberTv.text = newPassword
                        }
                    } catch (e: SQLiteConstraintException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                getString(R.string.phone_number_taken),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            } else {
                Toast.makeText(context, getString(R.string.empty_phone_number_warning), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun unauthorize() {
        val pref = ServiceLocator.getSharedPreferences()

        pref.edit()
            .remove(ParamsKey.IS_AUTHORIZED)
            .apply()
    }

    companion object {

        fun newInstance() = ProfileFragment()
    }
}
