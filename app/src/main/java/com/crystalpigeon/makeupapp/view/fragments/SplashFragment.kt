package com.crystalpigeon.makeupapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.viewmodel.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_splash_screen.*
import javax.inject.Inject

class SplashFragment : Fragment() {
    lateinit var navController: NavController
    @Inject
    lateinit var splashViewModel: SplashViewModel
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController =
            Navigation.findNavController(activity as MainActivity, R.id.nav_host_fragment)

        (activity as MainActivity).hideToolbar()

        val myAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.splashscreentransition)
        iv_splash.startAnimation(myAnim)
        tv_splash.startAnimation(myAnim)

        mAuthStateListener = FirebaseAuth.AuthStateListener { auth ->
            user = splashViewModel.getCurrentUser()
        }
        val timer: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (user != null) {
                        navController.navigate(R.id.productTypeFragment)

                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                }
            }
        }
        timer.start()
    }

    override fun onStart() {
        super.onStart()
        MakeUpApp.app.component.inject(this)
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }
}