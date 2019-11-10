package com.crystalpigeon.makeupapp.view.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crystalpigeon.makeupapp.R
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.DrawableContainer.DrawableContainerState
import java.lang.IllegalArgumentException

class ColorItemAdapter(colors: ArrayList<String>?) :
    RecyclerView.Adapter<ColorItemAdapter.ViewHolder>() {

    private var colors: ArrayList<String>? = null
    private var checkedPosition: Int = 0
    var pickedColor: String? = colors?.elementAt(0)

    init {
        this.colors = colors
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.color_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = colors?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colors?.elementAt(position))
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(color: String?) {

            val drawable = view.background as StateListDrawable
            val dcs = drawable.constantState as DrawableContainerState
            val drawableItems = dcs.children
            val gradientDrawableChecked = drawableItems[0] as GradientDrawable
            try {
                gradientDrawableChecked.setColor(Color.parseColor(color))
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            if (checkedPosition == adapterPosition) {
                gradientDrawableChecked.setStroke(2, Color.BLUE)
            } else {
                gradientDrawableChecked.setStroke(1, Color.BLACK)
            }
            view.setOnClickListener {
                if (checkedPosition != adapterPosition) {
                    checkedPosition = adapterPosition
                    pickedColor = colors!![checkedPosition]
                    notifyDataSetChanged()
                }


            }
        }
    }
}