package com.crystalpigeon.makeupapp.viewmodel

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.model.MakeUpService
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var api: MakeUpService
    @Inject
    lateinit var retrofit: Retrofit
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    init {
        MakeUpApp.app.component.inject(this)
    }

    private fun signOut() {
        mFirebaseAuth.signOut()
    }

    fun clickOnLogout(context: Context, navController: NavController){
        AlertDialog.Builder(context, R.style.AlertDialogStyle)
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                signOut()
                navController.navigate(R.id.loginFragment)
            }
            .setNegativeButton("No") { d, _ -> d.cancel() }
            .show()
    }
}