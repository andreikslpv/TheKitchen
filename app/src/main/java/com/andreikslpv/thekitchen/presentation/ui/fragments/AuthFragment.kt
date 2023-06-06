package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentAuthBinding
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.vm.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var signInIntent: Intent

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    private val viewModel by viewModels<AuthViewModel>()

    init {
        App.instance.dagger.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.googleSignInButton.setOnClickListener {
            resultLauncher.launch(signInIntent)
        }
        binding.anonymousButton.setOnClickListener {
            startTabsFragment()
        }

        initResultLauncher()
    }

    private fun initResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val googleSignInAccount = task.getResult(ApiException::class.java)
                        googleSignInAccount?.apply {
                            idToken?.let { idToken ->
                                signInWithGoogle(idToken)
                            }
                        }
                    } catch (e: ApiException) {
                        println("AAA initResultLauncher ${e.message}")
                        crashlytics.recordException(e)
                    }
                }
            }
    }

    private fun signInWithGoogle(idToken: String?) {
        viewModel.signInWithGoogle(idToken).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    val isNewUser = response.data
                    if (isNewUser) {
                        createUser()
                    } else {
                        startTabsFragment()
                        binding.progressBar.hide()
                    }
                }

                is Response.Failure -> {
                    println("AAA signInWithGoogle ${response.errorMessage}")
                    crashlytics.recordException(Throwable(response.errorMessage))
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun createUser() {
        viewModel.createUser().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    startTabsFragment()
                    binding.progressBar.hide()
                }

                is Response.Failure -> {
                    println("AAA createUser ${response.errorMessage}")
                    crashlytics.recordException(Throwable(response.errorMessage))
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun startTabsFragment() {
        // запускаем фрагмент Tabs, при этом убираем из стека AuthFragment
        findNavController().navigate(R.id.tabsFragment, null, navOptions {
            popUpTo(R.id.authFragment) {
                inclusive = true
            }
        })
    }

}