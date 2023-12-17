package com.itis.itis_android_tasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ActionMenuView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.database.LibraryDatabase
import com.itis.itis_android_tasks.database.entity.UserEntity
import com.itis.itis_android_tasks.databinding.FragmentAuthorizationBinding
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val viewBinding: FragmentAuthorizationBinding by viewBinding(FragmentAuthorizationBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

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
                        if (userEntity!!.password == password) {
                            findNavController().navigate(R.id.action_authorizationFragment_to_mainFragment, bundleOf(ParamsKey.USER_ID_KEY to userEntity!!.id))
                        } else {
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
    companion object {
        fun newInstance() = AuthorizationFragment()
    }
}
