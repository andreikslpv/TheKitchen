package com.andreikslpv.thekitchen.data.repository

import com.google.firebase.auth.FirebaseAuth

class SplashRepository(
    private val auth: FirebaseAuth
) {
    val isUserAuthenticatedInFirebase get() = auth.currentUser != null
}