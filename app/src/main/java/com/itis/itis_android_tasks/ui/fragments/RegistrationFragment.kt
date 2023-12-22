package com.itis.itis_android_tasks.ui.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.database.entity.UserEntity
import com.itis.itis_android_tasks.databinding.FragmentRegistrationBinding
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val viewBinding: FragmentRegistrationBinding by viewBinding(FragmentRegistrationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        viewBinding.apply {

            signupBtn.setOnClickListener {
                val pref = ServiceLocator.getSharedPreferences()

                val lastUserId = pref.getString(ParamsKey.LAST_USER_ID_KEY, null) ?: "0"

                val currentUserId = (lastUserId.toInt() + 1).toString()

                pref.edit()
                    .putString(ParamsKey.LAST_USER_ID_KEY, currentUserId)
                    .apply()

                val userEntity = UserEntity(
                    currentUserId,
                    name = nameEt.text.toString(),
                    phoneNumber = phoneNumberEt.text.toString(),
                    email = emailEt.text.toString(),
                    password = passwordEt.text.toString(),
                    null
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        ServiceLocator.getDbInstance().userDao.addUser(userEntity)

                        withContext(Dispatchers.Main) {
                            findNavController().navigate(R.id.action_registrationFragment_to_authorizationFragment)
                        }


                    } catch (e: SQLiteConstraintException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.email_phone_number_in_use),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }


            }

        }

    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }
}
