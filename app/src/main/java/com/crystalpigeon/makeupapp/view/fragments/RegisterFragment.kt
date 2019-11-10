package com.crystalpigeon.makeupapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.crystalpigeon.makeupapp.*
import com.google.firebase.auth.FirebaseAuth
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth
    @Inject
    lateinit var registerViewModel: RegisterViewModel
    private val compositeDisposable = CompositeDisposable()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MakeUpApp.app.component.inject(this)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideToolbar()
        navController =
            Navigation.findNavController(activity as MainActivity, R.id.nav_host_fragment)
        context ?: return
        btn_register.setOnClickListener {
            if (context?.checkRequiredFields(
                    et_username,
                    et_email,
                    et_password,
                    et_confirm_password
                ) == true && context?.isEmailValid(et_email) == true && context?.isPasswordValid(
                    et_password
                ) == true && context?.isConfirmPassValid(
                    et_password,
                    et_confirm_password
                ) == true
            ) {
                btn_register.isEnabled = false
                compositeDisposable.add(registerViewModel.register(
                    et_email.text.toString(),
                    et_password.text.toString()
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onComplete = {
                            (activity as MainActivity).hideKeyboard()
                            val snack = Snackbar.make(
                                view,
                                getString(R.string.registration_successful),
                                Snackbar.LENGTH_SHORT
                            )
                            snack.show()
                            navController.navigate(R.id.productTypeFragment)
                        },
                        onError = {
                            btn_register.isEnabled = true
                            val snack = Snackbar.make(
                                view,
                                getString(R.string.registration_failed),
                                Snackbar.LENGTH_SHORT
                            )
                            snack.show()
                        }
                    )
                )
            }
        }
    }
}