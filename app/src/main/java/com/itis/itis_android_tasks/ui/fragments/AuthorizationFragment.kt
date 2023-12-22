package com.itis.itis_android_tasks.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.database.entity.UserEntity
import com.itis.itis_android_tasks.databinding.FragmentAuthorizationBinding
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val viewBinding: FragmentAuthorizationBinding by viewBinding(FragmentAuthorizationBinding::bind)
    private lateinit var userId: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {

        val id = checkIfAuthorized()

        if (id != null) {
            findNavController().navigate(R.id.action_authorizationFragment_to_mainFragment, bundleOf(ParamsKey.USER_ID_KEY to id))
            return
        }

        viewBinding.apply {
            loginBtn.setOnClickListener {
                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()

                lifecycleScope.launch {
                    var userEntity: UserEntity?

                    withContext(Dispatchers.IO) {
                        userEntity = ServiceLocator.getDbInstance().userDao.getUserByEmail(email)
                    }

                    if (userEntity == null) {
                        Toast.makeText(requireContext(), getString(R.string.no_such_user_warning), Toast.LENGTH_SHORT).show()
                    } else {
                        if ((userEntity!!.deleteDate) != null) {

                            userId = userEntity!!.id

                            showAccountRestorationDialog()

                        } else if (userEntity!!.password == password) {

                            val pref = ServiceLocator.getSharedPreferences()

                            pref.edit()
                                .putString(ParamsKey.IS_AUTHORIZED, userEntity!!.id)
                                .apply()

                            findNavController().navigate(R.id.action_authorizationFragment_to_mainFragment, bundleOf(ParamsKey.USER_ID_KEY to userEntity!!.id))
                        }

                        else {
                            Toast.makeText(requireContext(), getString(R.string.wrong_password_warning), Toast.LENGTH_SHORT).show()
                        }

                    }

                }
            }
            signupLnk.setOnClickListener {
                findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
            }
        }
    }
    private fun showAccountRestorationDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(true)

        dialog.setContentView(R.layout.two_option_dialog)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val btnDeletePermanently = dialog.findViewById<MaterialButton>(R.id.first_option_btn)
        val btnRestore = dialog.findViewById<MaterialButton>(R.id.second_option_btn)
        val header = dialog.findViewById<TextView>(R.id.header_tv)

        btnDeletePermanently.text = getString(R.string.delete)
        btnRestore.text = getString(R.string.restore)
        header.text = getString(R.string.recently_deleted_account_message)

        btnDeletePermanently.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {

                ServiceLocator.getDbInstance().apply {
                    userDao.deleteUserById(userId)
                    userBookDao.deleteAllUserFavorites(userId)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), getString(R.string.account_permanently_deleted_message), Toast.LENGTH_LONG).show()

                    dialog.hide()
                }
            }
        }

        btnRestore.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                ServiceLocator.getDbInstance().userDao.updateUserDeleteDate(userId, null)

                WorkManager.getInstance(requireContext()).cancelUniqueWork(getString(R.string.delayed_user_deletion))


                withContext(Dispatchers.Main) {
                    dialog.hide()
                    findNavController().navigate(R.id.action_authorizationFragment_to_mainFragment, bundleOf(ParamsKey.USER_ID_KEY to userId))
                    Toast.makeText(requireContext(), getString(R.string.account_restored_message), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkIfAuthorized(): String? {
        val pref = ServiceLocator.getSharedPreferences()

        return pref.getString(ParamsKey.IS_AUTHORIZED, null)
    }
    companion object {
        fun newInstance() = AuthorizationFragment()
    }
}
