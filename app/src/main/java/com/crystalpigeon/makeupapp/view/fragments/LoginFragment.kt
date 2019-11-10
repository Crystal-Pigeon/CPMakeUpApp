package com.crystalpigeon.makeupapp.view.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.viewmodel.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject
import androidx.appcompat.app.AlertDialog
import com.crystalpigeon.makeupapp.*
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess


class LoginFragment : Fragment() {

    private lateinit var navController: NavController
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth
    @Inject
    lateinit var loginViewModel: LoginViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(activity as MainActivity, R.style.AlertDialogStyle)
                    .setMessage(getString(R.string.exit_app))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        activity?.moveTaskToBack(true)
                        android.os.Process.killProcess(android.os.Process.myPid())
                        exitProcess(1)
                    }
                    .setNegativeButton(getString(R.string.no)) { d, _ -> d.cancel() }
                    .show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MakeUpApp.app.component.inject(this)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context ?: return
        navController =
            Navigation.findNavController(activity as MainActivity, R.id.nav_host_fragment)

        (activity as MainActivity).hideToolbar()

        tv_sign_up?.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        btn_login.setOnClickListener {
            if (context?.checkRequiredFields(et_email, et_password) == true) {
                btn_login.isEnabled = false
                compositeDisposable.add(loginViewModel.login(
                    et_email.text.toString(),
                    et_password.text.toString()
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onComplete = {
                            (activity as MainActivity).hideKeyboard()
                            val snack= Snackbar.make(view, getString(R.string.successfully_logged_in), Snackbar.LENGTH_SHORT)
                            snack.show()
                            navController.navigate(R.id.productTypeFragment)
                        },
                        onError = {
                            btn_login.isEnabled = true
                            val snack= Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT)
                            snack.show()
                        }
                    )
                )
            }
        }

        tv_sign_up.setOnClickListener {
            navController.navigate(R.id.registerFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noRequiredFields(et_email, et_password)
    }
}