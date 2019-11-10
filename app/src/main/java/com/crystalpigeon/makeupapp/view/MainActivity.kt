package com.crystalpigeon.makeupapp.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.MakeUpApp.Companion.app
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ILoader {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.component.inject(this)
        setContentView(R.layout.activity_main)

        this.setSupportActionBar(toolbar as Toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)

        logout.setOnClickListener {
            mainViewModel.clickOnLogout(this, navController)
        }

        cart.setOnClickListener{
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right_enter)
                .setExitAnim(R.anim.slide_out_left_exit)
                .setPopEnterAnim(R.anim.slide_in_left_enter)
                .setPopExitAnim(R.anim.slide_out_right_exit).build()

            if (navController.currentDestination?.label != "cartScreenFragment"){
                navController.navigate(R.id.cartScreenFragment, null, navOptions)
            }
        }
    }

    fun showToolbar(){
        this.supportActionBar?.show()
    }

    fun hideToolbar(){
        this.supportActionBar?.hide()
    }

    fun setActionBarTitle(id: Int) {
        pageTitle.text = getString(id)
    }

    override fun show() {
        loader.visibility = View.VISIBLE
        loader.playAnimation()
    }

    override fun hide() {
        loader.cancelAnimation()
        loader.visibility = View.GONE
    }
}
