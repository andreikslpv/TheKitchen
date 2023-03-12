package com.andreikslpv.thekitchen.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.databinding.ActivityAuthBinding
import com.andreikslpv.thekitchen.di.MainIntent
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.vm.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent
import com.google.android.gms.common.api.ApiException
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {
    @MainIntent
    @Inject
    lateinit var mainIntent: Intent
    @Inject
    lateinit var signInIntent: Intent

    private lateinit var binding: ActivityAuthBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val viewModel by viewModels<AuthViewModel>()

    init {
        App.instance.dagger.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIfUserIsAuthenticated()

        binding.googleSignInButton.setOnClickListener {
            resultLauncher.launch(signInIntent)
        }
        initResultLauncher()

    }

    private fun checkIfUserIsAuthenticated() {
        if (viewModel.isUserAuthenticated) {
            goToMainActivity()
        }
    }


    private fun initResultLauncher() {
        resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = getSignedInAccountFromIntent(result.data)
                try {
                    val googleSignInAccount = task.getResult(ApiException::class.java)
                    googleSignInAccount?.apply {
                        idToken?.let { idToken ->
                            signInWithGoogle(idToken)
                        }
                    }
                } catch (e: ApiException) {
                    print(e.message)
                }
            }
        }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModel.signInWithGoogle(idToken).observe(this) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    goToMainActivity()
                    binding.progressBar.hide()
//                    val isNewUser = response.data
//                    if (isNewUser) {
//                        println("I/o isNewUser = true")
//                        createUser()
//                    } else {
//                        println("I/o isNewUser = false")
//                        goToMainActivity()
//                        binding.progressBar.hide()
//                    }
                }
                is Response.Failure -> {
                    print(response.errorMessage)
                    binding.progressBar.hide()
                }
            }
        }
    }

//    private fun createUser() {
//        viewModel.createUser().observe(this) { response ->
//            when (response) {
//                is Response.Loading -> binding.progressBar.show()
//                is Response.Success -> {
//                    goToMainActivity()
//                    binding.progressBar.hide()
//                }
//                is Response.Failure -> {
//                    print(response.errorMessage)
//                    binding.progressBar.hide()
//                }
//            }
//        }
//    }

    private fun goToMainActivity() {
        startActivity(mainIntent)
        finish()
    }
}