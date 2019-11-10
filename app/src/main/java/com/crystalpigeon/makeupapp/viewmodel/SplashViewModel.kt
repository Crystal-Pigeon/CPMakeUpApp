package com.crystalpigeon.makeupapp.viewmodel

import androidx.lifecycle.ViewModel
import com.crystalpigeon.makeupapp.MakeUpApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SplashViewModel : ViewModel() {
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    init {
        MakeUpApp.app.component.inject(this)
    }

    fun getCurrentUser () : FirebaseUser? {
        return mFirebaseAuth.currentUser
    }
}