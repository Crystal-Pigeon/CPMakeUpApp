package com.crystalpigeon.makeupapp.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.crystalpigeon.makeupapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_type_item.view.*

class ProductTypeAdapter(private val products: HashSet<String>?, context: Context, navController: NavController): RecyclerView.Adapter<ProductTypeAdapter.ViewHolder>() {

    private var navController: NavController
    private var context: Context? = null

    init {
        this.context = context
        this.navController = navController
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_type_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products?.elementAt(position), context)
    }

    override fun getItemCount(): Int = products?.size?:0

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun bind(type: String?, context: Context?){
            view.tvProductType.text = type
            setImageAndBackground(type, context)
            val bundle = bundleOf("type" to type)

            view.cvProductType.setOnClickListener {
                navController.navigate(R.id.action_productTypeFragment_to_listOfProductsFragment, bundle)
            }
        }

        private fun setImageAndBackground(type: String?, context: Context?) {
            when(type) {
                context?.getString(R.string.bronzer) -> {
                    Picasso.get()
                        .load(R.drawable.bronzer)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType))
                }
                context?.getString(R.string.foundation) -> {
                    Picasso.get()
                        .load(R.drawable.foundation)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType2))
                }
                context?.getString(R.string.eyeliner)-> {
                    Picasso.get()
                        .load(R.drawable.eyeliner)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType3))
                }
                context?.getString(R.string.blush) -> {
                    Picasso.get()
                        .load(R.drawable.blush)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType4))
                }
                context?.getString(R.string.eyebrow)-> {
                    Picasso.get()
                        .load(R.drawable.eyebrow_pencil)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType5))
                }
                context?.getString(R.string.eyeshadow) -> {
                    Picasso.get()
                        .load(R.drawable.eyeshadow)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType6))
                }
                context?.getString(R.string.lip_liner) -> {
                    Picasso.get()
                        .load(R.drawable.lipliner)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType7))
                }
                context?.getString(R.string.lipstick) -> {
                    Picasso.get()
                        .load(R.drawable.lipstick)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType8))
                }
                context?.getString(R.string.mascara) -> {
                    Picasso.get()
                        .load(R.drawable.mascara)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType9))
                }
                context?.getString(R.string.nail_polish) -> {
                    Picasso.get()
                        .load(R.drawable.nail)
                        .into(view.picture)
                    view.cvProductType.background.setTint(ContextCompat.getColor(context!!, R.color.colorForProductType10))
                }
            }
        }
    }

}