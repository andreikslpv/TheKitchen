package com.andreikslpv.thekitchen.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.FragmentProfileBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.ui.base.BaseFragment
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ExcludeRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.ItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeItemClickListener
import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeMiniRecyclerAdapter
import com.andreikslpv.thekitchen.presentation.ui.recyclers.itemDecoration.SpaceItemDecoration
import com.andreikslpv.thekitchen.presentation.utils.findTopNavController
import com.andreikslpv.thekitchen.presentation.utils.makeToast
import com.andreikslpv.thekitchen.presentation.utils.visible
import com.andreikslpv.thekitchen.presentation.vm.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val dialogAnimDuration = 500L
    private val dialogAnimAlfa = 1f

    private val viewModel by viewModels<ProfileViewModel>()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                viewModel.tryToChangeAvatar(uri).observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Response.Loading -> binding.progressBar.show()
                        is Response.Success -> {
                            viewModel.refreshUser()
                            binding.progressBar.hide()
                        }

                        is Response.Failure -> {
                            print(response.errorMessage)
                            binding.progressBar.hide()
                        }
                    }
                }
            } else {
                println("AAA PhotoPicker: No media selected")
            }
        }

    @Inject
    lateinit var signInIntent: Intent

    private lateinit var recipeHistoryAdapter: RecipeMiniRecyclerAdapter
    private lateinit var excludeAdapter: ExcludeRecyclerAdapter

    private val decorator = SpaceItemDecoration(
        paddingBottomInDp = 16,
        paddingRightInDp = 4,
        paddingLeftInDp = 4,
    )

    init {
        App.instance.dagger.inject(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileToolbar.setupWithNavController(findNavController())
        initRecipeHistoryCollect()
        initCategoryCollect()
        initRecyclers()
        initCurrentUserCollect()
        initSignOutButton()
        getAuthState()
        initDeleteUserButton()
        initResultLauncher()
        initEditNameFunction()
        initChangeAvatarFunction()
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
                                deleteUserDb()
                                deleteAvatar()
                                deleteUserAuth(idToken)
                            }
                        }
                    } catch (e: ApiException) {
                        println("AAA initResultLauncher ${e.message}")
                    }
                }
            }
    }

    private fun deleteUserAuth(idToken: String?) {
        viewModel.deleteUserAuth(idToken).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    binding.progressBar.hide()
                    getString(R.string.profile_delete_success).makeToast(requireContext())
                }

                is Response.Failure -> {
                    println("AAA deleteUserAuth ${response.errorMessage}")
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun deleteUserDb() {
        viewModel.deleteUserDb().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> binding.progressBar.hide()
                is Response.Failure -> {
                    println("AAA deleteUserDb ${response.errorMessage}")
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun deleteAvatar() {
        viewModel.deleteAvatar().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> binding.progressBar.hide()
                is Response.Failure -> {
                    println("AAA deleteAvatar ${response.errorMessage}")
                    binding.progressBar.hide()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecipeHistoryCollect() {
        viewModel.getRecipeHistory().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    recipeHistoryAdapter.changeItems(response.data)
                    recipeHistoryAdapter.notifyDataSetChanged()
                    binding.profileRecyclerRecipe.visible(true)
                    binding.historyEmptyView.visible(false)
                    binding.progressBar.hide()
                }

                is Response.Failure -> {
                    binding.profileRecyclerRecipe.visible(false)
                    binding.historyEmptyView.visible(true)
                    binding.progressBar.hide()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initCategoryCollect() {
        viewModel.getCategoryExclude().observe(viewLifecycleOwner) {
            excludeAdapter.changeItems(it)
            excludeAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclers() {
        binding.profileRecyclerRecipe.apply {
            recipeHistoryAdapter = RecipeMiniRecyclerAdapter(
                object : RecipeItemClickListener {
                    override fun click(recipePreview: RecipePreview) {
                        goToRecipeFragment(recipePreview)
                    }
                },
                object : ItemClickListener {
                    override fun click(id: String) {
                        val result = viewModel.tryToChangeFavoritesStatus(id)
                        if (!result) Snackbar.make(
                            binding.root, R.string.home_snackbar_text, Snackbar.LENGTH_LONG
                        ).setAction(R.string.home_snackbar_action) { goToAuthFragment() }.show()
                    }
                }
            )
            adapter = recipeHistoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //Применяем декоратор для отступов
            addItemDecoration(decorator)
        }

        binding.excludeRecyclerRecipe.apply {
            excludeAdapter = ExcludeRecyclerAdapter()
            adapter = excludeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = SpaceItemDecoration(
                paddingBottomInDp = 16,
            )
            addItemDecoration(decorator)
        }
    }

    private fun goToRecipeFragment(recipePreview: RecipePreview) {
        val direction = ProfileFragmentDirections.actionProfileFragmentToRecipeFragment2(
            recipePreview
        )
        findNavController().navigate(direction)
    }

    private fun goToAuthFragment() {
        findTopNavController().navigate(R.id.authFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }

    private fun initCurrentUserCollect() {
        viewModel.currentUser.observe(viewLifecycleOwner) {
            it?.let { user ->
                Glide.with(binding.profileAvatar)
                    .load(user.photoUrl)
                    .centerCrop()
                    .into(binding.profileAvatar)
                binding.profileName.setText(user.displayName)
                binding.profileEmail.text = user.email
            }
            binding.progressBar.hide()
        }

    }

    private fun initSignOutButton() {
        binding.signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        viewModel.signOut().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> binding.progressBar.hide()
                is Response.Failure -> {
                    print(response.errorMessage)
                    binding.progressBar.hide()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getAuthState() {
        viewModel.getAuthState().observe(viewLifecycleOwner) { isUserSignedOut ->
            if (isUserSignedOut) {
                findTopNavController().navigate(R.id.authFragment, null, navOptions {
                    popUpTo(R.id.tabsFragment) {
                        inclusive = true
                    }
                })
            }
        }
    }

    private fun initDeleteUserButton() {
        binding.deleteUserButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        binding.profileDialog.apply {
            this.visible(true)
            animate()
                .setDuration(dialogAnimDuration)
                .alpha(dialogAnimAlfa)
                .start()

            actionButton.setOnClickListener {
                viewModel.saveUidBeforeDeleteUser()
                resultLauncher.launch(signInIntent)
            }
        }
    }

    private fun initEditNameFunction() {
        binding.profilePencil.setOnClickListener {
            binding.profileName.apply {
                isEnabled = true
                requestFocus()
                val imm =
                    requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                selectAll()
            }
        }

        binding.profileName.setOnKeyListener { v, keyCode, event ->
            // if the event is a key down event on the enter button
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                (v as EditText).apply {
                    editUserName(text.toString())
                    clearFocus()
                    isCursorVisible = false
                    isEnabled = false
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun editUserName(newName: String) {
        viewModel.editUserName(newName).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> {
                    binding.progressBar.hide()
                    getString(R.string.profile_edit_name_success).makeToast(requireContext())
                }

                is Response.Failure -> {
                    println("AAA signInWithGoogle ${response.errorMessage}")
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun initChangeAvatarFunction() {
        binding.profileCamera.setOnClickListener {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

}