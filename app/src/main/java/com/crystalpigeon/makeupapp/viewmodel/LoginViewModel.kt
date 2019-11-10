package com.crystalpigeon.makeupapp.viewmodel

import androidx.lifecycle.ViewModel
import com.crystalpigeon.makeupapp.MakeUpApp
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import javax.inject.Inject

class LoginViewModel : ViewModel() {
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    init {
        MakeUpApp.app.component.inject(this)
    }

    fun login(email: String, password: String) = Completable.create { emitter ->
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }
}