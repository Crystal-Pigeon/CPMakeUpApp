package com.crystalpigeon.makeupapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.view.adapters.CartItemAdapter
import com.crystalpigeon.makeupapp.view.adapters.CartItemAdapter.OnDeleteClicked
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cart_screen.*
import javax.inject.Inject
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_cart_screen.view.*
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.crystalpigeon.makeupapp.hideLoader
import com.crystalpigeon.makeupapp.showLoader
import com.google.android.material.snackbar.Snackbar


class CartScreenFragment : Fragment(), OnDeleteClicked{


    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private lateinit var cartItemAdapter: CartItemAdapter
    private var database: DatabaseReference? = null
    private var totalSum = 0.0f
    private lateinit var tvSum: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        MakeUpApp.app.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSum = view.tv_sum

        (activity as MainActivity).showToolbar()
        (activity as MainActivity).setActionBarTitle(R.string.shopping_cart)

        tv_title_total.visibility = View.GONE

        getProductFromDatabase(view)
    }

    private fun getProductFromDatabase(view: View) {
        context?.showLoader()
        val user = mFirebaseAuth.currentUser
        if (verifyAvailableNetwork(activity as MainActivity)) {
            productInCart.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
            ivEmptyCart.visibility = View.GONE
            tvEmptyCart.visibility = View.GONE
            database?.child(user!!.uid)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        context?.hideLoader()
                        if (p0.exists()) {
                            val products = ArrayList<DataSnapshot>()
                            for (p in p0.children) {
                                products.add(p)
                            }
                            cartItemAdapter =
                                CartItemAdapter(products, context, this@CartScreenFragment)
                            rv_cart_products?.layoutManager = LinearLayoutManager(context)
                            rv_cart_products?.adapter = cartItemAdapter
                            tv_title_total.visibility = View.VISIBLE
                            tvSum.text = cartItemAdapter.countTotalPrice()
                        } else {
                            productInCart.visibility = View.GONE
                            errorLayout.visibility = View.GONE
                            ivEmptyCart.visibility = View.VISIBLE
                            tvEmptyCart.visibility = View.VISIBLE
                        }
                    }
                })
        } else {
            context?.hideLoader()
            Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_SHORT).show()

            productInCart.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            ivEmptyCart.visibility = View.GONE
            tvEmptyCart.visibility = View.GONE
            tryAgain.setOnClickListener { getProductFromDatabase(view) }
        }
    }

    override fun onDeleteClicked(dataSnapshot: DataSnapshot?) {
        AlertDialog.Builder(activity as MainActivity, R.style.AlertDialogStyle)
            .setMessage(getString(R.string.delete_product))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val user = mFirebaseAuth.currentUser
                database?.child(user!!.uid)?.child(dataSnapshot?.key!!)?.removeValue()
                    ?.addOnSuccessListener {
                        cartItemAdapter.deleteProduct(dataSnapshot)
                        tvSum.text = cartItemAdapter.countTotalPrice()
                    }
            }
            .setNegativeButton(getString(R.string.no)) { d, _ -> d.cancel() }
            .show()
    }

    private fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun cartIsEmpty() {
        productInCart.visibility = View.GONE
        errorLayout.visibility = View.GONE
        ivEmptyCart.visibility = View.VISIBLE
        tvEmptyCart.visibility = View.VISIBLE
    }
}