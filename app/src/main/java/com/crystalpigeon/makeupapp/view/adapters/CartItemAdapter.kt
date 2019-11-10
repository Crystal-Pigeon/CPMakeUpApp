package com.crystalpigeon.makeupapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_item.view.*

class CartItemAdapter(
    private val cartProducts: ArrayList<DataSnapshot>?,
    context: Context?,
    onDeleteClicked: OnDeleteClicked
) :
    RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    private var context: Context? = null
    private var onDeleteClicked: OnDeleteClicked? = null
    private var totalSum = 0.0f

    init {
        this.context = context
        this.onDeleteClicked = onDeleteClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cart_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = cartProducts?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartProducts?.get(position) ?: return)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(dataSnapshot: DataSnapshot) {
            val product = dataSnapshot.getValue(ProductModel::class.java) ?: return

            Picasso.get()
                .load(product.productImg)
                .error(R.drawable.no_image_available)
                .into(view.iv_product)

            view.product_name.text = product.productName
            view.product_price.text = product.productPrice.toString() + "$"

            view.iv_delete.setOnClickListener {
                onDeleteClicked?.onDeleteClicked(dataSnapshot)
            }
        }
    }

    fun deleteProduct(dataSnapshot: DataSnapshot) {
        cartProducts?.indexOf(dataSnapshot) ?: return
        cartProducts.remove(dataSnapshot)
        if (cartProducts.size == 0) {
            onDeleteClicked?.cartIsEmpty()
        }
        notifyDataSetChanged()
    }

    interface OnDeleteClicked {
        fun onDeleteClicked(dataSnapshot: DataSnapshot?)
        fun cartIsEmpty()
    }

    fun countTotalPrice(): String {
        totalSum = 0.0f
        for (cartProduct in this.cartProducts!!) {
            val product = cartProduct.getValue(ProductModel::class.java)
            totalSum += product?.productPrice ?: 0.0f
        }
        return "%.2f$".format(totalSum)
    }
}