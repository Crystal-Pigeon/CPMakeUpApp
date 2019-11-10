package com.crystalpigeon.makeupapp.view.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.model.Product
import com.crystalpigeon.makeupapp.model.ProductModel
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.view.adapters.ColorItemAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_add_to_cart.view.*
import kotlinx.android.synthetic.main.fragment_product_details_screen.*
import kotlinx.android.synthetic.main.fragment_product_details_screen.view.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ProductDetailsFragment : Fragment() {
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth
    private var colorItemAdapter: ColorItemAdapter? = null
    private var colorList: ArrayList<String> = ArrayList()
    private var numOfItems: Int = 1
    private lateinit var database: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var productModel: ProductModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        navController =
            Navigation.findNavController(activity as MainActivity, R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MakeUpApp.app.component.inject(this)
        return inflater.inflate(R.layout.fragment_product_details_screen, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product: Product = arguments?.getParcelable("product")?:return

        (activity as MainActivity).showToolbar()
        (activity as MainActivity).setActionBarTitle(R.string.product_details)
        view.rv_colors.layoutManager = GridLayoutManager(context, 6)
        if (product.product_colors?.isNotEmpty() == true) {
            product.product_colors.forEach {
                colorList.add(it.hex_value)
            }
            colorItemAdapter = ColorItemAdapter(colorList)
            rv_colors.adapter = colorItemAdapter
        }

        Picasso.get()
            .load(product.image_link)
            .error(R.drawable.no_image_available)
            .into(view.product_image)

        product_name.text = product.name
        product_brand.text = product.brand
        product_description.text = product.description
        product_price.text = product.price ?: "0.0"
        product_price.append("$")

        if (product.product_colors?.isEmpty() == true) {
            pick_color.visibility = View.GONE
        }

        btn_plus.setOnClickListener {
            numOfItems += 1
            num_of_items.text = numOfItems.toString()
        }

        btn_minus.setOnClickListener {
            if (numOfItems > 1) {
                numOfItems -= 1
            }
            num_of_items.text = numOfItems.toString()
        }

        btn_buy_product.setOnClickListener {
            val mBuilder = AlertDialog.Builder(context!!)
            val mView = layoutInflater.inflate(R.layout.dialog_add_to_cart, null)
            val tvName = mView.tv_name
            val tvBrand = mView.tv_brand
            val tvPrice = mView.tv_price
            val btnCancel = mView.btnCancel
            val btnConfirm = mView.btnConfirm
            mBuilder.setView(mView)
            val dialog = mBuilder.create()
            tvName.text = product.name
            tvBrand.text = product.brand
            val circle = mView.color_circle
            if (colorItemAdapter?.pickedColor != null) {
                val drawable =
                    mView.color_circle.background as StateListDrawable
                val dcs = drawable.constantState as DrawableContainer.DrawableContainerState
                val drawableItems = dcs.children
                val gradientDrawableChecked = drawableItems[0] as GradientDrawable
                try {
                    gradientDrawableChecked.setColor(Color.parseColor((colorItemAdapter as ColorItemAdapter).pickedColor))
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            } else {
                mView.tv_col.visibility = View.GONE
                circle.visibility = View.GONE
            }

            if (product.price != null) {
                tvPrice.text = priceSum(numOfItems, product.price).toString()
            } else {
                tvPrice.text = "0.0"
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            btnConfirm.setOnClickListener {
                if (colorItemAdapter == null) {
                    productModel = ProductModel(
                        product.id.toString(),
                        tvName.text.toString(),
                        tvBrand.text.toString(),
                        (tvPrice.text as String).toFloat(),
                        null,
                        product.image_link
                    )
                } else {
                    productModel =
                        (colorItemAdapter as ColorItemAdapter).pickedColor?.let { it1 ->
                            ProductModel(
                                product.id.toString(),
                                tvName.text.toString(),
                                tvBrand.text.toString(),
                                (tvPrice.text as String).toFloat(),
                                it1,
                                product.image_link
                            )
                        }!!

                }
                val user = mFirebaseAuth.currentUser
                database.child(user!!.uid).push().setValue(productModel)

                Snackbar.make(
                    mView,
                    getString(R.string.successfully_added_to_cart),
                    Snackbar.LENGTH_SHORT
                ).show()
                dialog.dismiss()
                navController.navigate(R.id.action_productDetailsFragment_to_cartScreenFragment)
            }
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        numOfItems = 1
    }

    private fun priceSum(numOfItems: Int, productPrice: String): Float {
        return numOfItems * productPrice.toFloat()
    }
}