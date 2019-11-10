package com.crystalpigeon.makeupapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.model.Product
import com.squareup.picasso.Picasso
import java.util.ArrayList

class ListOfProductsAdapter(
    private val products: ArrayList<Product>?,
    context: Context,
    navController: NavController
) :
    RecyclerView.Adapter<ListOfProductsAdapter.ViewHolder>() {

    private var context: Context? = null
    private var navController: NavController? = null

    init {
        this.context = context
        this.navController = navController
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_of_products_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = products!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products!!.elementAt(position))
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("DefaultLocale")
        fun bind(product: Product) {
            product.name =  product.name.trimStart()
            val bundle = bundleOf("product" to product)
            val cvProduct = view.findViewById<CardView>(R.id.cv_product)
            val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
            val ivProductImage = view.findViewById<ImageView>(R.id.ivProductImage)

            cvProduct.setOnClickListener {
                navController?.navigate(R.id.action_listOfProductsFragment_to_productDetailsFragment, bundle)
            }
            val brand = product.brand?.split(' ')?.joinToString(" ") { it.capitalize() }
            val name = product.name.split(' ').joinToString(" ") { it.capitalize() }

            tvProductName.text = brand ?: name
            Picasso.get()
                .load(product.image_link)
                .error(R.drawable.no_image_available)
                .into(ivProductImage)
        }
    }

}