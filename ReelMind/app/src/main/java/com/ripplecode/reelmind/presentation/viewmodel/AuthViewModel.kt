package com.ripplecode.reelmind.presentation.viewmodel

import android.content.IntentSender
import androidx.lifecycle.ViewModel
import com.ripplecode.reelmind.data.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun signInWithGoogle(callback: (IntentSender?, Exception?) -> Unit) {
        authRepository.getGoogleSignInIntent { intentSender, error ->
            callback(intentSender, error)
        }
    }

    fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        authRepository.firebaseAuthWithGoogle(idToken, onResult)
    }
}
