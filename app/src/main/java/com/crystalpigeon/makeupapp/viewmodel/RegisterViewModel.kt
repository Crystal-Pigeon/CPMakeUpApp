package com.crystalpigeon.makeupapp.viewmodel

import androidx.lifecycle.ViewModel
import com.crystalpigeon.makeupapp.MakeUpApp
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import javax.inject.Inject

class RegisterViewModel : ViewModel() {
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    init {
        MakeUpApp.app.component.inject(this)
    }

    fun register(email: String, pass: String) = Completable.create { emitter ->
        mFirebaseAuth.createUserWithEmailAndPassword(
            email,
            pass
        ).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception?:Throwable())
                    }
                }
            }
    }
}